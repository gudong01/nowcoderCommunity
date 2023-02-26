package com.nowcoder.community;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueTest {

    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
        new Thread(new Producer(queue)).start();
        new Thread(new Consumer(queue)).start();
        new Thread(new Consumer(queue)).start();
        new Thread(new Consumer(queue)).start();
    }
}

class Producer implements Runnable {

    private BlockingQueue<Integer> q;

    public Producer(BlockingQueue<Integer> q) {
        this.q = q;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; ++i) {
                Thread.sleep(20);
                q.put(i);
                System.out.println(Thread.currentThread().getName() + " 生产 " + q.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Consumer implements Runnable {

    private BlockingQueue<Integer> q;

    public Consumer(BlockingQueue<Integer> q) {
        this.q = q;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(new Random().nextInt(1000));
                q.take();
                System.out.println(Thread.currentThread().getName() + " 消费 " + q.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
