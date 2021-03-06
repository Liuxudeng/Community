package com.nowcoder.community;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueTests {
    public static void main(String[] args) {
        BlockingQueue queue = new ArrayBlockingQueue(10);

        new Thread(new Producer(queue)).start();

        new Thread(new Comsumer(queue)).start();
        new Thread(new Comsumer(queue)).start();
        new Thread(new Comsumer(queue)).start();


    }



}


class Producer implements Runnable{

    private BlockingQueue<Integer> queue;

    public Producer(BlockingQueue<Integer> queue){
        this.queue = queue;
    }

    @Override
    public void run() {


            try {
                for (int i = 0; i < 100; i++) {
                Thread.sleep(20);
                    queue.put(i);
                    System.out.println(Thread.currentThread().getName()+"生产："+queue.size());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


    }
}

class Comsumer implements Runnable{
    private BlockingQueue<Integer> queue;
    public Comsumer(BlockingQueue<Integer> queue){
        this.queue = queue;
    }

    @Override
    public void run() {

            try {
              while (true){
                  Thread.sleep(new Random().nextInt(1000));
                 int i = queue.take();
                  System.out.println(Thread.currentThread().getName()+"消费了："+queue.size());
              }


            } catch (InterruptedException e) {
                e.printStackTrace();
            }



    }
}
