package com.supplychain.ui;

import com.supplychain.db.NodeDAO;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.RenderingHints;

public class DeleteNodeUI extends JFrame {

    JTextField idField;

    // ── Palette ──────────────────────────────────────────────────────────────
    private static final Color BG_TOP        = new Color(0xF0F4F8);
    private static final Color BG_BOTTOM     = new Color(0xE2EAF4);
    private static final Color CARD_BG       = new Color(0xFFFFFF);
    private static final Color CARD_BORDER   = new Color(0xD0DCF0);
    private static final Color ACCENT        = new Color(0x2563EB);   // supply-chain blue
    private static final Color ACCENT_HOVER  = new Color(0x1D4ED8);
    private static final Color DANGER        = new Color(0xDC2626);
    private static final Color DANGER_HOVER  = new Color(0xB91C1C);
    private static final Color TEXT_PRIMARY  = new Color(0x1E293B);
    private static final Color TEXT_MUTED    = new Color(0x64748B);
    private static final Color FIELD_BG      = new Color(0xF8FAFC);
    private static final Color FIELD_BORDER  = new Color(0xCBD5E1);
    private static final Color FIELD_FOCUS   = new Color(0x2563EB);
    private static final Color NODE_CHIP     = new Color(0xEFF6FF);
    private static final Color NODE_CHIP_TXT = new Color(0x1D4ED8);

