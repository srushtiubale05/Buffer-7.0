package com.supplychain.ui;

import com.supplychain.model.*;
import com.supplychain.logic.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class RecoveryUI extends JFrame {

    private JComboBox<String> sourceBox, destBox;
    private JTextArea resultArea;
    private Graph graph;
    private JRadioButton fastestBtn, cheapestBtn, reliableBtn;
    private JButton applyBtn;
    private Map<String, PathResult> lastPlans;
    private JScrollPane centerPane;

    // ── Palette ───────────────────────────────────────────────────────────
    private static final Color BG_PAGE    = new Color(0xF1EFE8);
    private static final Color BG_CARD    = new Color(0xFFFFFF);
    private static final Color BG_HEADER  = new Color(0x1D9E75);
    private static final Color BG_FIELD   = new Color(0xF1EFE8);
    private static final Color BG_BTN     = new Color(0x1D9E75);
    private static final Color BG_BTN_HOV = new Color(0x0F6E56);
    private static final Color BG_SEC     = new Color(0xF7F6F2);

    private static final Color FG_TITLE   = Color.WHITE;
    private static final Color FG_SUB     = new Color(0xB0E8D0);
    private static final Color FG_PRIMARY = new Color(0x2C2C2A);
    private static final Color FG_MUTED   = new Color(0x888780);
    private static final Color FG_LABEL   = new Color(0x5F5E5A);
    private static final Color FG_BTN     = Color.WHITE;

    private static final Color BD_FIELD   = new Color(0xD3D1C7);
    private static final Color BD_CARD    = new Color(0xD3D1C7);

    private static final Color C_FASTEST  = new Color(0x185FA5);
    private static final Color C_CHEAPEST = new Color(0x1D9E75);
    private static final Color C_RELIABLE = new Color(0x534AB7);

    private static final Color ROW_ODD    = new Color(0xFFFFFF);
    private static final Color ROW_EVEN   = new Color(0xF7F6F2);
    private static final Color TH_BG      = new Color(0xF1EFE8);
    private static final Color TH_FG      = new Color(0x5F5E5A);

    // ── Constructor ───────────────────────────────────────────────────────
    public RecoveryUI(Graph graph) {
        this.graph = graph;

        setTitle("Recovery Planner");
        setSize(720, 580);
        setMinimumSize(new Dimension(600, 480));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        getContentPane().setBackground(BG_PAGE);

        add(buildHeader(),  BorderLayout.NORTH);
        add(buildCenter(),  BorderLayout.CENTER);
        add(buildFooter(),  BorderLayout.SOUTH);

        loadNodes();
        setVisible(true);

        applyBtn.addActionListener(e -> applyRecovery());
    }

    // ── Header ────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setBackground(BG_HEADER);
        header.setBorder(new EmptyBorder(16, 20, 16, 20));

        JPanel iconBox = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillRoundRect(0, 0, 36, 36, 8, 8);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                // route icon
                g2.drawLine(8, 28, 14, 14);
                g2.drawLine(14, 14, 22, 22);
                g2.drawLine(22, 22, 28, 8);
                g2.fillOval(6,  25, 5, 5);
                g2.fillOval(20, 19, 5, 5);
                g2.fillOval(26,  5, 5, 5);
            }
        };
        iconBox.setOpaque(false);
        iconBox.setPreferredSize(new Dimension(36, 36));

        JPanel text = new JPanel(new GridLayout(2, 1, 0, 2));
        text.setOpaque(false);
        JLabel t = new JLabel("Recovery Planner");
        t.setFont(new Font("Segoe UI", Font.BOLD, 15));
        t.setForeground(FG_TITLE);
        JLabel s = new JLabel("Generate and apply optimal recovery paths");
        s.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        s.setForeground(FG_SUB);
        text.add(t); text.add(s);

        header.add(iconBox, BorderLayout.WEST);
        header.add(text,    BorderLayout.CENTER);
        return header;
    }

    // ── Center ────────────────────────────────────────────────────────────
    private JPanel buildCenter() {
        JPanel outer = new JPanel(new BorderLayout(0, 12));
        outer.setBackground(BG_PAGE);
        outer.setBorder(new EmptyBorder(14, 14, 0, 14));

        // ── Input card ────────────────────────────────────────────
        JPanel inputCard = new JPanel(new BorderLayout());
        inputCard.setBackground(BG_CARD);
        inputCard.setBorder(new LineBorder(BD_CARD, 1, true));

        JPanel inputBody = new JPanel(new GridLayout(1, 2, 14, 0));
        inputBody.setBackground(BG_CARD);
        inputBody.setBorder(new EmptyBorder(16, 18, 16, 18));

        sourceBox = new JComboBox<>();
        destBox   = new JComboBox<>();
        styleCombo(sourceBox);
        styleCombo(destBox);

        inputBody.add(fieldBlock("Source node", sourceBox));
        inputBody.add(fieldBlock("Destination node", destBox));
        inputCard.add(inputBody, BorderLayout.CENTER);

        // Generate + Refresh buttons row
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 10));
        btnRow.setBackground(new Color(0xFAFAF8));
        btnRow.setBorder(new MatteBorder(1, 0, 0, 0, BD_FIELD));

        JButton refreshBtn  = outlineButton("Refresh nodes");
        JButton generateBtn = solidButton("Generate recovery plan");

        btnRow.add(refreshBtn);
        btnRow.add(generateBtn);
        inputCard.add(btnRow, BorderLayout.SOUTH);

        // ── Result area (default) ─────────────────────────────────
        resultArea = new JTextArea("Results will appear here after generating a plan.");
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        resultArea.setForeground(FG_MUTED);
        resultArea.setBackground(BG_CARD);
        resultArea.setMargin(new Insets(14, 16, 14, 16));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);

        centerPane = new JScrollPane(resultArea);
        centerPane.setBorder(new LineBorder(BD_CARD, 1, true));
        centerPane.getViewport().setBackground(BG_CARD);

        outer.add(inputCard,  BorderLayout.NORTH);
        outer.add(centerPane, BorderLayout.CENTER);

        // ── Listeners ─────────────────────────────────────────────
        generateBtn.addActionListener(e -> generateRecovery());
        refreshBtn.addActionListener(e -> loadNodes());

        return outer;
    }

    // ── Footer ────────────────────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(BG_PAGE);
        footer.setBorder(new EmptyBorder(10, 14, 14, 14));

        // Strategy selector
        JPanel stratCard = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        stratCard.setBackground(BG_CARD);
        stratCard.setBorder(new LineBorder(BD_CARD, 1, true));

        JLabel stratLbl = new JLabel("Strategy:");
        stratLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        stratLbl.setForeground(FG_LABEL);
        stratCard.add(stratLbl);

        fastestBtn  = styledRadio("Fastest",  C_FASTEST,  true);
        cheapestBtn = styledRadio("Cheapest", C_CHEAPEST, false);
        reliableBtn = styledRadio("Reliable", C_RELIABLE, false);

        ButtonGroup group = new ButtonGroup();
        group.add(fastestBtn); group.add(cheapestBtn); group.add(reliableBtn);

        stratCard.add(fastestBtn);
        stratCard.add(cheapestBtn);
        stratCard.add(reliableBtn);

        applyBtn = solidButton("Apply recovery");
        stratCard.add(Box.createHorizontalStrut(8));
        stratCard.add(applyBtn);

        footer.add(stratCard, BorderLayout.CENTER);
        return footer;
    }

    // ── Load nodes ────────────────────────────────────────────────────────
    private void loadNodes() {
        sourceBox.removeAllItems();
        destBox.removeAllItems();
        for (Node node : graph.getAllNodes()) {
            sourceBox.addItem(node.getName());
            destBox.addItem(node.getName());
        }
    }

    // ── Generate recovery ─────────────────────────────────────────────────
    private void generateRecovery() {
        String sourceName = (String) sourceBox.getSelectedItem();
        String destName   = (String) destBox.getSelectedItem();

        Node source = graph.getNodeByName(sourceName);
        Node dest   = graph.getNodeByName(destName);

        if (source == null || dest == null) {
            showDialog("Invalid nodes selected!", false);
            return;
        }

        RecoveryPlanner planner = new RecoveryPlanner(graph);
        Map<String, PathResult> plans = planner.getRecoveryPlans(source, dest);

        String[] columnNames = { "Strategy", "Path", "Value" };
        Object[][] data = new Object[plans.size()][3];
        int i = 0;
        for (String type : plans.keySet()) {
            PathResult result = plans.get(type);
            if (result.getTotalValue() == Double.MAX_VALUE) {
                data[i] = new Object[]{ type, "No path available", "—" };
            } else {
                StringBuilder pathStr = new StringBuilder();
                for (Node n : result.getPath()) pathStr.append(n.getName()).append(" → ");
                pathStr.append("END");
                data[i] = new Object[]{ type, pathStr.toString(),
                        String.format("%.2f", result.getTotalValue()) };
            }
            i++;
        }

        JTable table = new JTable(data, columnNames) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(BD_CARD, 1, true));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.getViewport().setBackground(BG_CARD);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // swap center pane
        Container parent = centerPane.getParent();
        if (parent != null) {
            parent.remove(centerPane);
            centerPane = scroll;
            parent.add(centerPane, BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        }

        lastPlans = plans;
    }

    // ── Apply recovery ────────────────────────────────────────────────────
    private void applyRecovery() {
        if (lastPlans == null) {
            showDialog("Generate a plan first!", false);
            return;
        }

        String selectedType = fastestBtn.isSelected()  ? "Fastest"
                : cheapestBtn.isSelected() ? "Cheapest"
                  :                            "Reliable";

        PathResult selectedPath = lastPlans.get(selectedType);

        if (selectedPath == null || selectedPath.getPath().isEmpty()) {
            showDialog("No path available for this strategy!", false);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("✅  Recovery applied — ").append(selectedType.toUpperCase()).append("\n\n");
        for (Node node : selectedPath.getPath()) sb.append(node.getName()).append(" → ");
        sb.append("END\n");
        sb.append("Value: ").append(String.format("%.2f", selectedPath.getTotalValue()));

        resultArea.setForeground(FG_PRIMARY);
        resultArea.setText(sb.toString());

        // swap back to resultArea if table is showing
        Container parent = centerPane.getParent();
        if (parent != null && !(centerPane.getViewport().getView() instanceof JTextArea)) {
            parent.remove(centerPane);
            centerPane = new JScrollPane(resultArea);
            centerPane.setBorder(new LineBorder(BD_CARD, 1, true));
            centerPane.getViewport().setBackground(BG_CARD);
            parent.add(centerPane, BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        }

        showDialog("Recovery applied using " + selectedType + " strategy!", true);
    }

    // ── Appendpath helper (unchanged) ─────────────────────────────────────
    private void appendPath(StringBuilder sb, PathResult result) {
        if (result.getTotalValue() == Double.MAX_VALUE) {
            sb.append("No Path Available\n");
            return;
        }
        for (Node node : result.getPath()) sb.append(node.getName()).append(" -> ");
        sb.append("END\n");
        sb.append("Value: ").append(result.getTotalValue()).append("\n");
    }

    // ── Table styling ─────────────────────────────────────────────────────
    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setForeground(FG_PRIMARY);
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(0xE1F5EE));
        table.setSelectionForeground(FG_PRIMARY);

        // Header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 11));
        header.setBackground(TH_BG);
        header.setForeground(TH_FG);
        header.setBorder(new MatteBorder(0, 0, 1, 0, BD_FIELD));
        header.setPreferredSize(new Dimension(0, 34));

        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(460);
        table.getColumnModel().getColumn(2).setPreferredWidth(90);

        // Centre col 0 and 2
        DefaultTableCellRenderer centre = new DefaultTableCellRenderer();
        centre.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centre);
        table.getColumnModel().getColumn(2).setCellRenderer(centre);

        // Alternating rows + strategy colour dot for col 0
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setFont(new Font("Segoe UI", col == 0 ? Font.BOLD : Font.PLAIN, 12));
                setBackground(sel ? new Color(0xE1F5EE) : (row % 2 == 0 ? ROW_ODD : ROW_EVEN));
                setForeground(col == 0 ? stratColor(val == null ? "" : val.toString()) : FG_PRIMARY);
                setBorder(new EmptyBorder(0, col == 1 ? 10 : 0, 0, 0));
                setHorizontalAlignment(col == 1 ? JLabel.LEFT : JLabel.CENTER);
                return this;
            }
        });
    }

    private Color stratColor(String type) {
        switch (type) {
            case "Fastest":  return C_FASTEST;
            case "Cheapest": return C_CHEAPEST;
            case "Reliable": return C_RELIABLE;
            default:         return FG_PRIMARY;
        }
    }

    // ── Component helpers ─────────────────────────────────────────────────
    private JPanel fieldBlock(String labelText, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setOpaque(false);
        JLabel l = new JLabel(labelText);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        l.setForeground(FG_LABEL);
        p.add(l, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private void styleCombo(JComboBox<String> box) {
        box.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        box.setBackground(BG_FIELD);
        box.setForeground(FG_PRIMARY);
        box.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BD_FIELD, 1, true),
                new EmptyBorder(2, 6, 2, 6)));
    }

    private JButton solidButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? BG_BTN_HOV : BG_BTN);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(FG_BTN);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 18, 8, 18));
        return btn;
    }

    private JButton outlineButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setForeground(FG_LABEL);
        btn.setBackground(new Color(0xF1EFE8));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BD_FIELD, 1, true),
                new EmptyBorder(7, 14, 7, 14)));
        return btn;
    }

    private JRadioButton styledRadio(String text, Color accent, boolean selected) {
        JRadioButton r = new JRadioButton(text, selected);
        r.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        r.setForeground(FG_LABEL);
        r.setBackground(BG_CARD);
        r.setOpaque(false);
        r.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return r;
    }

    private void showDialog(String msg, boolean success) {
        JOptionPane.showMessageDialog(this, msg,
                success ? "Success" : "Notice",
                success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
    }
}