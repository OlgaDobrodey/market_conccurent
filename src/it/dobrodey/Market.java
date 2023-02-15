package it.dobrodey;

import static it.dobrodey.Supervisor.countOfBuyersByFormula;
import static it.dobrodey.Supervisor.isMarketOpen;

import it.dobrodey.buyer.Buyer;
import it.dobrodey.cashir.Cashier;
import it.dobrodey.cashir.CashierUtils;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Market {

    public static void main(String[] args) throws InterruptedException {
        System.out.println(
                "===================================MARKET OPEN==========================================");
        ExecutorService cashierPool = createCashiers(CashierUtils.CASHIER_COUNTER);

        int numberBuyer = 0;
        int second = 0;
        MarketSemaphore semaphore = new MarketSemaphore(Supervisor.NUMBER_OF_BUYERS_CHOOSING_GOODS);

        while (isMarketOpen()) {
            int random = countOfBuyersByFormula(second);

            for (int i = 0; i < random; i++) {
                Buyer buyer = new Buyer(numberBuyer++, semaphore);
                if (isMarketOpen()) {
                    Buyer.setBuyer(buyer);
                    buyer.start();
                } else {
                    break;
                }
            }
            second++;
            Thread.sleep(1000);
        }

        for (Buyer buyer : Buyer.getBuyers()) {
            buyer.join();
        }

        closeAllCashiers();

        System.out.printf("%sIn %d second in market %d people\n",
                " ".repeat(80), --second,
                Supervisor.buyersEnterToMarket.get() - Supervisor.buyersLeavedMarket.get());

        while (!cashierPool.awaitTermination(1L, TimeUnit.HOURS)) {
        }

        System.out.println(
                "===================================MARKET CLOSE==========================================");
    }

    private static void closeAllCashiers() {
        QueueBuyersAndCashier.openAnyCashiers();
    }


    private static ExecutorService createCashiers(int countCashier) {
        ExecutorService cashierPool = Executors.newFixedThreadPool(countCashier);
        for (int i = 0; i < countCashier; i++) {
            cashierPool.submit(new Cashier());
        }
        cashierPool.shutdown();
        return cashierPool;
    }
}
