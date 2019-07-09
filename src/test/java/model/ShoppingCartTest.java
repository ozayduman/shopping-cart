package model;

import model.cartprinter.CartPrinter;
import model.deliverycostcalculator.DeliveryCostCalculator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ShoppingCartTest {
    private ShoppingCart cart;

    @Before
    public void setUp() {
        cart = new ShoppingCart();
    }

    @Test
    public void productsAddedToChartWithQuantity() {
        Category fruitCategory = new Category("Fruit");
        var apple = new Product("Apple", 12.0, fruitCategory);
        cart.addItem(apple, 3);
        assertEquals(3, cart.getQuantityOf(apple).intValue());

        Product almond = new Product("Almomnd", 20.0, fruitCategory);
        cart.addItem(almond, 1);
        assertEquals(1, cart.getQuantityOf(almond).intValue());
    }

    @Test
    public void cartShouldGetCartTotalBeforeDiscounts() {
        Category category = new Category("Food");
        Product product = new Product("Apple", 100.0, category);
        cart.addItem(product, 1);
        assertEquals(100, cart.getCartTotalBeforeDiscounts(), 0.0);
        Product product2 = new Product("Banana", 120, category);
        cart.addItem(product2, 1);
        assertEquals(220.0, cart.getCartTotalBeforeDiscounts(), 0.0);
    }

    @Test
    public void whenNeitherCampaignsNorCouponsExistThenTotalAmountAfterDiscountsCalculatedWithoutDiscounts() {
        Category foodCategory = new Category("Food");
        Category electronicCategory = new Category("Electronic");
        Product product1 = new Product("Apple", 12.0, foodCategory);
        Product product2 = new Product("MacBook Pro", 12_000.0, electronicCategory);

        cart.addItem(product1, 1);
        cart.addItem(product2, 1);

        assertEquals(12_012.0, cart.getTotalAmountAfterDiscounts(), 0.0);
    }

    @Test
    public void whenCampaignsExistThenTotalAmountAfterDiscountsCalculatedWithCampaigns() {
        Category foodCategory = new Category("Food");
        Campaign campaignWith20DiscountPercentage = new Campaign(foodCategory, 20.0, 3, DiscountType.Rate);
        Product product1 = new Product("Apple", 12.0, foodCategory);
        cart.addItem(product1, 6);
        cart.applyDiscounts(campaignWith20DiscountPercentage);

        double expectedTotalAmountAfterDiscounts = cart.getCartTotalBeforeDiscounts() - cart.getCampaignDiscount();

        assertEquals(expectedTotalAmountAfterDiscounts, cart.getTotalAmountAfterDiscounts(), 0.0);
    }

    @Test
    public void whenCouponsExistThenTotalAmountAfterDiscountsCalculatedWithCoupons() {
        Product product1 = new Product("Apple", 12.0, new Category("Food"));
        cart.addItem(product1, 10);
        Coupon coupon = new Coupon(100.0, 10, DiscountType.Rate);
        cart.applyCoupon(coupon);
        double expectedTotalAmountAfterDiscounts = cart.getCartTotalBeforeDiscounts() - cart.getCouponDiscount();

        assertEquals(expectedTotalAmountAfterDiscounts, cart.getTotalAmountAfterDiscounts(), 0.0);
    }

    @Test(expected = NullPointerException.class)
    public void whenCartHasNoDeliveryCostCalculator() {
        Product product = new Product("Apple", 12.0, new Category("Food"));
        cart.addItem(product, 1);

        cart.setDeliveryCostCalculator(null);
        cart.getDeliveryCost();
    }

    @Test
    public void whenCartHasDeliveryCostCalculatorThenCalculatedWithIt() {
        Product product = new Product("Apple", 12.0, new Category("Food"));
        cart.addItem(product, 1);
        DeliveryCostCalculator deliveryCostCalculator = mock(DeliveryCostCalculator.class);
        when(deliveryCostCalculator.calculateFor(any(ShoppingCart.class)))
                .thenReturn(1_000.0);
        cart.setDeliveryCostCalculator(deliveryCostCalculator);

        assertEquals(1_000.0, cart.getDeliveryCost(), 0.0);
        Mockito.verify(deliveryCostCalculator, times(1)).calculateFor(cart);
        verifyNoMoreInteractions(deliveryCostCalculator);
    }

    @Test
    public void cartShouldPrint() {
        String printed_result = "PRINTED RESULT";
        CartPrinter cartPrinter = mock(CartPrinter.class);
        when(cartPrinter.print(cart)).thenReturn(printed_result);
        cart.setCartPrinter(cartPrinter);
        String result = cart.print();

        assertEquals("They aren't same", printed_result, result);
        verify(cartPrinter, times(1)).print(cart);
    }

}
