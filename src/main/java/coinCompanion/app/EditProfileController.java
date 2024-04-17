/**
 * Date - April 08, 2024
 * T01 - TA Javad Sahebnasi
 *
 * @author Carlos Jonathan Bernal Torres
 * @author Mishela Alam
 * @author Badhan Chandra Saha
 */
package coinCompanion.app;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import coinCompanion.util.Helper;
import coinCompanion.util.Validator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import static coinCompanion.app.Main.currentProfile;

/**
 * This class controls all the events on the EditProfile.fxml
 */
public class EditProfileController implements Initializable {
  @FXML
  private TextField editBillAmount; // Text field for the edit bill amount
  @FXML
  private TextField editBillDate; // Text field for the edit bill date
  @FXML
  private TextField editGoal; // Text field for the edit goal
  @FXML
  private TextField editGoalAmount; // Text field for the edit goal amount
  @FXML
  private TextField editPayAmount; // Text field for the edit pay amount
  @FXML
  private TextField editPayday; // Text field for the edit pay day amount
  @FXML
  private Label invalidEditBillAmount; // This label displays any error status with the edit bill amount input
  @FXML
  private Label invalidEditBillDate;// This label displays any error status with the edit bill date input
  @FXML
  private Label invalidEditGoal;// This label displays any error status with the edit goal input
  @FXML
  private Label invalidEditGoalAmount; // This label displays any error status with the edit goal amount input
  @FXML
  private Label invalidEditPayDate; // This label displays any error status with the edit pay date input
  @FXML
  private Label invalidEditPaycheck; // This label displays any error status with the edit pay check input

  /**
   * Initializes text fields with the profile's current information.
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    editBillAmount.setText(String.valueOf(currentProfile.getFixedExpenses()));
    editBillDate.setText(String.valueOf(currentProfile.getBillDate()));
    editGoal.setText(String.valueOf(currentProfile.getGoalName()));
    editGoalAmount.setText(String.valueOf(currentProfile.getGoalAmount()));
    editPayAmount.setText(String.valueOf(currentProfile.getFixedIncome()));
    editPayday.setText(String.valueOf(currentProfile.getPayDate()));
  }

  /**
   * Edits a profile.
   */
  public void editProfile(ActionEvent event) throws IOException {
    // Get all user inputs.
    String paycheckAmountInput = editPayAmount.getText().trim();
    String paydayInput = editPayday.getText().trim();
    String billAmountInput = editBillAmount.getText().trim();
    String billDateInput = editBillDate.getText().trim();
    String goalInput = editGoal.getText().trim();
    String goalAmountInput = editGoalAmount.getText().trim();

    // Reset all error message fields.
    invalidEditPaycheck.setVisible(false);
    invalidEditPayDate.setVisible(false);
    invalidEditBillAmount.setVisible(false);
    invalidEditBillDate.setVisible(false);
    invalidEditGoal.setVisible(false);
    invalidEditGoalAmount.setVisible(false);

    // Validate field by field, revealing a field's error message if it's invalid.
    boolean validInput = true;

    double paycheckAmount = 0d;
    if (paycheckAmountInput.isEmpty())
      paycheckAmount = currentProfile.getFixedIncome();
    else
      try {
        paycheckAmount = Double.parseDouble(paycheckAmountInput);
      } catch (NumberFormatException exception) {
        invalidEditPaycheck.setVisible(true);
        validInput = false;
      }

    if (paydayInput.isEmpty())
      paydayInput = currentProfile.getPayDate();
    else if (!Validator.validatePaymentDate(paydayInput)) {
      invalidEditPayDate.setVisible(true);
      validInput = false;
    }

    double billAmount = 0d;
    if (billAmountInput.isEmpty())
      billAmount = currentProfile.getFixedIncome();
    else
      try {
        billAmount = Double.parseDouble(billAmountInput);
      } catch (NumberFormatException exception) {
        invalidEditBillAmount.setVisible(true);
        validInput = false;
      }

    if (billDateInput.isEmpty())
      billDateInput = currentProfile.getBillDate();
    else if (!Validator.validatePaymentDate(billDateInput)) {
      invalidEditBillDate.setVisible(true);
      validInput = false;
    }

    if (goalInput.isEmpty())
      goalInput = currentProfile.getGoalName();
    else if (!Validator.min3ConsecutiveMax20(goalInput, false)) {
      invalidEditGoal.setVisible(true);
      validInput = false;
    }

    double goalAmount = 0d;
    if (goalAmountInput.isEmpty())
      goalAmount = currentProfile.getGoalAmount();
    else
      try {
        goalAmount = Double.parseDouble(goalAmountInput);
      } catch (NumberFormatException exception) {
        invalidEditGoalAmount.setVisible(true);
        validInput = false;
      }

    // Prevent further execution if a single input was invalid.
    if (!validInput)
      return;

    FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
    Parent root = loader.load();
    MainController controller = loader.getController();

    currentProfile.setFixedIncome(paycheckAmount);
    currentProfile.setPayDate(paydayInput);
    currentProfile.setFixedExpenses(billAmount);
    currentProfile.setBillDate(billDateInput);
    currentProfile.setGoalName(goalInput);
    currentProfile.setGoalAmount(goalAmount);

    Helper.alert(AlertType.INFORMATION, "Successfully edited profile!",
        "Successfully edited current profile.");

    controller.init();

    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  public void switchToMainApp() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
    Parent root = loader.load();
    MainController controller = loader.getController();

    controller.init();

    Stage stage = Main.getPrimaryStage();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }
}
