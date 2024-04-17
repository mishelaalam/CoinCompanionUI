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
import java.util.ArrayList;
import java.util.Objects;
import java.util.HashMap;

import coinCompanion.objects.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import coinCompanion.util.Helper;
import coinCompanion.util.Loader;
import coinCompanion.util.Saver;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import static coinCompanion.app.Main.profiles;
import static coinCompanion.app.Main.currentProfile;

/**
 * This class controls all the events for the coinCompanion GUI
 */
public class MainController {
  // Backend properties.
  @FXML
  private Label goalNameLabel; // Goal Name
  @FXML
  private ProgressBar progressBar; // Progress bar for goal progress
  @FXML
  private Label goalProgressStatus; // Status of the current goal progress
  @FXML
  private Rectangle addIncomeRectangle; // Rectangle around add income button
  @FXML
  private Rectangle addExpenseRectangle; // Rectangle around add expense button
  @FXML
  private Rectangle incomeSavedRectangle; // Rectangle around income saved button
  @FXML
  private Rectangle spendingLimitRectangle; // Rectangle around spending limit button
  @FXML
  private Rectangle savingsRateRectangle; // Rectangle around savings rate button
  @FXML
  private Rectangle aboutRectangle; // Rectangle around about button
  @FXML
  private Rectangle quitRectangle; // Rectangle around quit button
  @FXML
  private TableColumn<CategoryExpense, Double> amount;
  @FXML
  private TableColumn<CategoryExpense, String> category;
  @FXML
  private TableColumn<CategoryExpense, String> date;
  @FXML
  private TableView<CategoryExpense> breakdownTable;
  ObservableList<CategoryExpense> breakdownList = FXCollections.observableArrayList();
  @FXML
  private Rectangle editProfileRectangle; // Rectangle around edit profile button
  @FXML
  private Rectangle addProfileRectangle; // Rectangle around add profile button
  @FXML
  private Rectangle loadDataRectangle; // Rectangle around load data button
  @FXML
  private Rectangle saveDataRectangle; // Rectangle around save data button
  @FXML
  private Rectangle logOutRectangle; // Rectangle around save data button
  @FXML
  private Label optionIdentifierLabel; // Displays name of top 3 button clicked
  @FXML
  private Label profileName; // Displays name of logged in profile
  @FXML
  private Label optionDisplayValue; // Displays value of top 3 button clicked
  @FXML
  private Label optionDisplayStatus; // Displays status of top 3 button clicked
  @FXML
  private Button balanceTopButton; // Top balance button
  @FXML
  private Button incomeTopButton; // Top income button
  @FXML
  private Button expenseTopButton; // Top expense button

  private double totalExpense; // Initializes total expense variable
  private double totalIncome; // Initializes total income variable

  /**
   * Loads all data from the default data.csv file, and initializes main page.
   */
  public void loadAndInit() {
    profiles = Loader.load("data.csv");

    if (profiles == null) {
      Helper.alert(AlertType.ERROR,
          "Could not load data, please try again later",
          "Error");
      return;
    }

    if (profiles.isEmpty()) {
      Helper.alert(AlertType.INFORMATION, "No profiles saved", "No Data Saved");
      return;
    }

    Helper.alert(AlertType.CONFIRMATION,
        String.format("Successfully loaded %d profile%s",
            profiles.size(), profiles.size() == 1 ? "" : "s"),
        "Data loaded");

    init();
  }

  /**
   * Initializes the main fxml page with the current profile's information.
   */
  public void init() {
    profileName.setText(currentProfile.getName());
    updateTable();
    displayGoalName();
    calculateGoalProgressBar();
    seeGoalProgressStatus();
    calculateCurrentBalance();
  }

  /**
   * Updates the spending breakdown table in the center.
   */
  public void updateTable() {
    breakdownTable.getItems().removeAll(breakdownList);

    for (Expense expense : currentProfile.getExpenses())
      breakdownList.add(new CategoryExpense(expense.getCategory(), expense.getDate(), expense.getAmount()));

    amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
    category.setCellValueFactory(new PropertyValueFactory<>("category"));
    date.setCellValueFactory(new PropertyValueFactory<>("date"));

    breakdownTable.setItems(breakdownList);
    Label customPlaceholder = new Label("Zero transactions? You must be living under a rock!");
    breakdownTable.setPlaceholder(customPlaceholder);
  }

