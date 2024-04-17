/**
 * Date - April 05, 2024
 * T01 - TA Javad Sahebnasi
 *
 * @author Carlos Jonathan Bernal Torres
 * @author Mishela Alam
 * @author Badhan Chandra Saha
 */
package coinCompanion.app;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.HashMap;
import coinCompanion.objects.Profile;
import coinCompanion.util.Loader;
import coinCompanion.util.Validator;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import static coinCompanion.app.Main.currentProfile;
import static coinCompanion.app.Main.profiles;
import static coinCompanion.app.Main.csv;

public class SignUpController {
  @FXML
  private TextField name; // Text field for the profile name
  @FXML
  private Label invalidName; // This label displays any error status with the name input
  @FXML
  private TextField balance; // Text field for the balance amount
  @FXML
  private TextField billAmount; // Text field for the bill amount
  @FXML
  private TextField billDate; // Text field for the bill date
  @FXML
  private TextField goal; // Text field for the goal name
  @FXML
  private TextField goalAmount; // Text field for the bill amount
  @FXML
  private Label invalidBalance; // This label displays any error status with the balance input
  @FXML
  private Label invalidBillAmount; // This label displays any error status with the bill amount input
  @FXML
  private Label invalidBillDate; // This label displays any error status with the bill date input
  @FXML
  private Label invalidGoal; // This label displays any error status with the goal input
  @FXML
  private Label invalidGoalAmount; // This label displays any error status with the goal amount input
  @FXML
  private Label invalidPayDate; // This label displays any error status with the pay date input
  @FXML
  private Label invalidPaycheck; // This label displays any error status with the pay check input
  @FXML
  private TextField payAmount; // Text field for the pay mount
  @FXML
  private TextField payday; // Text field for the pay day

  /**
   * Attempts to add a new profile, if the user inputs are valid.
   */
  public void addProfile() {
    // Get all user inputs.
    String nameInput = name.getText();
    String balanceInput = balance.getText();
    String paycheckAmountInput = payAmount.getText();
    String paydayInput = payday.getText();
    String billAmountInput = billAmount.getText();
    String billDateInput = billDate.getText();
    String goalInput = goal.getText();
    String goalAmountInput = goalAmount.getText();

    // Reset all error message fields.
    invalidName.setVisible(false);
    invalidBalance.setVisible(false);
    invalidPaycheck.setVisible(false);
    invalidPayDate.setVisible(false);
    invalidBillAmount.setVisible(false);
    invalidBillDate.setVisible(false);
    invalidGoal.setVisible(false);

    // Validate field by field, revealing a field's error message if it's invalid.
    boolean validInput = true;
    if (!Validator.min3ConsecutiveMax20(nameInput)) {
      invalidName.setVisible(true);
      invalidName.setText("Invalid Name. Only Letters (Min 3 chars)");
      validInput = false;
    }
    HashMap<String, Profile> savedProfiles = Loader.load(csv);
    boolean duplicateName = false;
    if (savedProfiles != null)
      for (var entry : savedProfiles.entrySet()) {
        if (entry.getValue().getName().equals(nameInput)) {
          invalidName.setVisible(true);
          invalidName.setText("A profile already exists with that name");
          validInput = false;
          duplicateName = true;
          break;
        }
      }
    // If no name conflict was found with the csv profiles, compare with the
    // profiles in the hashmap.
    if (!duplicateName && profiles != null)
      for (var entry : profiles.entrySet()) {
        if (entry.getValue().getName().toLowerCase().equals(nameInput.toLowerCase())) {
          invalidName.setVisible(true);
          invalidName.setText("A profile already exists with that name");
          validInput = false;
          duplicateName = true;
          break;
        }
      }

    double balance = 0d;
    if (!balanceInput.isEmpty())
      try {
        balance = Double.parseDouble(balanceInput);
      } catch (NumberFormatException exception) {
        invalidBalance.setVisible(true);
        validInput = false;
      }
    double paycheckAmount = 0d;
    try {
      paycheckAmount = Double.parseDouble(paycheckAmountInput);
    } catch (NumberFormatException exception) {
      invalidPaycheck.setVisible(true);
      validInput = false;
    }
    if (!Validator.validatePaymentDate(paydayInput)) {
      invalidPayDate.setVisible(true);
      validInput = false;
    }
    double billAmount = 0d;
    try {
      billAmount = Double.parseDouble(billAmountInput);
    } catch (NumberFormatException exception) {
      invalidBillAmount.setVisible(true);
      validInput = false;
    }
    if (!Validator.validatePaymentDate(billDateInput)) {
      invalidBillDate.setVisible(true);
      validInput = false;
    }
    if (!Validator.min3ConsecutiveMax20(goalInput, false)) {
      invalidGoal.setVisible(true);
      validInput = false;
    }
    double goalAmount = 0d;
    try {
      goalAmount = Double.parseDouble(goalAmountInput);
    } catch (NumberFormatException exception) {
      invalidGoalAmount.setVisible(true);
      validInput = false;
    }

    // Prevent further execution if a single input was invalid.
    if (!validInput)
      return;

    String creationDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

    Profile profile = new Profile(nameInput, creationDate, balance,
        paycheckAmount, paydayInput, billAmount,
        billDateInput, goalInput, goalAmount, new HashMap<>());
    if (savedProfiles != null)
      profiles = savedProfiles;
    profiles.put(profile.getId(), profile);
    currentProfile = profile;
    Alert alert = new Alert(AlertType.INFORMATION, "Successfully created " + nameInput + "'s profile!");
    alert.setHeaderText("Successfully created new profile");
    alert.show();

    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
      Parent root = loader.load();
      MainController controller = loader.getController();

      profiles.put(profile.getId(), profile);
      controller.init();

      Stage stage = Main.getPrimaryStage();
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      System.err.println("Caught exception");
      System.err.println(e);
    }
  }

  public void switchToLogIn() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("LogIn.fxml"));
    Parent root = loader.load();
    Stage stage = Main.getPrimaryStage();
    Scene scene = new Scene(root);
    LoginController controller = loader.getController();

    // User can use enter button on the keyboard instead of using mouse to click
    scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {

        if (Objects.requireNonNull(event.getCode()) == KeyCode.ENTER) {
          try {
            controller.logIn();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      }
    });

    stage.setScene(scene);
    stage.show();
  }
}
