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
 * A class to format category spending for the table UI.
 */
public class CategoryExpense {
    private SpendingCategories category; // category
    private String date; // date
    private Double amount; // amount

    /**
     * Creates a category object
     */
    public CategoryExpense(SpendingCategories _category, String _date, Double _amount) {
        category = _category;
        date = _date;
        amount = _amount;
    }

    // switch statements to get the categories
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

    /**
     *  Grabs the date
     */
    public String getDate() {
        return date;
    }

    /**
     *  Grabs the amount
     */
    public Double getAmount() {
        return amount;
    }
    /**
     *  Sets the category
     */
    public void setCategory(SpendingCategories category) {
        this.category = category;
    }
    /**
     *  Sets the date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     *  Sets the amount
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
