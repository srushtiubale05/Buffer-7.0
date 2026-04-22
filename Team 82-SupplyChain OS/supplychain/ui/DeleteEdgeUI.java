package com.supplychain.ui;

import com.supplychain.db.EdgeDAO;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

public class DeleteEdgeUI extends JFrame {

    JTextField sourceField, destField;

    // ── Supply-chain palette (light) ──────────────────────────────────────────
    private static final Color BG_PAGE       = new Color(0xF4F7FB);   // soft ice-blue
    private static final Color BG_CARD       = new Color(0xFFFFFF);
    private static final Color ACCENT_TEAL   = new Color(0x0D9488);   // teal-600
    private static final Color ACCENT_CORAL  = new Color(0xEF4444);   // red-500 (delete)
    private static final Color ACCENT_CORAL_DARK = new Color(0xB91C1C);
    private static final Color TEXT_PRIMARY  = new Color(0x0F172A);
    private static final Color TEXT_MUTED    = new Color(0x64748B);
    private static final Color BORDER_LIGHT  = new Color(0xE2E8F0);
    private static final Color FIELD_BG      = new Color(0xF8FAFC);

    public DeleteEdgeUI() {
        setTitle("Supply Chain — Remove Connection");
        setUndecorated(true);                  // custom chrome
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // ── Root panel with gradient background ──────────────────────────────
        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(0xE0F2FE),
                        getWidth(), getHeight(), new Color(0xF0FDF4));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        root.setPreferredSize(new Dimension(520, 380));

        // ── Draggable title bar ───────────────────────────────────────────────
        JPanel titleBar = buildTitleBar();
        root.add(titleBar, BorderLayout.NORTH);

        // ── Centre card ──────────────────────────────────────────────────────
        JPanel card = buildCard();
        JPanel cardWrapper = new JPanel(new GridBagLayout());
        cardWrapper.setOpaque(false);
        cardWrapper.add(card);
        root.add(cardWrapper, BorderLayout.CENTER);

        // ── Footer strip ─────────────────────────────────────────────────────
        JPanel footer = buildFooter();
        root.add(footer, BorderLayout.SOUTH);

