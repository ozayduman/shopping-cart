package model.cartprinter;

import model.Category;
import model.Product;
import model.ShoppingCart;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class CartPrinterImpl implements CartPrinter {

    private static final String NEW_LINE = "\n";
    private static final String SPACE = " ";

    @Override
    public String print(ShoppingCart shoppingCart) {
        Map<Category, List<Product>> categoryProductList = shoppingCart.groupProductsInCartByRootCategory();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CategoryName, ProductName, Quantity, Unit Price \n");
        categoryProductList.forEach(((category, products) -> products
                .stream()
                .sorted(Comparator.comparing(Product::getTitle))
                .forEach(product -> {
                    prepareProductDetail(shoppingCart, stringBuilder, category, product);
                })));
        prepareFooterDetail(shoppingCart, stringBuilder);
        return stringBuilder.toString();
    }

    private void prepareProductDetail(ShoppingCart shoppingCart, StringBuilder stringBuilder, Category category, Product product) {
        stringBuilder.append(category.getTitle());
        stringBuilder.append(SPACE);
        stringBuilder.append(product.getTitle());
        stringBuilder.append(SPACE);
        stringBuilder.append(shoppingCart.getQuantityOf(product));
        stringBuilder.append(SPACE);
        stringBuilder.append(product.getPrice());
        stringBuilder.append(NEW_LINE);
    }

    private void prepareFooterDetail(ShoppingCart shoppingCart, StringBuilder stringBuilder) {
        stringBuilder.append("Total Price :");
        stringBuilder.append(shoppingCart.getCartTotalBeforeDiscounts());
        stringBuilder.append(NEW_LINE);
        stringBuilder.append("Total Discount (coupon, campaign) :");
        stringBuilder.append(shoppingCart.getCampaignDiscount() + shoppingCart.getCouponDiscount());
        stringBuilder.append(NEW_LINE);
        stringBuilder.append("Total Amount: ");
        stringBuilder.append(shoppingCart.getTotalAmountAfterDiscounts());
        stringBuilder.append(" Delivery Cost: ");
        stringBuilder.append(shoppingCart.getDeliveryCost());
    }
}
