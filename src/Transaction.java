import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A single financial transaction — income (positive amount) or expense (negative amount).
 *
 * <p>Immutable by design: once created, a transaction can't be edited.
 * Replace it instead.
 *
 * @author Nabintou S. Fofana
 */
public final class Transaction {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("MMM d, yyyy");

    private final String description;
    private final double amount;
    private final LocalDate date;
    private final String category;
    private final String emoji;

    public Transaction(String description, double amount, LocalDate date, String category, String emoji) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("description must not be blank");
        }
        if (date == null) {
            throw new IllegalArgumentException("date must not be null");
        }
        this.description = description.trim();
        this.amount      = amount;
        this.date        = date;
        this.category    = category == null ? "" : category;
        this.emoji       = emoji == null ? "" : emoji;
    }

    public String    getDescription()   { return description; }
    public double    getAmount()        { return amount; }
    public LocalDate getDate()          { return date; }
    public String    getCategory()      { return category; }
    public String    getEmoji()         { return emoji; }

    /** @return true if this transaction is an expense (amount is negative). */
    public boolean isExpense() { return amount < 0; }

    /** @return true if this transaction is income (amount is positive). */
    public boolean isIncome() { return amount > 0; }

    /** @return amount formatted with sign and two decimals (e.g. "-$45.30"). */
    public String getFormattedAmount() {
        return (amount < 0 ? "-$" : "+$") + String.format("%.2f", Math.abs(amount));
    }

    /** @return date in "Mmm d, yyyy" format. */
    public String getFormattedDate() {
        return date.format(DATE_FMT);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s %s — %s (%s)",
                getFormattedDate(), emoji, description, getFormattedAmount(), category);
    }
}
