package it.dobrodey.stream;

import java.util.concurrent.CyclicBarrier;

public class CycleMain {

    public static void main(String[] args) throws InterruptedException {
        final CyclicBarrier cb = new CyclicBarrier(2, () -> System.out.println("Runnable"));

        for (int i = 0; i < 20; i++) {
            new Thread(new Task(cb, "Thread " + i)).start();
            Thread.sleep(10);
        }
    }

}
