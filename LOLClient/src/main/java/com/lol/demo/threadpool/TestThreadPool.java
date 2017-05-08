/******************************************************************************
 *
 * Module Name:  threadpool - TestFixedThreadPool.java
 * Version: 1.0.0
 * Original Author: java
 * Created Date: Nov 26, 2015
 * Last Updated By: java
 * Last Updated Date: Nov 26, 2015
 * Description: 
 *
 *******************************************************************************

 COPYRIGHT  STATEMENT

 Copyright(c) 2011
 by The Hong Kong Jockey Club

 All rights reserved. Copying, compilation, modification, distribution
 or any other use whatsoever of this material is strictly prohibited
 except in accordance with a Software License Agreement with
 The Hong Kong Jockey Club.

 ******************************************************************************/
/**
 *
 */
package com.lol.demo.threadpool;

import com.lol.demo.nio.NioUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @author java
 */
public class TestThreadPool {
    private static final Logger logger = LoggerFactory.getLogger(TestThreadPool.class);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        /**
         * 1
         * //
         */
        // callablesPoolInt();
        // futureTaskPoolInt();
        // callablesPoolStr();
        // futureTaskPoolStr();
        // completableFuture_handleAsync2();
        /**
         * 2
         */
        // ScheduledPool();
        // /**
        // * 3
        // */
        // fixedPool();
        //
        // /**
        // * 4
        // */
        // cachePool();
        // /**
        // * 5
        // */
        // singlePool();
        // /**
        // * 69
        // */
        // cyclicBarrierPool();
        // /**
        // * 7
        // */
        // countDownLatchPool();
        /**
         * 8
         */
        // semaphorePool();
        /**
         * 9
         */
        // completableFuture_runAsync();
        // completableFuture_runAsync2();
//         completableFuture_supplyAsync();
//        completableFuture_handleAsync();
        completableFuture_thenAplyAsync();
//        completableFuture_thenAcceptAsync();
//        completableFuture_thenAcceptBothAsync();
//        completableFuture_thenComposeAsync();
//        completableFuture_thenCombineAsync();
//        completableFuture_applyToEitherAsync();
//        completableFuture_acceptEitherAsync();
//        completableFuture_allof();
        /**
         * 10
         */

