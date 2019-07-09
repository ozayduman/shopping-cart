package model;

public class Coupon implements CouponDiscount {
    private final double minPurchaseAmount;
    private final double discount;
    private final DiscountType discountType;

    public Coupon(double minPurchaseAmount, double discount, DiscountType discountType) {

        this.minPurchaseAmount = minPurchaseAmount;
        this.discount = discount;
        this.discountType = discountType;
    }

    @Override
    public double calculateDiscountFor(ShoppingCart cart) {
        double cartTotal = cart.getCartTotalBeforeDiscounts();
        if (cartTotal >= this.minPurchaseAmount) {
            if (DiscountType.Rate.equals(this.discountType)) {
                return cartTotal * this.discount / 100;
            } else {
                return discount;
            }
        }
        return 0;
    }
}
