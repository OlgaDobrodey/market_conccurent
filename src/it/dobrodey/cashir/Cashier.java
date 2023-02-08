package it.dobrodey.cashir;

import static it.dobrodey.Generate.getRandomNumber;
import static it.dobrodey.QueueBuyersAndCashier.extract;
import static it.dobrodey.Supervisor.marketIsClosed;

import it.dobrodey.QueueBuyersAndCashier;
import it.dobrodey.Revenue;
import it.dobrodey.buyer.Buyer;
import java.time.LocalTime;
import java.util.Objects;

public class Cashier implements Runnable, ICashier {

    private final int number;
    private final Object cashierMonitor = new Object();
    private Buyer buyer;

    public Cashier() {

        this.number = CashierUtils.generateNamber();
    }

    @Override
    public void run() {
        System.out.println("Open BOX_OFFICE_" + number);
        while (!marketIsClosed()) {
            QueueBuyersAndCashier.updateCashierStatus(this);
            buyer = takeBuyerTOService();
            if (Objects.nonNull(buyer)) {
                System.out.println(this + " work with" + buyer + " TIME:" + LocalTime.now());
                buyerService();
                finishServingBuyer(buyer);
            } else {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println("CLOSE BOX_OFFICE_" + number);
    }

    @Override
    public Buyer takeBuyerTOService() {
        return extract();
    }

    @Override
    public void buyerService() {
        try {
            Thread.sleep(getRandomNumber(500, 2000));
            printCheck();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void printCheck() {
        Revenue.printCheck(buyer, this);
    }

    @Override
    public void finishServingBuyer(Buyer buyer) {
        synchronized (buyer.getNumber()) {
            buyer.setWaiting(false);
            buyer.getNumber().notify();
        }
    }

    @Override
    public String toString() {
        return "Cashier-" + number;
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public Object getCashierMonitor() {
        return cashierMonitor;
    }
}
