import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

public class BTCRPC {

    public static void main(String[] args){
        CoinUtils utils = null;
        try {
            utils = CoinUtils.getInstance();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        try {

            Object info = utils.getInfo();
            System.out.println(JSON.toJSON(info));

            Object blockhash = utils.getBlockhash(1);
            System.out.println("walletInfo: " + JSON.toJSON(blockhash));

            Object walletInfo = utils.getWalletInfo();
            System.out.println("walletInfo: " + JSON.toJSON(walletInfo));

            Object balance = utils.getBalance("omni02", 1, true);
            System.out.println("balance: " + JSON.toJSON(balance));

            Object listaccounts = utils.getListAccounts();
            System.out.println("listaccounts: " + JSON.toJSON(listaccounts));

            Object listunspent = utils.listunspent(0,9999,"n36oJBqaJsmXFPpvYhzxJXcW2o4iQRn2Zs");
            System.out.println("listunspent: " + JSON.toJSON(listunspent));
            System.out.println("listunspent size: " + JSONArray.parseArray(JSON.toJSON(listunspent).toString()).size());



        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
