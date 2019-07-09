package model;

public interface CampaignDiscount {
    double calculateDiscountFor(int itemQuantityInCategory, double totalCostAmountInCategory);

    Category getCategory();
}
