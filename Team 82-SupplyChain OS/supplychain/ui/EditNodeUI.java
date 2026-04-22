package com.supplychain.ui;

import com.supplychain.db.NodeDAO;
import com.supplychain.model.Node;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class EditNodeUI extends JFrame {

    JTextField idField, nameField, capacityField, healthField;
    JComboBox<String> typeBox;

    // ── Palette ──────────────────────────────────────────────────
    private static final Color BG_PAGE      = new Color(0xF0FAF6);
    private static final Color BG_CARD      = Color.WHITE;
    private static final Color BG_HEADER    = new Color(0x1D9E75);
    private static final Color BG_FIELD     = new Color(0xF1EFE8);
    private static final Color BORDER_FIELD = new Color(0xD3D1C7);
    private static final Color BORDER_CARD  = new Color(0xB4B2A9);
    private static final Color TEXT_PRIMARY = new Color(0x2C2C2A);
    private static final Color TEXT_MUTED   = new Color(0x5F5E5A);
    private static final Color TEXT_LABEL   = new Color(0x888780);
    private static final Color ACCENT       = new Color(0x1D9E75);
    private static final Color ACCENT_DARK  = new Color(0x0F6E56);
    private static final Color SUCCESS_BG   = new Color(0xE1F5EE);
    private static final Color SUCCESS_FG   = new Color(0x0F6E56);
    private static final Color ERROR_BG     = new Color(0xFCEBEB);
    private static final Color ERROR_FG     = new Color(0xA32D2D);

    private static final Color[] TYPE_COLORS = {
            new Color(0x1D9E75), // Supplier  – teal
            new Color(0x185FA5), // Factory   – blue
            new Color(0x534AB7), // Warehouse – purple
            new Color(0xBA7517)  // Retailer  – amber
    };

    private JPanel  headerPanel;
    private JLabel  toastLabel;
    private JPanel  meterPanel;
    private int     meterValue = 0;

    // ─────────────────────────────────────────────────────────────
    public EditNodeUI() {

        setTitle("Edit Supply Chain Node");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setUndecorated(false);
        setSize(540, 620);
        setLocationRelativeTo(null);
        setResizable(false);

        // Root – page background
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_PAGE);
        root.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        setContentPane(root);

        // ── Card ─────────────────────────────────────────────────
        JPanel card = new RoundedPanel(20, BG_CARD, BORDER_CARD);
        card.setLayout(new BorderLayout());
        root.add(card, BorderLayout.CENTER);

        // ── Header ───────────────────────────────────────────────
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BG_HEADER);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        headerPanel.setOpaque(true);

        JLabel titleLbl = new JLabel("Edit Supply Chain Node");
        titleLbl.setFont(new Font("SansSerif", Font.PLAIN, 17));
        titleLbl.setForeground(Color.WHITE);

        JLabel subLbl = new JLabel("Update node configuration and health metrics");
        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        subLbl.setForeground(new Color(255, 255, 255, 180));

        JPanel titleBlock = new JPanel(new GridLayout(2, 1, 0, 2));
        titleBlock.setOpaque(false);
        titleBlock.add(titleLbl);
        titleBlock.add(subLbl);
        headerPanel.add(titleBlock, BorderLayout.CENTER);

        // Badge row inside header
        JPanel badgeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        badgeRow.setOpaque(false);
        String[] types  = {"Supplier","Factory","Warehouse","Retailer"};
        Color[]  bgCols = {new Color(0xE1F5EE),new Color(0xE6F1FB),new Color(0xEEEDFE),new Color(0xFAEEDA)};
        Color[]  fgCols = {new Color(0x0F6E56),new Color(0x0C447C),new Color(0x3C3489),new Color(0x854F0B)};
        for (int i = 0; i < types.length; i++) {
            JLabel b = new JLabel(types[i]);
            b.setFont(new Font("SansSerif", Font.BOLD, 10));
            b.setForeground(fgCols[i]);
            b.setBackground(bgCols[i]);
            b.setOpaque(true);
            b.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(bgCols[i].darker(), 0),
                    BorderFactory.createEmptyBorder(3, 10, 3, 10)
            ));
        }

        JPanel headerWrap = new JPanel(new BorderLayout());
        headerWrap.setOpaque(false);
        headerWrap.add(headerPanel, BorderLayout.CENTER);
        card.add(headerWrap, BorderLayout.NORTH);

        // ── Form body ────────────────────────────────────────────
        JPanel body = new JPanel();
        body.setBackground(BG_CARD);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(BorderFactory.createEmptyBorder(22, 24, 24, 24));
        card.add(body, BorderLayout.CENTER);

        // Section: Node Identity
        body.add(sectionLabel("Node Identity"));
        body.add(Box.createVerticalStrut(10));

        JPanel row1 = new JPanel(new GridLayout(1, 2, 14, 0));
        row1.setOpaque(false);
        idField   = styledField("e.g. N-001");
        nameField = styledField("Node name");
        row1.add(fieldBlock("Node ID",   idField));
        row1.add(fieldBlock("Name",      nameField));
        row1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        body.add(row1);
        body.add(Box.createVerticalStrut(14));

        JPanel row2 = new JPanel(new GridLayout(1, 1));
        row2.setOpaque(false);
        typeBox = new JComboBox<>(new String[]{"Supplier","Factory","Warehouse","Retailer"});
        styleCombo(typeBox);
        row2.add(fieldBlock("Type", typeBox));
        row2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        body.add(row2);

        // Divider
        body.add(Box.createVerticalStrut(18));
        body.add(divider());
        body.add(Box.createVerticalStrut(18));

        // Section: Capacity & Health
        body.add(sectionLabel("Capacity & Health"));
        body.add(Box.createVerticalStrut(10));

        JPanel row3 = new JPanel(new GridLayout(1, 2, 14, 0));
        row3.setOpaque(false);
        capacityField = styledField("e.g. 500");
        healthField   = styledField("0 – 100");
        row3.add(fieldBlock("Capacity",      capacityField));
        row3.add(fieldBlock("Health (0–100)", healthField));
        row3.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        body.add(row3);

        // Health meter
        body.add(Box.createVerticalStrut(12));
        meterPanel = buildMeterPanel();
        meterPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        body.add(meterPanel);

        // Update button
        body.add(Box.createVerticalStrut(20));
        JButton updateBtn = new JButton("Update Node");
        styleButton(updateBtn);
        updateBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        body.add(updateBtn);

        // Toast
        body.add(Box.createVerticalStrut(12));
        toastLabel = new JLabel(" ");
        toastLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        toastLabel.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        toastLabel.setOpaque(false);
        toastLabel.setVisible(false);
        toastLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(toastLabel);

        // ── Listeners ────────────────────────────────────────────
        healthField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate (javax.swing.event.DocumentEvent e) { refreshMeter(); }
            public void removeUpdate (javax.swing.event.DocumentEvent e) { refreshMeter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { refreshMeter(); }
        });

        typeBox.addActionListener(e -> {
            int idx = typeBox.getSelectedIndex();
            headerPanel.setBackground(TYPE_COLORS[Math.max(0, idx)]);
            headerPanel.repaint();
        });

        updateBtn.addActionListener(e -> {
            try {
                String id       = idField.getText().trim();
                String name     = nameField.getText().trim();
                String type     = (String) typeBox.getSelectedItem();
                int    capacity = Integer.parseInt(capacityField.getText().trim());
                int    health   = Integer.parseInt(healthField.getText().trim());

                if (id.isEmpty() || name.isEmpty() || health < 0 || health > 100)
                    throw new IllegalArgumentException();

                Node    node = new Node(id, name, type, capacity, health);
                NodeDAO dao  = new NodeDAO();
                dao.updateNode(node);

                showToast("\u2713  Node " + id + " (" + name + ") updated successfully.", true);

            } catch (Exception ex) {
                showToast("\u26A0  Invalid input — please check all fields.", false);
            }
        });

        setVisible(true);
    }

    // ── Helpers ──────────────────────────────────────────────────

    private JLabel sectionLabel(String text) {
        JLabel lbl = new JLabel(text.toUpperCase());
        lbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        lbl.setForeground(TEXT_LABEL);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField styledField(String placeholder) {
        JTextField f = new JTextField();
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setForeground(TEXT_PRIMARY);
        f.setBackground(BG_FIELD);
        f.setCaretColor(ACCENT);
        f.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_FIELD, 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        f.putClientProperty("JTextField.placeholderText", placeholder);
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                f.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(ACCENT, 1, true),
                        BorderFactory.createEmptyBorder(6, 10, 6, 10)));
                f.setBackground(Color.WHITE);
            }
            public void focusLost(FocusEvent e) {
                f.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(BORDER_FIELD, 1, true),
                        BorderFactory.createEmptyBorder(6, 10, 6, 10)));
                f.setBackground(BG_FIELD);
            }
        });
        return f;
    }

    private void styleCombo(JComboBox<String> cb) {
        cb.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cb.setBackground(BG_FIELD);
        cb.setForeground(TEXT_PRIMARY);
        cb.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_FIELD, 1, true),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
    }

    private JPanel fieldBlock(String labelText, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setOpaque(false);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lbl.setForeground(TEXT_MUTED);
        p.add(lbl,   BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private JSeparator divider() {
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_FIELD);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }

    private JPanel buildMeterPanel() {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel("Health meter");
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lbl.setForeground(TEXT_MUTED);
        lbl.setPreferredSize(new Dimension(80, 20));
        p.add(lbl, BorderLayout.WEST);

        JPanel bar = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                // Track
                g2.setColor(BORDER_FIELD);
                g2.fillRoundRect(0, h/2-3, w, 6, 6, 6);
                // Fill
                int fill = (int)(w * (meterValue / 100.0));
                Color fc = meterValue > 70 ? new Color(0x1D9E75)
                        : meterValue > 40 ? new Color(0xBA7517)
                          : new Color(0xE24B4A);
                g2.setColor(fc);
                if (fill > 0) g2.fillRoundRect(0, h/2-3, fill, 6, 6, 6);
            }
        };
        bar.setOpaque(false);
        p.add(bar, BorderLayout.CENTER);

        JLabel pct = new JLabel("—");
        pct.setFont(new Font("SansSerif", Font.PLAIN, 11));
        pct.setForeground(TEXT_MUTED);
        pct.setName("pct");
        pct.setPreferredSize(new Dimension(32, 20));
        pct.setHorizontalAlignment(SwingConstants.RIGHT);
        p.add(pct, BorderLayout.EAST);

        return p;
    }

    private void refreshMeter() {
        try {
            int v = Integer.parseInt(healthField.getText().trim());
            meterValue = Math.max(0, Math.min(100, v));
        } catch (NumberFormatException e) {
            meterValue = 0;
        }
        // Update pct label
        for (Component c : meterPanel.getComponents()) {
            if (c instanceof JLabel && "pct".equals(c.getName())) {
                ((JLabel) c).setText(meterValue > 0 || !healthField.getText().isEmpty()
                        ? meterValue + "%" : "—");
            }
        }
        meterPanel.repaint();
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(ACCENT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(ACCENT_DARK); }
            public void mouseExited (MouseEvent e) { btn.setBackground(ACCENT); }
        });
    }

    private void showToast(String msg, boolean success) {
        toastLabel.setText(msg);
        toastLabel.setForeground(success ? SUCCESS_FG : ERROR_FG);
        toastLabel.setBackground(success ? SUCCESS_BG : ERROR_BG);
        toastLabel.setOpaque(true);
        toastLabel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(success ? new Color(0x9FE1CB) : new Color(0xF7C1C1), 1, true),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));
        toastLabel.setVisible(true);
        toastLabel.getParent().revalidate();
        toastLabel.getParent().repaint();
    }

    // ── Rounded card panel ───────────────────────────────────────
    static class RoundedPanel extends JPanel {
        private final int   radius;
        private final Color bg, border;
        RoundedPanel(int radius, Color bg, Color border) {
            super();
            this.radius = radius; this.bg = bg; this.border = border;
            setOpaque(false);
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, radius*2, radius*2);
            g2.setColor(border);
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius*2, radius*2);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}

