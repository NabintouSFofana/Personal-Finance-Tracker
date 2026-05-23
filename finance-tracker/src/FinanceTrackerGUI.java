import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.time.LocalDate;

/**
 * Personal Finance Tracker — main GUI.
 *
 * <p>A clean Swing window with FlatLaf theming, a stats sidebar, a transactions table,
 * and a "+ Add transaction" floating action. The original 2020-era Swing chrome is
 * gone; FlatLaf gives it a modern flat appearance that holds up next to any 2024 UI.
 *
 * <p>Dependencies: FlatLaf (single jar). See README for setup.
 *
 * @author Nabintou S. Fofana
 */
public class FinanceTrackerGUI {

    // ── Editorial palette (matches the portfolio brand) ──────────────────
    private static final Color BG        = new Color(0xF2, 0xEC, 0xE0);
    private static final Color PAPER     = new Color(0xFA, 0xF6, 0xEE);
    private static final Color INK       = new Color(0x14, 0x11, 0x0D);
    private static final Color INK_SOFT  = new Color(0x5C, 0x52, 0x47);
    private static final Color INK_MUTE  = new Color(0x8A, 0x7F, 0x6E);
    private static final Color ACCENT    = new Color(0xB6, 0x41, 0x1C);
    private static final Color GREEN     = new Color(0x2A, 0x6F, 0x4E);
    private static final Color RULE      = new Color(0x14, 0x11, 0x09, 0x33);

    // ── Fonts ────────────────────────────────────────────────────────────
    private static final Font HEAD_FONT  = new Font("Inter", Font.BOLD, 18);
    private static final Font BODY_FONT  = new Font("Inter", Font.PLAIN, 14);
    private static final Font MONO_FONT  = new Font("JetBrains Mono", Font.PLAIN, 11);
    private static final Font STAT_FONT  = new Font("Inter", Font.BOLD, 28);

    private final Account account = new Account();

    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel incomeValueLabel;
    private JLabel expensesValueLabel;
    private JLabel balanceValueLabel;
    private JLabel goalValueLabel;
    private JLabel progressLabel;
    private JProgressBar progressBar;

    public FinanceTrackerGUI() {
        installLookAndFeel();
        SwingUtilities.invokeLater(this::buildAndShow);
    }

