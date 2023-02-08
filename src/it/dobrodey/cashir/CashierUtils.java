package it.dobrodey.cashir;

import java.util.concurrent.atomic.AtomicInteger;

public class CashierUtils {

    private static AtomicInteger numberCashier = new AtomicInteger();
    public final static Integer CASHIER_COUNTER = 5;
    public final static Integer INDENTED = 30;

    public static int generateNamber() {
        return numberCashier.getAndIncrement();
    }

    public static String generateIndented(int numberCashier) {
        return " ".repeat(numberCashier * INDENTED);
    }

}
