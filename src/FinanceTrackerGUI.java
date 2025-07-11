import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

public class FinanceTrackerGUI {
    private Account account;
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel balanceLabel;
    private JLabel goalLabel;
    private JLabel progressLabel;

    public FinanceTrackerGUI() {
        account = new Account();

        frame = new JFrame("💸 Personal Finance Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);

        // Table to show transactions
        tableModel = new DefaultTableModel(new Object[]{"Date", "Description", "Amount", "Category", "Emoji"}, 0);
        table = new JTable(tableModel);

        JScrollPane tableScroll = new JScrollPane(table);

        // Add Transaction Button
        JButton addButton = new JButton("Add Transaction");
        addButton.addActionListener(e -> addTransaction());

        // Set Savings Goal Button
        JButton setGoalButton = new JButton("Set Savings Goal");
        setGoalButton.addActionListener(e -> setSavingsGoal());

        // Balance labels
        balanceLabel = new JLabel("Balance: $0.00");
        goalLabel = new JLabel("Savings Goal: $0.00");
        progressLabel = new JLabel("Progress: 0%");

        JPanel panel = new JPanel();
        panel.add(addButton);
        panel.add(setGoalButton);

        JPanel statsPanel = new JPanel(new GridLayout(3, 1));
        statsPanel.add(balanceLabel);
        statsPanel.add(goalLabel);
        statsPanel.add(progressLabel);

        frame.getContentPane().add(tableScroll, BorderLayout.CENTER);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);
        frame.getContentPane().add(statsPanel, BorderLayout.NORTH);

        frame.setVisible(true);
    }

    private void addTransaction() {
        JTextField descField = new JTextField();
        JTextField amountField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField emojiField = new JTextField();

        Object[] message = {
                "Description:", descField,
                "Amount (+income, -expense):", amountField,
                "Category:", categoryField,
                "Emoji:", emojiField
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Add Transaction", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String desc = descField.getText();
                double amount = Double.parseDouble(amountField.getText());
                String category = categoryField.getText();
                String emoji = emojiField.getText();

                Transaction t = new Transaction(desc, amount, LocalDate.now(), category, emoji);
                account.addTransaction(t);

                tableModel.addRow(new Object[]{t.getDate(), desc, amount, category, emoji});

                checkUnusualExpense(t);
                updateStats();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid amount entered!", "Error", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(frame, "Invalid goal amount!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void checkUnusualExpense(Transaction t) {
        double incomeTotal = account.getIncomeTotal();
        if (incomeTotal > 0 && Math.abs(t.getAmount()) > incomeTotal * 0.3 && t.getAmount() < 0) {
            JOptionPane.showMessageDialog(frame, "⚠️ Warning: This expense is more than 30% of your total income!");
        }
    }

    private void updateStats() {
        double balance = account.getBalance();
        double goal = account.getSavingsGoal();
        double progress = account.getMonthlySavingsProgress();

        balanceLabel.setText(String.format("Balance: $%.2f", balance));
        goalLabel.setText(String.format("Savings Goal: $%.2f", goal));
        if (goal > 0) {
            double percent = (progress / goal) * 100;
            progressLabel.setText(String.format("Progress: %.2f%%", percent));
        } else {
            progressLabel.setText("Progress: 0%");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FinanceTrackerGUI());
    }
}
