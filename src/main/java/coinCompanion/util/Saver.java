/**
 * Date - March 19, 2024
 * T01 - TA Javad Sahebnasi
 *
 * @author Carlos Jonathan Bernal Torres
 * @author Mishela Alam
 * @author Badhan Chandra Saha
 */

package coinCompanion.util;

import coinCompanion.objects.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Saver {
  private static final String csv = System.getProperty("user.dir") + "\\data.csv";

  /**
   * Stores data in a data.txt csv file in the current directory, for retrieval in
   * a later program boot-up.
   *
   * @return An indicator of whether the write was successful.
   */
  public static boolean save(HashMap<String, Profile> profiles, boolean append) {
    StringBuilder saveFile = new StringBuilder();

    int id = Profile.getProfileId();
    saveFile.append(id);
    saveFile.append("\n");

    for (var entry : profiles.entrySet()) {
      Profile profile = entry.getValue();
      ArrayList<Income> incomes = profile.getIncomes();
      ArrayList<Expense> expenses = profile.getExpenses();

      saveFile.append("Profile\n");
      saveFile.append(profile.toString());

      saveFile.append("\nIncomes\n");
      for (Income income : incomes)
        saveFile.append(income.toString());

      saveFile.append("Expenses\n");
      for (Expense expense : expenses)
        saveFile.append(expense.toString());
    }

    try {
      FileWriter writer = new FileWriter(csv, append);
      writer.write(saveFile.toString());
      writer.close();
      return true;
    } catch (IOException exception) {
      return false;
    }
  }

  /**
   * Overloaded save method with override set to false.
   * 
   * @param profiles
   * @return
   */
  public static boolean save(HashMap<String, Profile> profiles) {
    return save(profiles, false);
  }

}
