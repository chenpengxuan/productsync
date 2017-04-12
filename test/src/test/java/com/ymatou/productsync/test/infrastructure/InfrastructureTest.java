package com.ymatou.productsync.test.infrastructure;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * Created by zhangyong on 2017/2/16.
 */

public class InfrastructureTest {
    @Test
    public void testMultiThread() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1000);
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                Counter.inc();
                latch.countDown();
            }).start();
        }
        latch.await();
        System.out.println(Counter.count);
    }
}

class Counter {
    public volatile static int count = 0;

    public static void inc() {
        //这里延迟5毫秒，使得结果明显
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
        }
        synchronized(Counter.class) {
        count++;
        }
    }
}
