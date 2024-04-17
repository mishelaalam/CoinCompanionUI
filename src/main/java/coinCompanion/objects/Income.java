/**
 * Date - March 19, 2024
 * T01 - TA Javad Sahebnasi
 *
 * @author Carlos Jonathan Bernal Torres
 * @author Mishela Alam
 * @author Badhan Chandra Saha
 */

package coinCompanion.objects;

import java.util.Objects;

/**
 * The Income class represents a Transaction that is adding to the balance of the profile, useful for tracking the
 * income of the specific profile by storing the date and the amount of the income that was received
 */
public class Income extends Transaction {

    /**
     * Constructs an Income object with the specified attributes.
     *
     * @param date - the date income was received
     * @param amount - the amount that was received
     */
    public Income(String date, double amount) {
        super(date, amount);
    }

    /**
     * Creates a String representation for income objects
     */

    @Override
    public String toString() {
        return getDate() + "," + getAmount() + "\n";
    }

    /**
     * Checks if there is an identical income object with the same date and same amount
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Income income = (Income) o;
        return super.getDate().equals(income.getDate()) && super.getAmount() == income.getAmount();
    }

    /**
     * Checks if the two income objects have the same hashCode, which indicates it is the same object
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.getDate(), super.getAmount());
    }

}
