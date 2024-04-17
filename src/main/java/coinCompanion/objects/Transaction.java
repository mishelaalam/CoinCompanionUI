/**
 * Date - March 19, 2024
 * T01 - TA Javad Sahebnasi
 *
 * @author Carlos Jonathan Bernal Torres
 * @author Mishela Alam
 * @author Badhan Chandra Saha
 */

package coinCompanion.objects;

/**
 * This abstract class Transaction represents a generic transaction that takes
 * place within a profile
 * It defines common attributes and behaviours for different types of
 * transactions
 */
public abstract class Transaction {
    private final String date; // Date transaction took place, in dd/mm/yyyy format.
    private final double amount; // Amount of the transaction

    /**
     * Constructs a Transaction object with these specific attributes
     *
     * @param date   - date of transaction
     * @param amount - amount of transaction
     */
    Transaction(String date, double amount) {
        this.date = date;
        this.amount = amount;
    }

//    /**
//     * Retrieves the month for the given transaction.
//     *
//     * @return The month the transaction took place.
//     */
//    public int getMonth() {
//        String[] dayMonthYear = date.split("/");
//        return Integer.parseInt(dayMonthYear[1]);
//    }

    /**
     * Retrieves the date for the given transaction
     *
     * @return The date the transaction took place.
     */
    public String getDate() {
        return date;
    }

    /**
     * Retrieves the transaction amount for the given transaction
     */
    public double getAmount() {
        return amount;
    }
}
