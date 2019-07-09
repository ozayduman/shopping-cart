package model.deliverycostcalculator;

import model.Category;
import model.Product;
import model.ShoppingCart;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DeliveryCostCalculatorImplTest {
    private static final double COST_PER_DELIVERY = 11.0;
    private static final double COST_PER_PRODUCT = 3.0;
    private static final double FIXED_COST = 2.99;
    private DeliveryCostCalculator deliveryCostCalculator;
    private ShoppingCart cart;

    @Before
    public void setUp() {
        deliveryCostCalculator = new DeliveryCostCalculatorImpl(COST_PER_DELIVERY, COST_PER_PRODUCT, FIXED_COST);
        cart = new ShoppingCart();
    }

    @Test
    public void whenCartIsEmptyThenCostIsZero() {
        double cost = deliveryCostCalculator.calculateFor(cart);
        Assert.assertEquals(0.0, cost, 0.0);
    }

    @Test
    public void whenCartHasAProductWith_1_Quantiy_And_1_DeliveryThenCostCalculated() {
        Product product = new Product("Apple", 12.0, new Category("Food"));
        cart.addItem(product, 1);
        double cost = deliveryCostCalculator.calculateFor(cart);
        double expectedCost = calculateExpectedCost(1, 1);
        Assert.assertEquals(expectedCost, cost, 0.0);
    }

    @Test
    public void whenCartHas_2_ProductsWithDifferentCategoriesThenCostCalculatedAs_2_Deliveries() {
        Product product1 = new Product("Apple", 12.0, new Category("Food"));
        Product product2 = new Product("MacBook Pro", 12_000.0, new Category("Electronic"));
        cart.addItem(product1, 1);
        cart.addItem(product2, 1);
        double cost = deliveryCostCalculator.calculateFor(cart);
        double expectedCost = calculateExpectedCost(2, 2);
        Assert.assertEquals(expectedCost, cost, 0.0);
    }

    @Test
    public void whenCartHas_2_ProductsWithTheSameParentCategoryThenCostCalculatedAs_1_Delivery() {
        Category foodCategory = new Category("Food");
        Category fruitCategory = new Category("Fruit", foodCategory);
        Category dailyFoodCategory = new Category("Daily Food", foodCategory);

        Product product1 = new Product("Apple", 12.0, fruitCategory);
        Product product2 = new Product("Daily Milk", 12_000.0, dailyFoodCategory);
        cart.addItem(product1, 1);
        cart.addItem(product2, 1);
        double cost = deliveryCostCalculator.calculateFor(cart);
        double expectedCost = calculateExpectedCost(1, 2);
        Assert.assertEquals(expectedCost, cost, 0.0);
    }

    private double calculateExpectedCost(int numberOfDeliveries, int numberOfProducts) {
        return (COST_PER_DELIVERY * numberOfDeliveries) + (COST_PER_PRODUCT * numberOfProducts) + FIXED_COST;
    }

}
