package com.supplychain.ui;

import com.supplychain.db.EdgeDAO;
import com.supplychain.model.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.Set;

public class AddEdgeUI extends JFrame {
    private JComboBox<String> sourceBox, destBox;
    private JTextField costField, timeField, capacityField;
    private Graph graph;

    // ── Palette ────────────────────────────────────────────────────────────
    private static final Color BG_PAGE      = new Color(0xF4F7FB);
    private static final Color BG_CARD      = new Color(0xFFFFFF);
    private static final Color BG_HEADER    = new Color(0x1A3557);   // deep navy
    private static final Color ACCENT       = new Color(0x1D7FE8);   // vivid blue
    private static final Color ACCENT_LIGHT = new Color(0xE8F2FD);
    private static final Color BORDER_COL   = new Color(0xD5E3F5);
    private static final Color TEXT_PRIMARY = new Color(0x0F2944);
    private static final Color TEXT_LABEL   = new Color(0x5C7A9B);
    private static final Color SUCCESS_COL  = new Color(0x16A34A);
    private static final Color SUCCESS_BG   = new Color(0xDCFCE7);

    public AddEdgeUI(Graph graph) {
        this.graph = graph;

        setTitle("Supply Chain — Connect Nodes");
        setUndecorated(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);   // full screen
        setMinimumSize(new Dimension(900, 650));
        getContentPane().setBackground(BG_PAGE);
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        loadNodes();
        setVisible(true);
    }

