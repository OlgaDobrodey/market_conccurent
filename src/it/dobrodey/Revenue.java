package it.dobrodey;

import static it.dobrodey.cashir.CashierUtils.generateIndented;
import static it.dobrodey.QueueBuyersAndCashier.getQueueBuyersSize;

import it.dobrodey.buyer.Buyer;
import it.dobrodey.cashir.Cashier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Revenue {

    private static Integer total = 0;
    private static Lock lock = new ReentrantLock();

    public static void printCheck(Buyer buyer, Cashier cashier) {
        lock.lock();
        int totalSumma = buyer.getBasket()
                .values()
                .stream()
                .reduce((v1, v2) -> v1 + v2)
                .orElse(0);
        total = totalSumma + total;
        String indented = generateIndented(cashier.getNumber());
        try {
            System.out.printf(
                    "%sCHECK-%s\n"
                            + "%s%s buy:\n"
                            + "%s%s\n"
                            + "%sTOTAL:%d\n"
                            + "%sQueue Size: %d\n"
                            + "%sTOTAL: %d\n"
                    , indented, cashier
                    , indented, buyer
                    , indented, buyer.getBasket().toString()
                    , indented, totalSumma
                    , generateIndented(6), getQueueBuyersSize()
                    , generateIndented(7), total
            );
        } catch (Exception e) {
            System.out.println("EXCEPTION:" + e.getMessage());
        }
        lock.unlock();
    }
}
