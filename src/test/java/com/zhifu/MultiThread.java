package com.zhifu;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by thinkpad on 2016/7/28.
 */
class MyThread extends Thread{
    private int tid;

    public MyThread(int tid){
        this.tid=tid;
    }

    @Override
    public void run(){
        try{
            for(int i=0;i<10;i++){
                Thread.sleep(1000);
                System.out.println(String.format("thread one %d:%d ",tid,i));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String [] args){

    }
}

/**
 * 使用同步队列
 * 消费者
 * */
class Consumer implements  Runnable{

    private BlockingQueue<String> q;

    public Consumer(BlockingQueue<String> q){
        this.q=q;
    }
    @Override
    public void run() {
        try{
            while(true){
                System.out.println(Thread.currentThread().getName()+":"+q.take());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

/**
 * 使用同步队列
 * 消费者
 * */
class Producter implements  Runnable{

    private BlockingQueue<String> q;

    public Producter(BlockingQueue<String> q){
        this.q=q;
    }
    @Override
    public void run() {
        try{
            for(int i=1;i<101;i++){
                q.put(String.valueOf(i));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

public class MultiThread{

    public static void testThread(){
        for(int i=1;i<11;i++){
            new MyThread(i).start();
        }
    }

    public static void testRunnable(){
        for( int i=0;i<11;i++){
            final int fi=i;
           new Thread( new Runnable(){
               @Override
               public void run() {
                   try{
                       for(int j=0;j<10;j++){
                           Thread.sleep(1000);
                           System.out.println(String.format("thread two %d:%d ",fi,j));
                       }
                   }catch (Exception e){
                       e.printStackTrace();
                   }
               }
           }).start();
        }
    }
    Object object=new Object();
    public void testSynchronized1(){
        synchronized (object){
            try{
                for(int j=0;j<10;j++){
                    Thread.sleep(1000);
                    System.out.println(String.format("thread three :%d ",j));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void testSynchronized2(){
        synchronized (object){
            try{
                for(int j=0;j<10;j++){
                    Thread.sleep(1000);
                    System.out.println(String.format("thread three :%d ",j));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void testSynchronized(){
        for( int i=0;i<11;i++){

            new Thread( new Runnable(){
                @Override
                public void run() {
                    testSynchronized1();
                    testSynchronized2();
                }
            }).start();
        }
    }

    public static void testBlockingQueue(){
        BlockingQueue<String> q=new ArrayBlockingQueue<String>(10);

        new Thread(new Producter(q)).start();
        new Thread(new Consumer(q),"consumer1").start();
        new Thread(new Consumer(q),"consumer2").start();
    }

    /**
     * 使用线程池
     *提交任务后就不用管了，线程池处理就行了
     * */
    public static  void testExcutor(){
       // ExecutorService executor=Executors.newSingleThreadExecutor();
        ExecutorService executor=Executors.newFixedThreadPool(2);

        executor.submit(new Runnable() {
            @Override
            public void run() {
                try{
                    for(int i=0;i<10;i++){
                        Thread.sleep(100);
                        System.out.println("thread one:"+i);
                    }
                }catch ( Exception e){
                    e.printStackTrace();
                }
            }
        });
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try{
                    for(int i=0;i<10;i++){
                        Thread.sleep(100);
                        System.out.println("thread two:"+i);
                    }
                }catch ( Exception e){
                    e.printStackTrace();
                }
            }
        });


    }

    private static int counter=0;

    private static AtomicInteger atomicInteger=new AtomicInteger(0);

    /**
     * 原子变量
     * 每次只有一个线程对其操作
     * */
    public static void testAtomic(){
        for(int i=0;i<10;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        Thread.sleep(1000);
                        for(int i=0;i<10;i++){
                            counter++;
                            System.out.println(counter+":"+atomicInteger.incrementAndGet());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static  void testFuture(){
        ExecutorService service=Executors.newSingleThreadExecutor();
        Future<Integer> future=service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Thread.sleep(2000);
                return 1;
            }
        });
        System.out.println("-------------");
        try{
            System.out.println(future.get());
            System.out.println("-------------");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String [] args){
        //testThread();
        //testRunnable();
        //testBlockingQueue();
        //testExcutor();
        //testAtomic();
        testFuture();
    }
}
