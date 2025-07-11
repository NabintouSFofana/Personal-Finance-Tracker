import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;

public class FinanceTrackerGUI {
    private Account account;
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel balanceLabel, goalLabel, progressLabel;

    public FinanceTrackerGUI() {
        // ✅ Use native OS Look & Feel for modern look
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        account = new Account();

        frame = new JFrame("💸 Personal Finance Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout(10, 10)); // Padding between sections

        // ✅ Table model and table setup
        tableModel = new DefaultTableModel(new Object[]{"Date", "Description", "Amount", "Category", "Emoji"}, 0);
        table = new JTable(tableModel);
        table.setRowHeight(28);

        // ✅ Use emoji-safe font for table
        table.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(200, 200, 255));
        header.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);

        // ✅ Stats labels with nice fonts
        balanceLabel = new JLabel("Balance: $0.00");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));

        goalLabel = new JLabel("Savings Goal: $0.00");
        goalLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        progressLabel = new JLabel("Progress: 0%");
        progressLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel statsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        statsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Your Stats",
                TitledBorder.LEFT, TitledBorder.TOP));
        statsPanel.add(balanceLabel);
        statsPanel.add(goalLabel);
        statsPanel.add(progressLabel);

        // ✅ Buttons with emoji text
        JButton addButton = new JButton("➕ Add Transaction");
        addButton.setFont(new Font("Arial", Font.BOLD, 12));
        addButton.addActionListener(e -> addTransaction());

        JButton setGoalButton = new JButton("🎯 Set Savings Goal");
        setGoalButton.setFont(new Font("Arial", Font.BOLD, 12));
        setGoalButton.addActionListener(e -> setSavingsGoal());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(setGoalButton);

        // ✅ Add everything to the frame
        frame.getContentPane().add(statsPanel, BorderLayout.NORTH);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void addTransaction() {
        JTextField descField = new JTextField();
        JTextField amountField = new JTextField();
        String[] categories = {"Income", "Expense"};
        JComboBox<String> categoryBox = new JComboBox<>(categories);

        // ✅ Use emojis only (not emoji + text)
        String[] emojis = {"🛒", "🍔", "💡", "💼", "💰"};
        JComboBox<String> emojiBox = new JComboBox<>(emojis);

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Description:"));
        panel.add(descField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryBox);
        panel.add(new JLabel("Emoji:"));
        panel.add(emojiBox);

        int result = JOptionPane.showConfirmDialog(frame, panel, "➕ Add Transaction",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String desc = descField.getText();
                double amount = Double.parseDouble(amountField.getText());
                String category = (String) categoryBox.getSelectedItem();
                String emoji = (String) emojiBox.getSelectedItem();

                if ("Expense".equals(category) && amount > 0) {
                    amount *= -1;
                }

                Transaction t = new Transaction(desc, amount, LocalDate.now(), category, emoji);
                account.addTransaction(t);
                tableModel.addRow(new Object[]{t.getDate(), desc, amount, category, emoji});

                checkUnusualExpense(t);
                updateStats();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "⚠️ Invalid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void setSavingsGoal() {
        String input = JOptionPane.showInputDialog(frame, "Enter your monthly savings goal:");
        if (input != null) {
            try {
                double goal = Double.parseDouble(input);
                account.setSavingsGoal(goal);
                updateStats();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "⚠️ Invalid goal!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void checkUnusualExpense(Transaction t) {
        double incomeTotal = account.getIncomeTotal();
        if (incomeTotal > 0 && Math.abs(t.getAmount()) > incomeTotal * 0.3 && t.getAmount() < 0) {
            JOptionPane.showMessageDialog(frame, "⚠️ Warning: This expense is more than 30% of total income!");
        }
    }

    private void updateStats() {
        double balance = account.getBalance();
        double goal = account.getSavingsGoal();
        double progress = account.getMonthlySavingsProgress();
        double incomeTotal = account.getIncomeTotal();

        balanceLabel.setText(String.format("Balance: $%.2f", balance));
        goalLabel.setText(String.format("Savings Goal: $%.2f", goal));

        if (incomeTotal <= 0) {
            progressLabel.setText("Progress: 0%");
        } else if (goal > 0) {
            double percent = (progress / goal) * 100;
            if (percent < 0) percent = 0;
            progressLabel.setText(String.format("Progress: %.2f%%", percent));
        } else {
            progressLabel.setText("Progress: 0%");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FinanceTrackerGUI::new);
    }
}
