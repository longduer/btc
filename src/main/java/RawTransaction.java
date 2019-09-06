//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.commons.codec.binary.Hex;
//import org.bitcoinj.core.*;
//import org.bitcoinj.params.RegTestParams;
//import org.bitcoinj.script.Script;
//
//import static org.bitcoinj.core.Utils.*;
//
//public class RawTransaction {
//
//    /**
//     * @param @param  privKey 私钥
//     * @param @param  recevieAddr 收款地址
//     * @param @param  formAddr 发送地址
//     * @param @param  amount 金额
//     * @param @param  fee 手续费(自定义 或者 默认)
//     * @param @param  unUtxos 未交易的utxo
//     * @param @return 参数
//     * @return char[]    返回类型
//     * @throws
//     * @Title: signTransaction
//     */
//    public static String signTransaction(String privKey,
//                                         String recevieAddr,
//                                         String formAddr,
//                                         long amount,
//                                         long fee,
//                                         List<UnSpentUtxo> unUtxos) {
//
//        // 获得regtest网络
//        NetworkParameters params = RegTestParams.get();
//
//
//        if (!unUtxos.isEmpty() && null != unUtxos) {
//            List<UTXO> utxos = new ArrayList<UTXO>();
//
//            // String to a private key
//            DumpedPrivateKey dumpedPrivateKey = DumpedPrivateKey.fromBase58(params, privKey);
//            ECKey key = dumpedPrivateKey.getKey();
//
//            // 接收地址
//            Address receiveAddress = Address.fromString(params, recevieAddr);
//
//            // 构建交易
//            Transaction tx = new Transaction(params);
//
//            tx.addOutput(Coin.valueOf(amount), receiveAddress); // 转出
//            // 如果需要找零 消费列表总金额 - 已经转账的金额 - 手续费
//            long value = unUtxos.stream().mapToLong(UnSpentUtxo::getValue).sum();
//            Address toAddress = Address.fromString(params, formAddr);
//            long leave = value - amount - fee;
//            if (leave > 0) {
//                tx.addOutput(Coin.valueOf(leave), toAddress);
//            }
//            // utxos is an array of inputs from my wallet
//            for (UnSpentUtxo unUtxo : unUtxos) {
//                utxos.add(new UTXO(Sha256Hash.wrap(unUtxo.getHash()),
//                        unUtxo.getTxN(),
//                        Coin.valueOf(unUtxo.getValue()),
//                        unUtxo.getHeight(),
//                        false,
//                        new Script(HEX.decode(unUtxo.getScript())),
//                        unUtxo.getAddress()));
//            }
//            for (UTXO utxo : utxos) {
//                TransactionOutPoint outPoint = new TransactionOutPoint(params, utxo.getIndex(), utxo.getHash());
//                // YOU HAVE TO CHANGE THIS
//                tx.addSignedInput(outPoint, utxo.getScript(), key, Transaction.SigHash.ALL, true);
//            }
//            Context context = new Context(params);
//            tx.getConfidence().setSource(TransactionConfidence.Source.NETWORK);
//            tx.setPurpose(Transaction.Purpose.USER_PAYMENT);
//
//            System.out.println("=== [BTC] sign success,hash is :{} ===" + tx.toString());
//            return new String(Hex.encodeHex(tx.bitcoinSerialize()));
//        }
//        return null;
//    }
//
//    public static void main(String [] args){
//        // 获得regtest网络
//        NetworkParameters params = RegTestParams.get();
//
//        List<UnSpentUtxo> us = new ArrayList<>();
//        UnSpentUtxo u = new UnSpentUtxo();
//        u.setAddress("2NGCTN5pVZ9iGmD3BwVWcC82dS91QZvXAqr");
//        u.setHash("8ecb6a0166e3c9907f9009f3f616732c7ce21c231fe58e6ad71573288b5172d0");
//        u.setHeight(101);
//        u.setScript("76a914a1806613a51a81966779e2fa1537013cf4cd2b1788ac");
//        u.setTxN(1);
//        u.setValue(100000);
//
//        us.add(u);
//
//        System.out.println(JSON.toJSONString(us));
//        String c = signTransaction("cNRE3D1pbPPvGs9wpZd3X9NuLsuUQPzPa7ktQyF1nhqBabraocU9", "mifiHFYFPk5cri4oneXVsRZJZKovvdDcjo", "mvEtuEqYPMrLaKjJ5nTZ57vQAoYUtVmMaQ", 400000, 10000, us);
//        System.out.println(c);
//
//    }
//}
