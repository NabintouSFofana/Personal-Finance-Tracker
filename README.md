# Personal Finance Tracker

A modern Java desktop application for tracking income, expenses, balance, and savings goals.

Built with:
- Java
- Swing
- FlatLaf
- Maven
- Object-Oriented Programming

---

## Features

Modern desktop UI using FlatLaf  
Live financial dashboard  
Track income and expenses  
Monthly savings goal with progress tracking  
Unusual expense warning system
Transaction history table  
Console version included for testing

---

## Screenshots

<img width="1360" height="861" alt="image" src="https://github.com/user-attachments/assets/3587e791-4148-4fc4-a954-fbd1a571d1ca" />

---

## Project Structure

```plaintext
finance-tracker/
│
├── pom.xml
├── src/
│   ├── Account.java
│   ├── Transaction.java
│   ├── FinanceTrackerGUI.java
│   └── Main.java
│
├── README.md
└── out/
```

---

## Installation

### Clone

```bash
git clone <your-repository-url>
cd finance-tracker
```

---

## Install dependencies

This project uses Maven.

Dependencies install automatically:

```bash
mvn clean install
```

---

## Run GUI

```bash
mvn exec:java
```

OR run:

```plaintext
FinanceTrackerGUI.java
```

inside IntelliJ.

---

## Run Console Version

```bash
java Main
```

---

## Technologies

- Java 11+
- Swing
- FlatLaf 3.6
- Maven
- OOP

---

## Architecture

### Account
Handles:
- transactions
- balance
- income
- expenses
- savings progress

### Transaction
Immutable transaction model.

### FinanceTrackerGUI
Swing interface and dashboard.

### Main
Console version.

---

## Future Improvements

- Save transactions locally
- CSV export
- Edit/Delete transactions
- Charts & analytics
- Dark mode
- Filters

---

## What I Learned

This project helped me practice:

- Java OOP
- Swing UI development
- Maven dependency management
- Separation of concerns
- Desktop application architecture

---

## Author

Nabintou S. Fofana

Software Engineering Student

Portfolio:
https://nabintousfofana.github.io/portfolio/

GitHub:
https://github.com/NabintouSFofana

LinkedIn:
https://www.linkedin.com/in/nabintousfofana