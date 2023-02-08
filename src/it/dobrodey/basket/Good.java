package it.dobrodey.basket;

import java.util.Map;

public class Good {

    private static Map<String, Integer> goods =
            Map.of("Orange", 2,
                    "Tomato", 3,
                    "Fruit", 1,
                    "Apple", 4,
                    "Banana",5,
                    "Carrot", 1,
                    "Potato",2);

    public static Map<String, Integer> getGoods() {
        return goods;
    }

}
