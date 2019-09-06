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

public class Omni2 {

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

        Object listunspent = null;
        try {
            listunspent = utils.listunspent(0,999999, new String[]{Constants.coinbase}, true);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("coinbase utxo列表: " + JSON.toJSON(listunspent).toString());
        JSONArray listunspentArray = JSONArray.parseArray(JSON.toJSON(listunspent).toString());

        // 私钥
        DumpedPrivateKey dumpedPrivateKey = DumpedPrivateKey.fromBase58(params, Constants.coinbasePrivate);
        ECKey ecKey = dumpedPrivateKey.getKey();

        // 接收地址
        Address receiveAddress = LegacyAddress.fromBase58(params, Constants.A01);
        // 接收地址
        Address fromAddress = LegacyAddress.fromBase58(params, Constants.coinbase);

        List<UTXO> utxos = new ArrayList<UTXO>();
        long totalMoney = 0;

        // 遍历未花费的交易
        for(int i =0; i<listunspentArray.size(); i++){
            UnSpentUtxo2 vo = JSON.parseObject(listunspentArray.get(i).toString(), UnSpentUtxo2.class);
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
            totalMoney +=uo.getValue().getValue();
        }

        Coin total = Coin.valueOf(totalMoney);
        System.out.println("total : " + total);

        Coin amount = Coin.valueOf(546L);
        System.out.println("amount : " + amount);

        Coin fee = Coin.CENT;
        System.out.println("fee : " + fee);

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
        tx.addOutput(amount, receiveAddress); // 转出
        tx.addOutput(Coin.valueOf(0L), new Script(Utils.HEX.decode(usdtHex)));

        // 如果需要找零 消费列表总金额 - 已经转账的金额 - 手续费
//        long leave  = totalMoney - amount.getValue() - fee.getValue();
        long leave  =  totalMoney - fee.getValue() - amount.getValue();
        tx.addOutput(Coin.valueOf(leave), fromAddress);
        System.out.println("找零数额: " + leave);

        for (UTXO utxo : utxos) {
//            TransactionOutPoint outPoint = new TransactionOutPoint(params, utxo.getIndex(), utxo.getHash());
//            // YOU HAVE TO CHANGE THIS
//            tx.addSignedInput(outPoint, utxo.getScript(), ecKey, Transaction.SigHash.ALL, false);
            tx.addInput(utxo.getHash(), utxo.getIndex(), utxo.getScript());
        }
        //下面就是签名
        for (int i = 0; i < utxos.size(); i++) {
            UTXO output = utxos.get(i);
            TransactionInput transactionInput = tx.getInput(i);
            Script scriptPubKey = ScriptBuilder.createOutputScript(LegacyAddress.fromBase58(params, output.getAddress()));
            Sha256Hash hash = tx.hashForSignature(i, scriptPubKey, Transaction.SigHash.ALL, false);
            ECKey.ECDSASignature ecSig = ecKey.sign(hash);

            TransactionSignature txSig = new TransactionSignature(ecSig, Transaction.SigHash.ALL, false);
            transactionInput.setScriptSig(ScriptBuilder.createInputScript(txSig, ecKey));
        }
        System.out.println("tx.getTxId(): " + tx.getTxId());
        //这是签名之后的原始交易，直接去广播就行了
        try {
            String data = new String(Hex.encodeHex(tx.bitcoinSerialize()));
            Object sendrawtransaction = utils.sendrawtransaction(data);
            System.out.println("sendrawtransaction: " + JSON.toJSON(sendrawtransaction).toString());

            //            挖矿到地址
            Object blocks = utils.generatetoaddress(1, Constants.coinbase);
            System.out.println("generatetoaddress: " + JSON.toJSON(blocks));


        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        // 通过账户名查询USDT余额
        Object omni_getbalance = null;
        Object btc_getbalance = null;
        try {

            omni_getbalance = utils.omni_getallbalancesforaddress(Constants.coinbase);
            btc_getbalance = utils.getbalance("coinbase", 1, true);

            System.out.println("USDT余额: " + JSON.toJSON(omni_getbalance));
            System.out.println("BTC余额 : " + JSON.toJSON(btc_getbalance));

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
