import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.codec.binary.Hex;
import org.bitcoinj.core.*;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.script.Script;

import java.util.ArrayList;
import java.util.List;

import static org.bitcoinj.core.Utils.HEX;

public class BitcoinjBak {

    public static void main(String[] args) {

        NetworkParameters params = RegTestParams.get();

        // RPC
        CoinUtils utils = null;
        try {
            utils = CoinUtils.getInstance();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        // 私钥
        DumpedPrivateKey dumpedPrivateKey = DumpedPrivateKey.fromBase58(params, Constants.coinbasePrivate);
        ECKey ecKey = dumpedPrivateKey.getKey();

        Address fromAddress = LegacyAddress.fromBase58(params, Constants.coinbase);

        System.out.println("from: " + fromAddress.toString());

        // 接收地址
        Address receiveAddress = LegacyAddress.fromBase58(params, Constants.A01);
        System.out.println("to: " + receiveAddress.toString());

        List<UTXO> utxos = new ArrayList<>();
        long totalMoney = 0;
        Coin amount = Coin.COIN.multiply(101);
        System.out.println("amount : " + amount);

        Coin fee = Coin.valueOf(1000);
        System.out.println("fee : " + fee);

        try {
            Object listunspent = utils.listunspent(0,99999, new String[]{Constants.coinbase}, false);
            JSONArray listunspentArray = JSONArray.parseArray(JSON.toJSON(listunspent).toString());
            System.out.println("size:"+listunspentArray.size()+";listunspent: " + JSON.toJSON(listunspent).toString());

            // 遍历未花费的交易
            for(int i =0; i<listunspentArray.size(); i++){
                UnSpentUtxo2 vo = JSON.parseObject(listunspentArray.get(i).toString(), UnSpentUtxo2.class);
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
        Coin total = Coin.valueOf(totalMoney);
        System.out.println("total : " + total + ";用到的utxo: " + utxos.size());

        // 构建交易
        Transaction tx = new Transaction(params);
        // 转出
        tx.addOutput(amount, receiveAddress);
        // 找零
        long leave  = totalMoney - amount.getValue() - fee.getValue();
        if(leave>0){
            tx.addOutput(Coin.valueOf(leave), fromAddress);
        }
        System.out.println("找零数额: " + leave);

        for (UTXO utxo : utxos) {
            TransactionOutPoint outPoint = new TransactionOutPoint(params, utxo.getIndex(), utxo.getHash());
//            if (ScriptPattern.isP2PK(utxo.getScript())) {
//                System.out.println("isP2PK");
//            } else if (ScriptPattern.isP2PKH(utxo.getScript())) {
//                System.out.println("isP2PKH");
//            } else if (ScriptPattern.isP2WPKH(utxo.getScript())) {
//                System.out.println("isP2WPKH");
//            } else {
//                System.out.println("others");
//            }
            tx.addSignedInput(outPoint, utxo.getScript(), ecKey, Transaction.SigHash.ALL, true);

        }

        Context context = new Context(params);
        tx.getConfidence().setSource(TransactionConfidence.Source.NETWORK);
        tx.setPurpose(Transaction.Purpose.USER_PAYMENT);
        System.out.println("=== [BTC] sign success,hash is :{} ===" + tx.getTxId());
        String data = new String(Hex.encodeHex(tx.bitcoinSerialize()));
        try {
            Object sendrawtransaction = utils.sendrawtransaction(data);
            System.out.println("sendrawtransaction: " + JSON.toJSON(sendrawtransaction).toString());

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
