package com.lol.demo.threadpool;

class ThreadStop implements Runnable {
    private boolean stoped = false;

    @Override
    public void run() {
        while (!stoped) {
            System.out.println(Thread.currentThread().getName() + " is running...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " exception ,will be stoped ");
                System.out.println("interrupted : " + Thread.currentThread().isInterrupted());
            }
        }
        System.out.println(Thread.currentThread().getName() + " is stoped");
    }

    public void stop() {
        stoped = true;
        Thread.currentThread().interrupt();
    }
}