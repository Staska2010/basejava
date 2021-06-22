package ru.topjava.basejava;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MainConcurrent {
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    public static void main(String[] args) {
        //safeIncrement();
        //reentrantTest();
    }

    private static void reentrantTest() {
        Bottle bottle = new Bottle();
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    bottle.add();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    bottle.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static class Bottle {
        private final ReentrantLock lock;
        private final Condition condition;
        private Integer capacity = 0;

        Bottle() {
            lock = new ReentrantLock();
            condition = lock.newCondition();
        }

        public void add() throws InterruptedException {
            lock.lock();
            try {
                while (capacity > 9) {
                    condition.await();
                }
                System.out.println("Capacity:" + capacity);
                capacity++;
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }

        public void get() throws InterruptedException {
            lock.lock();
            try {
                while (capacity < 1) {
                    condition.await();
                }
                System.out.println("Capacity:" + capacity);
                capacity--;
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    private static void safeIncrement() {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Thread nextThread = new TestThread();
            nextThread.start();
            threads.add(nextThread);
        }
        for (Thread next : threads) {
            try {
                next.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(COUNTER.get());
    }

    private static void increment() {
        COUNTER.incrementAndGet();
    }

    private static class TestThread extends Thread {
        @Override
        public void run() {
            for (int j = 0; j < 100; j++) {
                increment();
            }
        }
    }
}

