package model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CouponTest {
    private Category category;
    private Product product;
    private ShoppingCart cart;
    private Coupon coupon;

    @Before
    public void initialize(){
        category = new Category("Food");
        product = new Product("Apple", 100.0, category);
        cart = new ShoppingCart();
        coupon = new Coupon(200.0,10,DiscountType.Rate);
    }

    @Test
    public void whenCartTotalLessThanMinAmountThenCouponNotApplied(){
        cart.addItem(product,1);
        cart.applyCoupon(coupon);
        assertEquals(0,cart.getCouponDiscount(),0.0);
    }

    @Test
    public void whenCartTotalMoreThanMinAmountThenCouponApplied() {
        cart.addItem(product,3);
        cart.applyCoupon(coupon);
        assertEquals(30.0,cart.getCouponDiscount(),0.0);
    }

    @Test
    public void whenCartTotalMoreThanMinAmountWithDiscountTypeAmountThenCouponApplied() {
        cart.addItem(product,3);
        coupon = new Coupon(200.0,5.0,DiscountType.Amount);
        cart.applyCoupon(coupon);
        assertEquals(5.0,cart.getCouponDiscount(),0.0);
    }
}
