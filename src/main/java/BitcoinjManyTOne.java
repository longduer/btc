import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class BitcoinjManyTOne {

    public static void main(String[] args) {
        // RPC
        CoinUtils utils = null;
        try {
            utils = CoinUtils.getInstance();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        // 手续费 由账户1出
        float fee = 0.01f;
        System.out.println("fee : " + fee);

        float utxoMoney1 = 0;
        Object listunspent1 = null;
        JSONArray listunspentArray1 = null;
        // RPC创建交易时的utxo
        JSONArray utxoJsonArray1 = new JSONArray();
        JSONArray utxoSignJsonArray1 = new JSONArray();

        try {
            listunspent1 = utils.listunspent(
                    0,
                    99999,
                    new String[]{Constants.A02},
                    false);
            listunspentArray1 = JSONArray.parseArray(JSON.toJSON(listunspent1).toString());
            System.out.println(
                    "size1:" + listunspentArray1.size() +
                            ";listunspent1: " + JSON.toJSON(listunspent1).toString());

            // 遍历未花费的交易
            for (int i = 0; i < listunspentArray1.size(); i++) {
                UnSpentUtxo2 vo = JSON.parseObject(listunspentArray1.get(i).toString(), UnSpentUtxo2.class);
                utxoMoney1 += vo.getAmount();
                JSONObject utxoJson = new JSONObject();
                JSONObject utxoSignJson = new JSONObject();

                utxoJson.put("txid", vo.getTxid());
                utxoJson.put("vout", vo.getVout());
                utxoJsonArray1.add(utxoJson);
                utxoSignJson.put("scriptPubKey", vo.getScriptPubKey());
                utxoSignJson.put("redeemScript", vo.getRedeemScript());
                utxoSignJson.put("amount", vo.getAmount());
                utxoSignJson.putAll(utxoJson);
                utxoSignJsonArray1.add(utxoSignJson);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("utxoMoney1 : " + utxoMoney1 + ";用到的utxo1: " + utxoJsonArray1.size());



        float utxoMoney2 = 0;
        Object listunspent2 = null;
        JSONArray listunspentArray2 = null;
        // RPC创建交易时的utxo
        JSONArray utxoJsonArray2 = new JSONArray();
        JSONArray utxoSignJsonArray2 = new JSONArray();
        try {
            listunspent2 = utils.listunspent(
                    0,
                    99999,
                    new String[]{Constants.A03},
                    false);
            listunspentArray2 = JSONArray.parseArray(JSON.toJSON(listunspent2).toString());
            System.out.println(
                    "size2:" + listunspentArray2.size() +
                            ";listunspent2: " + JSON.toJSON(listunspent2).toString());

            // 遍历未花费的交易
            for (int i = 0; i < listunspentArray2.size(); i++) {
                UnSpentUtxo2 vo = JSON.parseObject(listunspentArray2.get(i).toString(), UnSpentUtxo2.class);
                utxoMoney2 += vo.getAmount();
                JSONObject utxoJson = new JSONObject();
                JSONObject utxoSignJson = new JSONObject();

                utxoJson.put("txid", vo.getTxid());
                utxoJson.put("vout", vo.getVout());
                utxoJsonArray2.add(utxoJson);
                utxoSignJson.put("scriptPubKey", vo.getScriptPubKey());
                utxoSignJson.put("redeemScript", vo.getRedeemScript());
                utxoSignJson.put("amount", vo.getAmount());
                utxoSignJson.putAll(utxoJson);
                utxoSignJsonArray2.add(utxoSignJson);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("utxoMoney2 : " + utxoMoney2 + ";用到的utxo2: " + utxoJsonArray2.size());


        //预总消耗 = 转账数量+手续费
        float consumeMoney = utxoMoney1 + utxoMoney2;

        System.out.println("consumeMoney: " + consumeMoney);

        JSONArray outputs = new JSONArray();
        // 转出
        JSONObject output = new JSONObject();
        output.put("bcrt1qlpe7khkm8uqcej2m8393rqj8zx5l8qy2nwrlw9", consumeMoney-fee);
        outputs.add(output);


        utxoJsonArray1.addAll(utxoJsonArray2);
        try {
            Object createrawtransaction = utils.createrawtransaction(utxoJsonArray1, outputs);
            System.out.println("createrawtransaction: " + createrawtransaction.toString());

            JSONArray privKeys = new JSONArray();
            privKeys.add(Constants.A02Private);
            privKeys.add(Constants.A03Private);

            utxoSignJsonArray1.addAll(utxoSignJsonArray2);

            Object signrawtransaction = utils.signrawtransactionwithkey(createrawtransaction.toString(), privKeys , utxoSignJsonArray1);
            JSONObject hex = JSONObject.parseObject(JSON.toJSON(signrawtransaction).toString());
            System.out.println("signrawtransaction: " + hex.get("hex"));

            Object sendrawtransaction = utils.sendrawtransaction(hex.get("hex").toString());
            System.out.println("sendrawtransaction: " + JSON.toJSON(sendrawtransaction).toString());

            //挖矿到地址
            Object blocks = utils.generatetoaddress(1, Constants.coinbase);
            System.out.println("generatetoaddress: " + JSON.toJSON(blocks));

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