        System.out.println("Main end");
    }

    public static int getMoreData() {
        int rand = new Random().nextInt(100);
        System.out.println(rand);
        return rand;
    }

    private static void completableFuture_runAsync() throws InterruptedException, ExecutionException {
        // 1.no executor, will be call ForkJoinPool.commonpool() else will be call executor;
        // 2.return void.
        Runnable runnable = () -> {
            try {
                for (int i = 0; i < 5; i++) {
                    System.out.println(Thread.currentThread().getName() + " -------- task [" + i + "]");
                    int a = new Random().nextInt(4);
                    Thread.sleep(1000 * (a == 0 ? 1 : a));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        CompletableFuture<Void> result = CompletableFuture.runAsync(runnable);
        result.get();

    }

    private static void completableFuture_runAsync2() {
        // 1.return void.
        // 2.no executor, will be call ForkJoinPool.commonpool() else will be call executor;
        Runnable runnable = () -> {
            try {
                for (int i = 0; i < 5; i++) {
                    System.out.println(Thread.currentThread().getName() + " -------- task [" + i + "]");
                    int a = new Random().nextInt(4);
                    Thread.sleep(1000 * (a == 0 ? 1 : a));
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        ExecutorService es = Executors.newFixedThreadPool(1);
        CompletableFuture.runAsync(runnable, es);
        es.shutdown();

    }

    private static void completableFuture_supplyAsync() throws InterruptedException, ExecutionException {
        // 1.no executor, will be call ForkJoinPool.commonpool() else will be call executor;
        // 2.return U.
        // Supplier<Integer> supplier = ()->getMoreData();
        // CompletableFuture<Integer> result = CompletableFuture.supplyAsync(supplier);

        CompletableFuture<Integer> result = CompletableFuture.supplyAsync(TestThreadPool::getMoreData);

        CompletableFuture<Integer> result2 = result.whenCompleteAsync((v, e) -> {
            System.out.println(Thread.currentThread().getName() + " value : " + v);
            System.out.println(Thread.currentThread().getName() + " exception : " + e);
        });

        System.out.println(Thread.currentThread().getName() + " : " + result2.get());
    }

    private static void completableFuture_handleAsync() {
        // 1.return U.
        // 2.no executor, will be call ForkJoinPool.commonpool() else will be call executor;

        CompletableFuture<Integer> s = CompletableFuture.supplyAsync(() -> {
            int sum = 0;
            for (int i = 0; i < 5; i++) {
                int random = new Random().nextInt(10);
                System.out.println(random);
                sum += random;
            }
            return sum;
        });

        /** bellow will throw new Exception if catch */
        try {
            CompletableFuture<Integer> result = s.handleAsync((x, y) -> {
                System.out.println(Thread.currentThread().getName() + ":(x)" + x);
                System.out.println(Thread.currentThread().getName() + ":(y)" + x);
                return x;
            });

            System.out.println(Thread.currentThread().getName() + " result : " + result.get());
        } catch (Exception e) {
            logger.error("Excutor catch exception...", e);
        }
    }

    private static void completableFuture_thenAplyAsync() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> result = CompletableFuture.supplyAsync(() -> getMoreData());
        CompletableFuture<String> result2 = result.thenApplyAsync((x) -> {
            System.out.println("x : " + x);
            return x += getMoreData();
        }).thenApply(x -> {
            System.out.println("x : " + x);
            int rand = getMoreData();
            return x += rand;
        })
                .thenApply(x -> {
                    System.out.println("x : " + x);
                    int rand = getMoreData();
                    return x += rand;
                })
                .thenApply(x -> {
                    System.out.println("future reslt : " + x.toString());
                    return x.toString();
                });

        System.out.println("main result : " + result2.get());
    }

    private static void completableFuture_thenAcceptAsync() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> result = CompletableFuture.supplyAsync(() -> getMoreData());
        CompletableFuture<Void> result2 = result.thenAcceptAsync(System.out::println);
        System.out.println("get : " + result2.get());
    }

    private static void completableFuture_thenAcceptBothAsync() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> result = CompletableFuture.supplyAsync(() -> getMoreData());
        CompletableFuture<Integer> result2 = CompletableFuture.supplyAsync(() -> getMoreData());
        CompletableFuture<Void> result4 = result.thenAcceptBothAsync(result2, (x, y) -> {
            System.out.println("x+y : " + (x + y));
        });
        System.out.println("get : " + result4.get());
    }

    private static void completableFuture_thenComposeAsync() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> result = CompletableFuture.supplyAsync(() -> getMoreData());
        CompletableFuture<Integer> result4 = result.thenComposeAsync(i -> CompletableFuture.supplyAsync(() -> i + getMoreData()));
        System.out.println("get : " + result4.get());
    }

    private static void completableFuture_thenCombineAsync() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> result = CompletableFuture.supplyAsync(() -> {
            System.out.println("1");
            return 1;
        });
        CompletableFuture<Integer> result2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("2");
            return 2;
        });
        CompletableFuture<Integer> result4 = result.thenCombineAsync(result2, (x, y) -> x + y).whenCompleteAsync((x, y) -> System.out.println(x));
        System.out.println("get : " + result4.get());
    }

    private static void completableFuture_applyToEitherAsync() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> result = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(new Random().nextInt(1000));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 100;
        });
        CompletableFuture<Integer> result2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(new Random().nextInt(1000));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 200;
        });
        CompletableFuture<String> result4 = result.applyToEitherAsync(result2, i -> i.toString());
        System.out.println("get : " + result4.get());
    }

    private static void completableFuture_acceptEitherAsync() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> result = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(new Random().nextInt(1000));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 100;
        });
        CompletableFuture<Integer> result2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(new Random().nextInt(1000));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 200;
        });
        CompletableFuture<Void> result4 = result.acceptEitherAsync(result2, x -> {
            System.out.println(x);
        });
        System.out.println("get : " + result4.get());
    }

    private static void completableFuture_allof() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> result = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(new Random().nextInt(1000));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 100;
        });
        CompletableFuture<Integer> result2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(new Random().nextInt(1000));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 200;
        });
        CompletableFuture<Void> result4 = CompletableFuture.allOf(result, result2);
        System.out.println("get allof: " + result4.get());

        CompletableFuture<Object> result5 = CompletableFuture.anyOf(result, result2);
        System.out.println("get anyof: " + result5.get());
    }

    public static void futureTaskPoolInt() throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newFixedThreadPool(10);
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            Callable<Integer> c = new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return new Random().nextInt(10);
                }
            };

            FutureTask<Integer> ft = new FutureTask<Integer>(c);
            es.submit(ft);
            while (!ft.isDone()) {
                System.out.println(Thread.currentThread().getName() + " Thread return reuls : " + ft.get());
                sum += ft.get();

            }
        }

        es.shutdown();
        System.out.println("total: " + sum);

        System.out.println("run end!!!!!!");

    }

    public static void callablesPoolInt() throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newFixedThreadPool(10);
        CompletionService<Integer> service = new ExecutorCompletionService<Integer>(es);
        for (int i = 0; i < 10; i++) {
            Callable<Integer> c = new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return new Random().nextInt(10);
                }
            };

            service.submit(c);
        }

        int sum = 0;
        for (int i = 0; i < 13; i++) {
            Future<Integer> s = service.poll(1, TimeUnit.SECONDS);
            // Future<Integer> s = service.take(); // will not shutdown the thread
            /** bellow will throw new Exception if catch */
            try {
                Integer result = s == null ? 0 : s.get();
                sum += result;
                System.out.println(Thread.currentThread().getName() + " Thread return reuls : " + result);
            } catch (Exception e) {
                logger.error("Excutor catch exception...", e);
            }

        }

        es.shutdownNow();
        System.out.println("total: " + sum);
        System.out.println("run end!!!!!!");

    }

    public static Long callablesPoolReadFile(List<String> files) throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newFixedThreadPool(10);
        CompletionService<Long> service = new ExecutorCompletionService<Long>(es);
        long sum = 0;
        /**
         * 1
         */
        // files.stream().forEach(file -> {
        // Callable<Long> c = new Callable<Long>()
        // {
        // @Override
        // public Long call() throws Exception
        // {
        // Util util = new Util();
        // long startIn = System.currentTimeMillis();
        // Path path = Paths.get(file);
        // Long l = util.getCountDocsLimit(path,8246, "import");
        // System.out.println("per time : " + (System.currentTimeMillis() - startIn)
        // + " --> words count : " + l + " --> size : " + path.toFile().length()
        // + " (" + util.getSizeUnit(path.toFile().length()) + " )"
        // + " --> path : " + path.getParent() + File.separator + path.getFileName());
        // return l;
        // }
        // };
        // service.submit(c);
        // });
        //
        // sum = files.parallelStream().mapToLong(file -> {
        // long count = 0;
        // try
        // {
        // Future<Long> s = service.take();
        // Long result = s==null?0:s.get();
        // count += result;
        // }
        // catch (Exception e)
        // {
        // e.printStackTrace();
        // }
        //
        // return count;
        // }).sum();

        /**
         * 2
         */
        for (String file : files) {
            Callable<Long> c = new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    NioUtil util = new NioUtil();
                    long startIn = System.currentTimeMillis();
                    Path path = Paths.get(file);
                    Long l = util.getCountDocsLimit(path, 8246, "import");
                    System.out.println("per time : " + (System.currentTimeMillis() - startIn)
                            + " --> words count : " + l + " --> size : " + path.toFile().length()
                            + " (" + util.getSizeUnit(path.toFile().length()) + " )"
                            + " --> path : " + path.getParent() + File.separator + path.getFileName());
                    return l;
                }
            };
            service.submit(c);
        }

        for (int i = 0; i < files.size(); i++) {
            Future<Long> s = service.take();
            Long result = s == null ? 0L : s.get();
            sum += result;
        }

        es.shutdown();

        return sum;
    }

    public static void futureTaskPoolStr() throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newFixedThreadPool(10);
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            Callable<String> c = new Callable<String>() {

                @Override
                public String call() throws Exception {
                    return Thread.currentThread().getName() + " : " + new Random().nextInt(10);
                }
            };

            FutureTask<String> ft = new FutureTask<String>(c);
            es.submit(ft);
            while (!ft.isDone()) {
                System.out.println("Thread return resuls : " + ft.get());
                sum += Integer.parseInt(ft.get().split(":")[1].toString().trim());
            }
        }

        es.shutdown();
        System.out.println("run end!!!!!!");
        System.out.println("sum : " + sum);

    }

    public static void callablesPoolStr() throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newFixedThreadPool(10);
        CompletionService<String> service = new ExecutorCompletionService<String>(es);
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            Callable<String> c = new Callable<String>() {

                @Override
                public String call() throws Exception {
                    return Thread.currentThread().getName() + " : " + new Random().nextInt(10);
                }
            };

            service.submit(c);
        }

        for (int i = 0; i < 13; i++) {
            Future<String> s = service.poll(1, TimeUnit.SECONDS);
            // Future<Integer> s = service.take();
            String result = (s == null ? null : s.get());
            System.out.println("Thread return reuls : " + result);

            sum += result == null ? 0 : Integer.parseInt(result.split(":")[1].toString().trim());
        }

        es.shutdown();
        System.out.println("end!!! ->> " + Thread.currentThread().getName());
        System.out.println("sum : " + sum);
    }

    public static void ScheduledPool() {
        ScheduledExecutorService es2 = Executors.newScheduledThreadPool(1);
        es2.scheduleAtFixedRate(new NormalMyThread(111) {
            public void run() {
                System.out.println(Thread.currentThread().getName() + "~~~~~~~~~~~~~~~~");
            }
        }, 1, 5, TimeUnit.SECONDS);

        // for (int i = 1; i <= 15; i++)
        // {
        // es2.scheduleAtFixedRate(new NormalMyThread(222)
        // {
        // public void run()
        // {
        // System.out.println(System.nanoTime());
        // }
        // }, 1, 2, TimeUnit.SECONDS);
        // }
        //
        // es2.shutdown();
    }

    public static void fixedPool() {
        ExecutorService es = Executors.newFixedThreadPool(3);

        for (int i = 1; i <= 15; i++) {
            es.submit(new NormalMyThread(i));
        }

        es.shutdown();
        System.out.println("run end!!!!!!");
    }

    public static void cachePool() {
        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 1; i <= 15; i++) {
            es.execute(new NormalMyThread(i));
        }

        es.shutdown();
        System.out.println("run end!!!!!!");
    }

    public static void singlePool() {
        ExecutorService es = Executors.newSingleThreadExecutor();
        for (int i = 1; i <= 15; i++) {
            es.execute(new NormalMyThread(i));
        }

        es.shutdown();
        System.out.println("run end!!!!!!");
    }

    public static void cyclicBarrierPool() {
        CyclicBarrier c = new CyclicBarrier(5);
        ExecutorService es = Executors.newFixedThreadPool(5);
        for (int i = 1; i <= 10; i++) {
            es.execute(new CycliBarrierMyThread(c, i));
        }

        es.shutdown();
        System.out.println("run end!!!!!!");

    }

    public static void countDownLatchPool() throws InterruptedException {
        /**
         * such as running match with 10 man,1 thread for per man.
         */
        // 1 thread to per man for count down.
        final CountDownLatch start = new CountDownLatch(1);
        // there are 10 man join the match,when all man has run over,
        // the count is 0 then main thread is running,last match is over.
        final CountDownLatch end = new CountDownLatch(10);

        ExecutorService es = Executors.newFixedThreadPool(10);
        for (int i = 1; i <= 10; i++) {
            es.execute(new CountDownMyThread(start, end, i));
        }

        start.countDown();
        // wait for match over.
        end.await();

        es.shutdown();
        System.out.println("main thread,all child threads is over!!!!!!");
    }

    public static void semaphorePool() {
        Semaphore avalable = new Semaphore(1, true);// true : queue order by one-by-one
        ExecutorService es = Executors.newFixedThreadPool(5);
        for (int i = 1; i <= 10; i++) {
            es.execute(new SemaphoreThread(avalable, i));
        }
        es.shutdown();
        System.out.println("run end!!!!!!");
    }
}

