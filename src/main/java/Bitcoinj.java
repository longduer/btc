import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Bitcoinj {

    public static void main(String[] args) {

        // RPC
        CoinUtils utils = null;
        try {
            utils = CoinUtils.getInstance();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        // 转账数量
        float amount = 5;
        System.out.println("amount : " + amount);

        // 手续费
        float fee = 0.01f;
        System.out.println("fee : " + fee);

        //预总消耗 = 转账数量+手续费
        float consumeMoney = amount+fee;

        float utxoMoney = 0;
        Object listunspent = null;
        JSONArray listunspentArray = null;
        // RPC创建交易时的utxo
        JSONArray utxoJsonArray = new JSONArray();

        //
        JSONArray utxoSignJsonArray = new JSONArray();

        try {
            listunspent = utils.listunspent(
                    0,
                    99999,
                    new String[]{Constants.coinbase},
                    false);
            listunspentArray = JSONArray.parseArray(JSON.toJSON(listunspent).toString());
            System.out.println(
                    "size:" + listunspentArray.size() +
                            ";listunspent: " + JSON.toJSON(listunspent).toString());

            // 遍历未花费的交易
            for (int i = 0; i < listunspentArray.size(); i++) {
                UnSpentUtxo2 vo = JSON.parseObject(listunspentArray.get(i).toString(), UnSpentUtxo2.class);
                utxoMoney += vo.getAmount();
                JSONObject utxoJson = new JSONObject();
                JSONObject utxoSignJson = new JSONObject();

                utxoJson.put("txid", vo.getTxid());
                utxoJson.put("vout", vo.getVout());
                utxoJsonArray.add(utxoJson);
                utxoSignJson.put("scriptPubKey", vo.getScriptPubKey());
                utxoSignJson.put("redeemScript", vo.getRedeemScript());
                utxoSignJson.put("amount", vo.getAmount());
                utxoSignJson.putAll(utxoJson);
                utxoSignJsonArray.add(utxoSignJson);
                if (utxoMoney>= consumeMoney) {
                    break;
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        System.out.println("utxoMoney : " + utxoMoney + ";用到的utxo: " + utxoJsonArray.size());

        JSONArray outputs = new JSONArray();
        // 转出
        JSONObject output = new JSONObject();
        output.put(Constants.A01,amount);
        outputs.add(output);
        // 找零
        float leave = utxoMoney - consumeMoney;
        if (leave > 0) {
            output = new JSONObject();
            output.put(Constants.coinbase,leave);
            outputs.add(output);
        }
        System.out.println("找零数额: " + leave);

        System.out.println("utxo " + utxoJsonArray);
        System.out.println("outputs " + outputs);

        System.out.println("utxoSign " + utxoSignJsonArray);

        try {
            Object createrawtransaction = utils.createrawtransaction(utxoJsonArray, outputs);
            System.out.println("createrawtransaction: " + createrawtransaction.toString());

            JSONArray privKeys = new JSONArray();
            privKeys.add(Constants.coinbasePrivate);

            Object signrawtransaction = utils.signrawtransactionwithkey(createrawtransaction.toString(), privKeys ,utxoSignJsonArray);
            JSONObject hex = JSONObject.parseObject(JSON.toJSON(signrawtransaction).toString());
            System.out.println("signrawtransaction: " + hex.get("hex"));

//            Object sendrawtransaction = utils.sendrawtransaction(hex.get("hex").toString());
//            System.out.println("sendrawtransaction: " + JSON.toJSON(sendrawtransaction).toString());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
