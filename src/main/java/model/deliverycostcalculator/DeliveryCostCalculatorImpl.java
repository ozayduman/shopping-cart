package model.deliverycostcalculator;

import model.ShoppingCart;

public class DeliveryCostCalculatorImpl implements DeliveryCostCalculator {

    private final double costPerDelivery;
    private final double costPerProduct;
    private final double fixedCost;

    public DeliveryCostCalculatorImpl(double costPerDelivery, double costPerProduct, double fixedCost) {
        this.costPerDelivery = costPerDelivery;
        this.costPerProduct = costPerProduct;
        this.fixedCost = fixedCost;
    }

    public double calculateFor(ShoppingCart cart) {
        if (cart.isEmpty()) {
            return .0;
        }
        return (costPerDelivery * getNumberOfDeliveries(cart))
                + (costPerProduct * getNumberOfProducts(cart))
                + fixedCost;
    }

    private int getNumberOfDeliveries(ShoppingCart cart) {
        return cart.getNumberOfDistinctCategories();
    }

    private int getNumberOfProducts(ShoppingCart cart) {
        return cart.getNumberOfDifferentProducts();
    }
}
