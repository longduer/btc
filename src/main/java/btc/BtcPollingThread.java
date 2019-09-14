package btc;

import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.locks.Lock;

public class BtcPollingThread extends Thread implements Runnable {

    // 定义监听队列
    public static Queue<BtcMonitor> queue = new LinkedTransferQueue<BtcMonitor>();

    @Override
    public void run() {
        // 轮询
        while (true) {

            //非空时将数据取出并执行
            while (!queue.isEmpty()) {
                queue.poll().sycn();
            }

            //把队列中的消息全部打印完之后让线程阻塞
            synchronized (Lock.class)
            {
                try {
                    Lock.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}