package it.dobrodey;

import static it.dobrodey.cashir.CashierUtils.generateIndented;

import it.dobrodey.buyer.Buyer;
import it.dobrodey.cashir.Cashier;
import it.dobrodey.cashir.CashierUtils;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class QueueBuyersAndCashier {

    private static BlockingDeque<Buyer> dequeBuyer = new LinkedBlockingDeque<>(30);
    private static BlockingDeque<Buyer> dequePensionner = new LinkedBlockingDeque<>(30);
    private static Deque<Cashier> dequeCashier = new LinkedList<>();

    private static Lock lockBuyer = new ReentrantLock();
    private static Lock lockCashier = new ReentrantLock();

    public static void add(Buyer buyer) {
        lockBuyer.lock();
        if (buyer.getPensioneer()) {
            dequePensionner.addLast(buyer);
        } else {
            dequeBuyer.addLast(buyer);
        }
        lockBuyer.unlock();
        openAnyCashiers();
    }

    public static Buyer extract() {
        try {
            lockBuyer.lock();
            if (dequePensionner.size() > 0) {
                return dequePensionner.pollFirst();
            } else {
                return dequeBuyer.pollFirst();
            }
        } finally {
            lockBuyer.unlock();
        }
    }

    public static int getQueueBuyersSize() {
        System.out.println(generateIndented(6) + dequeBuyer);
        System.out.println(generateIndented(6) + dequePensionner);
        return dequeBuyer.size() + dequePensionner.size();
    }

    public static void closeCashier(Cashier cashier) {
        lockCashier.lock();
        dequeCashier.addLast(cashier);
        System.out.printf("%s CLOSED\n", cashier);
        lockCashier.unlock();
        synchronized (cashier.getCashierMonitor()) {
            try {
                cashier.getCashierMonitor().wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private static Cashier openCashier() {
        lockCashier.lock();
        Cashier cashier = dequeCashier.pollFirst();
        System.out.printf("%s OPEN\n", cashier);
        lockCashier.unlock();
        if (cashier != null) {
            synchronized (cashier.getCashierMonitor()) {
                cashier.getCashierMonitor().notify();
                System.out.println();
            }
        }
        return cashier;
    }

    public static Boolean updateCashierStatus(Cashier cashier) {
        int size = getQueueBuyersSize();
        int numberOpenCashier = getNumberOpenCashierByFormula(size);
        int sizeClosedCashiers = dequeCashier.size();

        if (numberOpenCashier < (CashierUtils.CASHIER_COUNTER - sizeClosedCashiers)) {
            closeCashier(cashier);
            return true;
        }
        return false;
    }

    public static void openAnyCashiers() {
        int size = getQueueBuyersSize();
        int sizeCashiers = dequeCashier.size();

        int numberOpenCashier = getNumberOpenCashierByFormula(size);
        System.out.printf("%sWorked %d CASHIERS for QUEUE: %d, %s\n", generateIndented(6),
                numberOpenCashier, size, dequeCashier);

        if (numberOpenCashier >= (5 - sizeCashiers)) {
            for (int i = 0; i < (numberOpenCashier - (5 - sizeCashiers)); i++) {
                openCashier();
            }
        }
    }

    private static int getNumberOpenCashierByFormula(int size) {
        int numberOpenCashier;
        if ((Supervisor.marketIsClosed())) {
            numberOpenCashier = 5;
        } else if ((size >= 20)) {
            numberOpenCashier = 5;
        } else if (size >= 15) {
            numberOpenCashier = 4;
        } else if (size >= 10) {
            numberOpenCashier = 3;
        } else if (size >= 5) {
            numberOpenCashier = 2;
        } else {
            numberOpenCashier = 1;
        }
        return numberOpenCashier;
    }

}
