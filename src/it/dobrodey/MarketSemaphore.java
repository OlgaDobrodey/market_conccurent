package it.dobrodey;

import java.util.concurrent.Semaphore;

public class MarketSemaphore {

    private Semaphore semaphore;

    public MarketSemaphore(int slotLimit) {
        semaphore = new Semaphore(slotLimit, true);
    }

    public boolean tryGoToMarket() {
        return semaphore.tryAcquire();
    }

    public void finishChooseGoods() {
        semaphore.release();
    }
    public int drainPermits(){
        return semaphore.drainPermits();
    }
    public int getQueueLength(){
        return semaphore.getQueueLength();
    }
    public int availablePermits(){
        return semaphore.availablePermits();
    }

}
