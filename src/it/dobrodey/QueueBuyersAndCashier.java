package it.dobrodey;

import static it.dobrodey.cashir.CashierUtils.generateIndented;

import it.dobrodey.buyer.Buyer;
import it.dobrodey.cashir.Cashier;
import it.dobrodey.cashir.CashierUtils;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueBuyersAndCashier {

    private static final ConcurrentLinkedQueue<Buyer> dequeBuyer = new ConcurrentLinkedQueue<>();
    private static final ConcurrentLinkedQueue<Buyer> dequePensionner = new ConcurrentLinkedQueue<>();
    private static final ConcurrentLinkedQueue<Cashier> dequeCashier = new ConcurrentLinkedQueue<>();

    public static void add(Buyer buyer) {
        if (buyer.getPensioneer()) {
            dequePensionner.add(buyer);
        } else {
            dequeBuyer.add(buyer);
        }
        openAnyCashiers();
    }

    public static Buyer extract() {
        if (dequePensionner.size() > 0) {
            return dequePensionner.poll();
        } else {
            return dequeBuyer.poll();
        }
    }

    public static int getQueueBuyersSize() {
        System.out.println(generateIndented(6) + dequeBuyer);
        System.out.println(generateIndented(6) + dequePensionner);
        return dequeBuyer.size() + dequePensionner.size();
    }

    public static void closeCashier(Cashier cashier) {
        dequeCashier.add(cashier);
        System.out.printf("%s CLOSED\n", cashier);
        synchronized (cashier.getCashierMonitor()) {
            try {
                cashier.getCashierMonitor().wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private static Cashier openCashier() {
        Cashier cashier = dequeCashier.poll();
        System.out.printf("%s OPEN\n", cashier);

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
