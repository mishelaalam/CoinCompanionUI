# CoinCompanion

## About
Authors: Mishela Alam, Badhan Chandra Saha, Carlos Jonathan Bernal Torres

Emails: mishela.alam@ucalgary.ca , badhan.saha@ucalgary.ca , carlos.bernaltorres@ucalgary.ca

`
Version: 3.0
`

## Project Description
Coin Companion is a personal finance tracker, that tracks a profile's savings, income, and expenses, in order to reach a profile's desired goal. It also has the ability to track the finances of multiple profiles simultaneously, thanks to our login page feature. It even has the ability to track finances over a period of time.

## Requirements to Run CoinCompanion
 - Java 21 JDK 
 - JavaFX 21.0.2 SDK

## To Run CoinCompanion through IDE
- Compile src\main\java\coinCompanion\app\Main to launch CoinCompanion.

## To Run CoinCompanion through Terminal
### Verify Java Installation:
- Open a command prompt (Windows) or terminal (Mac/Linux).
- Type java -version and press Enter.
- If Java is installed, you'll see the Java version information. If not, proceed to download and install Java.

### Launch CoinCompanion
- Open a command prompt or terminal window in the project folder
- Type the following command:
```
java --module-path "C:\Program Files\Java\javafx-sdk-21.0.2\lib" --add-modules javafx.controls,javafx.fxml coinCompanion.app.Main [location to csv]
```
- Replace `"C:\Program Files\Java\javafx-sdk-21.0.2\lib"` with the location for your JavaFX SDK.

## To Run CoinCompanion using CoinCompanion.jar
### Verify Java Installation:
- Open a command prompt (Windows) or terminal (Mac/Linux).
- Type java -version and press Enter.
- If Java is installed, you'll see the Java version information. If not, proceed to download and install Java.

### Launch CoinCompanion.jar
- Open a command prompt or terminal window in the project folder
- Type the following command:
```
java --module-path "C:\Program Files\Java\javafx-sdk-21.0.2\lib" --add-modules javafx.controls,javafx.fxml -jar CoinCompanion.jar data.csv
```
- Replace `"C:\Program Files\Java\javafx-sdk-21.0.2\lib"` with the location for your JavaFX SDK.