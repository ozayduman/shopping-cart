package model.deliverycostcalculator;

import model.ShoppingCart;

public interface DeliveryCostCalculator {
    double calculateFor(ShoppingCart cart);
}