  /**
   * Saves all current data to the default data.csv file.
   */
  public void save() {
    if (profiles == null || profiles.size() == 0) {
      Helper.alert(AlertType.INFORMATION, "No data to save", "No Data to Save");
      return;
    }

    boolean saveSuccessful = Saver.save(profiles);
    if (saveSuccessful) {
      Helper.alert(AlertType.CONFIRMATION,
          String.format("Successfully saved %d profile%s", profiles.size(), profiles.size() == 1 ? "" : "s"),
          "Data saved");
      return;
    }

    Helper.alert(AlertType.ERROR, "Oops - something went wrong, please try again later", "Error");
  }

  /**
   * Switches the currently logged in profile.
   *
   * @param profileId The id of the profile to switch to.
   *                  PRECONDITION: Assumes profileId is a valid id associated
   *                  with a valid profile.
   */
  public void switchProfile(String profileId) {
    currentProfile = profiles.get(profileId);
  }

  /**
   * Gets the savings rate for the currently logged in profile.
   */
  public void getSavingsRate() {
    HashMap<String, Double> balanceChanges = currentProfile.computeCurrentAndPastAverageBalanceChange();

    double pastAverage = balanceChanges.get("average");
    double current = balanceChanges.get("current");

    StringBuilder output = new StringBuilder();
    output.append(
        String.format("Your past %s average: %.2f\nYour %s this month: %.2f\n",
            pastAverage > 0 ? "savings" : "expenses",
            pastAverage, current > 0 ? "savings" : "losses", current));

    if (current == 0) {
      output.append("Since there is no data for this month, we cannot compute a rate.");
      Helper.alert(AlertType.INFORMATION, output.toString(), "No Data to Compare");
      return;
    }

    double rate;
    if (pastAverage > 0) {
      // You usually save...
      if (current > pastAverage) {
        // ...but you saved even more than usual.
        rate = Math.abs((current / pastAverage) - 1) * 100;
        output.append(String.format("Great job! Your savings this month are up %.2f%%. Keep it up! ^_~", rate));
      } else if (current == pastAverage) {
        // ...and this month you saved just as much.
        output.append("Nice! You kept your savings this month just as high as usual! ^_^");
      } else if (current >= 0) {
        // ...but you saved less this month.
        rate = Math.abs((current / pastAverage) - 1) * 100;
        output.append(String.format("Oof! Your savings this month are down %.2f%%. >_<", rate));
      } else {
        // ...but you had losses this month.
        rate = Math.abs(current / pastAverage) * 100;
        output.append(String.format(
            "Careful!\nWhile on average you save money, this month's losses were %.2f%% that of your average monthly savings! ╰（‵□′）╯",
            rate));
      }
    } else if (pastAverage < 0) {
      // You usually have losses...
      if (current < pastAverage) {
        // ...but you had even greater losses than usual.
        rate = Math.abs((current / pastAverage) - 1) * 100;
        output.append(String.format(
            "Wha..\nNot only do you incur losses on average, this month they were %.2f%% greater than usual... ಠ_ಠ",
            rate));
      } else if (current == pastAverage) {
        // ...and this month you lost just as much.
        output.append("Your losses are just as high as usual! (≧﹏ ≦)");
      } else if (current <= 0) {
        // ...but you had smaller losses than usual.
        rate = Math.abs((current / pastAverage) - 1);
        output.append(
            String.format("Nice! You brought your expenses down by %.2f%% from the usual - keep it up! (¬‿¬)", rate));
      } else {
        // ...but you actually saved this month.
        rate = Math.abs(current / pastAverage);
        output.append(String.format(
            "High five!\nThis month, you saved %.2f%% as much as you typically splurge. (⌐■_■)", rate));
      }
    } else
      // Your balance does not change on average.
      output.append(current > 0
          ? "Nice savings this month! However, since your balance usually doesn't change, we cannot compute a rate. Keep it up!"
          : "Oof, some losses this month. Since your balance usually doesn't change, we cannot compute a rate.");

    Helper.alert(AlertType.INFORMATION, output.toString(), "Savings Rate");
  }

