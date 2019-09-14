import com.alibaba.fastjson.JSONArray;
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
     * importaddress "address" ( "label" rescan p2sh )
     *
     * @param address
     * @return
     * @throws Throwable
     */
    public Object importaddress(String address) throws Throwable {
        return client.invoke("importaddress", new Object[]{address}, Object.class);
    }

    /**
     * importmulti "requests" ( "options" )
     *
     * @param requests
     * @return
     * @throws Throwable
     */
    public Object importmulti(JSONArray requests, Object option) throws Throwable {
        return client.invoke("importmulti", new Object[]{requests, option}, Object.class);

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
     *  listunspent ( minconf maxconf  ["addresses",...] [include_unsafe] [query_options])
     * @param minconf
     * @param maxconf
     * @param addresses
     * @return
     * @throws Throwable
     */
    public Object listunspent(int minconf, int maxconf, String [] addresses, boolean include_unsafe ) throws Throwable {
        return client.invoke("listunspent", new Object[]{minconf, maxconf, addresses}, Object.class);
    }

    /**
     * listlockunspent
     *
     * @return
     * @throws Throwable
     */
    public Object listlockunspent() throws Throwable {
        return client.invoke("listlockunspent", new Object[]{}, Object.class);
    }

    /**
     * listaddressgroupings
     *
     * @return
     * @throws Throwable
     */
    public Object listaddressgroupings() throws Throwable {
        return client.invoke("listaddressgroupings", new Object[]{}, Object.class);
    }


    /**
     * 生成新的接收地址
     *
     * @return getnewaddress
     * @throws Throwable
     */
    public Object getnewaddress(String name) throws Throwable {
        return client.invoke("getnewaddress", new Object[]{name}, Object.class);
    }

    /**
     * 获取钱包信息
     *
     * @return
     * @throws Throwable
     */
    public Object getinfo() throws Throwable {
        return client.invoke("getinfo", new Object[]{}, Object.class);
    }

    /**
     * 获取钱包信息
     *
     * @return
     * @throws Throwable
     */
    public Object getwalletinfo() throws Throwable {
        return client.invoke("getwalletinfo", new Object[]{}, Object.class);
    }

    /**
     * 获取钱包信息
     *
     * @return
     * @throws Throwable
     */
    public Object listaccounts() throws Throwable {
        return client.invoke("listaccounts", new Object[]{}, Object.class);
    }

    /**
     * 获取钱包信息
     *
     * @return
     * @throws Throwable
     */
    public Object getbalance(String account, int confirmations,boolean watchOnlyIncl) throws Throwable {
        return client.invoke("getbalance", new Object[]{account, confirmations, watchOnlyIncl}, Object.class);
    }

    /**
     * getblock "hash" ( verbose )
     *
     * @return
     * @throws Throwable
     */
    public Object getblock(String hash) throws Throwable {
        return client.invoke("getblock", new Object[]{hash}, Object.class);
    }

    /**
     * 通过区块编号获取区块Hash
     *
     * @return
     * @throws Throwable
     */
    public Object getblockhash(int blockNumber) throws Throwable {
        return client.invoke("getblockhash", new Object[]{blockNumber}, Object.class);
    }

    /**
     * 获取区块高度getblockcount
     *
     * @return
     * @throws Throwable
     */
    public Object getblockcount() throws Throwable {
        return client.invoke("getblockcount", new Object[]{}, Object.class);
    }

    /**
     * 通过账户名获得账户地址 getAddressesByAccount
     *
     * @return
     * @throws Throwable
     */
    public Object getaddressesbyaccount(String account) throws Throwable {
        return client.invoke("getaddressesbyaccount", new Object[]{account}, Object.class);
    }

    /**
     * gettransaction "txid" ( includeWatchonly )
     *
     * @return
     * @throws Throwable
     */
    public Object gettransaction(String txid) throws Throwable {
        return client.invoke("gettransaction", new Object[]{txid}, Object.class);
    }

    /**
     * generatetoaddress numblocks address (maxtries)
     *
     * @return
     * @throws Throwable
     */
    public Object generatetoaddress (int number,String address) throws Throwable {
        return client.invoke("generatetoaddress", new Object[]{number, address}, Object.class);
    }

    /**
     * sendtoaddress "address" amount ( "comment" "comment_to" subtractfeefromamount replaceable conf_target "estimate_mode")
     *
     * @return
     * @throws Throwable
     */
    public Object sendtoaddress (String address, int number) throws Throwable {
        return client.invoke("sendtoaddress", new Object[]{address, number}, Object.class);
    }


    /**
     *
     * sendfrom "fromaccount" "toaddress" amount ( minconf "comment" "comment_to" )
     * @return
     * @throws Throwable
     */
    public Object sendfrom (String from , String to, float number) throws Throwable {
        return client.invoke("sendfrom", new Object[]{from, to , number}, Object.class);
    }


    /**
     *
     * getrawmempool ( verbose )
     * @return
     * @throws Throwable
     */
    public Object getrawmempool () throws Throwable {
        return client.invoke("getrawmempool", new Object[]{}, Object.class);
    }

    /**
     *
     * dumpprivkey "address"
     * @return
     * @throws Throwable
     */
    public Object dumpprivkey (String address) throws Throwable {
        return client.invoke("dumpprivkey", new Object[]{address}, Object.class);
    }


    /**
     *
     * createrawtransaction [{"txid":"id","vout":n},...] [{"address":amount},{"data":"hex"},...] ( locktime ) ( replaceable )
     * @return
     * @throws Throwable
     */
    public Object createrawtransaction (JSONArray utxos, JSONArray outputs) throws Throwable {
        return client.invoke("createrawtransaction", new Object[]{utxos, outputs}, Object.class);
    }

    /**
     *
     * decoderawtransaction "hexstring" ( iswitness )
     * @return
     * @throws Throwable
     */
    public Object decoderawtransaction (String hexstring) throws Throwable {
        return client.invoke("decoderawtransaction", new Object[]{hexstring}, Object.class);
    }

    /**
     *
     * signrawtransactionwithkey "hexstring" ["privatekey1",...] ( [{"txid":"id","vout":n,"scriptPubKey":"hex","redeemScript":"hex"},...] sighashtype )
     * @return
     * @throws Throwable
     */
    public Object signrawtransactionwithkey (String hexstring, JSONArray privkes, JSONArray utxos) throws Throwable {
        return client.invoke("signrawtransactionwithkey", new Object[]{hexstring, privkes, utxos}, Object.class);
    }

    /**
     *
     * sendrawtransaction "hexstring" ( allowhighfees )
     * @return
     * @throws Throwable
     */
    public Object sendrawtransaction (String hexstring) throws Throwable {
        return client.invoke("sendrawtransaction", new Object[]{hexstring}, Object.class);
    }




    /**
     * 通过地址获取 omni 余额
     * omni_getbalance "address" propertyid
     * @return
     * @throws Throwable
     */
    public Object omni_getbalance(String account, int propertyid) throws Throwable {
        return client.invoke("omni_getbalance", new Object[]{account, propertyid}, Object.class);
    }

    /**
     * 通过地址获取 omni 余额
     * omni_getallbalancesforaddress "address"
     * @return
     * @throws Throwable
     */
    public Object omni_getallbalancesforaddress(String address) throws Throwable {
        return client.invoke("omni_getallbalancesforaddress", new Object[]{address}, Object.class);
    }

    /**
     * 通过地址历史交易列表
     * omni_listtransactions ( "address" count skip startblock endblock )
     * @return
     * @throws Throwable
     */
    public Object omni_listtransactions(String address, int count, int skip, int startblock, int endblock) throws Throwable {
        return client.invoke("omni_listtransactions", new Object[]{address,count,skip ,startblock,endblock}, Object.class);
    }

    /**
     * omni转账操作
     * omni_funded_send "fromaddress" "toaddress" propertyid "amount" "feeaddress"
     * @return
     * @throws Throwable
     */
    public Object omni_funded_send(String fromaddress, String toaddress, int propertyid, int amount, String feeaddress) throws Throwable {
        return client.invoke("omni_funded_send", new Object[]{fromaddress,toaddress,propertyid ,amount,feeaddress}, Object.class);
    }

    /**
     * omni转账操作
     * omni_funded_sendall "fromaddress" "toaddress" ecosystem "feeaddress"
     * @return
     * @throws Throwable
     */
    public Object omni_funded_sendall(String fromaddress, String toaddress, String ecosystem, String feeaddress) throws Throwable {
        return client.invoke("omni_funded_sendall", new Object[]{ fromaddress, toaddress, ecosystem ,feeaddress}, Object.class);
    }

    /**
     * omni转账操作
     * omni_send "fromaddress" "toaddress" propertyid "amount" ( "redeemaddress" "referenceamount" )
     * @return
     * @throws Throwable
     */
    public Object omni_send(String fromaddress, String toaddress, int propertyid, int amount, String feeaddress) throws Throwable {
        return client.invoke("omni_send", new Object[]{fromaddress,toaddress,propertyid ,amount,feeaddress}, Object.class);
    }




}