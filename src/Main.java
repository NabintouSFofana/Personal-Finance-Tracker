import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Account account = new Account();
        Scanner in = new Scanner(System.in);

        System.out.println();
        System.out.println("=================================================");
        System.out.println("   Personal Finance Tracker — console edition");
        System.out.println("=================================================");
        System.out.println();

        promptGoal(in, account);
        boolean running = true;
        while (running) {
            running = menu(in, account);
        }
        in.close();
    }

    private static void promptGoal(Scanner in, Account account) {
        while (true) {
            System.out.print("Set your monthly savings goal: $");
            try {
                double goal = in.nextDouble();
                in.nextLine();
                if (goal < 0) { System.out.println("⚠️  Goal can't be negative."); continue; }
                account.setMonthlySavingsGoal(goal);
                System.out.printf("✓ Goal set to $%.2f%n%n", goal);
                return;
            } catch (InputMismatchException ex) {
                System.out.println("⚠️  Please enter a number.");
                in.nextLine();
            }
        }
    }

    private static boolean menu(Scanner in, Account account) {
        System.out.println("---- Menu ----");
        System.out.println("  1. Add transaction");
        System.out.println("  2. View transactions");
        System.out.println("  3. View balance & savings progress");
        System.out.println("  4. Exit");
        System.out.print("Choose: ");

        int choice;
        try {
            choice = in.nextInt();
            in.nextLine();
        } catch (InputMismatchException ex) {
            System.out.println("⚠️  Please enter a number between 1 and 4.\n");
            in.nextLine();
            return true;
        }

        switch (choice) {
            case 1: addTransaction(in, account); break;
            case 2: viewTransactions(account); break;
            case 3: viewSummary(account); break;
            case 4: System.out.println("Goodbye 👋"); return false;
            default: System.out.println("⚠️  Try again.\n");
        }
        return true;
    }

    private static void addTransaction(Scanner in, Account account) {
        System.out.print("Description: ");
        String desc = in.nextLine();

        double amount;
        while (true) {
            System.out.print("Amount (positive=income, negative=expense): $");
            try {
                amount = in.nextDouble();
                in.nextLine();
                if (amount == 0) { System.out.println("⚠️  Amount can't be zero."); continue; }
                break;
            } catch (InputMismatchException ex) {
                System.out.println("⚠️  Not a number, try again.");
                in.nextLine();
            }
        }

        System.out.print("Category: ");
        String category = in.nextLine();
        System.out.print("Emoji tag (optional, e.g. 🛒 or 🍔): ");
        String emoji = in.nextLine();

        Transaction t = new Transaction(desc, amount, LocalDate.now(), category, emoji);
        account.add(t);
        System.out.println("✓ Added.");
        if (account.isUnusualExpense(t)) {
            System.out.println("⚠️  Heads up: that's more than 30% of your total income.");
        }
        System.out.println();
    }

    private static void viewTransactions(Account account) {
        if (account.size() == 0) {
            System.out.println("Nothing logged yet.\n");
            return;
        }
        System.out.println();
        for (Transaction t : account.getTransactions()) System.out.println("  " + t);
        System.out.println();
    }

    private static void viewSummary(Account account) {
        System.out.println();
        System.out.printf("  Income:    $%,.2f%n", account.getTotalIncome());
        System.out.printf("  Expenses:  $%,.2f%n", account.getTotalExpenses());
        System.out.printf("  Balance:   $%,.2f%n", account.getBalance());
        System.out.printf("  Goal:      $%,.2f%n", account.getMonthlySavingsGoal());
        System.out.printf("  Progress:  %.1f%%%n%n", account.getSavingsProgressPercent());
    }
}
