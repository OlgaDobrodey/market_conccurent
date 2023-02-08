package it.dobrodey.basket;

import java.util.Map;

public interface IUseBasket {
    void takeBasket(); //взял корзину
    void putGoodsToBasket(); //положил выбранный товар в корзину

    Map<String, Integer> getBasket();
}
