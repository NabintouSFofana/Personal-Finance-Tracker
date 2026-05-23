import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The user's account: a list of {@link Transaction}s plus a savings goal.
 *
 * <p>Computes income, expenses, balance, and progress against the goal —
 * all derived from the transaction list, so there's never two sources of truth.
 *
 * @author Nabintou S. Fofana
 */
public class Account {

    private final List<Transaction> transactions = new ArrayList<>();
    private double monthlySavingsGoal = 0.0;

    public void add(Transaction t) {
        if (t == null) throw new IllegalArgumentException("transaction must not be null");
        transactions.add(t);
    }

    public void remove(Transaction t) {
        transactions.remove(t);
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public int size() { return transactions.size(); }

    /** @return total income (sum of positive amounts). Always >= 0. */
    public double getTotalIncome() {
        return transactions.stream()
                .filter(Transaction::isIncome)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /** @return total expenses as a positive number (sum of |negative amounts|). */
    public double getTotalExpenses() {
        return transactions.stream()
                .filter(Transaction::isExpense)
                .mapToDouble(t -> -t.getAmount())
                .sum();
    }

    /** @return current balance: income minus expenses. */
    public double getBalance() {
        return getTotalIncome() - getTotalExpenses();
    }

    /**
     * @return savings progress as a percentage of the monthly goal, clamped to [0, 100+].
     *         Returns 0 if no goal has been set.
     */
    public double getSavingsProgressPercent() {
        if (monthlySavingsGoal <= 0) return 0;
        double balance = getBalance();
        if (balance <= 0) return 0;
        return (balance / monthlySavingsGoal) * 100.0;
    }

    public void setMonthlySavingsGoal(double goal) {
        if (goal < 0) throw new IllegalArgumentException("savings goal cannot be negative");
        this.monthlySavingsGoal = goal;
    }
    public double getMonthlySavingsGoal() { return monthlySavingsGoal; }

    /**
     * @param t the transaction to evaluate
     * @return true if this expense is greater than 30% of total income — the threshold
     *         the original app uses to warn the user about unusual spending.
     */
    public boolean isUnusualExpense(Transaction t) {
        if (!t.isExpense()) return false;
        double income = getTotalIncome();
        if (income <= 0) return false;
        return Math.abs(t.getAmount()) > income * 0.30;
    }
}