    public DeleteNodeUI() {

        setTitle("Supply Chain Manager — Delete Node");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setUndecorated(false);

        // ── Root gradient panel ───────────────────────────────────────────────
        JPanel root = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0, BG_TOP,
                        0, getHeight(), BG_BOTTOM);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                drawNetworkDecor(g2, getWidth(), getHeight());
            }
        };
        root.setLayout(new GridBagLayout());
        setContentPane(root);

        // ── Centre card ───────────────────────────────────────────────────────
        JPanel card = createCard();
        card.setLayout(new BorderLayout(0, 0));
        card.setPreferredSize(new Dimension(480, 520));

        card.add(buildHeader(), BorderLayout.NORTH);
        card.add(buildBody(),   BorderLayout.CENTER);
        card.add(buildFooter(), BorderLayout.SOUTH);

        root.add(card);
        setVisible(true);
    }

    // ── Header ────────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel hdr = new JPanel();
        hdr.setOpaque(false);
        hdr.setLayout(new BoxLayout(hdr, BoxLayout.Y_AXIS));
        hdr.setBorder(new EmptyBorder(36, 40, 20, 40));

        // Icon badge
        JPanel badge = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xFEE2E2));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(DANGER);
                g2.setStroke(new BasicStroke(2f));
                int cx = getWidth() / 2, cy = getHeight() / 2, r = 9;
                g2.drawLine(cx - r, cy - r, cx + r, cy + r);
                g2.drawLine(cx + r, cy - r, cx - r, cy + r);
            }
            @Override public Dimension getPreferredSize() { return new Dimension(52, 52); }
        };
        badge.setOpaque(false);
        badge.setMaximumSize(badge.getPreferredSize());

        JLabel title = new JLabel("Delete Supply Node");
        title.setFont(loadFont("Georgia", Font.BOLD, 22));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Permanently remove a node from the supply chain network");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(TEXT_MUTED);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Breadcrumb chip row
        JPanel chips = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        chips.setOpaque(false);
        chips.setAlignmentX(Component.LEFT_ALIGNMENT);
        chips.add(chip("Supply Chain"));
        chips.add(chipArrow());
        chips.add(chip("Nodes"));
        chips.add(chipArrow());
        chips.add(chip("Delete", true));

        hdr.add(badge);
        hdr.add(Box.createVerticalStrut(14));
        hdr.add(chips);
        hdr.add(Box.createVerticalStrut(10));
        hdr.add(title);
        hdr.add(Box.createVerticalStrut(6));
        hdr.add(sub);
        return hdr;
    }

    // ── Body ──────────────────────────────────────────────────────────────────
    private JPanel buildBody() {
        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(0, 40, 24, 40));

        // Divider
        JSeparator sep = new JSeparator();
        sep.setForeground(CARD_BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        body.add(sep);
        body.add(Box.createVerticalStrut(24));

        // Label
        JLabel lbl = new JLabel("NODE  ID");
        lbl.setFont(new Font("Monospaced", Font.BOLD, 10));
        lbl.setForeground(TEXT_MUTED);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(lbl);
        body.add(Box.createVerticalStrut(8));

        // ── idField ── (name kept for backend)
        idField = new JTextField();
        idField.setFont(new Font("Monospaced", Font.PLAIN, 15));
        idField.setForeground(TEXT_PRIMARY);
        idField.setBackground(FIELD_BG);
        idField.setCaretColor(ACCENT);
        idField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        idField.setPreferredSize(new Dimension(400, 46));
        idField.setBorder(new CompoundBorder(
                new LineBorder(FIELD_BORDER, 1, true),
                new EmptyBorder(8, 14, 8, 14)));
        idField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Focus highlight
        idField.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                idField.setBorder(new CompoundBorder(
                        new LineBorder(FIELD_FOCUS, 2, true),
                        new EmptyBorder(7, 13, 7, 13)));
            }
            @Override public void focusLost(FocusEvent e) {
                idField.setBorder(new CompoundBorder(
                        new LineBorder(FIELD_BORDER, 1, true),
                        new EmptyBorder(8, 14, 8, 14)));
            }
        });
        body.add(idField);
        body.add(Box.createVerticalStrut(8));

        JLabel hint = new JLabel("Enter the unique identifier assigned to the node");
        hint.setFont(new Font("SansSerif", Font.PLAIN, 12));
        hint.setForeground(TEXT_MUTED);
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(hint);
        body.add(Box.createVerticalStrut(28));

        // Warning box
        JPanel warn = createWarningBox();
        warn.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(warn);

        return body;
    }

    // ── Footer ────────────────────────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        foot.setOpaque(false);
        foot.setBorder(new EmptyBorder(0, 40, 36, 40));

        // Cancel button
        JButton cancelBtn = styledButton("Cancel", CARD_BG, TEXT_MUTED,
                new Color(0xE2E8F0), new Color(0xCBD5E1), false);
        cancelBtn.addActionListener(e -> dispose());

        // ── deleteBtn ── (name kept for backend)
        JButton deleteBtn = styledButton("Delete Node", DANGER, Color.WHITE,
                DANGER, DANGER_HOVER, true);

        deleteBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            if (id.isEmpty()) {
                shakeField(idField);
                showToast("Please enter a Node ID before deleting.", false);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "<html><b>Delete node «" + id + "»?</b><br>"
                            + "This action cannot be undone and will remove<br>"
                            + "the node from the entire supply chain graph.</html>",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                NodeDAO dao = new NodeDAO();
                dao.deleteNode(id);
                showToast("Node " + id + " deleted successfully.", true);
                idField.setText("");
            }
        });

        foot.add(cancelBtn);
        foot.add(deleteBtn);
        return foot;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private JPanel createCard() {
        return new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                // Shadow
                for (int i = 12; i > 0; i--) {
                    float alpha = 0.018f * (12 - i + 1);
                    g2.setColor(new Color(0, 0, 0, (int)(alpha * 255)));
                    g2.fillRoundRect(i, i, getWidth() - i * 2,
                            getHeight() - i * 2, 24, 24);
                }
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth() - 12, getHeight() - 12, 20, 20);
                g2.setColor(CARD_BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 13, getHeight() - 13, 20, 20);
            }
        };
    }

    private JPanel createWarningBox() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(new Color(0xFFF7ED));
        p.setBorder(new CompoundBorder(
                new LineBorder(new Color(0xFED7AA), 1, true),
                new EmptyBorder(12, 16, 12, 16)));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel icon = new JLabel("⚠  Important");
        icon.setFont(new Font("SansSerif", Font.BOLD, 12));
        icon.setForeground(new Color(0xC2410C));
        icon.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel msg = new JLabel("<html>Deleting a node will cascade-remove all connected<br>"
                + "edges and route dependencies in the network.</html>");
        msg.setFont(new Font("SansSerif", Font.PLAIN, 12));
        msg.setForeground(new Color(0x9A3412));
        msg.setAlignmentX(Component.LEFT_ALIGNMENT);

        p.add(icon);
        p.add(Box.createVerticalStrut(4));
        p.add(msg);
        return p;
    }

    private JButton styledButton(String text, Color bg, Color fg,
                                 Color border, Color hover, boolean bold) {
        JButton btn = new JButton(text) {
            boolean hovered = false;
            { addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                public void mouseExited (MouseEvent e) { hovered = false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hovered ? hover : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(border);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("SansSerif", bold ? Font.BOLD : Font.PLAIN, 14));
        btn.setForeground(fg);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 42));
        return btn;
    }

    private JLabel chip(String text) { return chip(text, false); }
    private JLabel chip(String text, boolean active) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.PLAIN, 11));
        l.setForeground(active ? NODE_CHIP_TXT : TEXT_MUTED);
        l.setBackground(active ? NODE_CHIP : new Color(0xF1F5F9));
        l.setOpaque(true);
        l.setBorder(new CompoundBorder(
                new LineBorder(active ? new Color(0xBFDBFE) : new Color(0xE2E8F0), 1, true),
                new EmptyBorder(2, 8, 2, 8)));
        return l;
    }
    private JLabel chipArrow() {
        JLabel a = new JLabel("›");
        a.setFont(new Font("SansSerif", Font.PLAIN, 14));
        a.setForeground(TEXT_MUTED);
        return a;
    }

    private void shakeField(JTextField field) {
        Point origin = field.getLocation();
        Timer t = new Timer(30, null);
        int[] seq = {-6, 6, -4, 4, -2, 2, 0};
        final int[] idx = {0};
        t.addActionListener(e -> {
            if (idx[0] < seq.length) {
                field.setLocation(origin.x + seq[idx[0]++], origin.y);
            } else {
                field.setLocation(origin);
                t.stop();
            }
        });
        t.start();
    }

    private void showToast(String message, boolean success) {
        JOptionPane.showMessageDialog(
                this, message,
                success ? "Success" : "Validation Error",
                success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
    }

    private Font loadFont(String name, int style, float size) {
        return new Font(name, style, (int) size);
    }

    /** Draws faint network/graph decoration in the background */
    private void drawNetworkDecor(Graphics2D g2, int w, int h) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int[][] nodes = {{80,60},{w-100,80},{60,h-90},{w-80,h-70},
                {w/2,30},{30,h/2},{w-40,h/2}};
        g2.setColor(new Color(0xBFD7F0));
        g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                0, new float[]{4, 6}, 0));
        int[][] edges = {{0,4},{1,4},{2,5},{3,6},{4,5},{4,6},{0,5},{1,6}};
        for (int[] e : edges) {
            g2.drawLine(nodes[e[0]][0], nodes[e[0]][1],
                    nodes[e[1]][0], nodes[e[1]][1]);
        }
        g2.setStroke(new BasicStroke(1f));
        for (int[] n : nodes) {
            g2.setColor(new Color(0xBFD7F0));
            g2.fillOval(n[0]-6, n[1]-6, 12, 12);
            g2.setColor(new Color(0x93C5FD));
            g2.fillOval(n[0]-3, n[1]-3, 6, 6);
        }
    }
}

