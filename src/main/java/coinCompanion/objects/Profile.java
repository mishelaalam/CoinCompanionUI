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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a user's financial profile, this class holds all the necessary
 * information about a user's profile,
 * such as their income, expenses, spending limits for different expense
 * categories. Each profile has a unique ID and
 * contains details such as the profile's name, creation date, balance, fixed
 * income, pay date, fixed expenses, bill
 * date, goal name, and goal amount.
 */

public class Profile {
    private static int profileId = 0; // The unique integer used in profile id construction.
    private final String id; // ID of the profile
    private final String name; // name of the profile
    private final String creationDate; // day profile was created
    private double balance; // balance of the profile
    private double fixedIncome; // fixed monthly income of the profile
    private String payDate; // monthly payment date
    private double fixedExpenses; // fixed monthly expense of the profile
    private String billDate; // monthly bill date
    private String goalName; // name of goal
    private double goalAmount; // goal amount
    private final HashMap<SpendingCategories, Double> spendingLimits; // Hashmap for spending limits for categories
    private final ArrayList<Income> incomes = new ArrayList<>(); // Arraylist that stores all the income for profile
    private final ArrayList<Expense> expenses = new ArrayList<>(); // Arraylist that stores all the expenses for profile

    /**
     * Constructs a Profile object with the specific attributes
     *
     * @param name           - name of the profile
     * @param creationDate   - date the profile was created
     * @param balance        - balance of the profile
     * @param fixedIncome    - fixed monthly income
     * @param payDate        - day you receive income
     * @param fixedExpenses  - fixed monthly expenses
     * @param billDate       - day you pay for your bills
     * @param goalName       - name of your goal you are trying to reach
     * @param goalAmount     - the amount you need to save to reach your goal
     * @param spendingLimits - limits you place for each spending category
     */
    public Profile(String name, String creationDate, double balance, double fixedIncome, String payDate,
                   double fixedExpenses, String billDate, String goalName, double goalAmount,
                   HashMap<SpendingCategories, Double> spendingLimits) {
        this.name = name;
        this.creationDate = creationDate;
        this.balance = balance;
        this.fixedIncome = fixedIncome;
        this.payDate = payDate;
        this.fixedExpenses = fixedExpenses;
        this.billDate = billDate;
        this.goalName = goalName;
        this.goalAmount = goalAmount;
        this.spendingLimits = spendingLimits;
        this.id = generateId();
    }

    // Start of Getter and Setters

    /**
     * Retrieves the id for the profile
     */
    public String getId() {
        return this.id;
    }

    /**
     * Retrieves the goal name for the profile
     */
    public String getGoalName() {
        return goalName;
    }

    /**
     * Sets a new goal name
     *
     * @param goalName - new goal name
     */
    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    /**
     * Retrieves the goal amount for the profile
     */
    public double getGoalAmount() {
        return goalAmount;
    }

    /**
     * Sets and updates the goal amount
     *
     * @param goalAmount - new goal amount
     */
    public void setGoalAmount(double goalAmount) {
        this.goalAmount = goalAmount;
    }

    /**
     * Retrieves the balance of the profile
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Sets and updates the balance
     *
     * @param balance - new updated balance
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Retrieves the fixed expenses for the profile
     */
    public double getFixedExpenses() {
        return fixedExpenses;
    }

    /**
     * Sets and updates the fixed expenses
     *
     * @param fixedExpenses - new fixed expense
     */
    public void setFixedExpenses(double fixedExpenses) {
        this.fixedExpenses = fixedExpenses;
    }

    /**
     * Retrieves the pay date for the profile
     */
    public String getPayDate() {
        return payDate;
    }

    /**
     * Sets and updates the pay date
     *
     * @param payDate - pay date
     */
    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    /**
     * Retrieves the bill date for the profile
     */
    public String getBillDate() {
        return billDate;
    }

    /**
     * Sets and updates the bill date
     *
     * @param billDate - new bill date
     */
    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    /**
     * Retrieves the name for the profile
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the fixed income for the profile
     */
    public double getFixedIncome() {
        return fixedIncome;
    }

    /**
     * Sets and updates the fixed income amount
     *
     * @param fixedIncome - new fixed income
     */
    public void setFixedIncome(double fixedIncome) {
        this.fixedIncome = fixedIncome;
    }

    /**
     * Retrieves all the spending limits for specific spending categories, that have
     * been placed so far
     */
    public HashMap<SpendingCategories, Double> getSpendingLimits() {
        return spendingLimits;
    }

    /**
     * Sets a new spending limit amount, specific to the expense category
     */
    public void setSpendingLimit(SpendingCategories category, double limit) {
        spendingLimits.put(category, limit);
    }

    /**
     * Retrieves all the expenses that have stored to the expense arraylist
     */
    public ArrayList<Expense> getExpenses() {
        return expenses;
    }

    /**
     * Retrieves all the incomes that have been stored to the income arraylist
     */
    public ArrayList<Income> getIncomes() {
        return incomes;
    }

