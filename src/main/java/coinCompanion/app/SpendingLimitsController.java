/**
 * Date - April 08, 2024
 * T01 - TA Javad Sahebnasi
 *
 * @author Carlos Jonathan Bernal Torres
 * @author Mishela Alam
 * @author Badhan Chandra Saha
 */
package coinCompanion.app;

import coinCompanion.objects.SpendingLimit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import static coinCompanion.app.Main.currentProfile;
import coinCompanion.enums.SpendingCategories;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class controls the events on the SpendingLimits.fxml
 */

public class SpendingLimitsController implements Initializable {
  @FXML
  private TableColumn<SpendingLimit, String> spendingLimitCategory; // Table column for spending limit category
  @FXML
  private TableColumn<SpendingLimit, Double> spendingLimitLimit; // Table column for spending limit amount
  @FXML
  private TableColumn<SpendingLimit, Double> spendingExceededBy; // Table column showing how much exceeded amount
  @FXML
  private TableView<SpendingLimit> spendingTable; // Table feature
  ObservableList<SpendingLimit> spendingList = FXCollections.observableArrayList(); // Arraylist

  /**
   * Adds and refreshes the table with the latest values
   */
  public void refreshSpendingLimitTable() {
    spendingTable.getItems().removeAll(spendingList);

    for (SpendingCategories category : currentProfile.getSpendingLimits().keySet()) {
      spendingList.add(new SpendingLimit(category, currentProfile.getSpendingLimits().get(category),
          currentProfile.getExceededBy(category)));
    }

    spendingLimitCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
    spendingLimitLimit.setCellValueFactory(new PropertyValueFactory<>("limit"));
    spendingExceededBy.setCellValueFactory(new PropertyValueFactory<>("exceededBy"));

    spendingTable.setItems(spendingList);
  }

  public void switchToAddSpendingLimitPopup(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("AddSpendingLimit.fxml"));
    Parent root = loader.load();

    // Create a new Stage for the AddExpense.fxml content
    Stage spendingLimitStage = new Stage();
    spendingLimitStage.setScene(new Scene(root));
    spendingLimitStage.setResizable(false);
    spendingLimitStage.setTitle("Add Spending Limit");
    Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    spendingLimitStage.initOwner(mainStage); // Set main window as owner
    spendingLimitStage.initModality(Modality.WINDOW_MODAL); // Block main window events

    // Show the Stage and wait for it to be closed
    spendingLimitStage.showAndWait();
    refreshSpendingLimitTable();
  }

  /**
   * Refreshes the table generator
   * 
   * @param location  - location
   * @param resources - resources
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    refreshSpendingLimitTable();
    Label customPlaceholder = new Label("No Spending Limits Set :(");
    spendingTable.setPlaceholder(customPlaceholder);
  }
}
