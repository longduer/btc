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
            // 创建新账户并指定账户名
//            Object address = utils.getnewaddress("coinbase");
//            System.out.println("coinbase: " + JSON.toJSON(address));
//            address = utils.getnewaddress("A01");
//            System.out.println("A01: " + JSON.toJSON(address));
//            address = utils.getnewaddress("A02");
//            System.out.println("A02: " + JSON.toJSON(address));
//            address = utils.getnewaddress("A03");
//            System.out.println("A03: " + JSON.toJSON(address));

////          挖矿到地址
//            Object blocks = utils.generatetoaddress(101, Constants.coinbase);
//            System.out.println("generatetoaddress: " + JSON.toJSON(blocks));
//
//            // 当前区块高度
//            Object blockCount = utils.getblockcount();
//            System.out.println("blockCount: " + JSON.toJSON(blockCount));
////
//            Object omni_getallbalancesforaddress = utils.omni_getallbalancesforaddress(coinbase);
//            System.out.println("omni_getallbalancesforaddress: " + JSON.toJSON(omni_getallbalancesforaddress));
////
////            // 返回交易池信息
//            Object getrawmempool = utils.getrawmempool();
//            System.out.println("getrawmempool: " + JSON.toJSON(getrawmempool));
//
////            // 账户与余额列表
            Object listaccounts = utils.listaccounts();
            System.out.println("listaccounts: " + JSON.toJSON(listaccounts));
////            // 通过地址查询utxo列表
//            Object listunspent = utils.listunspent(0,9999, new String[]{A02},true);
//            System.out.println("listunspent size: " + JSONArray.parseArray(JSON.toJSON(listunspent).toString()).size() +
//                    ";listunspent: " + JSON.toJSON(listunspent));

//          utils.sendrawtransaction("01000000017cd2e7e70b6bca946b5f83608ff20b8b7ac0acc1d41685d9b388c6556b72672d000000006a473044022008ffbe8c640a2d0a15ce7c35c15b35385b2bf174f526a3bbe2c9de42c354cfa502206dc6f55ccbcde6e3197571a8374a0b4d09a11f67c89534ac557d1faea82398198121031eaff266f19df29cdd414261c88fce6d78b82318f111438d3f374f8a7d12e804ffffffff020065cd1d000000001976a914d2704b94556bcb0f319acaa8bc0e94cbf9ea9eab88ac00b33f71000000001976a9144989239b79274bf72945bf0fb17ac7db5647ba8888ac00000000");

            // 转账操作
//            Object tx = utils.sendfrom("coinbase", A02,50f);
//            System.out.println("tx: " + JSON.toJSON(tx));

            // 通过区块编号查询区块hash
//            Object blockhash = utils.getblockhash(1);
//            System.out.println("blockhash: " + JSON.toJSON(blockhash));
//
//            // 通过区块hash查询区块信息
//            Object block = utils.getblock(JSON.toJSON(blockhash).toString());
//            System.out.println("block: " + JSON.toJSON(block));

            // 通过交易hash查询交易详情
//            Object transaction = utils.gettransaction("2bd4fde7e0556aa291eb8913a47383d661b25f3d510a854e7429e0518dc4acab");
//            System.out.println("transaction: " + JSON.toJSON(transaction));

            // 导出私钥
            Object dumpprivkey = utils.dumpprivkey(Constants.coinbase);
            System.out.println("address: "+ Constants.coinbase+" - dumpprivkey: " + JSON.toJSON(dumpprivkey));
             dumpprivkey = utils.dumpprivkey(Constants.A01);
            System.out.println("address: "+ Constants.A01+" - dumpprivkey: " + JSON.toJSON(dumpprivkey));
             dumpprivkey = utils.dumpprivkey(Constants.A02);
            System.out.println("address: "+ Constants.A02+" - dumpprivkey: " + JSON.toJSON(dumpprivkey));
             dumpprivkey = utils.dumpprivkey(Constants.A03);
            System.out.println("address: "+ Constants.A03+" - dumpprivkey: " + JSON.toJSON(dumpprivkey));

            // 通过账户名 查询账户地址
//            Object addresses = utils.getaddressesbyaccount("coinbase");
//            System.out.println("addresses: " + JSON.toJSON(addresses));

//            Object listaddressgroupings = utils.listaddressgroupings();
//            System.out.println("listaddressgroupings: " + JSON.toJSON(listaddressgroupings));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
