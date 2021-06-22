package ru.topjava.basejava;

public class DeadLock {
    private static final Object SHARED_1 = new Object();
    private static final Object SHARED_2 = new Object();

    public static void main(String[] args) {
         new Thread(() -> getSharedResources(SHARED_1, SHARED_2)).start();
         new Thread(() -> getSharedResources(SHARED_2, SHARED_1)).start();
    }

    private static void getSharedResources(Object resource1, Object resource2) {
        synchronized (resource1) {
            System.out.println(Thread.currentThread().getName() + " has captured " + resource1.toString() );
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (resource2) {
                System.out.println(Thread.currentThread().getName() + " has captured "+  resource2.toString());
            }
        }
    }
}