    private void installLookAndFeel() {
        try {
            // Customize before installation
            UIManager.put("Component.arc",          16);
            UIManager.put("Button.arc",             999);
            UIManager.put("ProgressBar.arc",        999);
            UIManager.put("TextComponent.arc",      12);
            UIManager.put("Component.focusColor",   ACCENT);
            UIManager.put("Component.focusWidth",   1);
            UIManager.put("Component.borderColor",  RULE);
            UIManager.put("Button.default.background", INK);
            UIManager.put("Button.default.foreground", PAPER);
            UIManager.put("TitlePane.background",   PAPER);
            UIManager.put("Table.gridColor",        RULE);
            UIManager.put("Table.showVerticalLines",   false);
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.intercellSpacing",  new Dimension(0, 0));
            UIManager.put("ScrollPane.background",   PAPER);

            FlatLightLaf.setup();
        } catch (Exception ex) {
            System.err.println("FlatLaf failed to load — falling back to system L&F: " + ex.getMessage());
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}
        }
    }

    private void buildAndShow() {
        frame = new JFrame("Personal Finance Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 700);
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(new Dimension(900, 600));
        frame.getContentPane().setBackground(BG);

        JPanel root = new JPanel(new BorderLayout(24, 24));
        root.setBorder(new EmptyBorder(28, 32, 24, 32));
        root.setBackground(BG);

        root.add(buildHeader(),  BorderLayout.NORTH);
        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildTable(),   BorderLayout.CENTER);
        root.add(buildActions(), BorderLayout.SOUTH);

        frame.setContentPane(root);
        frame.setVisible(true);

        refreshStats();
    }

    // ─────────────────────────────────────────────────────────────────────
    // HEADER
    // ─────────────────────────────────────────────────────────────────────
    private JComponent buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titleCol = new JPanel();
        titleCol.setLayout(new BoxLayout(titleCol, BoxLayout.Y_AXIS));
        titleCol.setOpaque(false);

        JLabel eyebrow = new JLabel("§ 01  ·  Personal Finance");
        eyebrow.setFont(MONO_FONT.deriveFont(11f));
        eyebrow.setForeground(ACCENT);

        JLabel title = new JLabel("Track what comes in, what goes out.");
        title.setFont(new Font("Inter", Font.BOLD, 26));
        title.setForeground(INK);

        titleCol.add(eyebrow);
        titleCol.add(Box.createVerticalStrut(6));
        titleCol.add(title);

        header.add(titleCol, BorderLayout.WEST);
        return header;
    }

    // ─────────────────────────────────────────────────────────────────────
    // SIDEBAR — stats cards
    // ─────────────────────────────────────────────────────────────────────
    private JComponent buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setOpaque(false);
        sidebar.setPreferredSize(new Dimension(260, 0));

        balanceValueLabel  = statValueLabel("$0.00");
        incomeValueLabel   = statValueLabel("$0.00");
        expensesValueLabel = statValueLabel("$0.00");
        goalValueLabel     = statValueLabel("$0.00");

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setForeground(GREEN);
        progressBar.setBackground(new Color(0x14, 0x11, 0x09, 0x11));
        progressBar.setStringPainted(false);
        progressBar.setBorderPainted(false);
        progressBar.setPreferredSize(new Dimension(0, 6));

        progressLabel = new JLabel("0%");
        progressLabel.setFont(MONO_FONT);
        progressLabel.setForeground(INK_MUTE);

        sidebar.add(buildStatCard("Balance",         balanceValueLabel,  ACCENT));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(buildStatCard("Total income",    incomeValueLabel,   GREEN));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(buildStatCard("Total expenses",  expensesValueLabel, ACCENT));
        sidebar.add(Box.createVerticalStrut(18));
        sidebar.add(buildGoalCard());
        sidebar.add(Box.createVerticalGlue());

        return sidebar;
    }

    private JPanel buildStatCard(String label, JLabel valueLabel, Color accent) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(PAPER);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(RULE),
                new EmptyBorder(14, 16, 14, 16)));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel l = new JLabel(label.toUpperCase());
        l.setFont(MONO_FONT.deriveFont(10f));
        l.setForeground(INK_SOFT);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);

        valueLabel.setForeground(accent);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(l);
        card.add(Box.createVerticalStrut(6));
        card.add(valueLabel);
        return card;
    }

    private JPanel buildGoalCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(PAPER);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(RULE),
                new EmptyBorder(14, 16, 14, 16)));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        JLabel l = new JLabel("MONTHLY GOAL");
        l.setFont(MONO_FONT.deriveFont(10f));
        l.setForeground(INK_SOFT);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);

        goalValueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        progressLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(l);
        card.add(Box.createVerticalStrut(6));
        card.add(goalValueLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(progressBar);
        card.add(Box.createVerticalStrut(6));
        card.add(progressLabel);
        return card;
    }

    private JLabel statValueLabel(String initial) {
        JLabel l = new JLabel(initial);
        l.setFont(STAT_FONT);
        return l;
    }

    // ─────────────────────────────────────────────────────────────────────
    // TABLE
    // ─────────────────────────────────────────────────────────────────────
    private JComponent buildTable() {
        tableModel = new DefaultTableModel(
                new Object[]{"Date", "Description", "Category", "Amount", ""}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(38);
        table.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        table.setBackground(PAPER);
        table.setSelectionBackground(new Color(0xB6, 0x41, 0x1C, 0x22));
        table.setSelectionForeground(INK);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = table.getTableHeader();
        header.setFont(MONO_FONT.deriveFont(10f));
        header.setForeground(INK_SOFT);
        header.setBackground(BG);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, RULE));
        header.setPreferredSize(new Dimension(0, 32));

        // Column widths
        TableColumn dateCol   = table.getColumnModel().getColumn(0);
        TableColumn descCol   = table.getColumnModel().getColumn(1);
        TableColumn catCol    = table.getColumnModel().getColumn(2);
        TableColumn amountCol = table.getColumnModel().getColumn(3);
        TableColumn iconCol   = table.getColumnModel().getColumn(4);
        dateCol.setPreferredWidth(110);
        descCol.setPreferredWidth(300);
        catCol.setPreferredWidth(120);
        amountCol.setPreferredWidth(120);
        iconCol.setPreferredWidth(40);

        // Right-align + color the amount column
        amountCol.setCellRenderer(new AmountRenderer());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(RULE));
        scroll.getViewport().setBackground(PAPER);

        return scroll;
    }

    private class AmountRenderer extends javax.swing.table.DefaultTableCellRenderer {
        public AmountRenderer() {
            setHorizontalAlignment(SwingConstants.RIGHT);
            setFont(MONO_FONT.deriveFont(13f));
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value instanceof String) {
                String s = (String) value;
                if (!isSelected) {
                    c.setForeground(s.startsWith("-") ? ACCENT : GREEN);
                }
            }
            return c;
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // FOOTER ACTIONS
    // ─────────────────────────────────────────────────────────────────────
    private JComponent buildActions() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panel.setOpaque(false);

        JButton goalBtn = new JButton("Set monthly goal");
        goalBtn.setFont(BODY_FONT);
        goalBtn.addActionListener(e -> openGoalDialog());

        JButton addBtn = new JButton("+ Add transaction");
        addBtn.setFont(BODY_FONT.deriveFont(Font.BOLD));
        addBtn.putClientProperty("JButton.buttonType", "default");
        addBtn.setBackground(INK);
        addBtn.setForeground(PAPER);
        addBtn.addActionListener(e -> openAddDialog());

        panel.add(goalBtn);
        panel.add(addBtn);
        return panel;
    }

    // ─────────────────────────────────────────────────────────────────────
    // DIALOGS
    // ─────────────────────────────────────────────────────────────────────
    private void openAddDialog() {
        JTextField descField   = new JTextField();
        JTextField amountField = new JTextField();
        JComboBox<String> typeBox     = new JComboBox<>(new String[]{"Income", "Expense"});
        JComboBox<String> categoryBox = new JComboBox<>(new String[]{
                "Groceries", "Food", "Bills", "Salary", "Other"});
        JComboBox<String> emojiBox    = new JComboBox<>(new String[]{
                "🛒", "🍔", "💡", "💼", "💰", "🎁", "🚗", "🏠", "✈️"});

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 4, 6, 4);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;

        int row = 0;
        addRow(form, c, row++, "Description",  descField);
        addRow(form, c, row++, "Amount ($)",   amountField);
        addRow(form, c, row++, "Type",         typeBox);
        addRow(form, c, row++, "Category",     categoryBox);
        addRow(form, c, row++, "Emoji tag",    emojiBox);

        form.setBorder(new EmptyBorder(8, 4, 8, 4));
        form.setPreferredSize(new Dimension(360, 240));

        int result = JOptionPane.showConfirmDialog(
                frame, form, "Add transaction",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return;

        // Validate
        String desc = descField.getText().trim();
        if (desc.isEmpty()) {
            error("Please enter a description.");
            return;
        }
        double amount;
        try {
            amount = Double.parseDouble(amountField.getText().trim());
            if (amount == 0) { error("Amount must not be zero."); return; }
        } catch (NumberFormatException ex) {
            error("That doesn't look like a number.");
            return;
        }

        boolean isExpense = "Expense".equals(typeBox.getSelectedItem());
        // Always store income as positive, expense as negative; ignore sign the user typed.
        amount = isExpense ? -Math.abs(amount) : Math.abs(amount);

        Transaction t = new Transaction(
                desc, amount, LocalDate.now(),
                (String) categoryBox.getSelectedItem(),
                (String) emojiBox.getSelectedItem()
        );
        account.add(t);

        tableModel.addRow(new Object[]{
                t.getFormattedDate(), t.getDescription(),
                t.getCategory(), t.getFormattedAmount(), t.getEmoji()
        });

        if (account.isUnusualExpense(t)) {
            JOptionPane.showMessageDialog(frame,
                "<html><body style='width: 280px'><b>Heads up.</b><br><br>" +
                "This expense is more than 30% of your total income. " +
                "Worth double-checking, in case it was a typo.</body></html>",
                "Unusual expense", JOptionPane.WARNING_MESSAGE);
        }

        refreshStats();
    }

    private void openGoalDialog() {
        String input = JOptionPane.showInputDialog(frame,
                "What's your monthly savings goal?",
                "Set monthly goal", JOptionPane.PLAIN_MESSAGE);
        if (input == null) return;
        try {
            double goal = Double.parseDouble(input.trim());
            if (goal < 0) { error("Goal can't be negative."); return; }
            account.setMonthlySavingsGoal(goal);
            refreshStats();
        } catch (NumberFormatException ex) {
            error("That doesn't look like a number.");
        }
    }

    private void addRow(JPanel panel, GridBagConstraints c, int row, String label, JComponent field) {
        JLabel l = new JLabel(label);
        l.setFont(MONO_FONT.deriveFont(10f));
        l.setForeground(INK_SOFT);
        c.gridx = 0; c.gridy = row; c.weightx = 0;
        panel.add(l, c);
        c.gridx = 1; c.gridy = row; c.weightx = 1;
        panel.add(field, c);
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(frame, msg, "Hmm", JOptionPane.ERROR_MESSAGE);
    }

    // ─────────────────────────────────────────────────────────────────────
    // STATS REFRESH
    // ─────────────────────────────────────────────────────────────────────
    private void refreshStats() {
        balanceValueLabel.setText(money(account.getBalance()));
        incomeValueLabel.setText(money(account.getTotalIncome()));
        expensesValueLabel.setText(money(account.getTotalExpenses()));
        goalValueLabel.setText(money(account.getMonthlySavingsGoal()));

        double pct = account.getSavingsProgressPercent();
        int barPct = (int) Math.min(100, Math.max(0, pct));
        progressBar.setValue(barPct);
        progressLabel.setText(String.format("%.0f%% of goal", pct));

        // Color the balance based on sign
        balanceValueLabel.setForeground(account.getBalance() < 0 ? ACCENT : GREEN);
    }

    private static String money(double v) {
        return String.format("$%,.2f", v);
    }

    // ─────────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        new FinanceTrackerGUI();
    }
}
