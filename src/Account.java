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

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public double getBalance() {
        double balance = 0.0;
        for (Transaction t : transactions) {
            balance += t.getAmount();
        }
        return balance;
    }

    public double getIncomeTotal() {
        double total = 0.0;
        for (Transaction t : transactions) {
            if (t.getAmount() > 0) total += t.getAmount();
        }
        return total;
    }

    public double getExpenseTotal() {
        double total = 0.0;
        for (Transaction t : transactions) {
            if (t.getAmount() < 0) total += t.getAmount();
        }
        return total;
    }

    public double getMonthlySavingsProgress() {
        return getIncomeTotal() + getExpenseTotal();
    }

    public void setSavingsGoal(double goal) {
        this.savingsGoal = goal;
    }

    public double getSavingsGoal() {
        return savingsGoal;
    }
}
