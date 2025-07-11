import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Account account = new Account();
        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        System.out.println("=== Welcome to the Personal Finance Tracker ===");

        // Ask for a savings goal once at the start
        while (true) {
            System.out.print("Set your monthly savings goal: $");
            try {
                double goal = scanner.nextDouble();
                scanner.nextLine(); // consume newline
                account.setSavingsGoal(goal);
                System.out.println("✅ Savings goal set to $" + goal);
                break;
            } catch (InputMismatchException e) {
                System.out.println("⚠️ Please enter a valid number.");
                scanner.nextLine(); // clear input
            }
        }

        while (running) {
            System.out.println("\n=== Menu ===");
            System.out.println("1. Add Transaction");
            System.out.println("2. View All Transactions");
            System.out.println("3. View Balance & Savings Progress");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Enter description: ");
                        String desc = scanner.nextLine();

                        double amount;
                        while (true) {
                            System.out.print("Enter amount (+income, -expense): ");
                            try {
                                amount = scanner.nextDouble();
                                scanner.nextLine(); // consume newline
                                break;
                            } catch (InputMismatchException e) {
                                System.out.println("⚠️ Invalid amount. Please enter a number.");
                                scanner.nextLine();
                            }
                        }

                        System.out.print("Enter category: ");
                        String category = scanner.nextLine();

                        System.out.print("Add emoji tag (optional, like 🛒 or 🍔): ");
                        String emoji = scanner.nextLine();

                        LocalDate date = LocalDate.now();
                        Transaction t = new Transaction(desc, amount, date, category, emoji);
                        account.addTransaction(t);
                        System.out.println("✅ Transaction added!");

                        // Check for unusual expense
                        double incomeTotal = account.getIncomeTotal();
                        if (incomeTotal > 0 && Math.abs(amount) > incomeTotal * 0.3 && amount < 0) {
                            System.out.println("⚠️ Warning: This expense is more than 30% of your total income!");
                        }

                        break;

                    case 2:
                        System.out.println("\n=== All Transactions ===");
                        if (account.getTransactions().isEmpty()) {
                            System.out.println("No transactions yet!");
                        } else {
                            for (Transaction tr : account.getTransactions()) {
                                System.out.println(tr);
                            }
                        }
                        break;

                    case 3:
                        double balance = account.getBalance();
                        double saved = account.getMonthlySavingsProgress();
                        double goal = account.getSavingsGoal();
                        double income = account.getIncomeTotal();

                        System.out.printf("Current balance: $%.2f\n", balance);
                        System.out.printf("This month's savings progress: $%.2f / $%.2f\n", saved, goal);

                        if (income <= 0) {
                            System.out.println("⚠️ You haven't added any income yet! Your savings progress may be inaccurate.");
                            System.out.println("Progress: 0%");
                        } else if (goal > 0) {
                            double percent = (saved / goal) * 100;
                            if (percent < 0) percent = 0;
                            System.out.printf("Progress: %.2f%%\n", percent);
                        } else {
                            System.out.println("Progress: 0%");
                        }
                        break;

                    case 4:
                        running = false;
                        System.out.println("Goodbye! 👋");
                        break;

                    default:
                        System.out.println("⚠️ Invalid option. Try again.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("⚠️ Please enter a valid number.");
                scanner.nextLine(); // clear bad input
            } catch (Exception e) {
                System.out.println("⚠️ Oops! Something went wrong: " + e.getMessage());
                running = false; // exit safely
            }
        }

        scanner.close();
    }
}
