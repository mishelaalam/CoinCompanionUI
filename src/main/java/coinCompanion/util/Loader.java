/**
 * Date - March 19, 2024
 * T01 - TA Javad Sahebnasi
 *
 * @author Carlos Jonathan Bernal Torres
 * @author Mishela Alam
 * @author Badhan Chandra Saha
 */

package coinCompanion.util;


import coinCompanion.enums.SpendingCategories;
import coinCompanion.objects.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Loader {
    public static HashMap<String, Profile> load(String fileName) {
        HashMap<String, Profile> profiles = new HashMap<>();
        try {
            FileReader readerAlpha = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(readerAlpha);

            // Start of Data Validation
            // Validates if the first line is the profileInteger
            int profileInteger;
            try {
                profileInteger = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                System.err.println("FileLoader failed to parse Profile Integer.");
                reader.close();
                return null;
            }

            // Starts Looping until all data is read.
            String nextLine = reader.readLine();
            while (true) {
                if (!nextLine.equals("Profile")) {
                    System.err.println("CSV format unrecognized.");
                    reader.close();
                    return null;
                }

                String[] profile = reader.readLine().split(",");
                if (profile.length != 11 && profile.length != 10) {
                    System.err.println("Missing attributes in profiles.");
                    reader.close();
                    return null;
                }

                String profileID = profile[0];
                String lastChar = profileID.substring(profileID.length() - 1);
                if (!Validator.isInteger(lastChar)) {
                    System.err.println("Failed to parse profileID");
                    reader.close();
                    return null;
                }

                String name = profile[1];

                String creationDate = profile[2];
                if (!Validator.isValidDate(creationDate)) {
                    System.err.printf("Creation Date format is invalid in %s's profile.\n", name);
                    reader.close();
                    return null;
                }
                if (!Validator.checkFutureDatesAndPresentDay(creationDate)) {
                    System.err.printf("Creation Date for %s's profile is in the future.\n", name);
                    reader.close();
                    return null;
                }

                double balance;
                try {
                    balance = Helper.parseDouble(profile[3]);
                } catch (NumberFormatException e) {
                    System.err.printf("FileLoader failed to parse %s's Balance.\n", name);
                    reader.close();
                    return null;
                }

                double fixedIncome;
                try {
                    fixedIncome = Helper.parseDouble(profile[4]);
                } catch (NumberFormatException e) {
                    System.err.printf("FileLoader failed to parse %s's Fixed Income.\n", name);
                    reader.close();
                    return null;
                }

                String payDate = profile[5];
                try {
                    if (Integer.parseInt(payDate) != Validator.validatePositiveIntIsBetween(payDate, 1, 28)) {
                        System.err.printf("Pay Date integer is invalid in %s's profile.\n", name);
                        return null;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Failed to parse Pay Date");
                }

                double fixedExpense;
                try {
                    fixedExpense = Helper.parseDouble(profile[6]);
                } catch (NumberFormatException e) {
                    System.err.printf("FileLoader failed to parse %s's Fixed Expense.\n", name);
                    reader.close();
                    return null;
                }

                String billDate = profile[7];
                try {
                    if (Integer.parseInt(billDate) != Validator.validatePositiveIntIsBetween(billDate, 1, 28)) {
                        System.err.printf("Bill Date integer is invalid in %s's profile.\n", name);
                        return null;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Failed to parse Bill Date");
                }

                String goalName = profile[8];

                double goalAmount;
                try {
                    goalAmount = Helper.parseDouble(profile[9]);
                } catch (NumberFormatException e) {
                    System.err.printf("FileLoader failed to parse %s's Goal Amount.\n", name);
                    reader.close();
                    return null;
                }

                HashMap<SpendingCategories, Double> spendingCategories = new HashMap<>();
                if (profile.length == 11) {
                    String[] spendingLimit = profile[10].split(";");
                    for (String element : spendingLimit) {
                        String[] currentCategory = element.split("~");
                        try {
                            spendingCategories.put(SpendingCategories.valueOf(currentCategory[0]),
                                    Helper.parseDouble(currentCategory[1]));
                        } catch (IllegalArgumentException e) {
                            System.err.printf("Spending Categories failed to validate for profile - %s.\n", name);
                            reader.close();
                            return null;
                        }
                    }
                }

                // Construct the initial profile with validated data
                Profile constructProfile = new Profile(name, creationDate, balance, fixedIncome, payDate, fixedExpense,
                        billDate, goalName, goalAmount, spendingCategories);

                //////////////////////////////////////////////////////////////////////////////////////////
                // Start validating and adding incomes
                nextLine = reader.readLine();
                if (!nextLine.equals("Incomes")) {
                    System.err.println("CSV format unrecognized.");
                    reader.close();
                    return null;
                }

                nextLine = reader.readLine();
                while (!nextLine.equals("Expenses")) {
                    String[] income = nextLine.split(",");

                    if (income.length != 2) {
                        System.err.printf("Wrong number of attributes in Income for profile - %s.\n", name);
                        reader.close();
                        return null;
                    }

                    String incomeDate = income[0];
                    if (!Validator.isValidDate(incomeDate)) {
                        System.err.printf("Income Date invalid for profile - %s.\n", name);
                        reader.close();
                        return null;
                    }
                    if (!Validator.checkFutureDatesAndPresentDay(incomeDate)) {
                        System.err.printf("Income Date for %s's profile is in the future.\n", name);
                        reader.close();
                        return null;
                    }

                    double incomeAmount;
                    try {
                        incomeAmount = Helper.parseDouble(income[1]);
                    } catch (NumberFormatException e) {
                        System.err.printf("FileLoader failed to parse %s's Income amount.\n", name);
                        reader.close();
                        return null;
                    }

                    constructProfile.writeIncome(incomeDate, incomeAmount);
                    nextLine = reader.readLine();
                    if (nextLine == null) {
                        System.err.println("CSV format unrecognized.");
                        reader.close();
                        return null;
                    }
                }

                //////////////////////////////////////////////////////////////////////////////////////////
                // Start validating and adding expense
                nextLine = reader.readLine();
                while (nextLine != null && !nextLine.equals("Profile")) {
                    String[] expense = nextLine.split(",");

                    if (expense.length != 3) {
                        System.err.printf("Wrong number of attributes in Expense for profile - %s.\n", name);
                        reader.close();
                        return null;
                    }

                    String expenseDate = expense[0];
                    if (!Validator.isValidDate(expenseDate)) {
                        System.err.printf("Expense date invalid for profile - %s.\n", name);
                        reader.close();
                        return null;
                    }
                    if (!Validator.checkFutureDatesAndPresentDay(expenseDate)) {
                        System.err.printf("Expense Date for %s's profile is in the future.\n", name);
                        reader.close();
                        return null;
                    }

                    double expenseAmount;
                    try {
                        expenseAmount = Helper.parseDouble(expense[1]);
                    } catch (NumberFormatException e) {
                        System.err.printf("FileLoader failed to parse %s's Expense amount.\n", name);
                        reader.close();
                        return null;
                    }

                    SpendingCategories expenseCategory;
                    try {
                        expenseCategory = SpendingCategories.valueOf(expense[2]);
                    } catch (IllegalArgumentException e) {
                        System.err.printf("FileLoader failed to validate Expense Category for profile - %s.\n", name);
                        reader.close();
                        return null;
                    }

                    constructProfile.writeExpense(expenseDate, expenseAmount, expenseCategory);
                    nextLine = reader.readLine();
                }
                profiles.put(profileID, constructProfile);
                if (nextLine == null) {
                    Profile.setProfileId(profileInteger);
                    reader.close();
                    readerAlpha.close();
                    return profiles;
                }
            }
        } catch (IOException e) {
            System.err.println("FileLoader failed to load file - " + fileName);
            return null;
        }
    }

}
