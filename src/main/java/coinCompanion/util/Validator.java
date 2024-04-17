/**
 * Date - March 19, 2024
 * T01 - TA Javad Sahebnasi
 *
 * @author Carlos Jonathan Bernal Torres
 * @author Mishela Alam
 * @author Badhan Chandra Saha
 */

package coinCompanion.util;

import java.time.LocalDate;

/**
 * Validates users' string inputs according to the expected data, and
 * converts data to the appropriate type if necessary.
 */
public class Validator {

    /**
     * Validates that the input contains at least three consecutive letters,
     * does not exceed twenty characters, and meets the digit requirements.
     *
     * @param input
     * @param allowDigits
     * @return Indicator of whether the name meets the requirements.
     */
    public static boolean min3ConsecutiveMax20(String input, boolean allowDigits) {
        String wholeStringRegex = String.format("^(?i)[a-z\\s%s]{3,20}$", allowDigits ? "\\d" : "");

        return input.matches("(?i).*[a-z]{3,}.*") && input.matches(wholeStringRegex);
    }

    /**
     * Validates that the input contains at least three consecutive letters,
     * and that the total string does not exceed twenty characters. Disallows
     * digits unless otherwise specified.
     *
     * @param input
     * @return A boolean indicator of whether the input meets the requirements.
     */
    public static boolean min3ConsecutiveMax20(String input) {
        return min3ConsecutiveMax20(input, false);
    }

    /**
     * Validates that the date input is a valid day that always exists in any given
     * month.
     *
     * @param paymentDateInput
     * @return An indicator of whether the date is always valid.
     */
    public static boolean validatePaymentDate(String paymentDateInput) {
        return paymentDateInput.matches("^0*([1-9]|1[0-9]|2[0-8])$");
    }

    /**
     * Checks whether a date in the format dd/mm/yyyy is valid or not.
     * Includes Checking for the number of days in a month (including leap years).
     *
     * @param date: The date to be verified
     * @return True if the date is valid, false otherwise
     */
    public static boolean isValidDate(String date) {
        String[] parts = date.split("/");
        if (parts.length != 3) {
            return false;
        }

        // Check if each part of the date consists only of numeric characters
        for (String part : parts) {
            if (!part.matches("\\d+")) {
                return false;
            }
        }

        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        if (year < 0) {
            return false; // Year cannot be negative
        }
        if (month < 1 || month > 12) {
            return false; // Month should be between 1 and 12
        }

        int maxDaysInMonth = 31; // Default to the maximum number of days

        // Adjust maxDaysInMonth for months with less than 31 days
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            maxDaysInMonth = 30;
        } else if (month == 2) {
            // Adjust maxDaysInMonth for February based on leap year
            if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                maxDaysInMonth = 29;
            } else {
                maxDaysInMonth = 28;
            }
        }

        return day >= 1 && day <= maxDaysInMonth;
    }

    /**
     * Validates that the input can be parsed into an integer, and that that integer
     * is between the specified range (inclusive).
     *
     * @param integerInput
     * @param min
     * @param max
     * @return The parsed integer within the range, or the int -1 if the
     *         integerInput did not meet the conditions.
     */
    public static int validatePositiveIntIsBetween(String integerInput, int min, int max) {
        try {
            int parsedInteger = Integer.parseInt(integerInput);
            return min <= parsedInteger && parsedInteger <= max && parsedInteger >= 0 ? parsedInteger : -1;
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    /**
     * Validates whether the input is an integer or not
     *
     * @param input: The String to be verified
     * @return True if the parameter is an integer, false otherwise
     */
    public static boolean isInteger(String input) {
        if (input == null) {
            return false;
        }
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the string representation of the date is in the future.
     * True - Date in the Past or present
     * False - Date in the future
     *
     * @param dateToCheck The date to check.
     * @return True if the date is not in the future, false otherwise
     *
     */
    public static boolean checkFutureDatesAndPresentDay(String dateToCheck) {
        LocalDate now = LocalDate.now();
        String[] dayMonthYear = dateToCheck.split("/");
        int day = Integer.parseInt(dayMonthYear[0]);
        int month = Integer.parseInt(dayMonthYear[1]);
        int year = Integer.parseInt(dayMonthYear[2]);
        LocalDate date = LocalDate.of(year, month, day);

        return now.isAfter(date) || now.isEqual(date);
    }

}