    /**
     * Updates the unique id int used when constructing profiles. Required when
     * loading data from a save file.
     *
     * @param id - profile id
     */
    public static void setProfileId(int id) {
        profileId = id;
    }

    /**
     * Retrieves profile id
     */

    public static int getProfileId() {
        return profileId;
    }

    // End of Getter and Setters

    /**
     * Creates a String representation of the Profile Object and what is has stored
     * so far
     */
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        output.append(id).append(",");
        output.append(name).append(",");
        output.append(creationDate).append(",");
        output.append(balance).append(",");
        output.append(fixedIncome).append(",");
        output.append(payDate).append(",");
        output.append(fixedExpenses).append(",");
        output.append(billDate).append(",");
        output.append(goalName).append(",");
        output.append(goalAmount).append(",");

        for (var entry : spendingLimits.entrySet()) {
            String category = entry.getKey().toString();
            double limit = entry.getValue();
            output.append(String.format("%s~%.2f;", category, limit));
        }

        return output.toString();
    }

    /**
     * Creates an income object and stores the date and the income amount, specific
     * to the profile object
     *
     * @param date   - the date amount was received
     * @param amount - income amount
     */
    public void writeIncome(String date, double amount) {
        Income income = new Income(date, amount);
        incomes.add(income);
    }

    /**
     * Creates an expense object and stores the date, expense amount and expense
     * category, specific to the profile object
     *
     * @param date     - the date the expense took place
     * @param amount   - the expense amount
     * @param category - expense category
     */
    public void writeExpense(String date, double amount, SpendingCategories category) {
        Expense expense = new Expense(date, amount, category);
        expenses.add(expense);
    }

    /**
     * Generates a unique key to map to the given profile, to avoid overlapping key
     * errors in the hash map.
     *
     * @return The unique key, consisting of the first name in the profile's name
     *         property, and a unique number.
     */
    private String generateId() {
        Matcher firstName = Pattern.compile("^(?i)([a-z]+)").matcher(name);

        if (!firstName.find())
            return String.valueOf(profileId++);

        StringBuilder id = new StringBuilder();
        id.append(firstName.group(1));
        id.append(profileId);
        profileId++;
        return id.toString();
    }

    /**
     * Computes the average balance change from all data except for the current
     * month, and the balance change for the current month.
     *
     * @return A hash map containing a boolean indicator under "success" of whether
     *         the computation was successful. If successful, under "average" and
     *         "current" respectively, the average balance change over past months
     *         and the balance change in the current month.
     */
    public HashMap<String, Double> computeCurrentAndPastAverageBalanceChange() {
        HashMap<String, Double> result = new HashMap<>();
        HashMap<String, HashMap<String, Double>> changePerYearAndMonth = computeBalanceChangePerYearAndMonth();
        LocalDate now = LocalDate.now();
        String currentYear = String.valueOf(now.getYear());
        int currentMonth = now.getMonthValue();

        double average = 0;
        int monthCount = 0;
        for (var yearEntry : changePerYearAndMonth.entrySet()) {
            String year = yearEntry.getKey();
            HashMap<String, Double> monthChanges = yearEntry.getValue();

            for (var monthEntry : monthChanges.entrySet()) {
                String month = monthEntry.getKey();
                double balanceChange = monthEntry.getValue();

                if (year.equals(currentYear) && Integer.parseInt(month) == currentMonth) {
                    result.put("current", balanceChange);
                    continue;
                }

                average += balanceChange;
                monthCount++;
            }
        }

        if (monthCount != 0)
            average /= monthCount;
        result.put("average", average);
        if (!result.containsKey("current")) {
            double zero = 0;
            result.put("current", zero);
        }

        return result;
    }

    /**
     * Computes the spending and income per month, and for each month computes the
     * balance change (that is, the income of that month minus the spending of that
     * month).
     *
     * @return A HashMap mapping the year to a nested HashMap which maps the month
     *         to the change of that month.
     */
    private HashMap<String, HashMap<String, Double>> computeBalanceChangePerYearAndMonth() {
        HashMap<String, HashMap<String, Double>> changePerYearPerMonth = new HashMap<>();
        HashMap<String, HashMap<String, Double>> incomePerYearPerMonth = getSpendingOrIncomePerYearAndMonth(true);
        HashMap<String, HashMap<String, Double>> expensesPerYearPerMonth = getSpendingOrIncomePerYearAndMonth(false);

        for (var yearEntry : incomePerYearPerMonth.entrySet()) {
            String year = yearEntry.getKey();
            HashMap<String, Double> incomeForYear = yearEntry.getValue();
            HashMap<String, Double> changesForYear = changePerYearPerMonth.get(year);

            // If there's no income data for the current year, initialize it.
            if (changesForYear == null) {
                changesForYear = new HashMap<>();
                changePerYearPerMonth.put(year, changesForYear);
            }

            // O(n)
            for (var monthEntry : incomeForYear.entrySet()) {
                String month = monthEntry.getKey();
                double income = monthEntry.getValue();

                try {
                    // Check if there is an expense value for the same month (in the same year), and
                    // if so, subtract it from the current income. This now leaves only the expense
                    // months that do not have income data associated with them to account for.
                    Double expense = expensesPerYearPerMonth.get(year).get(month);
                    changesForYear.put(month, income - expense);
                } catch (NumberFormatException | NullPointerException exception) {
                    changesForYear.put(month, income);
                }
            }
        }

        for (var yearEntry : expensesPerYearPerMonth.entrySet()) {
            String year = yearEntry.getKey();
            HashMap<String, Double> expensesForYear = yearEntry.getValue();
            HashMap<String, Double> changesForYear = changePerYearPerMonth.get(year);

            if (changesForYear == null) {
                // If the current year still does not exist in savings, we know there was no
                // income data associated with that year, and so the savings for that year will
                // be the exact same hash map as the expenses for that year, except with the
                // values turned negative (as they are expenses).
                changesForYear = new HashMap<>();
                for (var monthEntry : expensesForYear.entrySet())
                    changesForYear.put(monthEntry.getKey(), -monthEntry.getValue());

                changePerYearPerMonth.put(year, changesForYear);
                continue;
            }

            // If we pass the previous guard clause, the current year did have income data,
            // and so we have to check for any months in the year that had only expense data
            // and no income data (if any).
            for (var monthEntry : expensesForYear.entrySet()) {
                String month = monthEntry.getKey();

                try {
                    if (incomePerYearPerMonth.get(year).containsKey(month))
                        continue;
                } catch (Exception e) {
                    continue;
                }

                // If we passed the guard clause above, it means this month of expenses has no
                // associated income data, and we can simply add its negative.
                changesForYear.put(month, -monthEntry.getValue());
            }
        }

        return changePerYearPerMonth;
    }

    /**
     * Computes the spending or income per year, per month.
     *
     * @param income A boolean indicator of whether to compute the income. If false,
     *               computes the spending instead.
     * @return A hash map mapping each year present in the incomes (or expenses) to
     *         another map, where the key-value pairs are the month and the month's
     *         spending/income, respectively. Hash map returned is empty if no
     *         expenses or income are found.
     */
    private HashMap<String, HashMap<String, Double>> getSpendingOrIncomePerYearAndMonth(boolean income) {
        HashMap<String, HashMap<String, Double>> valuesPerYearAndMonth = new HashMap<>();
        ArrayList<? extends Transaction> transactions = income ? incomes : expenses;

        for (Transaction transaction : transactions) {
            // Get year and month.
            String[] dayMonthYear = transaction.getDate().split("/");
            String year = dayMonthYear[2];
            String month = dayMonthYear[1];

            // Add map to current year if there isn't one.
            HashMap<String, Double> yearAmounts = valuesPerYearAndMonth.get(year);
            if (yearAmounts == null) {
                yearAmounts = new HashMap<>();
                valuesPerYearAndMonth.put(year, yearAmounts);
            }

            // Increase this month's spending if present.
            double transactionAmount = transaction.getAmount();
            if (yearAmounts.containsKey(month)) {
                double current = yearAmounts.get(month);
                yearAmounts.put(month, current + transactionAmount);
                continue;
            }

            // Otherwise, initialize it.
            yearAmounts.put(month, transactionAmount);
        }

        return valuesPerYearAndMonth;
    }

    /**
     * Calculates Percentages of Current Monthly Income Saved
     */
    public double calculatePercentageOfMonthlyIncomeSaved() {
        double totalIncome = 0.0;
        double totalExpenses = 0.0;

        // Grabs the current month
        LocalDate date = LocalDate.now();
        int currentMonth = date.getMonthValue();

        // Calculates total income for the current month
        for (Income income : incomes) {
            String dateString = income.getDate();
            LocalDate incomeDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            if (incomeDate.getMonthValue() == currentMonth) {
                totalIncome += income.getAmount();
            }
        }

        // Calculates total expenses for the current month
        for (Expense expense : expenses) {
            String dateString = expense.getDate();
            LocalDate expenseDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            if (expenseDate.getMonthValue() == currentMonth) {
                totalExpenses += expense.getAmount();
            }
        }

        // Calculates savings for this month
        double savings = totalIncome - totalExpenses;

        // Calculates the percentage of income saved
        double percentageSaved;
        percentageSaved = (savings / totalIncome) * 100;

        // Returns the percentage of income saved
        return percentageSaved;
    }

    /**
     * Calculates the total expense for a category
     *
     * @param category: The category to calculate the expenses for
     * @return a double of the total value
     */
    public double totalExpensesForCategory(SpendingCategories category) {
        double amount = 0;
        for (Expense expense : this.expenses) {
            if (expense.getCategory().equals(category)) {
                amount = amount + expense.getAmount();
            }
        }
        return amount;
    }

    public double getExceededBy(SpendingCategories category) {
        double totalExpenseForCategory = totalExpensesForCategory(category);
        double categoryLimit = spendingLimits.get(category);
        double exceededBy;
        if (totalExpenseForCategory <= categoryLimit) {
            exceededBy = 0;
        } else {
            exceededBy = totalExpenseForCategory - categoryLimit;
        }
        return exceededBy;
    }
}