        setContentPane(root);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ── Title bar ─────────────────────────────────────────────────────────────
    private JPanel buildTitleBar() {
        JPanel bar = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ACCENT_TEAL);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // subtle diagonal stripe
                g2.setColor(new Color(255, 255, 255, 18));
                for (int x = -getHeight(); x < getWidth() + getHeight(); x += 28) {
                    int[] xs = {x, x + 14, x + 14 + getHeight(), x + getHeight()};
                    int[] ys = {getHeight(), getHeight(), 0, 0};
                    g2.fillPolygon(xs, ys, 4);
                }
                g2.dispose();
            }
        };
        bar.setPreferredSize(new Dimension(0, 56));
        bar.setOpaque(false);

        // Chain icon label
        JLabel icon = new JLabel("⛓");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        icon.setForeground(Color.WHITE);
        icon.setBorder(new EmptyBorder(0, 18, 0, 8));

        JLabel title = new JLabel("Delete Edge");
        title.setFont(new Font("Georgia", Font.BOLD, 17));
        title.setForeground(Color.WHITE);

        JLabel sub = new JLabel("  · Supply Chain Manager");
        sub.setFont(new Font("Georgia", Font.ITALIC, 12));
        sub.setForeground(new Color(255, 255, 255, 180));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);
        left.add(icon); left.add(title); left.add(sub);
        bar.add(left, BorderLayout.CENTER);

        // Close button
        JButton close = new JButton("✕");
        close.setFont(new Font("SansSerif", Font.BOLD, 13));
        close.setForeground(Color.WHITE);
        close.setContentAreaFilled(false);
        close.setBorderPainted(false);
        close.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        close.setBorder(new EmptyBorder(0, 0, 0, 16));
        close.addActionListener(e -> dispose());
        bar.add(close, BorderLayout.EAST);

        // Drag support
        Point[] origin = {null};
        bar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { origin[0] = e.getPoint(); }
        });
        bar.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point loc = getLocation();
                setLocation(loc.x + e.getX() - origin[0].x,
                        loc.y + e.getY() - origin[0].y);
            }
        });
        return bar;
    }

    // ── Centre form card ──────────────────────────────────────────────────────
    private JPanel buildCard() {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.setColor(BORDER_LIGHT);
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new RoundRectangle2D.Float(0.6f, 0.6f, getWidth()-1.2f, getHeight()-1.2f, 20, 20));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(420, 240));
        card.setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(10, 24, 6, 24);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;

        // ── Row 0: info banner ───────────────────────────────────────────────
        gc.gridx = 0; gc.gridy = 0; gc.gridwidth = 2;
        JPanel banner = buildBanner();
        card.add(banner, gc);

        // ── Row 1: Source label + field ──────────────────────────────────────
        gc.gridwidth = 1; gc.gridy = 1;
        gc.gridx = 0; gc.weightx = 0.38;
        card.add(styledLabel("Source Node ID"), gc);

        gc.gridx = 1; gc.weightx = 0.62;
        sourceField = styledField("e.g.  WH-01");
        card.add(sourceField, gc);

        // ── Row 2: Destination label + field ─────────────────────────────────
        gc.gridy = 2;
        gc.gridx = 0; gc.weightx = 0.38;
        card.add(styledLabel("Destination Node ID"), gc);

        gc.gridx = 1; gc.weightx = 0.62;
        destField = styledField("e.g.  DC-07");
        card.add(destField, gc);

        // ── Row 3: Delete button ──────────────────────────────────────────────
        gc.gridy = 3; gc.gridx = 0; gc.gridwidth = 2;
        gc.insets = new Insets(14, 24, 18, 24);
        JButton deleteBtn = buildDeleteButton();
        card.add(deleteBtn, gc);

        // Wire action
        deleteBtn.addActionListener(e -> {
            String source = sourceField.getText().trim();
            String dest   = destField.getText().trim();

            EdgeDAO dao = new EdgeDAO();
            dao.deleteEdge(source, dest);

            showSuccessDialog();
        });

        return card;
    }

    // ── Info banner ───────────────────────────────────────────────────────────
    private JPanel buildBanner() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xFFF1F2));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(new Color(0xFECACA));
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, 10, 10));
                g2.dispose();
            }
        };
        p.setOpaque(false);

        JLabel ico = new JLabel("⚠");
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        ico.setForeground(new Color(0xB91C1C));

        JLabel msg = new JLabel("<html><b>Caution:</b> This will permanently remove the supply chain link.</html>");
        msg.setFont(new Font("Tahoma", Font.PLAIN, 11));
        msg.setForeground(new Color(0x7F1D1D));

        p.add(ico); p.add(msg);
        return p;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private JLabel styledLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Tahoma", Font.BOLD, 12));
        lbl.setForeground(TEXT_PRIMARY);
        return lbl;
    }

    private JTextField styledField(String placeholder) {
        JTextField tf = new JTextField(14) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FIELD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    g2.setColor(new Color(0xCBD5E1));
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    g2.drawString(placeholder, 10, getHeight() / 2 + 5);
                }
                g2.dispose();
            }
            @Override protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isFocusOwner() ? ACCENT_TEAL : BORDER_LIGHT);
                g2.setStroke(new BasicStroke(isFocusOwner() ? 1.8f : 1.2f));
                g2.draw(new RoundRectangle2D.Float(0.9f, 0.9f, getWidth()-1.8f, getHeight()-1.8f, 10, 10));
                g2.dispose();
            }
        };
        tf.setOpaque(false);
        tf.setBorder(new EmptyBorder(8, 10, 8, 10));
        tf.setFont(new Font("Consolas", Font.PLAIN, 13));
        tf.setForeground(TEXT_PRIMARY);
        tf.setPreferredSize(new Dimension(0, 38));
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { tf.repaint(); }
            public void focusLost(FocusEvent e)   { tf.repaint(); }
        });
        return tf;
    }

    private JButton buildDeleteButton() {
        JButton btn = new JButton("Delete Connection") {
            private float hoverAlpha = 0f;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hoverAlpha = 1f; repaint(); }
                    public void mouseExited(MouseEvent e)  { hoverAlpha = 0f; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color base  = ACCENT_CORAL;
                Color hover = ACCENT_CORAL_DARK;
                Color mixed = blend(base, hover, hoverAlpha);
                g2.setColor(mixed);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                // icon
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
                g2.setColor(Color.WHITE);
                g2.drawString("🗑", 18, getHeight() / 2 + 6);
                // label
                g2.setFont(new Font("Tahoma", Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                String label = "Delete Connection";
                int tx = (getWidth() - fm.stringWidth(label)) / 2 + 10;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(label, tx, ty);
                g2.dispose();
            }
            @Override protected void paintBorder(Graphics g) {}
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 44));
        return btn;
    }

    private void showSuccessDialog() {
        JDialog dlg = new JDialog(this, true);
        dlg.setUndecorated(true);
        JPanel p = new JPanel(new BorderLayout(0, 10)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.setColor(BORDER_LIGHT);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, 16, 16));
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(24, 32, 20, 32));

        JLabel ico  = new JLabel("✅", SwingConstants.CENTER);
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        JLabel msg  = new JLabel("Connection Deleted", SwingConstants.CENTER);
        msg.setFont(new Font("Georgia", Font.BOLD, 16));
        msg.setForeground(ACCENT_TEAL);
        JLabel sub  = new JLabel("The supply chain edge has been removed.", SwingConstants.CENTER);
        sub.setFont(new Font("Tahoma", Font.PLAIN, 12));
        sub.setForeground(TEXT_MUTED);
        JButton ok  = buildSmallOkButton(dlg);

        p.add(ico,  BorderLayout.NORTH);
        p.add(msg,  BorderLayout.CENTER);
        JPanel bot = new JPanel(new BorderLayout(0, 6));
        bot.setOpaque(false);
        bot.add(sub, BorderLayout.NORTH);
        bot.add(ok,  BorderLayout.SOUTH);
        p.add(bot,  BorderLayout.SOUTH);

        dlg.setContentPane(p);
        dlg.pack();
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private JButton buildSmallOkButton(JDialog dlg) {
        JButton btn = new JButton("OK") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ACCENT_TEAL);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setFont(new Font("Tahoma", Font.BOLD, 12));
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("OK", (getWidth()-fm.stringWidth("OK"))/2, (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
            @Override protected void paintBorder(Graphics g) {}
        };
        btn.setPreferredSize(new Dimension(80, 34));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> dlg.dispose());
        return btn;
    }

    // ── Footer ────────────────────────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 6));
        p.setOpaque(false);
        JLabel lbl = new JLabel("Supply Chain Management System  ·  Edge Module");
        lbl.setFont(new Font("Tahoma", Font.PLAIN, 10));
        lbl.setForeground(new Color(0x94A3B8));
        p.add(lbl);
        p.setPreferredSize(new Dimension(0, 30));
        return p;
    }

    // ── Utility ───────────────────────────────────────────────────────────────
    private static Color blend(Color a, Color b, float t) {
        return new Color(
                (int)(a.getRed()   + (b.getRed()   - a.getRed())   * t),
                (int)(a.getGreen() + (b.getGreen() - a.getGreen()) * t),
                (int)(a.getBlue()  + (b.getBlue()  - a.getBlue())  * t)
        );
    }
}

