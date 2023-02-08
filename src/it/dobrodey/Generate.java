package it.dobrodey;

public class Generate {

    public static String addIndentName(Integer number, Boolean pensioner) {
        return String.format("Buyer%s-%d", pensioner ? "Pensioner" : "", number);
    }

    /**
     * generate random number from min to max (max - include)
     */
    public static int getRandomNumber(Integer min, Integer max) {
        return (int) (Math.random() * (max + 1 - min) + min);
    }
}

