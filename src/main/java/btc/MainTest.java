package btc;

import java.util.concurrent.locks.Lock;

public class MainTest {
    public static void main(String[] args){

        BtcPollingThread pollingThread = new BtcPollingThread();
        pollingThread.start();
        int i = 1;

        while(true)
        {
            // 检测到新区块编号时加入
            BtcPollingThread.queue.offer(new BtcMonitor(i));
            i++;

            // 有消息入队后激活轮询线程
            synchronized (Lock.class)
            {
                Lock.class.notify();
            }
            try {
                // 每millis执行一次
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}