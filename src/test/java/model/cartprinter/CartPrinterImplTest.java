package model.cartprinter;

import model.Category;
import model.Product;
import model.ShoppingCart;
import model.deliverycostcalculator.DeliveryCostCalculator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CartPrinterImplTest {

    @Test
    public void shouldPrintCartContent() {
        Category foodCategory = new Category("Food");
        Category fruitCategory = new Category("Fruit", foodCategory);
        Category dailyFoodCategory = new Category("Daily Food", foodCategory);

        Product product1 = new Product("Apple", 12.0, fruitCategory);
        Product product2 = new Product("Daily Milk", 12_000.0, dailyFoodCategory);
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(product1, 1);
        cart.addItem(product2, 1);
        DeliveryCostCalculator deliveryCostCalculator = mock(DeliveryCostCalculator.class);
        when(deliveryCostCalculator.calculateFor(cart)).thenReturn(1_000.0);
        cart.setDeliveryCostCalculator(deliveryCostCalculator);


        String expectedResult = "CategoryName, ProductName, Quantity, Unit Price \n" +
                "Food Apple 1 12.0\n" +
                "Food Daily Milk 1 12000.0\n" +
                "Total Price :12012.0\n" +
                "Total Discount (coupon, campaign) :0.0\n" +
                "Total Amount: 12012.0 Delivery Cost: 1000.0";
        var cartPrinter = new CartPrinterImpl();
        String result = cartPrinter.print(cart);
        assertEquals("They aren't same", expectedResult, result);

        verify(deliveryCostCalculator, times(1)).calculateFor(cart);
        verifyNoMoreInteractions(deliveryCostCalculator);
    }
}