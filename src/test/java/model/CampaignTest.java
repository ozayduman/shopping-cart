package model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CampaignTest {
    private static final double APPLE_PRICE = 12.0;
    private Category category;
    private Product product;
    private Campaign campaign;
    private ShoppingCart cart;

    @Before
    public void setUp() {
        category = new Category("Food");
        product = new Product("Apple", APPLE_PRICE, category);
        campaign = new Campaign(category, 20.0, 3, DiscountType.Rate);
        cart = new ShoppingCart();
    }

    @Test
    public void campaingsApplicableToAproductCategory() {
        Assert.assertNotNull(campaign.getCategory());
    }

    @Test
    public void whenLessThan2ItemsBoughtThenNoCampaignApplied() {
        cart.addItem(product, 2);
        Campaign campaign = new Campaign(category, 20.0, 3, DiscountType.Rate);
        cart.applyDiscounts(campaign);
        assertEquals(0, cart.getCampaignDiscount(), 0.0);
    }

    @Test
    public void whenMoreThan3ItemsBoughtWithSingleSuitableCampaignThenCampaignApplied() {
        int quantity = 4;
        cart.addItem(product, quantity);
        double discountPercentage = 20.0;
        Campaign campaign = new Campaign(category, discountPercentage, 3, DiscountType.Rate);

        cart.applyDiscounts(campaign);
        assertEquals(APPLE_PRICE * quantity * discountPercentage / 100, cart.getCampaignDiscount(), 0.0);
    }

    @Test
    public void whenMoreThan3ItemsBoughtWithAmountCampaignThenCampaignApplied() {
        int quantity = 4;
        cart.addItem(product, quantity);
        Campaign campaign = new Campaign(category, 5.0, 3, DiscountType.Amount);

        cart.applyDiscounts(campaign);
        assertEquals(5.0, cart.getCampaignDiscount(), 0.0);
    }

    @Test
    public void whenMoreThan3ItemsBoughtWithTwoSuitableCampaignThenTheBestCampaignApplied() {
        int quantity = 6;
        cart.addItem(product, quantity);

        Campaign campaignWith20DiscountPercentage = new Campaign(category, 20.0, 3, DiscountType.Rate);
        Campaign campaignWith50DiscountPercentage = new Campaign(category, 50.0, 5, DiscountType.Rate);

        cart.applyDiscounts(campaignWith20DiscountPercentage, campaignWith50DiscountPercentage);
        assertEquals(APPLE_PRICE * quantity * 50 / 100, cart.getCampaignDiscount(), 0.0);

        Campaign campaignWith70TL = new Campaign(category, 70.0, 5, DiscountType.Amount);
        cart.applyDiscounts(campaignWith20DiscountPercentage, campaignWith50DiscountPercentage
                , campaignWith70TL);
        assertEquals(70.0,cart.getCampaignDiscount(),0.0);
    }

    @Test
    public void whenBoughtItemHaveDifferentCategoriesWithEachCategoryHavingSuitableCampaignsThenTheBestApplied() {
        Category categoryA = new Category("A");
        Category categoryB = new Category("B", categoryA);
        Category categoryC = new Category("C", categoryB);
        Product product = new Product("Apple", 100.0, categoryC);
        cart.addItem(product, 6);
        Campaign campaignForCategoryA = new Campaign(categoryA, 50.0, 5, DiscountType.Rate);
        Campaign campaignForCategoryB = new Campaign(categoryB, 20.0, 3, DiscountType.Rate);
        Campaign campaignForCategoryC = new Campaign(categoryC, 10.0, 3, DiscountType.Rate);

        cart.applyDiscounts(campaignForCategoryA, campaignForCategoryB, campaignForCategoryC);

        assertEquals(300.0, cart.getCampaignDiscount(), 0.0);

        Campaign anotherCampaignForCategoryA = new Campaign(categoryA, 400.0, 5, DiscountType.Amount);
        cart.applyDiscounts(campaignForCategoryA, campaignForCategoryB, campaignForCategoryC, anotherCampaignForCategoryA);

        assertEquals(400.0, cart.getCampaignDiscount(), 0.0);
    }

    @Test
    public void whenBoughtItemsHaveDifferentCategoriesWithEachCategoryHavingSuitableCampaignsThenTheBestApplied2() {
        Category categoryA = new Category("A");
        Category categoryB = new Category("B", categoryA);
        Category categoryC = new Category("C", categoryB);
        Product product = new Product("Apple", 100.0, categoryC);
        Category categoryElectronic = new Category("Electronic");
        Product productTV = new Product("TV", 2_000.0, categoryElectronic);
        cart.addItem(product, 6);
        cart.addItem(productTV, 3);
        Campaign campaignForCategoryA = new Campaign(categoryA, 50.0, 5, DiscountType.Rate);
        Campaign campaignForCategoryB = new Campaign(categoryB, 20.0, 3, DiscountType.Rate);
        Campaign campaignForCategoryC = new Campaign(categoryC, 10.0, 3, DiscountType.Rate);
        Campaign campaignForElectronic = new Campaign(categoryElectronic, 10, 2, DiscountType.Rate);

        cart.applyDiscounts(campaignForCategoryA, campaignForCategoryB, campaignForCategoryC, campaignForElectronic);

        assertEquals(900.0, cart.getCampaignDiscount(), 0.0);

        Campaign anotherCampaignForCategoryA = new Campaign(categoryA, 400.0, 5, DiscountType.Amount);
        cart.applyDiscounts(campaignForCategoryA, campaignForCategoryB, campaignForCategoryC, anotherCampaignForCategoryA, campaignForElectronic);

        assertEquals(1_000.0, cart.getCampaignDiscount(), 0.0);
    }
}
