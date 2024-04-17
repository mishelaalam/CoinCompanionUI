/**
 * Date - March 19, 2024
 * T01 - TA Javad Sahebnasi
 *
 * @author Carlos Jonathan Bernal Torres
 * @author Mishela Alam
 * @author Badhan Chandra Saha
 */

package coinCompanion.objects;

import coinCompanion.enums.SpendingCategories;

import java.util.Objects;

/**
 * The Expense Class represents a Transaction that is taking away from the balance of the profile, useful for tracking
 * the spending of the specific profile by storing the date and the amount of the expense and the type of expense
 * category
 */
public class Expense extends Transaction {
    private final SpendingCategories category;

    /**
     * Constructs an Expense object with the specified attributes.
     *
     * @param date - the date income was received
     * @param amount - the amount that was received
     * @param category - type of expense
     */
    public Expense(String date, double amount, SpendingCategories category) {
        super(date, amount);
        this.category = category;
    }

    /**
     * Retrieves the category type that was related to the expense
     */
    public SpendingCategories getCategory() {
        return category;
    }

    /**
     * Creates a String representation for expense objects
     */

    @Override
    public String toString() {
        return getDate() + "," + getAmount() + "," + getCategory() + "\n";
    }

    /**
     * Checks if there is an identical expense object with the same date, same amount, and same category
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return super.getDate().equals(expense.getDate()) && super.getAmount() == expense.getAmount() && this.category == expense.category;
    }

    /**
     * Checks if the two income objects have identical hashCode, indicating it is the same object
     */

    @Override
    public int hashCode() {
        return Objects.hash(super.getDate(), super.getAmount(), category);
    }
}
