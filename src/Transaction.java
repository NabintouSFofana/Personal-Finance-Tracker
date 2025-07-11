import java.time.LocalDate;

public class Transaction {
    private String description;
    private double amount;
    private LocalDate date;
    private String category;
    private String emoji;

    public Transaction(String description, double amount, LocalDate date, String category, String emoji) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.emoji = emoji;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public String getEmoji() {
        return emoji;
    }

    @Override
    public String toString() {
        return "[" + date + "] " + category + " " + emoji + ": " + description + " - $" + amount;
    }
}