class NormalMyThread implements Runnable {
    private int num;

    NormalMyThread(int num) {
        this.num = num;
    }

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName() + " -------- task [" + num + "]");
            int a = new Random().nextInt(4);
            Thread.sleep(1000 * (a == 0 ? 1 : a));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class CycliBarrierMyThread implements Runnable {
    private int num;

    private CyclicBarrier cb = null;

    CycliBarrierMyThread(CyclicBarrier cb, int num) {
        this.cb = cb;
        this.num = num;
    }

    @Override
    public void run() {
        try {
            int a = new Random().nextInt(4);
            Thread.sleep(1000 * (a == 0 ? 1 : a));
            System.out.println(Thread.currentThread().getName() + " (ready) -------- task [" + num + "]");
            cb.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + " (start)");
    }

}

class CountDownMyThread implements Runnable {
    private int num;

    private CountDownLatch start = null;

    private CountDownLatch end = null;

    CountDownMyThread(CountDownLatch start, CountDownLatch end, int num) {
        this.start = start;
        this.end = end;
        this.num = num;
    }

    @Override
    public void run() {
        try {
            start.await();
            int a = new Random().nextInt(4);
            Thread.sleep(1000 * (a == 0 ? 1 : a));
            System.out.println(Thread.currentThread().getName() + " -------- task [" + num + "] is over.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            end.countDown();
        }
    }

}

class SemaphoreThread implements Runnable {
    private int num;

    private Semaphore avalable = null;

    SemaphoreThread(Semaphore avalable, int num) {
        this.avalable = avalable;
        this.num = num;
    }

    @Override
    public void run() {
        try {
            avalable.acquire();
            long start = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + " (start working) -------- task [" + num + "]");
            int a = new Random().nextInt(4);
            Thread.sleep(1000 * (a == 0 ? 1 : a));
            System.out.println(Thread.currentThread().getName() + " (end work)-------- task [" + num + "]");
            System.out.println("case time(" + a + "): " + (System.currentTimeMillis() - start));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            avalable.release();
        }
    }

}