    // ── HEADER ──────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // gradient background
                GradientPaint gp = new GradientPaint(0, 0, BG_HEADER,
                        getWidth(), getHeight(), new Color(0x0D2540));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // subtle dot grid
                g2.setColor(new Color(255, 255, 255, 18));
                for (int x = 0; x < getWidth(); x += 28)
                    for (int y = 0; y < getHeight(); y += 28)
                        g2.fillOval(x, y, 3, 3);
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(0, 110));
        header.setBorder(new EmptyBorder(0, 48, 0, 48));

        // left: icon + title stack
        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel chip = new JLabel("  ⬡  SUPPLY CHAIN MANAGER  ");
        chip.setFont(new Font("Monospaced", Font.BOLD, 10));
        chip.setForeground(new Color(0x93C5FD));
        chip.setOpaque(true);
        chip.setBackground(new Color(255, 255, 255, 30));
        chip.setBorder(new EmptyBorder(3, 8, 3, 8));

        JLabel title = new JLabel("Connect Supply Nodes");
        title.setFont(new Font("Georgia", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Define edges between nodes to build your logistics network");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(new Color(0xBAD4F5));

        left.add(Box.createVerticalGlue());
        left.add(chip);
        left.add(Box.createVerticalStrut(6));
        left.add(title);
        left.add(Box.createVerticalStrut(3));
        left.add(sub);
        left.add(Box.createVerticalGlue());

        // right: refresh button
        JButton refreshButton = makeIconButton("↻  Refresh Nodes", ACCENT, Color.WHITE);
        refreshButton.addActionListener(e -> {
            loadNodes();
            showToast("Node list refreshed!");
        });
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(refreshButton);

        header.add(left, BorderLayout.CENTER);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    // ── CENTER CARD ────────────────────────────────────────────────────────
    private JPanel buildCenter() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(40, 48, 20, 48));

        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                // top accent stripe
                g2.setColor(ACCENT);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), 5, 5, 5));
                // subtle inner shadow at top
                GradientPaint shadow = new GradientPaint(0, 5, new Color(0, 0, 0, 18),
                        0, 35, new Color(0, 0, 0, 0));
                g2.setPaint(shadow);
                g2.fillRect(0, 5, getWidth(), 30);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                new DropShadowBorder(),
                new EmptyBorder(36, 48, 40, 48)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 24, 0);

        // ── Section: Route ──
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        card.add(sectionLabel("Route Configuration"), gbc);

        gbc.gridwidth = 1; gbc.gridy++;
        gbc.gridx = 0; gbc.weightx = 0.5; gbc.insets = new Insets(0, 0, 24, 12);
        card.add(fieldGroup("Source Node", sourceBox = styledCombo()), gbc);

        gbc.gridx = 1; gbc.insets = new Insets(0, 12, 24, 0);
        card.add(fieldGroup("Destination Node", destBox = styledCombo()), gbc);

        // ── Section: Parameters ──
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        gbc.insets = new Insets(8, 0, 24, 0);
        card.add(sectionLabel("Edge Parameters"), gbc);

        gbc.gridwidth = 1; gbc.gridy++;
        gbc.gridx = 0; gbc.insets = new Insets(0, 0, 24, 12);
        costField = styledField("e.g.  1500.00");
        card.add(fieldGroup("Cost  (₹ / $)", costField), gbc);

        gbc.gridx = 1; gbc.insets = new Insets(0, 12, 24, 0);
        timeField = styledField("e.g.  48");
        card.add(fieldGroup("Transit Time  (hrs)", timeField), gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 32, 12);
        capacityField = styledField("e.g.  500");
        card.add(fieldGroup("Capacity  (units)", capacityField), gbc);

        // ── Add button ──
        gbc.gridx = 1; gbc.gridy++; gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 12, 0, 0);
        JButton addButton = makeIconButton("＋  Add Edge", ACCENT, Color.WHITE);
        addButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        addButton.setPreferredSize(new Dimension(0, 52));
        card.add(addButton, gbc);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sourceName = (String) sourceBox.getSelectedItem();
                String destName   = (String) destBox.getSelectedItem();
                Node source = graph.getNodeByName(sourceName);
                Node dest   = graph.getNodeByName(destName);
                double cost     = Double.parseDouble(costField.getText().trim());
                double time     = Double.parseDouble(timeField.getText().trim());
                int    capacity = Integer.parseInt(capacityField.getText().trim());
                graph.addEdge(source, dest, cost, time, capacity);
                EdgeDAO dao = new EdgeDAO();
                dao.insertEdge(source, dest, cost, time, capacity);
                showToast("Edge Added Successfully!  " + sourceName + " → " + destName);
                clearFields();
            }
        });

        GridBagConstraints wbc = new GridBagConstraints();
        wbc.fill = GridBagConstraints.BOTH;
        wbc.weightx = 1; wbc.weighty = 1;
        wrapper.add(card, wbc);
        return wrapper;
    }

    // ── FOOTER ──────────────────────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(8, 48, 20, 48));
        JLabel hint = new JLabel("Tip: Make sure both nodes exist before connecting them.");
        hint.setFont(new Font("SansSerif", Font.ITALIC, 12));
        hint.setForeground(TEXT_LABEL);
        footer.add(hint, BorderLayout.WEST);
        return footer;
    }

    // ── HELPERS ─────────────────────────────────────────────────────────────
    private JLabel sectionLabel(String text) {
        JLabel lbl = new JLabel(text.toUpperCase());
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setForeground(ACCENT);
        lbl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COL),
                new EmptyBorder(0, 0, 8, 0)));
        return lbl;
    }

    private JPanel fieldGroup(String label, JComponent field) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(TEXT_LABEL);
        lbl.setBorder(new EmptyBorder(0, 0, 6, 0));
        p.add(lbl);
        p.add(field);
        return p;
    }

    private JTextField styledField(String placeholder) {
        JTextField f = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    g2.setColor(new Color(0xB0BEC5));
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(placeholder, getInsets().left + 2,
                            (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                }
                g2.dispose();
            }
        };
        f.setOpaque(false);
        f.setBackground(ACCENT_LIGHT);
        f.setForeground(TEXT_PRIMARY);
        f.setFont(new Font("SansSerif", Font.PLAIN, 14));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COL, 1, true),
                new EmptyBorder(10, 14, 10, 14)));
        f.setPreferredSize(new Dimension(0, 46));
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                f.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(ACCENT, 2, true),
                        new EmptyBorder(9, 13, 9, 13)));
            }
            public void focusLost(FocusEvent e) {
                f.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COL, 1, true),
                        new EmptyBorder(10, 14, 10, 14)));
            }
        });
        return f;
    }

    private JComboBox<String> styledCombo() {
        JComboBox<String> cb = new JComboBox<>();
        cb.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cb.setForeground(TEXT_PRIMARY);
        cb.setBackground(ACCENT_LIGHT);
        cb.setBorder(BorderFactory.createLineBorder(BORDER_COL, 1));
        cb.setPreferredSize(new Dimension(0, 46));
        ((JLabel) cb.getRenderer()).setBorder(new EmptyBorder(0, 8, 0, 0));
        return cb;
    }

    private JButton makeIconButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color base = getModel().isPressed()  ? bg.darker()
                        : getModel().isRollover() ? bg.brighter()
                          : bg;
                g2.setColor(base);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                super.paintComponent(g);
                g2.dispose();
            }
        };
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setForeground(fg);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 44));
        btn.setFocusPainted(false);
        return btn;
    }

    private void showToast(String message) {
        JDialog toast = new JDialog(this, false);
        toast.setUndecorated(true);
        JLabel msg = new JLabel("  ✓  " + message + "  ");
        msg.setFont(new Font("SansSerif", Font.BOLD, 13));
        msg.setForeground(SUCCESS_COL);
        msg.setOpaque(true);
        msg.setBackground(SUCCESS_BG);
        msg.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x86EFAC), 1, true),
                new EmptyBorder(10, 16, 10, 16)));
        toast.add(msg);
        toast.pack();
        // position: top-center of this window
        Point loc = getLocationOnScreen();
        toast.setLocation(loc.x + getWidth() / 2 - toast.getWidth() / 2, loc.y + 120);
        toast.setVisible(true);
        new Timer(2500, ev -> toast.dispose()).start();
    }

    private void clearFields() {
        costField.setText("");
        timeField.setText("");
        capacityField.setText("");
    }

    private void loadNodes() {
        sourceBox.removeAllItems();
        destBox.removeAllItems();
        for (Node node : graph.getAllNodes()) {
            sourceBox.addItem(node.getName());
            destBox.addItem(node.getName());
        }
    }

    // ── Drop shadow border ──────────────────────────────────────────────────
    private static class DropShadowBorder extends AbstractBorder {
        private static final int S = 12;
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for (int i = 0; i < S; i++) {
                float alpha = 0.04f * (S - i);
                g2.setColor(new Color(0, 30, 80, (int)(alpha * 255)));
                g2.setStroke(new BasicStroke(i));
                g2.draw(new RoundRectangle2D.Float(x + i, y + i, w - i * 2, h - i * 2, 20, 20));
            }
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c) { return new Insets(S, S, S, S); }
    }
}

