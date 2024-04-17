/**
 * Date - April 05, 2024
 * T01 - TA Javad Sahebnasi
 *
 * @author Carlos Jonathan Bernal Torres
 * @author Mishela Alam
 * @author Badhan Chandra Saha
 */
package coinCompanion.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import java.util.HashMap;
import java.util.Objects;
import coinCompanion.objects.Profile;
import coinCompanion.util.Helper;
import coinCompanion.util.Loader;

/**
 * This class launches the Login GUI and loads the .csv file
 */
public class Main extends Application {
  public static final String csv = System.getProperty("user.dir") + "\\data.csv";
  public static HashMap<String, Profile> profiles = new HashMap<>(); // hash map of all profiles
  public static Profile currentProfile; // hash map of all profiles
  private static Stage primaryStage; // primary stage

  @Override
  public void start(Stage stage) throws IOException {
    primaryStage = stage;

    // loads the Login.fmxl
    FXMLLoader loader = new FXMLLoader(getClass().getResource("LogIn.fxml"));
    Parent root = loader.load();
    LoginController controller = loader.getController();

    // sets the size of the GUI
    Scene scene = new Scene(root, 869, 551);

    controller.setProfiles(profiles);

    // Does not allow window resizing
    stage.setResizable(false);
    stage.setTitle("CoinCompanion");
    stage.initStyle(StageStyle.DECORATED);

    // User can hit enter on keyboard instead of clicking the sign in button
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

    stage.setOnCloseRequest(event -> {
      event.consume();
      ButtonType choice = Helper.getConfirmation("Quit Confirmation", "Delete Unsaved Data?",
          "Any unsaved data will be lost. Continue?");

      if (choice.equals(ButtonType.OK))
        stage.close();
    });
  }

  public static Stage getPrimaryStage() {
    return primaryStage;
  }

  public static void main(String[] args) {
    if (args.length > 0) {
      String fileName = args[0];
      profiles = Loader.load(fileName);
    }
    launch();
  }
}