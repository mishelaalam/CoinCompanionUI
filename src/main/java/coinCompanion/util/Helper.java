/**
 * Date - April 06, 2024
 * T01 - TA Javad Sahebnasi
 *
 * @author Carlos Jonathan Bernal Torres
 * @author Mishela Alam
 * @author Badhan Chandra Saha
 */
package coinCompanion.util;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert.AlertType;

/**
 * This is the helper class that contains different kind of alerts
 */
public class Helper {
    /**
     * Helper function to help show alerts to users.
     *
     * @param type -  alert type
     * @param message -  context message
     * @param header -  header message
     */
    public static void alert(AlertType type, String message, String header) {
        Alert alert = new Alert(type, message);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setPrefSize(350, 200);
        alert.setHeaderText(header);
        alert.show();
    }

    /**
     * Helper function to show a customized confirmation, and get the user's input
     * (yes/no).
     *
     * @param title - title of alert
     * @param header - header text
     * @param content -  content text
     * @return The user's input.
     */
    public static ButtonType getConfirmation(String title, String header, String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        Optional<ButtonType> buttonType = alert.showAndWait();
        return buttonType.get();
    }

    /**
     * Parses both Canadian and French decimal formats.
     *
     * @param input - input
     * @return The parsed double.
     */
    public static Double parseDouble(String input) {
        return Double.parseDouble(input.replaceAll(",", ""));
    }
}
