package model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ProductTest {

    private String title;
    private double price;
    private Category category;

    @Before
    public void setUp() throws Exception {
        this.title = "Apple";
        this.price = 12.0;
        this.category = new Category(title);
    }

    @Test
    public void productShouldHaveTitleAndPrice() {
        Product apple = new Product(title,price, category);
        assertEquals(title,apple.getTitle());
        assertEquals(price,apple.getPrice(),0.0);
    }

    @Test
    public void productShouldBelongToACategory() {
        Product product = new Product(title, price, category);
        assertNotNull(product.getCategory());
    }
}
