/**
 * Date - April 08, 2024
 * T01 - TA Javad Sahebnasi
 *
 * @author Carlos Jonathan Bernal Torres
 * @author Mishela Alam
 * @author Badhan Chandra Saha
 */
package coinCompanion.app;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import coinCompanion.enums.SpendingCategories;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import static coinCompanion.app.Main.currentProfile;

/**
 * This class controls all the events that take place on the AddExpense.fxml, it
 * allows the user to add an expense,
 * it first takes in the expense amount,then takes a date with a calendar
 * feature that does not allow to select any
 * future dates, then allows the user to select the expense type from a choice
 * box, then the action of clicking the
 * confirm button, then triggers the goal progress bar and the expense breakdown
 * table, and it also changes the
 * total expense value and balance value, which can be seen from the
 * expense/balance button on the top of the Main GUI
 */
public class AddExpenseController implements Initializable {
  @FXML
  public ChoiceBox<String> expenseSpendingCategory; // Choice box that contains all the spending categories
  @FXML
  private DatePicker expenseDatePicker; // Calendar feature
  @FXML
  private TextField expenseAmount; // Expense amount the user enters
  @FXML
  private Label expenseAmountError; // This label displays any error status with text field input
  @FXML
  private Label expenseCategoryError; // This label displays any error status with the choice box input
  @FXML
  private Label expenseDateError; // This label displays any error status with the date input
  @FXML
  private Button expenseConfirmButton; // Confirm button that adds the expense

  /**
   * This method deals with all the backend for the expense GUI, it adds the
   * expense amount, date and category
   */
  public void expenseBackend() {

    // Grabs the text from the textfield
    String expenseAmountString = expenseAmount.getText();
    boolean wasExpenseAmountWrong = false;
    double expenseAmountDouble = 0;

    // Parses text into a double
    try {
      expenseAmountDouble = Double.parseDouble(expenseAmountString);
      expenseAmountError.setVisible(false);

      // If any error is found, status label for text field becomes visible
    } catch (NumberFormatException e) {
      expenseAmountError.setVisible(true);
      wasExpenseAmountWrong = true;
    }

    // Grabs the value from the choice box
    String spendingCategoryString = expenseSpendingCategory.getValue();
    expenseCategoryError.setVisible(spendingCategoryString == null);

    // Grabs the value from the calendar
    String expenseDateString = String.valueOf(expenseDatePicker.getValue());
    expenseDateError.setVisible(expenseDateString.equals("null"));

    if (spendingCategoryString == null || expenseDateString.equals("null") || wasExpenseAmountWrong) {
      return;
    }

    // grabs current balance, and if the expense amount exceeds the balance it will
    // display an error message as
    // an alert
    double currentBalance = currentProfile.getBalance();
    if (currentBalance < expenseAmountDouble) {
      Alert alert = new Alert(Alert.AlertType.ERROR,
          String.valueOf("The expense Amount higher is than the current balance"));
      alert.setHeaderText("Can't afford the Transaction :(");
      alert.show();
      expenseAmount.setText("");
      return;
    }

    // Define the input date formatter
    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Parse the input string into a LocalDate object
    LocalDate date = LocalDate.parse(expenseDateString, inputFormatter);

    // Define the output date formatter
    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Format the LocalDate object into the desired output format
    String formattedDate = date.format(outputFormatter);

    // switch cases for spending categories using the enum
    SpendingCategories categoryType = switch (spendingCategoryString) {
      case "Transportation" -> SpendingCategories.transportation;
      case "Groceries" -> SpendingCategories.groceries;
      case "Dining" -> SpendingCategories.dining;
      case "Personal" -> SpendingCategories.personal;
      case "Entertainment" -> SpendingCategories.entertainment;
      case "Clothing" -> SpendingCategories.clothing;
      case "Health" -> SpendingCategories.health;
      case "Investments" -> SpendingCategories.investments;
      case "Travel" -> SpendingCategories.travel;
      default -> SpendingCategories.other;
    };

    // If successful, it subtracts the expense amount from the balance, then sets
    // the new balance
    currentBalance = currentBalance - expenseAmountDouble;
    currentProfile.setBalance(currentBalance);

    // writes the expense into expense class, with the date, amount and category
    // type
    currentProfile.writeExpense(formattedDate, expenseAmountDouble, categoryType);

    // If successful, it will display an alert
    Alert successfulAddExpense = new Alert(Alert.AlertType.INFORMATION);
    successfulAddExpense.setTitle("Successful Add Expense");
    successfulAddExpense.setHeaderText("Success!");
    successfulAddExpense.setContentText("Successfully added expense!");
    successfulAddExpense.showAndWait();

    Stage stage = (Stage) expenseConfirmButton.getScene().getWindow();

    // Close the stage
    stage.close();

  }

  /**
   * This method is creating the choice box for all the spending categories, also
   * this method disables future dates
   * inside the calendar feature
   * 
   * @param location  - location
   * @param resources - resource
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // populates the choice box with these text values
    String[] categories = { "Transportation", "Groceries", "Dining", "Personal", "Entertainment", "Clothing", "Health",
        "Investments", "Travel", "Other" };
    expenseSpendingCategory.getItems().addAll((categories));

    // Set the date picker cell factory to disable future dates
    expenseDatePicker.setDayCellFactory(picker -> new DateCell() {
      @Override
      public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);
        // Disable dates in the future
        if (date.isAfter(LocalDate.now())) {
          setDisable(true);
          setStyle("-fx-background-color: #bababa;"); // Optional: change disabled date style
        }
      }
    });

    // Set a specific date format for the date picker
    String pattern = "dd/MM/yyyy";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
      @Override
      public String toString(LocalDate date) {
        if (date != null) {
          return formatter.format(date);
        } else {
          return "";
        }
      }

      @Override
      public LocalDate fromString(String string) {
        if (string != null && !string.isEmpty()) {
          return LocalDate.parse(string, formatter);
        } else {
          return null;
        }
      }
    };
    // Sets the UI text field to the date format
    expenseDatePicker.setConverter(converter);
  }

}
