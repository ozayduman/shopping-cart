package model;

import model.cartprinter.CartPrinter;
import model.deliverycostcalculator.DeliveryCostCalculator;

import java.util.*;
import java.util.Map.Entry;

public class ShoppingCart {
    private final Map<Product, Integer> productQuantityMap;
    private double campaignDiscount;
    private double couponDiscount;
    private DeliveryCostCalculator deliveryCostCalculator;
    private CartPrinter cartPrinter;

    public ShoppingCart() {
        this.productQuantityMap = new HashMap<>();
    }

    public ShoppingCart(CartPrinter cartPrinter, DeliveryCostCalculator deliveryCostCalculator) {
        this();
        this.cartPrinter = cartPrinter;
        this.deliveryCostCalculator = deliveryCostCalculator;
    }

    public void addItem(Product product, int quantity) {
        this.productQuantityMap.put(product, quantity);
    }

    public Integer getQuantityOf(Product product) {
        return this.productQuantityMap.get(product);
    }

    public void applyDiscounts(CampaignDiscount... campaigns) {
        Map<Category, List<Product>> categoryProductListMap = groupProductsByCategory();
        Map<Category, List<CampaignDiscount>> rootCategoryCampaignListMap = groupCampaignsByRootCategory(campaigns);
        double totalCampaignDiscounts = .0;
        for (Entry<Category, List<CampaignDiscount>> entries : rootCategoryCampaignListMap.entrySet()) {
            List<CampaignDiscount> campaignsInTheSameCategoryTree = entries.getValue();
            double bestCampaignDiscountInTheSameCategoryTree = .0;
            for (CampaignDiscount campaign : campaignsInTheSameCategoryTree) {
                double discountAmountForCampaign = calculateCampaignDiscount(campaign, categoryProductListMap);
                bestCampaignDiscountInTheSameCategoryTree = Math.max(bestCampaignDiscountInTheSameCategoryTree, discountAmountForCampaign);
            }
            totalCampaignDiscounts += bestCampaignDiscountInTheSameCategoryTree;
        }
        campaignDiscount = totalCampaignDiscounts;
    }

    private Map<Category, List<Product>> groupProductsByCategory() {
        Map<Category, List<Product>> categoryProductListMap = new HashMap<>();
        productQuantityMap.forEach(((product, quantity) -> {
            Category productCategory = product.getCategory();
            categoryProductListMap.computeIfAbsent(productCategory, (category) -> new ArrayList<>()).add(product);
            while (productCategory.getParent() != null) {
                productCategory = productCategory.getParent();
                categoryProductListMap.computeIfAbsent(productCategory, (category) -> new ArrayList<>()).add(product);
            }
        }));
        return categoryProductListMap;
    }

    public double calculateCampaignDiscount(CampaignDiscount campaign, Map<Category, List<Product>> categoryProductListMap) {
        List<Product> productList = categoryProductListMap.getOrDefault(campaign.getCategory(), Collections.EMPTY_LIST);
        int totalItemQuantityInCategory = 0;
        double totalProductCostInCategory = .0;
        for (Product product : productList) {
            Integer quantity = productQuantityMap.get(product);
            totalItemQuantityInCategory += quantity;
            totalProductCostInCategory = product.getPrice() * quantity;
        }
        return campaign.calculateDiscountFor(totalItemQuantityInCategory, totalProductCostInCategory);
    }

    public void applyCoupon(final CouponDiscount coupon) {
        couponDiscount = coupon.calculateDiscountFor(this);
    }

    public double getCartTotalBeforeDiscounts() {
        return productQuantityMap.entrySet()
                .stream()
                .map(entry -> entry.getKey().getPrice() * entry.getValue())
                .reduce(Double::sum)
                .orElse(0.0);
    }

    public double getCouponDiscount() {
        return couponDiscount;
    }

    public double getCampaignDiscount() {
        return campaignDiscount;
    }

    public int getNumberOfDifferentProducts() {
        return productQuantityMap.size();
    }

    public boolean isEmpty() {
        return productQuantityMap.isEmpty();
    }

    public int getNumberOfDistinctCategories() {
        Set<Category> categorySet = new HashSet<>();
        int numberOfDistinctCategories = 0;
        for (Product product : productQuantityMap.keySet()) {
            boolean anyCategorieaOfTheProductExistsInTheSet = checkAnyCategoriesOfProductExist(categorySet, product);

            if (!anyCategorieaOfTheProductExistsInTheSet) {
                numberOfDistinctCategories++;
            }
        }
        return numberOfDistinctCategories;
    }

    private boolean checkAnyCategoriesOfProductExist(Set<Category> categorySet, Product product) {
        Category categoryOfProduct = product.getCategory();
        boolean anyCategoriesOfTheProductExistsInTheSet = false;
        while (categoryOfProduct != null) {
            if (categorySet.contains(categoryOfProduct)) {
                anyCategoriesOfTheProductExistsInTheSet = true;
                break;
            } else {
                categorySet.add(categoryOfProduct);
            }
            categoryOfProduct = categoryOfProduct.getParent();
        }
        return anyCategoriesOfTheProductExistsInTheSet;
    }

    public double getTotalAmountAfterDiscounts() {
        return getCartTotalBeforeDiscounts() - getCampaignDiscount() - getCouponDiscount();
    }

    public double getDeliveryCost() {
        return this.deliveryCostCalculator.calculateFor(this);
    }

    public String print() {
        return cartPrinter.print(this);
    }

    public Map<Category, List<Product>> groupProductsInCartByRootCategory() {
        Map<Category, List<Product>> categoryProductList = new HashMap<>();
        productQuantityMap.forEach((product, quantity) -> {
            Category productCategory = product.getCategory();
            while (productCategory.getParent() != null) {
                productCategory = productCategory.getParent();
            }
            categoryProductList.computeIfAbsent(productCategory, (category) -> new ArrayList<>()).add(product);
        });
        return categoryProductList;
    }

    public Map<Category, List<CampaignDiscount>> groupCampaignsByRootCategory(CampaignDiscount... campaigns) {
        Map<Category, List<CampaignDiscount>> rootCategoryCampaignList = new HashMap<>();
        for (CampaignDiscount campaign : campaigns) {
            Category campaignCategory = campaign.getCategory();
            while (campaignCategory.getParent() != null) {
                campaignCategory = campaignCategory.getParent();
            }
            rootCategoryCampaignList.computeIfAbsent(campaignCategory, (category -> new ArrayList<>())).add(campaign);
        }
        return rootCategoryCampaignList;
    }

    public void setDeliveryCostCalculator(DeliveryCostCalculator deliveryCostCalculator) {
        this.deliveryCostCalculator = deliveryCostCalculator;
    }

    public void setCartPrinter(CartPrinter cartPrinter) {
        this.cartPrinter = cartPrinter;
    }
}
