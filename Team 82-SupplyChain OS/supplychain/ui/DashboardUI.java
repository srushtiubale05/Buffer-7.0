package com.supplychain.ui;

import com.supplychain.model.*;
import com.supplychain.analytics.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.List;
import java.util.Set;

public class DashboardUI extends JFrame {

    private Graph graph;
    private AnalyticsEngine analyticsEngine;
    private SupplierManager supplierManager;

    private JLabel costLabel, lossLabel, highestLossLabel;
    private JTable metricsTable, riskTable, alertsTable;
    private DefaultTableModel metricsModel, riskModel, alertsModel;
    private JTextField searchField;
    private JTextArea searchResultsArea;
    private JButton refreshButton, blacklistButton;

    // ── Palette ───────────────────────────────────────────────────────────
    private static final Color BG_PAGE     = new Color(0xF1EFE8);
    private static final Color BG_CARD     = new Color(0xFFFFFF);
    private static final Color BG_HEADER   = new Color(0x1E293B);
    private static final Color BG_TAB      = new Color(0xF7F6F2);

    private static final Color FG_TITLE    = Color.WHITE;
    private static final Color FG_SUB      = new Color(0x94A3B8);
    private static final Color FG_PRIMARY  = new Color(0x1E293B);
    private static final Color FG_MUTED    = new Color(0x888780);
    private static final Color FG_LIGHT    = new Color(0x94A3B8);

    private static final Color BD_CARD     = new Color(0xD3D1C7);
    private static final Color BD_FIELD    = new Color(0xD3D1C7);
    private static final Color DIVIDER     = new Color(0xE2E8F0);

    private static final Color C_BLUE      = new Color(0x2563EB);
    private static final Color C_GREEN     = new Color(0x16A34A);
    private static final Color C_AMBER     = new Color(0xD97706);
    private static final Color C_RED       = new Color(0xDC2626);
    private static final Color C_VIOLET    = new Color(0x7C3AED);
    private static final Color C_TEAL      = new Color(0x0D9488);

    private static final Color ROW_ODD     = new Color(0xFFFFFF);
    private static final Color ROW_EVEN    = new Color(0xF7F6F2);
    private static final Color TH_BG       = new Color(0xF1EFE8);
    private static final Color TH_FG       = new Color(0x5F5E5A);

    // ── Constructor ───────────────────────────────────────────────────────
    public DashboardUI(Graph graph) {
        this.graph           = graph;
        this.analyticsEngine = new AnalyticsEngine(graph);
        this.supplierManager = new SupplierManager(graph, analyticsEngine);

        setTitle("Supply Chain Dashboard & Supplier Intelligence");
        setSize(1280, 820);
        setMinimumSize(new Dimension(1000, 680));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        getContentPane().setBackground(BG_PAGE);

        add(buildHeader(),      BorderLayout.NORTH);
        add(buildBody(),        BorderLayout.CENTER);
        add(buildFooter(),      BorderLayout.SOUTH);

        setVisible(true);
        refreshDashboard();
    }

