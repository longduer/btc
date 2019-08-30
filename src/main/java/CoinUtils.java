import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CoinUtils {

    private static CoinUtils instance;

    private static void init() throws Throwable {
        if (null == instance) {
            instance = new CoinUtils();
        }
    }

    private JsonRpcHttpClient client;

    public CoinUtils() throws Throwable {
        // 身份认证
        String cred = Base64.encode((Constants.RPC_USER + ":" + Constants.RPC_PASSWORD).getBytes());
        Map<String, String> headers = new HashMap<String, String>(2);
        headers.put("Authorization", "Basic " + cred);
        headers.put("Content-Type", "text/plain");

        client = new JsonRpcHttpClient(new URL("http://" + Constants.RPC_ALLOWIP + ":" + Constants.RPC_PORT), headers);
    }


    public static CoinUtils getInstance() throws Throwable {
        init();
        return instance;
    }

    /**
     * 验证地址是否存在
     *
     * @param address
     * @return
     * @throws Throwable
     */
    public Object validateaddress(String address) throws Throwable {
        return client.invoke("validateaddress", new Object[]{address}, Object.class);
    }


    /**
     * 如果钱包加密需要临时解锁钱包
     *
     * @param password
     * @param time
     * @return
     * @throws Throwable
     */
    public Object walletpassphase(String password, int time) throws Throwable {
        return client.invoke("walletpassphase", new Object[]{password, time}, Object.class);
    }

    /**
     * 转账到制定的账户中
     *
     * @param address
     * @param amount
     * @return
     * @throws Throwable
     */
    public Object sendtoaddress(String address, double amount) throws Throwable {
        return client.invoke("sendtoaddress", new Object[]{address, amount}, Object.class);
    }

    /**
     * 查询账户下的交易记录
     *
     * @param account
     * @param count
     * @param offset
     * @return
     * @throws Throwable
     */
    public Object listtransactions(String account, int count, int offset) throws Throwable {
        return client.invoke("listtransactions", new Object[]{account, count, offset}, Object.class);
    }

    /**
     * 获取地址下未花费的币量
     *
     * @param minconf
     * @param maxconf
     * @param address
     * @return
     * @throws Throwable
     */
    public Object listunspent(int minconf, int maxconf, String address) throws Throwable {
        String[] addresss = new String[]{address};
        return client.invoke("listunspent", new Object[]{minconf, maxconf, addresss}, Object.class);
    }

    /**
     * 生成新的接收地址
     *
     * @return
     * @throws Throwable
     */
    public Object getNewaddress() throws Throwable {
        return client.invoke("getnewaddress", new Object[]{}, Object.class);
    }

    /**
     * 获取钱包信息
     *
     * @return
     * @throws Throwable
     */
    public Object getInfo() throws Throwable {
        return client.invoke("getinfo", new Object[]{}, Object.class);
    }

    /**
     * 获取钱包信息
     *
     * @return
     * @throws Throwable
     */
    public Object getWalletInfo() throws Throwable {
        return client.invoke("getwalletinfo", new Object[]{}, Object.class);
    }

    /**
     * 获取钱包信息
     *
     * @return
     * @throws Throwable
     */
    public Object getListAccounts() throws Throwable {
        return client.invoke("listaccounts", new Object[]{}, Object.class);
    }

    /**
     * 获取钱包信息
     *
     * @return
     * @throws Throwable
     */
    public Object getBalance(String account, int confirmations,boolean watchOnlyIncl) throws Throwable {
        return client.invoke("getbalance", new Object[]{account, confirmations, watchOnlyIncl}, Object.class);
    }

    /**
     * 获取钱包信息
     *
     * @return
     * @throws Throwable
     */
    public Object getBlockhash(int blockNumber) throws Throwable {
        return client.invoke("getblockhash", new Object[]{blockNumber}, Object.class);
    }
}