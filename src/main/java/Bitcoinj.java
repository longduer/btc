import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.codec.binary.Hex;
import org.bitcoinj.core.*;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.script.Script;

import java.util.ArrayList;
import java.util.List;

import static org.bitcoinj.core.Utils.HEX;

public class Bitcoinj {

    public static void main(String[] args) {

        // RPC
        CoinUtils utils = null;
        try {
            utils = CoinUtils.getInstance();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        // local regtest network
        NetworkParameters params = RegTestParams.get();

        // 私钥
        DumpedPrivateKey dumpedPrivateKey = DumpedPrivateKey.fromBase58(params, Constants.coinbasePrivate);
        ECKey ecKey = dumpedPrivateKey.getKey();

        Address fromAddress = LegacyAddress.fromBase58(params, Constants.coinbase);

        // 接收地址
        Address receiveAddress = LegacyAddress.fromBase58(params, Constants.A01);

        List<UTXO> utxos = new ArrayList<UTXO>();
        long totalMoney = 0;

        Coin amount = Coin.COIN.multiply(10);
        System.out.println("amount : " + amount);

        Coin fee = Coin.CENT;
        System.out.println("fee : " + fee);

        try {
            Object listunspent = utils.listunspent(0,9999, new String[]{Constants.coinbase}, true);
            System.out.println("listunspent: " + JSON.toJSON(listunspent).toString());
            JSONArray listunspentArray = JSONArray.parseArray(JSON.toJSON(listunspent).toString());

            // 遍历未花费的交易
            for(int i =0; i<listunspentArray.size(); i++){
                UnSpentUtxo2 vo = JSON.parseObject(listunspentArray.get(i).toString(),UnSpentUtxo2.class);
                double d = vo.getAmount()*Math.pow(10,8);
                UTXO uo = new UTXO(
                        Sha256Hash.wrap(vo.getTxid()),
                        vo.getVout(),
                        Coin.valueOf(Math.round(d)),
                        vo.getConfirmations(),
                        false,
                        new Script(HEX.decode(vo.getScriptPubKey())),
                        vo.getAddress());
                utxos.add(uo);
                totalMoney +=uo.getValue().getValue();
                if (totalMoney>=(amount.getValue() + fee.getValue())){
                    break;
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        // 构建交易
        Transaction tx = new Transaction(params);

        Coin total = Coin.valueOf(totalMoney);
        System.out.println("total : " + total);

        tx.addOutput(Coin.valueOf(amount.getValue()), receiveAddress); // 转出
        // 如果需要找零 消费列表总金额 - 已经转账的金额 - 手续费

        long leave  = totalMoney - amount.getValue() - fee.getValue();
        tx.addOutput(Coin.valueOf(leave), fromAddress);
        System.out.println("找零数额: " + leave);

        for (UTXO utxo : utxos) {
            TransactionOutPoint outPoint = new TransactionOutPoint(params, utxo.getIndex(), utxo.getHash());
            // YOU HAVE TO CHANGE THIS
            tx.addSignedInput(outPoint, utxo.getScript(), ecKey, Transaction.SigHash.ALL, false);
        }

        Context context = new Context(params);
        tx.getConfidence().setSource(TransactionConfidence.Source.NETWORK);
        tx.setPurpose(Transaction.Purpose.USER_PAYMENT);
        System.out.println("=== [BTC] sign success,hash is :{} ===" + tx.getTxId());
        String data = new String(Hex.encodeHex(tx.bitcoinSerialize()));
        try {
            Object sendrawtransaction = utils.sendrawtransaction(data);
            System.out.println("sendrawtransaction: " + JSON.toJSON(sendrawtransaction).toString());

            //            挖矿到地址
            Object blocks = utils.generatetoaddress(1, Constants.coinbase);
            System.out.println("generatetoaddress: " + JSON.toJSON(blocks));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }


    }
}
