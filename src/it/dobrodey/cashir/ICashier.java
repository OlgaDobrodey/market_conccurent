package it.dobrodey.cashir;

import it.dobrodey.buyer.Buyer;

public interface ICashier {
    Buyer takeBuyerTOService();
    void buyerService();
    void finishServingBuyer(Buyer buyer);
    int getNumber();
    Object getCashierMonitor();
}
