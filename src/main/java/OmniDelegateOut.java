import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.codec.binary.Hex;
import org.bitcoinj.core.*;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.script.Script;

import java.util.ArrayList;
import java.util.List;

import static org.bitcoinj.core.Utils.HEX;

public class OmniDelegateOut {

    public static void main(String[] args) {

        // local regtest network
        NetworkParameters params = RegTestParams.get();

        // local omni-core rpc tools
        CoinUtils utils = null;
        try {
            utils = CoinUtils.getInstance();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        // 账户A01 作为omni资产的转出者，以及小量546聪比特币
        Address senderAddress = LegacyAddress.fromBase58(params, Constants.A01);
        // 通过私钥加载ECKey
        DumpedPrivateKey dumpedPrivateKeyA01 = DumpedPrivateKey.fromBase58(params, Constants.A01Private);
        ECKey usdtKey = dumpedPrivateKeyA01.getKey();

        // 账户A02 Fee provider 燃料费提供者，只有BTC，没有omni
        Address feeAddress = LegacyAddress.fromBase58(params, Constants.A02);
        DumpedPrivateKey dumpedPrivateKeyA02 = DumpedPrivateKey.fromBase58(params, Constants.A02Private);
        ECKey btcKey = dumpedPrivateKeyA02.getKey();

        // 账户A03 omni receiver 没有btc也没有omni资产，只用omni资产的接收者
        Address receiveAddress = LegacyAddress.fromBase58(params, Constants.A03);

        // 通过rpc接口获取 账户A01 链上的uxto
        Object usdtunspent = null;
        try {
            usdtunspent = utils.listunspent(0,999999, new String[]{Constants.A01}, true);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        JSONArray usdtlistunspentArray = JSONArray.parseArray(JSON.toJSON(usdtunspent).toString());

        // 通过rpc接口获取 账户A02 链上的uxto
        Object btcunspent = null;
        try {
            btcunspent = utils.listunspent(0,999999, new String[]{Constants.A02}, true);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        JSONArray listunspentArray2 = JSONArray.parseArray(JSON.toJSON(btcunspent).toString());

        List<UTXO> utxos_usdt_provider = new ArrayList<UTXO>();
        List<UTXO> utxos_fee_provider = new ArrayList<UTXO>();

        // 遍历账户A01未花费的交易 获取omni发送者所有的btc 单位：聪
        long usdtBTCTotal = 0;
        for(int i =0; i<usdtlistunspentArray.size(); i++){
            UnSpentUtxo2 vo = JSON.parseObject(usdtlistunspentArray.get(i).toString(), UnSpentUtxo2.class);
            double d = vo.getAmount()*Coin.COIN.getValue();
            UTXO uo = new UTXO(
                    Sha256Hash.wrap(vo.getTxid()),
                    vo.getVout(),
                    Coin.valueOf(Math.round(d)),
                    vo.getConfirmations(),
                    false,
                    new Script(HEX.decode(vo.getScriptPubKey())),
                    vo.getAddress());
            utxos_usdt_provider.add(uo);
            usdtBTCTotal +=uo.getValue().getValue();
        }
        System.out.println("btc total for omni owner : " + usdtBTCTotal);


        // 遍历账户A02未花费的交易 获取代理地址（即提供交易燃料者）所有的btc 单位：聪
        long btcBTCTotal = 0;
        for(int i =0; i<listunspentArray2.size(); i++){
            UnSpentUtxo2 vo = JSON.parseObject(listunspentArray2.get(i).toString(), UnSpentUtxo2.class);
            double d = vo.getAmount()*Coin.COIN.getValue();
            UTXO uo = new UTXO(
                    Sha256Hash.wrap(vo.getTxid()),
                    vo.getVout(),
                    Coin.valueOf(Math.round(d)),
                    vo.getConfirmations(),
                    false,
                    new Script(HEX.decode(vo.getScriptPubKey())),
                    vo.getAddress());
            utxos_fee_provider.add(uo);
            btcBTCTotal +=uo.getValue().getValue();
        }
        System.out.println("btc  total for gas owner: " + btcBTCTotal);
        // 构建omni layer协议脚本 详情自行google
        // 6a146f6d6e69 代表omin协议，String.format("%016x", 1)其中1代表资产编号，String.format("%016x", Coin.COIN.multiply(1).getValue()
        String usdtHex = "6a146f6d6e69" + String.format("%016x", 1) + String.format("%016x", Coin.COIN.multiply(1).getValue());

        // 构建交易
        Transaction tx = new Transaction(params);
        // 交易费 这里我们用CENT
        Coin fee = Coin.CENT;

        // OMNI用户保证btc完整
        // 当账户中有BTC时，虽然代理转账usdt，但也不要影响其原有BTC的余额，所以设置找零
        tx.addOutput(Coin.valueOf(usdtBTCTotal), senderAddress);

        // 计算代理地址（即提供交易燃料者）A02上USDT的找零btc余额； 消费列表总金额 - 已经转账的金额 - 手续费
        long leave  = (btcBTCTotal) - fee.getValue() - 546;
        // omni交易脚本
        tx.addOutput(Coin.valueOf(0), new Script(Utils.HEX.decode(usdtHex)));
        // 找零
        tx.addOutput(Coin.valueOf(leave), feeAddress);
        // 接收omni的对象，放在最后，指定脚本执行
        tx.addOutput(Coin.valueOf(546), receiveAddress);

        // create A01 usdt utxo data
        for (UTXO utxo : utxos_usdt_provider) {
            TransactionOutPoint outPoint = new TransactionOutPoint(params, utxo.getIndex(), utxo.getHash());
            tx.addSignedInput(outPoint, utxo.getScript(), usdtKey, Transaction.SigHash.ALL, true);
        }

        // create A02 btc utxo data
        for (UTXO utxo : utxos_fee_provider) {
            TransactionOutPoint outPoint = new TransactionOutPoint(params, utxo.getIndex(), utxo.getHash());
            tx.addSignedInput(outPoint, utxo.getScript(), btcKey, Transaction.SigHash.ALL, true);
        }


        Context context = new Context(params);
        tx.getConfidence().setSource(TransactionConfidence.Source.NETWORK);
        tx.setPurpose(Transaction.Purpose.USER_PAYMENT);

        // TX ID
        System.out.println("=== [USDT] sign success,hash is :{} ===" + tx.getTxId());

        try {
            //这是签名之后的原始交易，直接去广播就行了
            String data = new String(Hex.encodeHex(tx.bitcoinSerialize()));
            Object sendrawtransaction = utils.sendrawtransaction(data);
            System.out.println("sendrawtransaction: " + JSON.toJSON(sendrawtransaction).toString());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