    // ── Header ────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0,0,new Color(0x1E293B),getWidth(),0,new Color(0x0F4C81)));
                g2.fillRect(0,0,getWidth(),getHeight());
                g2.setColor(new Color(255,255,255,10));
                g2.fillOval(getWidth()-200,-40,260,260);
                g2.setColor(new Color(37,99,235,30));
                g2.fillOval(-40,getHeight()-60,160,160);
            }
        };
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(18, 32, 18, 32));

        JPanel left = new JPanel(new GridLayout(2,1,0,3));
        left.setOpaque(false);
        JLabel t = new JLabel("Analytics Dashboard");
        t.setFont(new Font("Georgia", Font.BOLD, 20));
        t.setForeground(FG_TITLE);
        JLabel s = new JLabel("Real-time metrics · Supplier intelligence · Alerts · Search");
        s.setFont(new Font("SansSerif", Font.PLAIN, 12));
        s.setForeground(FG_SUB);
        left.add(t); left.add(s);

        // Quick metric pills in header
        JPanel pills = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pills.setOpaque(false);

        costLabel        = headerPill("Network Cost",  "$—",    C_TEAL);
        lossLabel        = headerPill("Loss/Hour",     "$—",    C_RED);
        highestLossLabel = headerPill("Highest Risk",  "—",     C_AMBER);

        pills.add(costLabel);
        pills.add(lossLabel);
        pills.add(highestLossLabel);

        header.add(left,  BorderLayout.WEST);
        header.add(pills, BorderLayout.EAST);
        return header;
    }

    // header pill — wraps the JLabel we expose as the field
    private JLabel headerPill(String title, String val, Color accent) {
        JLabel l = new JLabel("<html><center>"
                + "<span style='font-size:9px;color:#94A3B8'>" + title + "</span><br>"
                + "<span style='font-size:13px;font-weight:bold;color:white'>" + val + "</span>"
                + "</center></html>") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(accent.getRed(),accent.getGreen(),accent.getBlue(),40));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
                g2.setColor(new Color(accent.getRed(),accent.getGreen(),accent.getBlue(),100));
                g2.setStroke(new BasicStroke(0.5f));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,10,10);
                super.paintComponent(g);
            }
        };
        l.setOpaque(false);
        l.setBorder(new EmptyBorder(8, 16, 8, 16));
        l.setHorizontalAlignment(SwingConstants.CENTER);
        l.putClientProperty("accent", accent);
        return l;
    }

    // updates header pill text in-place
    private void updatePill(JLabel pill, String title, String val) {
        Color accent = (Color) pill.getClientProperty("accent");
        String accentHex = String.format("#%02X%02X%02X", accent.getRed(), accent.getGreen(), accent.getBlue());
        pill.setText("<html><center>"
                + "<span style='font-size:9px;color:#94A3B8'>" + title + "</span><br>"
                + "<span style='font-size:13px;color:white'><b>" + val + "</b></span>"
                + "</center></html>");
    }

    // ── Body ──────────────────────────────────────────────────────────────
    private JPanel buildBody() {
        JPanel body = new JPanel(new BorderLayout(0, 0));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(16, 16, 0, 16));

        // Tabs
        JTabbedPane tabs = buildTabs();
        body.add(tabs, BorderLayout.CENTER);
        return body;
    }

    // ── Tabbed pane ───────────────────────────────────────────────────────
    private JTabbedPane buildTabs() {
        JTabbedPane tabs = new JTabbedPane() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(BG_CARD);
                g.fillRect(0,0,getWidth(),getHeight());
                super.paintComponent(g);
            }
        };
        tabs.setOpaque(false);
        tabs.setFont(new Font("SansSerif", Font.BOLD, 12));
        tabs.setBackground(BG_CARD);
        tabs.setBorder(new LineBorder(BD_CARD, 1, true));

        tabs.addTab("Real-time Metrics", createMetricsTab());
        tabs.addTab("Supplier Risk",     createRiskTab());
        tabs.addTab("Alerts",            createAlertsTab());
        tabs.addTab("Supplier Search",   createSearchTab());

        return tabs;
    }

    // ── Metrics tab ───────────────────────────────────────────────────────
    private JPanel createMetricsTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(BG_CARD);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        metricsModel = new DefaultTableModel(new String[]{"Metric", "Value"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        metricsTable = styledTable(metricsModel);
        metricsTable.getColumnModel().getColumn(0).setPreferredWidth(320);
        metricsTable.getColumnModel().getColumn(1).setPreferredWidth(200);

        panel.add(tableCard(metricsTable), BorderLayout.CENTER);
        return panel;
    }

    // ── Risk tab ──────────────────────────────────────────────────────────
    private JPanel createRiskTab() {
        JPanel panel = new JPanel(new BorderLayout(0,0));
        panel.setBackground(BG_CARD);
        panel.setBorder(new EmptyBorder(16,16,16,16));

        riskModel = new DefaultTableModel(
                new String[]{"Supplier Name","Type","Health","Capacity","Risk Score","Blacklisted"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        riskTable = styledTable(riskModel);
        riskTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        riskTable.getColumnModel().getColumn(1).setPreferredWidth(110);
        riskTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        riskTable.getColumnModel().getColumn(3).setPreferredWidth(90);
        riskTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        riskTable.getColumnModel().getColumn(5).setPreferredWidth(110);

        // custom renderer to colour risk score and blacklist
        riskTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t,val,sel,foc,row,col);
                setBackground(sel ? new Color(0xE1F5EE) : row%2==0 ? ROW_ODD : ROW_EVEN);
                setForeground(FG_PRIMARY);
                setFont(new Font("SansSerif", col==0?Font.BOLD:Font.PLAIN, 12));
                setBorder(new EmptyBorder(0,col==0?12:0,0,0));
                setHorizontalAlignment(col==0 ? LEFT : CENTER);
                if (col==4 && val!=null) {
                    try {
                        double risk = Double.parseDouble(val.toString());
                        setForeground(risk>0.7 ? C_RED : risk>0.4 ? C_AMBER : C_GREEN);
                        setFont(new Font("SansSerif", Font.BOLD, 12));
                    } catch (NumberFormatException ignored) {}
                }
                if (col==5 && val!=null && val.toString().contains("YES"))
                    setForeground(C_RED);
                return this;
            }
        });

        panel.add(tableCard(riskTable), BorderLayout.CENTER);
        return panel;
    }

    // ── Alerts tab ────────────────────────────────────────────────────────
    private JPanel createAlertsTab() {
        JPanel panel = new JPanel(new BorderLayout(0,0));
        panel.setBackground(BG_CARD);
        panel.setBorder(new EmptyBorder(16,16,16,16));

        alertsModel = new DefaultTableModel(new String[]{"Alert Type","Details"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        alertsTable = styledTable(alertsModel);
        alertsTable.getColumnModel().getColumn(0).setPreferredWidth(140);
        alertsTable.getColumnModel().getColumn(1).setPreferredWidth(600);

        alertsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t,val,sel,foc,row,col);
                setBackground(sel ? new Color(0xE1F5EE) : row%2==0 ? ROW_ODD : ROW_EVEN);
                setFont(new Font("SansSerif", col==0?Font.BOLD:Font.PLAIN, 12));
                setBorder(new EmptyBorder(0,col==0?12:8,0,0));
                setHorizontalAlignment(col==0 ? CENTER : LEFT);
                if (col==0 && val!=null) {
                    String s = val.toString();
                    setForeground(s.equals("Health") ? C_RED : s.equals("Capacity") ? C_AMBER : C_GREEN);
                } else {
                    setForeground(FG_PRIMARY);
                }
                return this;
            }
        });

        panel.add(tableCard(alertsTable), BorderLayout.CENTER);
        return panel;
    }

    // ── Search tab ────────────────────────────────────────────────────────
    private JPanel createSearchTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(BG_CARD);
        panel.setBorder(new EmptyBorder(16,16,16,16));

        // Search bar card
        JPanel searchBar = new JPanel(new BorderLayout(10,0));
        searchBar.setBackground(BG_CARD);
        searchBar.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BD_FIELD,1,true),
                new EmptyBorder(10,14,10,14)));

        JLabel searchIcon = new JLabel("Search supplier:");
        searchIcon.setFont(new Font("SansSerif", Font.PLAIN, 12));
        searchIcon.setForeground(FG_MUTED);

        searchField = new JTextField();
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        searchField.setForeground(FG_PRIMARY);
        searchField.setBackground(new Color(0xF7F6F2));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BD_FIELD,1,true),
                new EmptyBorder(6,10,6,10)));
        searchField.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { performSearch(); }
        });

        searchBar.add(searchIcon,  BorderLayout.WEST);
        searchBar.add(searchField, BorderLayout.CENTER);

        // Results area
        searchResultsArea = new JTextArea();
        searchResultsArea.setEditable(false);
        searchResultsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        searchResultsArea.setForeground(FG_PRIMARY);
        searchResultsArea.setBackground(new Color(0xFAFAF8));
        searchResultsArea.setMargin(new Insets(14,16,14,16));

        JScrollPane scroll = new JScrollPane(searchResultsArea);
        scroll.setBorder(new LineBorder(BD_CARD,1,true));
        scroll.getViewport().setBackground(new Color(0xFAFAF8));

        panel.add(searchBar, BorderLayout.NORTH);
        panel.add(scroll,    BorderLayout.CENTER);
        return panel;
    }

    // ── Footer (action buttons) ───────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(BG_PAGE);
        footer.setBorder(new EmptyBorder(10, 16, 14, 16));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);

        refreshButton  = solidBtn("Refresh Metrics",  C_TEAL);
        blacklistButton = solidBtn("Add to Blacklist", C_RED);

        refreshButton.addActionListener(e  -> refreshDashboard());
        blacklistButton.addActionListener(e -> addToBlacklist());

        left.add(refreshButton);
        left.add(blacklistButton);

        JLabel hint = new JLabel("Data refreshes on demand · Click any row to inspect");
        hint.setFont(new Font("SansSerif", Font.PLAIN, 11));
        hint.setForeground(FG_LIGHT);

        footer.add(left, BorderLayout.WEST);
        footer.add(hint, BorderLayout.EAST);
        return footer;
    }

    // ── Helpers ───────────────────────────────────────────────────────────
    private JScrollPane tableCard(JTable table) {
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(BD_CARD, 1, true));
        scroll.getViewport().setBackground(BG_CARD);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        return scroll;
    }

    private JTable styledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.setForeground(FG_PRIMARY);
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0,0));
        table.setSelectionBackground(new Color(0xE1F5EE));
        table.setSelectionForeground(FG_PRIMARY);
        table.setBackground(BG_CARD);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 11));
        header.setBackground(TH_BG);
        header.setForeground(TH_FG);
        header.setBorder(new MatteBorder(0,0,1,0,BD_FIELD));
        header.setPreferredSize(new Dimension(0, 34));
        ((DefaultTableCellRenderer)header.getDefaultRenderer())
                .setHorizontalAlignment(JLabel.LEFT);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t,val,sel,foc,row,col);
                setBackground(sel ? new Color(0xE1F5EE) : row%2==0 ? ROW_ODD : ROW_EVEN);
                setForeground(FG_PRIMARY);
                setFont(new Font("SansSerif", col==0?Font.BOLD:Font.PLAIN, 12));
                setBorder(new EmptyBorder(0,12,0,0));
                return this;
            }
        });

        return table;
    }

    private JButton solidBtn(String text, Color accent) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? accent.darker() : accent);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8);
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 20, 8, 20));
        return btn;
    }

    // ── Original logic (unchanged) ────────────────────────────────────────
    private void refreshDashboard() {
        double totalCost    = analyticsEngine.calculateTotalNetworkCost();
        Set<Node> allNodes  = graph.getAllNodes();
        double lossPerHour  = analyticsEngine.calculateLossPerHour(allNodes);

        updatePill(costLabel,        "Network Cost",  String.format("$%.0f", totalCost));
        updatePill(lossLabel,        "Loss/Hour",     String.format("$%.0f", lossPerHour));

        MaxHeap.HeapNode highestLoss = analyticsEngine.getHighestLossNode();
        if (highestLoss != null)
            updatePill(highestLossLabel, "Highest Risk",
                    highestLoss.getNode().getName());

        updateMetricsTable(totalCost, lossPerHour);
        updateRiskTable();
        updateAlertsTable();

        costLabel.repaint();
        lossLabel.repaint();
        highestLossLabel.repaint();
    }

    private void updateMetricsTable(double totalCost, double lossPerHour) {
        metricsModel.setRowCount(0);
        metricsModel.addRow(new Object[]{"Total Network Cost",   String.format("$%.2f", totalCost)});
        metricsModel.addRow(new Object[]{"Estimated Loss/Hour",  String.format("$%.2f", lossPerHour)});
        metricsModel.addRow(new Object[]{"Total Nodes",          graph.getAllNodes().size()});
        metricsModel.addRow(new Object[]{"High Risk Suppliers",  analyticsEngine.getHighRiskSuppliers().size()});
        metricsModel.addRow(new Object[]{"Historical Total Loss",String.format("$%d", analyticsEngine.getTotalHistoricalLoss())});
    }

    private void updateRiskTable() {
        riskModel.setRowCount(0);
        for (SupplierManager.SupplierRiskInfo info : supplierManager.getAllSuppliersWithRisk()) {
            Node node = info.node;
            riskModel.addRow(new Object[]{
                    node.getName(), node.getType(),
                    node.getHealth() + "%", node.getCapacity(),
                    String.format("%.2f", info.riskScore),
                    info.isBlacklisted ? "🚫 YES" : "✓ No"
            });
        }
    }

    private void updateAlertsTable() {
        alertsModel.setRowCount(0);
        List<String> alerts = analyticsEngine.checkAlerts();
        for (String alert : alerts) {
            if (alert.contains("HEALTH"))
                alertsModel.addRow(new Object[]{"Health",   alert.substring(alert.indexOf(":")+1).trim()});
            else if (alert.contains("CAPACITY"))
                alertsModel.addRow(new Object[]{"Capacity", alert.substring(alert.indexOf(":")+1).trim()});
        }
        if (alerts.isEmpty())
            alertsModel.addRow(new Object[]{"Status", "✓ All systems normal"});
    }

    private void performSearch() {
        String prefix = searchField.getText();
        List<String> results = supplierManager.searchSupplier(prefix);
        StringBuilder sb = new StringBuilder();
        sb.append("Search results for: \"").append(prefix).append("\"\n");
        sb.append("─".repeat(50)).append("\n\n");
        if (results.isEmpty()) {
            sb.append("No suppliers found.");
        } else {
            for (String name : results) {
                Node node = supplierManager.getSupplierByName(name);
                if (node != null) {
                    double risk       = analyticsEngine.getSupplierRisk(node);
                    boolean blacklisted = supplierManager.isBlacklisted(name);
                    sb.append("▸ ").append(name).append("\n");
                    sb.append("   Type:       ").append(node.getType()).append("\n");
                    sb.append("   Health:     ").append(node.getHealth()).append("%\n");
                    sb.append("   Capacity:   ").append(node.getCapacity()).append("\n");
                    sb.append("   Risk Score: ").append(String.format("%.2f", risk)).append("\n");
                    if (blacklisted) sb.append("   ⚠  BLACKLISTED\n");
                    sb.append("\n");
                }
            }
        }
        searchResultsArea.setText(sb.toString());
        searchResultsArea.setCaretPosition(0);
    }

    private void addToBlacklist() {
        String name = JOptionPane.showInputDialog(this, "Enter supplier name to blacklist:");
        if (name != null && !name.isEmpty()) {
            supplierManager.addToBlacklist(name);
            JOptionPane.showMessageDialog(this, name + " added to blacklist!");
            refreshDashboard();
        }
    }
}