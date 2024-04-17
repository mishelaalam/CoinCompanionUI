/**
 * Date - April 08, 2024
 * T01 - TA Javad Sahebnasi
 *
 * @author Carlos Jonathan Bernal Torres
 * @author Mishela Alam
 * @author Badhan Chandra Saha
 */
package coinCompanion.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import static coinCompanion.app.Main.currentProfile;

/**
 * This class controls all the events that take place on the AddIncome.fxml, it
 * allows the user to add an extra income,
 * it first takes in the income amount,then takes a date with a calendar feature
 * that does not allow to select any
 * future dates, then the action of clicking the confirm button, then triggers
 * the goal progress bar, and it also
 * changes the total income value and balance, which can be seen from the
 * income/balance buttons on the top of the
 * Main GUI
 */

public class AddIncomeController implements Initializable {
  @FXML
  private DatePicker incomeDatePicker; // Calender feature
  @FXML
  private TextField addIncomeAmount; // Text field that takes in the income amount
  @FXML
  private Button addIncomeConfirm; // Confirm button that adds the income
  @FXML
  private Label addIncomeStatus; // This label displays any error status with text field input
  @FXML
  private Label addIncomeDateStatus; // This label displays any error status with the date input

  /**
   * This method deals with all the backend for the income GUI, it adds the income
   * amount, and date
   * It also creates an income object for the current profile
   * 
   * @param event - mouse click on Confirm button
   */
  public void addExtraIncome(ActionEvent event) {

    // grabs the text field value
    String incomeAmountTextValue = addIncomeAmount.getText();
    boolean wasIncomeAmountWrong = false;
    double validIncome = 0;

    try {
      // Parses into a double
      validIncome = Double.parseDouble(incomeAmountTextValue);
      addIncomeStatus.setVisible(false);

      // If there is an exception, will display error label
    } catch (NumberFormatException e) {
      addIncomeStatus.setVisible(true);
      wasIncomeAmountWrong = true;
    }

    // Grabs the value of the date picker field
    String incomeDateValue = String.valueOf(incomeDatePicker.getValue());
    addIncomeDateStatus.setVisible(incomeDateValue.equals("null"));

    if (addIncomeDateStatus == null || wasIncomeAmountWrong) {
      return;
    }

    // Define the input date formatter
    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Parse the input string into a LocalDate object
    LocalDate date = LocalDate.parse(incomeDateValue, inputFormatter);

    // Define the output date formatter
    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Format the LocalDate object into the desired output format
    String formattedDate = date.format(outputFormatter);

    // Adds the income to the balance, then sets the new balance
    double balance = currentProfile.getBalance();
    currentProfile.setBalance(balance + validIncome);

    // writes into the Income class with the data and income amount
    currentProfile.writeIncome(formattedDate, validIncome);

    // if income add is successful, it creates an alert showing add is successful
    Alert successfulAddIncome = new Alert(Alert.AlertType.INFORMATION);
    successfulAddIncome.setTitle("Successful Add Income");
    successfulAddIncome.setHeaderText("Success!");
    successfulAddIncome.setContentText("Successfully added income!");
    successfulAddIncome.showAndWait();

    Stage stage = (Stage) addIncomeConfirm.getScene().getWindow();

    // Close the stage
    stage.close();

  }

  /**
   * This feature disables future dates inside the calender feature, and formats
   * the date to "dd/MM/yyyy" inside GUI
   *
   * @param location  - GUI
   * @param resources - resources
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {

    // Set the date picker cell factory to disable future dates
    incomeDatePicker.setDayCellFactory(picker -> new DateCell() {
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
    incomeDatePicker.setConverter(converter);
  }

}
