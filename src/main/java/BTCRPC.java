import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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
            
//            挖矿到地址
//            Object blocks = utils.generatetoaddress(1, Constants.coinbase);
//            System.out.println("generatetoaddress: " + JSON.toJSON(blocks));
//
            // 当前区块高度
//            Object blockCountObj = utils.getblockcount();
//            Integer blockCount = Integer.valueOf(blockCountObj.toString());
//            System.out.println("blockCount: " + blockCount);

            // 通过区块编号查询区块hash
//            String blockhash = utils.getblockhash(blockCount).toString();
//            System.out.println("blockhash: " + blockhash);
//
//            // 通过区块hash查询区块信息
//            Object block = utils.getblock(blockhash);
//            System.out.println("block: " + JSONObject.toJSON(block));

//            Object importaddress = utils.importaddress("tb1qkhd93txhk2awl8y2e5h2w5h9m60drdps6a786x");
//            System.out.println("importaddress: " + JSONObject.toJSON(importaddress));

//            String address = "2N7QJy6d2rGRVFR34i7uSgaF4tuFPZexLQq";
//            JSONObject importI = new JSONObject();
//            importI.put("address",address);
//            JSONObject importO = new JSONObject();
//            importO.put("scriptPubKey",importI);
//            importO.put("label", address);
//            importO.put("timestamp",0);
//            JSONArray array = new JSONArray();
//            array.add(importO);
//            System.out.println(array.toString());
//
//            JSONObject rescan = new JSONObject();
//            rescan.put("rescan", false);
//
//            Object importmulti = utils.importmulti(array, rescan);
//            System.out.println("importmulti: " + JSONObject.toJSON(importmulti));

            // 通过交易hash查询交易详情
//            Object transaction = utils.gettransaction("4629974bb6c106ea3c5243af217832b04601782120a3f3551d063c31d44e06c0");
//            System.out.println("transaction: " + JSON.toJSON(transaction));
//
//            Object omni_getallbalancesforaddress = utils.omni_getallbalancesforaddress(coinbase);
//            System.out.println("omni_getallbalancesforaddress: " + JSON.toJSON(omni_getallbalancesforaddress));
////
////            // 返回交易池信息
//            Object getrawmempool = utils.getrawmempool();
//            System.out.println("getrawmempool: " + JSON.toJSON(getrawmempool));
//
////            // 账户与余额列表
//            Object listaccounts = utils.listaccounts();
//            System.out.println("listaccounts: " + JSON.toJSON(listaccounts));
//            // 通过地址查询utxo列表
            Object listunspent = utils.listunspent(0,9999, new String[]{Constants.A02},true);
            System.out.println("listunspent size: " + JSONArray.parseArray(JSON.toJSON(listunspent).toString()).size() +
                    ";listunspent: " + JSON.toJSON(listunspent));

            listunspent = utils.listunspent(0,9999, new String[]{Constants.A03},true);
            System.out.println("listunspent size: " + JSONArray.parseArray(JSON.toJSON(listunspent).toString()).size() +
                    ";listunspent: " + JSON.toJSON(listunspent));

            listunspent = utils.listunspent(0,9999, new String[]{"bcrt1qlpe7khkm8uqcej2m8393rqj8zx5l8qy2nwrlw9"},true);
            System.out.println("listunspent size: " + JSONArray.parseArray(JSON.toJSON(listunspent).toString()).size() +
                    ";listunspent: " + JSON.toJSON(listunspent));

//          utils.sendrawtransaction("01000000017cd2e7e70b6bca946b5f83608ff20b8b7ac0acc1d41685d9b388c6556b72672d000000006a473044022008ffbe8c640a2d0a15ce7c35c15b35385b2bf174f526a3bbe2c9de42c354cfa502206dc6f55ccbcde6e3197571a8374a0b4d09a11f67c89534ac557d1faea82398198121031eaff266f19df29cdd414261c88fce6d78b82318f111438d3f374f8a7d12e804ffffffff020065cd1d000000001976a914d2704b94556bcb0f319acaa8bc0e94cbf9ea9eab88ac00b33f71000000001976a9144989239b79274bf72945bf0fb17ac7db5647ba8888ac00000000");

            // 转账操作
//            Object tx = utils.sendfrom("coinbase", Constants.A01,100);
//            System.out.println("tx: " + JSON.toJSON(tx));

            // 导出私钥
//            Object dumpprivkey = utils.dumpprivkey(Constants.coinbase);
//            System.out.println("address: "+ Constants.coinbase+" - dumpprivkey: " + JSON.toJSON(dumpprivkey));
//             dumpprivkey = utils.dumpprivkey(Constants.A01);
//            System.out.println("address: "+ Constants.A01+" - dumpprivkey: " + JSON.toJSON(dumpprivkey));
//             dumpprivkey = utils.dumpprivkey(Constants.A02);
//            System.out.println("address: "+ Constants.A02+" - dumpprivkey: " + JSON.toJSON(dumpprivkey));
//             dumpprivkey = utils.dumpprivkey(Constants.A03);
//            System.out.println("address: "+ Constants.A03+" - dumpprivkey: " + JSON.toJSON(dumpprivkey));

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
