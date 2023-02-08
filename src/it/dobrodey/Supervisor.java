package it.dobrodey;

import it.dobrodey.buyer.Buyer;
import java.util.concurrent.atomic.AtomicInteger;

public class Supervisor {

    public static AtomicInteger buyersEnterToMarket = new AtomicInteger();
    public static AtomicInteger buyersLeavedMarket = new AtomicInteger();
    public final static Integer NUMBER_OF_BUYERS_CHOOSING_GOODS = 20;
    private final static Integer ALL_BUYER_COUNT = 100;
    private final static Integer SECOND_IN_HOUR = 60;
    private final static Integer INDENT = 80;

    public static int countOfBuyersByFormula(int second) {

        int countByFormula = getCountByFormula(second % SECOND_IN_HOUR);
        int count = buyersEnterToMarket.get() - buyersLeavedMarket.get();
        int result = 0;

        if (countByFormula > count) {
            result = countByFormula - count;
        }

        System.out.printf("%sSecond = %d: by formula - %d in market = %d should add = %d\n",
                " ".repeat(INDENT), second, countByFormula, count, result);

        return result;
    }

    private static int getCountByFormula(int secondInMinute) {
        int countByFormula;
        if (secondInMinute <= 30) {
            countByFormula = secondInMinute + 10;
        } else {
            countByFormula = 40 + (30 - secondInMinute);
        }
        return countByFormula;
    }

    public static boolean isMarketOpen() {
        return Buyer.getBuyers().size() < ALL_BUYER_COUNT;
    }

    public static boolean marketIsClosed() {
        return buyersLeavedMarket.get() == ALL_BUYER_COUNT;
    }
}
