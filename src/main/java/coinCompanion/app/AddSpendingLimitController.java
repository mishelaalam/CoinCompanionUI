/**
 * Date - April 12, 2024
 * T01 - TA Javad Sahebnasi
 *
 * @author Carlos Jonathan Bernal Torres
 * @author Mishela Alam
 * @author Badhan Chandra Saha
 */
package coinCompanion.app;

import coinCompanion.enums.SpendingCategories;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;
import static coinCompanion.app.Main.currentProfile;

/**
 * This class controls all the events that take place on the
 * AddSpendingLimit.fxml, it allows the user to add a spending
 * limit to a spending category.Then the action of clicking the confirm button,
 * then triggers the spending limit table,
 * where the new value appears and can be seen whenever you click on the
 * spending limit button in the Main GUI.
 */
public class AddSpendingLimitController implements Initializable {
  @FXML
  private ChoiceBox<String> spendingLimitCategory; // Choice box for the spending categories
  @FXML
  private TextField spendingLimitAmount; // text field that takes in the spending limit amount
  @FXML
  private Label spendingLimitAmountError; // This label displays any error status with text field input
  @FXML
  private Label spendingLimitCategoryError; // This label displays any error status with choice box input
  @FXML
  private Button addSpendingLimitConfirmButton; // Confirm button that adds the spending limit

  /**
   * This method deals with all the backend for the spending limit GUI, it adds
   * the category, then the spending limit
   * amount
   */
  public void setSpendingLimit() {
    // Grabs the text of the spending limit amount
    String spendingLimitAmountString = spendingLimitAmount.getText();
    double spendingLimitAmountDouble = 0;
    try {
      // parses it into a double
      spendingLimitAmountDouble = Double.parseDouble(spendingLimitAmountString);
      spendingLimitAmountError.setVisible(false);

      // if there is an error it displays the error message
    } catch (NumberFormatException e) {
      spendingLimitAmountError.setVisible(true);
    }

    // grabs the value of the choice box
    String spendingCategoryString = spendingLimitCategory.getValue();
    spendingLimitCategoryError.setVisible(spendingCategoryString == null);

    if (spendingLimitAmountString == null || spendingLimitAmountString.isEmpty() || spendingCategoryString == null) {
      return;
    }

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

    // if successful, sets the spending limit into the spending limit class
    currentProfile.setSpendingLimit(categoryType, spendingLimitAmountDouble);

    // If successful, then displays successful alert message
    Alert successfulAddSpendingLimit = new Alert(Alert.AlertType.INFORMATION);
    successfulAddSpendingLimit.setTitle("Successful Add Spending Limit");
    successfulAddSpendingLimit.setHeaderText("Success!");
    successfulAddSpendingLimit.setContentText("Successfully added the Spending Limit!");
    successfulAddSpendingLimit.showAndWait();

    Stage stage = (Stage) addSpendingLimitConfirmButton.getScene().getWindow();

    // Close the stage
    stage.close();
  }

  /**
   * This method populates the choice box with these text values
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    String[] categories = { "Transportation", "Groceries", "Dining", "Personal", "Entertainment", "Clothing", "Health",
        "Investments", "Travel", "Other" };
    spendingLimitCategory.getItems().addAll((categories));
  }
}
