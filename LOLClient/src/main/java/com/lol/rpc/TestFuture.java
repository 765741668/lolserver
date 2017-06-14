package com.lol.rpc;

import java.util.HashMap;
import java.util.concurrent.*;

public class TestFuture {

    public static void main(String args[]) throws InterruptedException, ExecutionException {
        TestFuture.sync();
//        TestFuture.async();
//        TestFuture.async2();
    }

    static void sync() {
        System.out.println("hello world");

        ExecutorService es = Executors.newCachedThreadPool();
        Future fu = es.submit(new Callable() {
            @Override
            public String call() throws Exception {
                Thread.sleep(3000);
                return "syn hello world";
            }
        });
        System.out.println("hello world1111111111");
        String result = "";
        try {
            System.out.println("hello world222222");
            result = (String) fu.get();
            System.out.println("hello world3333333");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(result);
        es.shutdown();
    }

    static void async() throws InterruptedException, ExecutionException {
        System.out.println("hello world");

        ExecutorService es = Executors.newFixedThreadPool(1);
        FutureTask<String> ft = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(3000);
                return "asyn hello world";
            }
        });
        es.submit(ft);
        System.out.println("hello world1111111111");
        String result = "";
        try {
            System.out.println("hello world222222");
            boolean flag = true;
            while(flag){
                if(ft.isDone() && !ft.isCancelled()){
                    result = ft.get();
                    System.out.println(result);
                    flag = false;
                }
            }
            System.out.println("hello world3333333");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        es.shutdown();
    }

    static void async2() throws InterruptedException, ExecutionException {
        System.out.println("====进入主线程执行任务");

        //通过线程池管理多线程
        ExecutorService threadPool = Executors.newCachedThreadPool();

        //线程池提交一个异步任务
        System.out.println("====提交异步任务");
        Future<HashMap<String,String>> future = threadPool.submit(new Callable<HashMap<String,String>>() {

            @Override
            public HashMap<String,String> call() throws Exception {

                System.out.println("异步任务开始执行....");
                Thread.sleep(2000);
                System.out.println("异步任务执行完毕，返回执行结果!!!!");

                return new HashMap<String,String>(){
                    {this.put("futureKey", "成功获取future异步任务结果");}
                };
            }

        });

        System.out.println("====提交异步任务之后，立马返回到主线程继续往下执行");
        Thread.sleep(1000);

        System.out.println("====此时需要获取上面异步任务的执行结果");

        boolean flag = true;
        while(flag){
            //异步任务完成并且未被取消，则获取返回的结果
            if(future.isDone() && !future.isCancelled()){
                HashMap<String,String> futureResult = future.get();
                System.out.println("====异步任务返回的结果是："+futureResult.get("futureKey"));
                flag = false;
            }
        }

        //关闭线程池
        if(!threadPool.isShutdown()){
            threadPool.shutdown();
        }
    }
}