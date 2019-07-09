package model;

public class Campaign implements CampaignDiscount {

    private final Category category;
    private final double discount;
    private final int minItemQuantity;
    private final DiscountType discountType;

    public Campaign(Category category, double discount, int minItemQuantity, DiscountType discountType) {
        this.category = category;
        this.discount = discount;
        this.minItemQuantity = minItemQuantity;
        this.discountType = discountType;
    }

    @Override
    public Category getCategory() {
        return category;
    }


    @Override
    public double calculateDiscountFor(int itemQuantityInCategory, double totalCostAmountInCategory) {
        if (itemQuantityInCategory < this.minItemQuantity) {
            return 0;
        }

        if (DiscountType.Rate.equals(this.discountType)) {
            return totalCostAmountInCategory * this.discount / 100;
        } else {
            return this.discount;
        }
    }
}