  /**
   * Prompts the user if they wish to save changes before closing the program.
   * Cancellable.
   */
  public void quit() {
    // Either the user cancelled the quit operation, or they wish to proceed and:
    // they want to save what's stored, or not.
    ButtonType choice = Helper.getConfirmation("Quit Confirmation", "Delete Unsaved Data?",
        "Any unsaved data will be lost. Continue?");

    if (choice.equals(ButtonType.CANCEL))
      return;

    Platform.exit();
  }

  /**
   * Displays goal name on the label
   */
  public void displayGoalName() {
    if (currentProfile != null) {
      String goalName = currentProfile.getGoalName();
      goalNameLabel.setText(goalName);
    } else {
      goalNameLabel.setText("No goal set");
    }
  }

  /**
   * Displays the goal progress on progress bar
   */
  public void calculateGoalProgressBar() {
    if (currentProfile != null) {
      double balance = currentProfile.getBalance();
      double goalAmount = currentProfile.getGoalAmount();
      if (goalAmount != 0) {
        double goalProgressBar = (balance / goalAmount);
        if (goalProgressBar < 0) {
          goalProgressBar = 100;
        }
        progressBar.setProgress(goalProgressBar);
      } else {
        progressBar.setProgress(0); // Set progress to 0 if goal amount is 0 to avoid division by zero
      }
    }
  }

  /**
   * Displays the status of the current goal progress
   */
  public void seeGoalProgressStatus() {
    if (currentProfile != null) {
      double balance = currentProfile.getBalance();
      double goalAmount = currentProfile.getGoalAmount();
      double percentageComplete = 0;
      if (goalAmount > balance) {
        percentageComplete = (balance / goalAmount) * 100;
      } else {
        // If balance is greater than or equal to goalAmount, calculate the percentage
        // over the goal
        percentageComplete = (balance / goalAmount) * 100;
        if (percentageComplete > 100) {
          // Limit the percentage to 200% for the progress bar
          percentageComplete = 100;
        }
      }
      if (percentageComplete >= 100) {
        goalProgressStatus
            .setText(String.format("Congratulations! You have completed %.2f%% of your goal!", percentageComplete));
      } else {
        goalProgressStatus.setText(String.format("You have completed %.2f%% towards your goal.", percentageComplete));
      }
    }
  }

