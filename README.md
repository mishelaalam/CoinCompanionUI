# CoinCompanion 💰

A personal finance tracker built with Java and JavaFX that helps users 
track savings, income, and expenses to reach their financial goals.

## Features
- Track income, expenses, and savings across multiple user profiles
- Login system supporting simultaneous multi-profile management
- Visualize finances over time to monitor progress toward goals
- CSV-based data persistence for easy portability

## Tech Stack
Java 21, JavaFX 21.0.2, CSV data storage

## Requirements
- Java 21 JDK
- JavaFX 21.0.2 SDK

## How to Run

**Through an IDE:**
Compile and run `src/main/java/coinCompanion/app/Main`

**Through Terminal:**
```bash
java --module-path "/path/to/javafx-sdk-21.0.2/lib" \
  --add-modules javafx.controls,javafx.fxml \
  coinCompanion.app.Main data.csv
```

**Using the JAR:**
```bash
java --module-path "/path/to/javafx-sdk-21.0.2/lib" \
  --add-modules javafx.controls,javafx.fxml \
  -jar CoinCompanion.jar data.csv
```

## Authors
Mishela Alam, Badhan Chandra Saha, Carlos Jonathan Bernal Torres
