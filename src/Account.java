import java.util.ArrayList;
import java.util.List;

public class Account {
    private List<Transaction> transactions;
    private double savingsGoal;

    public Account() {
        transactions = new ArrayList<>();
        savingsGoal = 0.0;
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public double getBalance() {
        double balance = 0.0;
        for (Transaction t : transactions) {
            balance += t.getAmount();
        }
        return balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setSavingsGoal(double goal) {
        this.savingsGoal = goal;
    }

    public double getSavingsGoal() {
        return this.savingsGoal;
    }

    public double getIncomeTotal() {
        double total = 0.0;
        for (Transaction t : transactions) {
            if (t.getAmount() > 0) {
                total += t.getAmount();
            }
        }
        return total;
    }

    public double getExpenseTotal() {
        double total = 0.0;
        for (Transaction t : transactions) {
            if (t.getAmount() < 0) {
                total += t.getAmount(); // will be negative
            }
        }
        return total;
    }

    public double getMonthlySavingsProgress() {
        double netSavings = getIncomeTotal() + getExpenseTotal(); // expenses are negative
        return netSavings;
    }

    public void checkUnusualExpense(Transaction t) {
        double incomeTotal = getIncomeTotal();
        if (incomeTotal > 0 && Math.abs(t.getAmount()) > incomeTotal * 0.3 && t.getAmount() < 0) {
            System.out.println("⚠️ Warning: This expense is more than 30% of your total income!");
        }
    }
}
