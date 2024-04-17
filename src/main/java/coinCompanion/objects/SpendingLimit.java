/**
 * Date - April 12, 2024
 * T01 - TA Javad Sahebnasi
 *
 * @author Carlos Jonathan Bernal Torres
 * @author Mishela Alam
 * @author Badhan Chandra Saha
 */
package coinCompanion.objects;

import coinCompanion.enums.SpendingCategories;

/**
 * Class for the Spending Limits
 */
public class SpendingLimit {
    private final SpendingCategories category; // category
    private final double limit; // limit amount
    private final double exceededBy; // amount that has exceeded limit

    /**
     * Creates a Spending Limit Object
     * @param category - spending category
     * @param limit - limit amount
     * @param exceededBy -  amount that has exceeded limit
     */
    public SpendingLimit(SpendingCategories category, double limit, double exceededBy) {
        this.category = category;
        this.limit = limit;
        this.exceededBy = exceededBy;
    }

    /**
     * grabs the amount it has exceeded by
     */
    public double getExceededBy() {
        return exceededBy;
    }

    /**
     * grabs the limit
     */
    public double getLimit() {
        return limit;
    }

    /**
     * grabs the category
     */
    public String getCategory() {
        return switch (this.category) {
            case transportation -> "Transportation";
            case groceries -> "Groceries";
            case dining -> "Dining";
            case personal -> "Personal";
            case entertainment -> "Entertainment";
            case clothing -> "Clothing";
            case health -> "Health";
            case investments -> "Investments";
            case travel -> "Travel";
            default -> "Other";
        };
    }
}
