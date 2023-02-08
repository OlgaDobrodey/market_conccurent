package it.dobrodey.buyer;

import java.util.Map;

public interface IBuyer {
    void enterToMarket(); //вошел в магазин (мгновенно)
    void chooseGoods(); //выбрал товар (от 0,5 до 2 секунд)
    void goOut(); //отправился на выход(мгновенно)
    Map<String, Integer> getBasket();
    Boolean getPensioneer();
    Integer getNumber();
}
