package it.dobrodey.basket;

import static it.dobrodey.Generate.getRandomNumber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Basket implements IUseBasket {


    private Map<String, Integer> basket = new HashMap<>();
    private String name;
    private Double speed;

    public Basket(String name, double speed) {
        this.name = name;
        this.speed = speed;
    }

    @Override
    public void takeBasket() {
        System.out.println("take basket");
    }

    @Override
    public void putGoodsToBasket() {
        Map<String, Integer> goods = Good.getGoods();
        List<String> keysAsArray = new ArrayList<>(goods.keySet());

        int count = getRandomNumber(1, 4);
        for (int i = 0; i < count; i++) {

            String goodName = keysAsArray.get(getRandomNumber(0, keysAsArray.size() - 1));
            basket.merge(goodName, goods.get(goodName), (k, v) -> v + goods.get(goodName));

//            System.out.println(name + " get " + goodName + " and put good in basket " + basket);
            try {
                Thread.sleep((long)(speed*getRandomNumber(500, 2000)));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Map<String, Integer> getBasket() {
        return basket;
    }
}