  /**
   * Switches to the page for editing profile properties.
   */
  public void switchToEditProfile() throws IOException {
    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("EditProfile.fxml")));
    Stage stage = Main.getPrimaryStage();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  /**
   * Displays the name of the option button clicked
   *
   * @param event - either of the top 3 buttons clicked
   */
  public void topThreeButtonsMouseClicked(ActionEvent event) {
    if (event.getSource() == incomeTopButton) {
      optionIdentifierLabel.setText("Income");
      calculateCurrentMonthIncome();
      calculatePreviousMonthIncome();

    } else if (event.getSource() == expenseTopButton) {
      optionIdentifierLabel.setText("Expense");
      calculateCurrentMonthExpense();
      calculatePreviousMonthExpense();

    } else if (event.getSource() == balanceTopButton) {
      optionIdentifierLabel.setText("Balance");
      calculateCurrentBalance();
      calculatePreviousMonthBalance();
      displayGoalName();
      calculateGoalProgressBar();
      seeGoalProgressStatus();
    }
  }

  /**
   * Calculates current profile's current month's Income
   */
  public void calculateCurrentMonthIncome() {
    totalIncome = 0.0;
    ArrayList<Income> currentProfileIncomes = currentProfile.getIncomes();
    if (currentProfileIncomes == null || currentProfileIncomes.isEmpty()) {
      optionDisplayValue.setText("$0.00");
      return;
    }

    // Grabs the current month
    LocalDate date = LocalDate.now();
    int currentMonth = date.getMonthValue();
    ArrayList<Income> incomes = currentProfile.getIncomes();

    // Calculates total income for the current month
    for (Income income : incomes) {
      String dateString = income.getDate();
      LocalDate incomeDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
      if (incomeDate.getMonthValue() == currentMonth) {
        totalIncome += income.getAmount();
        optionDisplayValue.setText("$" + String.valueOf(totalIncome));
      }
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

  /**
   * Calculate previous month income and compare to current month income, display
   * status accordingly
   */
  public void calculatePreviousMonthIncome() {

    double totalPreviousIncome = 0.0;

    // Grabs the current month and year
    LocalDate currentDate = LocalDate.now();
    int currentMonth = currentDate.getMonthValue();
    int currentYear = currentDate.getYear();

    // Calculates previous month
    int previousMonth = currentMonth - 1;
    int previousYear = currentYear;
    if (previousMonth == 0) {
      previousMonth = 12;
      previousYear--;
    }

    // Get incomes for the profile
    ArrayList<Income> incomes = currentProfile.getIncomes();

    // Calculate total income for the previous month
    for (Income income : incomes) {
      String dateString = income.getDate();
      LocalDate incomeDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
      int incomeMonth = incomeDate.getMonthValue();
      int incomeYear = incomeDate.getYear();

      // Check if income date matches the previous month
      if (incomeMonth == previousMonth && incomeYear == previousYear) {
        totalPreviousIncome += income.getAmount();
      }
    }
    // Calculate income difference and display status
    double incomeDifference = totalIncome - totalPreviousIncome;
    // double incomeDifferencePercent = incomeDifference / totalPreviousIncome *
    // 100;

    if (totalPreviousIncome == 0) {
      optionDisplayStatus.setText("No income recorded for the previous month.");
    } else if (incomeDifference > 0) {
      optionDisplayStatus.setText(String.format("Great job! Your income is up by $%.2f.", incomeDifference));
    } else if (incomeDifference == 0) {
      optionDisplayStatus.setText("There has been no change in your income.");
    } else if (incomeDifference < 0) {
      optionDisplayStatus.setText(String.format("Your income has gone down by $%.2f.", -incomeDifference));
    }
  }

  /**
   * Calculates current total expense
   */
  public void calculateCurrentMonthExpense() {
    totalExpense = 0.0;
    ArrayList<Expense> currentProfileExpenses = currentProfile.getExpenses();
    if (currentProfileExpenses == null || currentProfileExpenses.isEmpty()) {
      optionDisplayValue.setText("$0.00");
      return;
    }

    // Grabs the current month
    LocalDate date = LocalDate.now();
    int currentMonth = date.getMonthValue();
    ArrayList<Expense> expenses = currentProfile.getExpenses();

    // Calculates total income for the current month
    for (Expense expense : expenses) {
      String dateString = expense.getDate();
      LocalDate incomeDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
      if (incomeDate.getMonthValue() == currentMonth) {
        totalExpense += expense.getAmount();
        optionDisplayValue.setText("$" + String.valueOf(totalExpense));
      }
    }
  }

  /**
   * Calculate previous month expense and compare to current month expense,
   * display status accordingly
   */
  public void calculatePreviousMonthExpense() {
    // Initialize totalPreviousExpense
    double totalPreviousExpense = 0.0;

    // Grabs the current month and year
    LocalDate currentDate = LocalDate.now();
    int currentMonth = currentDate.getMonthValue();
    int currentYear = currentDate.getYear();

    // Calculates previous month
    int previousMonth = currentMonth - 1;
    int previousYear = currentYear;
    if (previousMonth == 0) {
      previousMonth = 12;
      previousYear--;
    }
    // Get expenses for the profile
    ArrayList<Expense> expenses = currentProfile.getExpenses();

    // Calculate total expense for the previous month
    for (Expense expense : expenses) {
      String dateString = expense.getDate();
      LocalDate expenseDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
      int expenseMonth = expenseDate.getMonthValue();
      int expenseYear = expenseDate.getYear();

      // Check if expense date matches the previous month
      if (expenseMonth == previousMonth && expenseYear == previousYear) {
        totalPreviousExpense += expense.getAmount();
      }
    }
    // Calculate expense difference and display status
    double expenseDifference = totalExpense - totalPreviousExpense;
    // double expenseDifferencePercent = expenseDifference / totalPreviousExpense *
    // 100;

    if (totalPreviousExpense == 0) {
      optionDisplayStatus.setText("No expenses recorded for the previous month.");
    } else if (expenseDifference > 0) {
      optionDisplayStatus.setText(String.format("Your expenses has gone up by $%.2f.", expenseDifference));
    } else if (expenseDifference == 0) {
      optionDisplayStatus.setText("There has been no change in your expenses.");
    } else if (expenseDifference < 0) {
      optionDisplayStatus.setText(String.format("Your expenses has gone down by $%.2f.", -expenseDifference));
    }
  }

  /**
   * Calculate current month total balance and display it
   */
  public void calculateCurrentBalance() {
    double balance = currentProfile.getBalance();
    optionDisplayValue.setText("$" + String.valueOf(balance));
  }

  /**
   * Calculate previous month balance and display it
   */
  public void calculatePreviousMonthBalance() {
    // Initialize totalPreviousIncome and totalPreviousExpense
    double totalPreviousIncome = 0.0;
    double totalPreviousExpense = 0.0;

    // Grabs the current month and year
    LocalDate currentDate = LocalDate.now();
    int currentMonth = currentDate.getMonthValue();
    int currentYear = currentDate.getYear();

    // Calculates previous month
    int previousMonth = currentMonth - 1;
    int previousYear = currentYear;
    if (previousMonth == 0) {
      previousMonth = 12;
      previousYear--;
    }
    // Get incomes and expenses for the profile
    ArrayList<Income> incomes = currentProfile.getIncomes();
    ArrayList<Expense> expenses = currentProfile.getExpenses();

    // Calculate total income for the previous month
    for (Income income : incomes) {
      String dateString = income.getDate();
      LocalDate incomeDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
      int incomeMonth = incomeDate.getMonthValue();
      int incomeYear = incomeDate.getYear();

      // Check if income date matches the previous month
      if (incomeMonth == previousMonth && incomeYear == previousYear) {
        totalPreviousIncome += income.getAmount();
      }
    }
    // Calculate total expense for the previous month
    for (Expense expense : expenses) {
      String dateString = expense.getDate();
      LocalDate expenseDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
      int expenseMonth = expenseDate.getMonthValue();
      int expenseYear = expenseDate.getYear();

      // Check if expense date matches the previous month
      if (expenseMonth == previousMonth && expenseYear == previousYear) {
        totalPreviousExpense += expense.getAmount();
      }
    }
    // Calculate balance difference and display status
    double previousMonthBalance = totalPreviousIncome - totalPreviousExpense;

    if (totalPreviousIncome == 0 && totalPreviousExpense == 0) {
      optionDisplayStatus.setText("No balance recorded for the previous month.");
    } else if (previousMonthBalance > 0) {
      optionDisplayStatus.setText(String.format("Great job! Your balance is up by $%.2f.", previousMonthBalance));
    } else if (previousMonthBalance == 0) {
      optionDisplayStatus.setText("There has been no change in your balance.");
    } else if (previousMonthBalance < 0) {
      optionDisplayStatus.setText(String.format("Your balance has gone down by $%.2f.", -previousMonthBalance));
    }
  }

  /**
   * Triggers an alert for income Saved, displays the alert title and status
   * accordingly
   */
  @FXML
  public void incomeSavedButtonClicked() {
    if (currentProfile != null) {
      double incomeSavedAmount = currentProfile.calculatePercentageOfMonthlyIncomeSaved();
      String incomeSavedAmountString = String.format("%.2f", incomeSavedAmount);
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Percentage of Monthly Income Saved");

      // Set the size of the dialog
      DialogPane dialogPane = alert.getDialogPane();
      dialogPane.setPrefSize(350, 180); // Width, Height

      if (incomeSavedAmount > 0) {
        alert.setHeaderText("Income Saved: " + incomeSavedAmountString + "%");
      } else {
        alert.setHeaderText("Income Saved: 0%");
      }
      alert.setContentText(getSavingsMessage(incomeSavedAmount));
      alert.showAndWait();
    } else {
      System.out.println("Error: currentProfile is null.");
    }
  }

  /**
   * Displays different statuses for different income
   *
   * @param incomeSavedAmount - calculated income amount
   * @return - returns different context statuses
   */
  private String getSavingsMessage(double incomeSavedAmount) {
    if (incomeSavedAmount >= 100) {
      return "Wow! You have saved 100% of your income this month! You are a true master at saving!";
    } else if (incomeSavedAmount >= 80) {
      return String.format("Impressive! You have saved %.2f%% of your income this month. Keep filling that piggy bank!",
          incomeSavedAmount);
    } else if (incomeSavedAmount >= 50) {
      return String.format("Good job! You have saved %.2f%% of your income this month. Keep stashing that $$$!",
          incomeSavedAmount);
    } else if (incomeSavedAmount >= 20) {
      return String.format("You have saved %.2f%% of your income this month. Every little bit helps! Keep on saving!",
          incomeSavedAmount);
    } else if (incomeSavedAmount > 0) {
      return String.format(
          "You have saved %.2f%% of your income this month. Consider saving more next month! You can do it!",
          incomeSavedAmount);
    } else {
      return "Your savings this month? Oh dear, that's a zero. Time to re-evaluate your spending!";
    }
  }

  public void switchToSignUpForAddProfile() {
    Alert alert = new Alert(AlertType.CONFIRMATION, "You will be logged out of your current profile. Continue?");
    alert.setHeaderText("This Action will Log You Out!");
    // Show the alert and wait for the user's response
    alert.showAndWait().ifPresent(response -> {
      if (response == ButtonType.OK) {
        try {
          // Proceed to switch to SignUp.fxml only if the user clicked OK
          Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("SignUp.fxml")));
          Stage stage = Main.getPrimaryStage();
          Scene scene = new Scene(root);
          stage.setScene(scene);
          stage.show();
        } catch (IOException e) {
          System.err.print(e); // Handle the exception appropriately
        }
      }
    });
  }

  public void switchToAddExpensePopup() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("AddExpense.fxml"));
    Parent root = loader.load();

    // Create a new Stage for the AddExpense.fxml content
    Stage addExpenseStage = new Stage();
    addExpenseStage.setScene(new Scene(root));
    addExpenseStage.setResizable(false);
    addExpenseStage.setTitle("Add Expense"); // Set a title for the pop-up window
    Stage mainStage = Main.getPrimaryStage();
    addExpenseStage.initOwner(mainStage); // Set main window as owner
    addExpenseStage.initModality(Modality.WINDOW_MODAL); // Block main window events

    // Show the Stage and wait for it to be closed
    addExpenseStage.showAndWait();
    String currentView = optionIdentifierLabel.getText();
    if (currentView.equals("Balance")) {
      calculateCurrentBalance();
    }
    if (currentView.equals("Expense")) {
      calculateCurrentMonthExpense();
    }
    calculateGoalProgressBar();
    seeGoalProgressStatus();
    updateTable();
  }

  public void switchToAddIncomePopup() throws IOException {
    Stage addIncomeStage = new Stage();
    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AddIncome.fxml")));
    addIncomeStage.setScene(new Scene(root));
    addIncomeStage.setResizable(false);
    addIncomeStage.setTitle("Add Income"); // Set a title for the pop-up window
    Stage mainStage = Main.getPrimaryStage();
    addIncomeStage.initOwner(mainStage); // Set main window as owner
    addIncomeStage.initModality(Modality.WINDOW_MODAL); // Block main window events
    addIncomeStage.showAndWait(); // Show and wait for it to be closed

    String currentView = optionIdentifierLabel.getText();
    if (currentView.equals("Balance")) {
      calculateCurrentBalance();
    }
    if (currentView.equals("Income")) {
      calculateCurrentMonthIncome();
    }
    calculateGoalProgressBar();
    seeGoalProgressStatus();
  }

  public void aboutPopup() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION,
        "Author: Mishela Alam\nEmail: mishela.alam@ucalgary.ca\n\nAuthor: Badhan Chandra Saha\nEmail: badhan.saha@ucalgary.ca\n\nAuthor: Carlos Jonathan Bernal Torres\nEmail: carlos.bernaltorres@ucalgary.ca\n\nCoinCompanion is your Personal Finance Tracker!\n\nIt helps you reach your desired goal by tracking your monthly savings, income, and expenses!\n\nThank you for using this application!\nCheers!");
    alert.setTitle("About");
    alert.setHeaderText("CoinCompanion");
    alert.show();
  }

  public void switchToSpendingLimitPopup() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("SpendingLimits.fxml"));
    Parent root = loader.load();

    // Create a new Stage for the AddExpense.fxml content
    Stage spendingLimitStage = new Stage();
    spendingLimitStage.setScene(new Scene(root));
    spendingLimitStage.setResizable(false);
    spendingLimitStage.setTitle("Spending Limits"); // Set a title for the pop-up window
    Stage mainStage = Main.getPrimaryStage();
    spendingLimitStage.initOwner(mainStage); // Set main window as owner
    spendingLimitStage.initModality(Modality.WINDOW_MODAL); // Block main window events

    // Show the Stage and wait for it to be closed
    spendingLimitStage.showAndWait();
  }

  //////////////////// Methods for Nav Bar Interaction on Hover
  //////////////////// ////////////////////

  public void addIncomeHoverEntered() {
    addIncomeRectangle.setStroke(Color.BLACK);
  }

  public void addIncomeHoverExited() {
    addIncomeRectangle.setStroke(Color.TRANSPARENT);
  }

  public void addExpenseHoverEntered() {
    addExpenseRectangle.setStroke(Color.BLACK);
  }

  public void addExpenseHoverExited() {
    addExpenseRectangle.setStroke(Color.TRANSPARENT);
  }

  public void incomeSavedHoverEntered() {
    incomeSavedRectangle.setStroke(Color.BLACK);
  }

  public void incomeSavedHoverExited() {
    incomeSavedRectangle.setStroke(Color.TRANSPARENT);
  }

  public void spendingLimitsHoverEntered() {
    spendingLimitRectangle.setStroke(Color.BLACK);
  }

  public void spendingLimitsHoverExited() {
    spendingLimitRectangle.setStroke(Color.TRANSPARENT);
  }

  public void savingsRateHoverEntered() {
    savingsRateRectangle.setStroke(Color.BLACK);
  }

  public void savingsRateHoverExited() {
    savingsRateRectangle.setStroke(Color.TRANSPARENT);
  }

  public void aboutHoverEntered() {
    aboutRectangle.setStroke(Color.BLACK);
  }

  public void aboutHoverExited() {
    aboutRectangle.setStroke(Color.TRANSPARENT);
  }

  public void quitHoverEntered() {
    quitRectangle.setStroke(Color.BLACK);
  }

  public void quitHoverExited() {
    quitRectangle.setStroke(Color.TRANSPARENT);
  }

  public void editProfileHoverEntered() {
    editProfileRectangle.setStroke(Color.BLACK);
  }

  public void editProfileHoverExited() {
    editProfileRectangle.setStroke(Color.TRANSPARENT);
  }

  public void addProfileHoverEntered() {
    addProfileRectangle.setStroke(Color.BLACK);
  }

  public void addProfileHoverExited() {
    addProfileRectangle.setStroke(Color.TRANSPARENT);
  }

  public void loadDataHoverEntered() {
    loadDataRectangle.setStroke(Color.BLACK);
  }

  public void loadDataHoverExited() {
    loadDataRectangle.setStroke(Color.TRANSPARENT);
  }

  public void saveDataHoverEntered() {
    saveDataRectangle.setStroke(Color.BLACK);
  }

  public void saveDataHoverExited() {
    saveDataRectangle.setStroke(Color.TRANSPARENT);
  }

  public void logOutHoverEntered() {
    logOutRectangle.setStroke(Color.web("#0e0f37"));
  }

  public void logOutHoverExited() {
    logOutRectangle.setStroke(Color.TRANSPARENT);
  }

  public void setProfiles(HashMap<String, Profile> profilesToSet) {
    profiles = profilesToSet;
  }

}