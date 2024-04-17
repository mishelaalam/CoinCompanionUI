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
import java.util.Objects;
import java.util.HashMap;
import coinCompanion.objects.Profile;
import coinCompanion.util.Helper;
import coinCompanion.util.Loader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import static coinCompanion.app.Main.profiles;
import static coinCompanion.app.Main.currentProfile;

/**
 * This class controls all the events on the Login.fxml
 */
public class LoginController {
  @FXML
  private Label loginError; // This label displays any error status with the login input
  @FXML
  private TextField profileName; // Text field that takes in profile name input

  /**
   * Attempts to log in the user using a profile name.
   */
  public void logIn() throws IOException {
    profiles = load(false);
    loginError.setVisible(false);

    if (profiles == null || profiles.size() == 0) {
      loginError.setText("Unable to login - no profiles saved.");
      loginError.setVisible(true);
    }

    String nameInput = profileName.getText();
    if (nameInput.trim().equals("")) {
      loginError.setText("Please enter a user profile to log in");
      loginError.setVisible(true);
      return;
    }

    for (Profile profile : profiles.values()) {
      if (profile.getName().toLowerCase().equals(nameInput.toLowerCase())) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root = loader.load();
        MainController controller = loader.getController();

        currentProfile = profile;
        controller.setProfiles(profiles);
        controller.init();

        Stage stage = Main.getPrimaryStage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
      }
    }

    loginError.setText(String.format("Could not find a profile under \"%s\"", nameInput));
    loginError.setVisible(true);
  }

  /**
   * Loads the csv data and shows alerts if directed to.
   */
  public HashMap<String, Profile> load(boolean showAlerts) {
    if (profiles == null || profiles.isEmpty())
      profiles = Loader.load("data.csv");

    if (profiles == null) {
      if (showAlerts)
        Helper.alert(AlertType.ERROR,
            "Could not load data, please try again later",
            "Error");
      return profiles;
    }

    if (profiles.isEmpty()) {
      if (showAlerts)
        Helper.alert(AlertType.INFORMATION, "No profiles saved", "No Data Saved");
      return profiles;
    }

    if (showAlerts)
      Helper.alert(AlertType.CONFIRMATION,
          String.format("Successfully loaded %d profile%s",
              profiles.size(), profiles.size() == 1 ? "" : "s"),
          "Data loaded");
    return profiles;
  }

  public void setProfiles(HashMap<String, Profile> profilesToSet) {
    profiles = profilesToSet;
  }

  public void switchToSignUp() throws IOException {
    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("SignUp.fxml")));
    Stage stage = Main.getPrimaryStage();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }
}
