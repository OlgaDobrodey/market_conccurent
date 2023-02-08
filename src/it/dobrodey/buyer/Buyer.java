package it.dobrodey.buyer;

import static it.dobrodey.Generate.addIndentName;
import static it.dobrodey.QueueBuyersAndCashier.add;

import it.dobrodey.MarketSemaphore;
import it.dobrodey.Supervisor;
import it.dobrodey.basket.Basket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Buyer extends Thread implements IBuyer {

    private static List<Buyer> buyers = new ArrayList<>();

    private final Boolean pensioneer;
    private MarketSemaphore semaphore;
    private final Integer number;
    private Boolean waiting = true;

    private Map<String, Integer> basket;

    public Buyer(Integer number, MarketSemaphore semaphore) {
        this.semaphore = semaphore;
        this.number = number;
        this.pensioneer = isPensioneer();
        this.setName(addIndentName(number, pensioneer));
    }

    @Override
    public void run() {
        Supervisor.buyersEnterToMarket.getAndIncrement();
        enterToMarket();
        semaphore.tryGoToMarket();
        chooseGoods();
        semaphore.finishChooseGoods();
        goToQueue();
        goOut();
        Supervisor.buyersLeavedMarket.getAndIncrement();
    }

    @Override
    public void enterToMarket() {
        System.out.println(this.getName() + " enter to market");
    }

    @Override
    public void chooseGoods() {
        Basket basket = new Basket(this.getName(), getSpeedBuyer());
        basket.takeBasket();
        System.out.println(this.getName() + " choose goods");
        basket.putGoodsToBasket();
        this.basket = basket.getBasket();
        System.out.println(this.getName() + " create basket" + getBasket());
    }

    public void goToQueue() {
        add(this);
        synchronized (number) {
            try {
                while (waiting) {
                    this.getNumber().wait();
                }
            } catch (InterruptedException e) {
                   throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void goOut() {
        System.out.println(this.getName() + " go out");
    }

    private double getSpeedBuyer() {
        return pensioneer ? 1.5 : 1;
    }

    private boolean isPensioneer() {
        return ((number % 4 == 0) && (number != 0));
    }

    public void setWaiting(Boolean waiting) {
        this.waiting = waiting;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public Map<String, Integer> getBasket() {
        return basket;
    }

    @Override
    public Boolean getPensioneer() {
        return pensioneer;
    }

    @Override
    public Integer getNumber(){return number;}

    public static List<Buyer> getBuyers() {
        return buyers;
    }

    public static void setBuyer(Buyer buyer) {
        buyers.add(buyer);
    }
}
