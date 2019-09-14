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

public class Omni {

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
            return;
        }

        // USDT
        String A01 = "miarWNPRgLetdPTKKs3L4dz7zJw9MHxeEd";
        String A01PrivateKey = "cS8nkyKmxmbyfP1A6YmzPMEhTZtyoxQdqaoveUUNCEeGNjRq33fZ";
        //usdt sender
//        Address fromAddress = LegacyAddress.fromBase58(params, A01);
        // 私钥
        DumpedPrivateKey dumpedPrivateKeyA01 = DumpedPrivateKey.fromBase58(params, A01PrivateKey);
        ECKey usdtKey = dumpedPrivateKeyA01.getKey();

        // BTC
        String A02 = "mz1YFz2JqbPTVjBFhYvwHZAh5iiPCnd2dh"; // cRA3btQDS4R2e93ksoEb584m1uDW6Z9mAtrAQFLf5XRBWRW1L1SC
        String A02PrivateKey = "cRA3btQDS4R2e93ksoEb584m1uDW6Z9mAtrAQFLf5XRBWRW1L1SC";
        // 私钥
        DumpedPrivateKey dumpedPrivateKeyA02 = DumpedPrivateKey.fromBase58(params, A02PrivateKey);
        ECKey btcKey = dumpedPrivateKeyA02.getKey();
        //btc fee address
        Address feeAddress = LegacyAddress.fromBase58(params, A02);

        // A03作为接受地址
        String A03 = "mmpUobgfi2ZXABSwELJnsvEBUuyZPtqpLy"; // cUEpyhn6RMsVYsRHAr8FSCVfCKjDMimc8wXreYn1Y4rT4UApu2ng
        //usdt receiver
        Address receiveAddress = LegacyAddress.fromBase58(params, A03);

        // 通过账户名查询USDT余额
        Object omni_getbalance = null;
        Object btc_getbalance = null;
        try {

            omni_getbalance = utils.omni_getallbalancesforaddress(A01);
            btc_getbalance = utils.getbalance("A01", 1, true);

            System.out.println("USDT余额: " + JSON.toJSON(omni_getbalance));
            System.out.println("BTC余额 : " + JSON.toJSON(btc_getbalance));

            btc_getbalance = utils.getbalance("A01", 1, true);
            System.out.println("A01 BTC余额 : " + JSON.toJSON(btc_getbalance));


        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
//
//        Object omni_listtransactions = null;
//        try {
//            omni_listtransactions = utils.omni_listtransactions(A01, 1000, 0,1, 9999999);
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//        System.out.println("usdt历史交易列表: " + JSON.toJSON(omni_listtransactions));

        // USDT账户的utxo
        Object usdtunspent = null;
        try {
            usdtunspent = utils.listunspent(0,999999, new String[]{A01}, true);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("usdt utxo列表: " + JSON.toJSON(usdtunspent).toString());
        JSONArray usdtlistunspentArray = JSONArray.parseArray(JSON.toJSON(usdtunspent).toString());

        // BTC手续费账户的utxo
        Object btcunspent = null;
        try {
            btcunspent = utils.listunspent(0,999999, new String[]{A02}, true);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("btc  utxo列表: " + JSON.toJSON(btcunspent).toString());
        JSONArray listunspentArray2 = JSONArray.parseArray(JSON.toJSON(btcunspent).toString());

        List<UTXO> utxos = new ArrayList<UTXO>();
        List<UTXO> utxos2 = new ArrayList<UTXO>();

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
            utxos.add(uo);
            usdtBTCTotal +=uo.getValue().getValue();
        }
        System.out.println("usdt total : " + usdtBTCTotal);


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
            utxos2.add(uo);
            btcBTCTotal +=uo.getValue().getValue();
        }
        System.out.println("btc  total : " + btcBTCTotal);

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
        System.out.println("fee: " + fee);

        // 如果需要找零 消费列表总金额 - 已经转账的金额 - 手续费
        long leave  = (usdtBTCTotal + btcBTCTotal) - fee.getValue() - 1092;
        System.out.println("找零数额: " + leave);
        // omni交易脚本
        tx.addOutput(Coin.valueOf(546L), new Script(Utils.HEX.decode(usdtHex)));
        // 也转入微量usdt
        tx.addOutput(Coin.valueOf(546L), receiveAddress);
        // 找零
        tx.addOutput(Coin.valueOf(leave), feeAddress);
        // create usdt utxo data
        for (UTXO utxo : utxos) {
            TransactionOutPoint outPoint = new TransactionOutPoint(params, utxo.getIndex(), utxo.getHash());
            tx.addSignedInput(outPoint, utxo.getScript(), usdtKey, Transaction.SigHash.ALL, true);
        }

        // create btc utxo data
        for (UTXO utxo : utxos2) {
            TransactionOutPoint outPoint = new TransactionOutPoint(params, utxo.getIndex(), utxo.getHash());
            tx.addSignedInput(outPoint, utxo.getScript(), btcKey, Transaction.SigHash.ALL, true);
        }

        Context context = new Context(params);
        tx.getConfidence().setSource(TransactionConfidence.Source.NETWORK);
        tx.setPurpose(Transaction.Purpose.USER_PAYMENT);

        System.out.println("=== [USDT] sign success,hash is :{} ===" + tx.getTxId());
        //这是签名之后的原始交易，直接去广播就行了
//        try {
//            String data = new String(Hex.encodeHex(tx.bitcoinSerialize()));
//            System.out.println(data);
//            Object sendrawtransaction = utils.sendrawtransaction(data);
//            System.out.println("sendrawtransaction: " + JSON.toJSON(sendrawtransaction).toString());
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
    }
}
