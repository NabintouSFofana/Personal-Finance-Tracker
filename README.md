# Personal Finance Tracker

A Java desktop app for tracking income, expenses, and a monthly savings goal. Built with Swing and [FlatLaf](https://www.formdev.com/flatlaf/) so it doesn't look like 1998.

## What it does

You add transactions (income or expense), set a monthly savings goal, and the app shows you where you stand — balance, total income, total expenses, and a progress bar against your goal. The transaction table colors expenses in rust and income in green so you can see the shape of your month at a glance.

If you log a single expense that's more than 30% of your total income, the app pauses and asks if you really want to add it. A small safety net for typos.

## What's inside

```
src/
├── Account.java                income/expenses, stats, goal logic
├── Transaction.java            immutable transaction record
├── FinanceTrackerGUI.java      the Swing UI
└── Main.java                   console version using the same model classes
```

## Features

- Sidebar with three stat cards (balance, total income, total expenses) plus a goal card with a progress bar
- Transactions table with amount coloring and category column
- "Add transaction" and "Set monthly goal" dialogs with form validation
- Unusual-expense warning when one expense is >30% of total income
- A console version (`Main.java`) that uses the same model classes — same logic, no GUI

## Run it

You'll need the FlatLaf jar in a `lib/` folder. [Download it here](https://www.formdev.com/flatlaf/) (the `flatlaf-3.x.x.jar` file).

```bash
git clone https://github.com/NabintouSFofana/Personal-Finance-Tracker.git
cd Personal-Finance-Tracker
mkdir lib && mv ~/Downloads/flatlaf-*.jar lib/

# macOS / Linux
javac -cp "lib/*" -d bin src/*.java
java -cp "bin:lib/*" FinanceTrackerGUI

# Windows
javac -cp "lib/*" -d bin src/*.java
java -cp "bin;lib/*" FinanceTrackerGUI
```

Or for the console version:

```bash
java -cp "bin:lib/*" Main
```

## Built with

Java 17, Swing, FlatLaf for the look-and-feel, all OOP, no external database.

## What I learned

Swing's table model and reading FlatLaf's docs until I understood what I was customizing. The bigger thing was keeping logic out of the UI. My first version had the stats math written inside the same method that drew the labels. After I pulled the math into the `Account` class, I was able to write a console version that shared all the same logic — and that moment of "oh, this just works in two places" is what made the lesson stick.

## License

MIT — see [LICENSE](LICENSE).

---

Built by [Nabintou S. Fofana](https://nabintousfofana.github.io/portfolio/) · 2025
