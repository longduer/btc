package btc;

public class BtcMonitor {

    // 定义消息内容 这里我们只监听最新区块编号
    private int blockNumber;

    // 构造函数
    public BtcMonitor(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    // 同步区块及交易
    public void sycn() {
        System.out.println("当前区块号: " + blockNumber);
    }
}