# Shopping Cart Kata using Java 11
## Build
```
mvn package
```

## Test
```
mvn test
```

## Improvements to do
- instead of using double for money, money pattern( with BigDecimal) should be used for accuracy
- percentage edge cases such as -1,100 should be checked in Coupon and Campaign classes and tests should be added.
- percentage and money amount currently using the same variable. They must be two distinct varibles with clear purposes as follows:
Money discountAmount=..
double discountPercentage=;
- ShoppingCart's default constructor should be removed. It has dependencies to CartPrinter and DeliveryCostCalculator and without them it loses meaning.
- Optional.ofNullable should be used for parentCategory field in the Category class