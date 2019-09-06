import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.codec.binary.Hex;
import org.bitcoinj.core.*;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.bitcoinj.core.Utils.HEX;

public class OmniDelegate {

    /**
     *
     * https://bitcoinfees.earn.com/api/v1/fees/recommended
     * @param args
     */

    public static void main(String[] args) {

        // local regtest network
        NetworkParameters params = RegTestParams.get();

        // RPC
        CoinUtils utils = null;
        try {
            utils = CoinUtils.getInstance();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        // 通过账户名查询USDT余额
        Object omni_getbalance = null;
        Object btc_getbalance = null;
        try {

            omni_getbalance = utils.omni_getallbalancesforaddress(Constants.A01);
            btc_getbalance = utils.getbalance("A01", 1, true);

            System.out.println("A01 USDT余额: " + JSON.toJSON(omni_getbalance));
            System.out.println("A01 BTC余额 : " + JSON.toJSON(btc_getbalance));

            btc_getbalance = utils.getbalance("A02", 1, true);
            omni_getbalance = utils.omni_getallbalancesforaddress(Constants.A02);
            System.out.println("A02 USDT余额: " + JSON.toJSON(omni_getbalance));
            System.out.println("A02 BTC余额 : " + JSON.toJSON(btc_getbalance));

            btc_getbalance = utils.getbalance("A03", 1, true);
            omni_getbalance = utils.omni_getallbalancesforaddress(Constants.A03);
            System.out.println("A03 USDT余额: " + JSON.toJSON(omni_getbalance));
            System.out.println("A03 BTC余额 : " + JSON.toJSON(btc_getbalance));

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        // 只有omni，没有充足的btc作为燃料费
        DumpedPrivateKey dumpedPrivateKeyA01 = DumpedPrivateKey.fromBase58(params, Constants.A01Private);
        ECKey usdtKey = dumpedPrivateKeyA01.getKey();
        Address senderAddress = LegacyAddress.fromBase58(params, Constants.A01);

        // Fee provider 燃料费提供者，只有BTC，没有omni
        Address feeAddress = LegacyAddress.fromBase58(params, Constants.A02);
        // BTC
        DumpedPrivateKey dumpedPrivateKeyA02 = DumpedPrivateKey.fromBase58(params, Constants.A02Private);
        ECKey btcKey = dumpedPrivateKeyA02.getKey();

        // omni receiver 啥都没有，只作接收之用
        Address receiveAddress = LegacyAddress.fromBase58(params, Constants.A03);

        // USDT账户的utxo
        Object usdtunspent = null;
        try {
            usdtunspent = utils.listunspent(0,999999, new String[]{Constants.A01}, true);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("usdt所有者: " + JSON.toJSON(usdtunspent).toString());
        JSONArray usdtlistunspentArray = JSONArray.parseArray(JSON.toJSON(usdtunspent).toString());

        // BTC手续费账户的utxo
        Object btcunspent = null;
        try {
            btcunspent = utils.listunspent(0,999999, new String[]{Constants.A02}, true);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("btc燃料费: " + JSON.toJSON(btcunspent).toString());
        JSONArray listunspentArray2 = JSONArray.parseArray(JSON.toJSON(btcunspent).toString());


        // BTC手续费账户的utxo
        try {
            btcunspent = utils.listunspent(0,999999, new String[]{Constants.A03}, true);
            System.out.println("接收者utxo: " + JSON.toJSON(btcunspent).toString());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        List<UTXO> utxos_usdt_provider = new ArrayList<UTXO>();
        List<UTXO> utxos_fee_provider = new ArrayList<UTXO>();
        long usdtBTCTotal = 0;
        // 遍历未花费的交易
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


        long btcBTCTotal = 0;
        // 遍历未花费的交易
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

        //构建usdt的输出脚本 注意这里的金额是要乘10的8次方
        // 6a146f6d6e69 usdt普通转账
        // createSimpleSendHex
//         String rawTxHex = "6a146f6d6e69" + String.format("00000000%08x%016x", 1, Coin.COIN.multiply(5).getValue());
        // createSendToOwnersHex
        // String rawTxHex = String.format("00000003%08x%016x", currencyId.getValue(), amount.getWilletts());
        String usdtHex = "6a146f6d6e69" + String.format("%016x", 1) + String.format("%016x", Coin.COIN.multiply(1).getValue());
//        System.out.println(usdtHex);
//        System.out.println(rawTxHex.equalsIgnoreCase(usdtHex));
        // 构建交易
        Transaction tx = new Transaction(params);

        Coin fee = Coin.CENT;
        System.out.println("total tx fee: " + fee);

        //OMNI用户保证btc完整

        // 当账户中有BTC时，虽然代理转账usdt，但也不要影响其原有BTC的余额
        tx.addOutput(Coin.valueOf(usdtBTCTotal), senderAddress);

        // 如果需要找零 消费列表总金额 - 已经转账的金额 - 手续费
        long leave  = (btcBTCTotal) - fee.getValue() - 546;
        System.out.println("找零数额: " + leave);
        // omni交易脚本
        tx.addOutput(Coin.valueOf(0), new Script(Utils.HEX.decode(usdtHex)));
        // 找零
        tx.addOutput(Coin.valueOf(leave), feeAddress);
        // 接收omni的对象，放在最后，指定脚本执行
        tx.addOutput(Coin.valueOf(546), receiveAddress);


        System.out.println("utxos_usdt_provider: " + utxos_usdt_provider.size());
        // create usdt utxo data
        for (UTXO utxo : utxos_usdt_provider) {
            TransactionOutPoint outPoint = new TransactionOutPoint(params, utxo.getIndex(), utxo.getHash());
            tx.addSignedInput(outPoint, utxo.getScript(), usdtKey, Transaction.SigHash.ALL, true);
        }

        System.out.println("utxos_fee_provider: " + utxos_fee_provider.size());
        // create btc utxo data
        for (UTXO utxo : utxos_fee_provider) {
            TransactionOutPoint outPoint = new TransactionOutPoint(params, utxo.getIndex(), utxo.getHash());
            tx.addSignedInput(outPoint, utxo.getScript(), btcKey, Transaction.SigHash.ALL, true);
        }

        Context context = new Context(params);
        tx.getConfidence().setSource(TransactionConfidence.Source.NETWORK);
        tx.setPurpose(Transaction.Purpose.USER_PAYMENT);

        System.out.println("=== [USDT] sign success,hash is :{} ===" + tx.getTxId());
        //这是签名之后的原始交易，直接去广播就行了
        try {
            String data = new String(Hex.encodeHex(tx.bitcoinSerialize()));
            System.out.println(data);
            Object sendrawtransaction = utils.sendrawtransaction(data);
            System.out.println("sendrawtransaction: " + JSON.toJSON(sendrawtransaction).toString());
            Object blocks = utils.generatetoaddress(1, Constants.coinbase);
            System.out.println("generatetoaddress: " + JSON.toJSON(blocks));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
