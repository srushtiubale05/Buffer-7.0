package com.supplychain.ui;

import com.supplychain.model.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import com.supplychain.db.NodeDAO;

public class AddNodeUI extends JFrame {

    private JTextField idField, nameField, capacityField, healthField;
    private JComboBox<String> typeBox;
    private Graph graph;

    // ── Palette ──────────────────────────────────────────────────────────────
    private static final Color BG_TOP        = new Color(0xF0F7FF);
    private static final Color BG_BOT        = new Color(0xE8F0FE);
    private static final Color CARD_BG       = new Color(0xFFFFFF);
    private static final Color ACCENT        = new Color(0x2563EB);
    private static final Color ACCENT_LIGHT  = new Color(0xDBEAFE);
    private static final Color TEXT_DARK     = new Color(0x1E293B);
    private static final Color TEXT_MID      = new Color(0x475569);
    private static final Color TEXT_LIGHT    = new Color(0x94A3B8);
    private static final Color INPUT_BG      = new Color(0xF8FAFC);
    private static final Color INPUT_BORDER  = new Color(0xCBD5E1);
    private static final Color SUCCESS_GREEN = new Color(0x16A34A);
    private static final Color NODE_SUPPLIER = new Color(0xEFF6FF);
    private static final Color NODE_FACTORY  = new Color(0xF0FDF4);
    private static final Color NODE_WAREHOUSE= new Color(0xFFFBEB);
    private static final Color NODE_RETAILER = new Color(0xFDF2F8);

    public AddNodeUI(Graph graph) {
        this.graph = graph;

        setTitle("Supply Chain · Add Node");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setUndecorated(false);

        // ── Root panel with gradient background ──────────────────────────────
        GradientPanel root = new GradientPanel(BG_TOP, BG_BOT);
        root.setLayout(new BorderLayout());
        setContentPane(root);

        // ── Top nav bar ──────────────────────────────────────────────────────
        JPanel navbar = createNavBar();
        root.add(navbar, BorderLayout.NORTH);

        // ── Center: side panel + card ─────────────────────────────────────
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        root.add(center, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.28;
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        center.add(createSidePanel(), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.72;
        gbc.insets = new Insets(40, 0, 40, 60);
        center.add(createFormArea(), gbc);

        // ── Status bar ───────────────────────────────────────────────────────
        root.add(createStatusBar(), BorderLayout.SOUTH);

        setVisible(true);
    }

    // ═════════════════════════════════════════════════════════════════════════
    // NAV BAR
    // ═════════════════════════════════════════════════════════════════════════
    private JPanel createNavBar() {
        JPanel nav = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(0xE2E8F0));
                g2.fillRect(0, getHeight() - 1, getWidth(), 1);
            }
        };
        nav.setOpaque(false);
        nav.setPreferredSize(new Dimension(0, 64));
        nav.setBorder(new EmptyBorder(0, 32, 0, 32));

        // Logo + breadcrumb
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);

        JLabel logo = new JLabel("⬡ SupplyChain");
        logo.setFont(new Font("Georgia", Font.BOLD, 20));
        logo.setForeground(ACCENT);
        logo.setBorder(new EmptyBorder(0, 0, 0, 20));

        JLabel sep = new JLabel("  /  ");
        sep.setForeground(TEXT_LIGHT);
        sep.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JLabel crumb = new JLabel("Add Node");
        crumb.setFont(new Font("SansSerif", Font.PLAIN, 14));
        crumb.setForeground(TEXT_MID);

        left.add(logo);
        left.add(sep);
        left.add(crumb);

        // Right actions
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);

        JLabel userTag = new JLabel("Admin  ●");
        userTag.setFont(new Font("SansSerif", Font.PLAIN, 13));
        userTag.setForeground(SUCCESS_GREEN);
        right.add(userTag);

        nav.add(left, BorderLayout.WEST);
        nav.add(right, BorderLayout.EAST);

        // Vertical centering wrapper
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setPreferredSize(new Dimension(0, 64));
        wrap.add(nav, BorderLayout.CENTER);
        return wrap;
    }

    // ═════════════════════════════════════════════════════════════════════════
    // SIDE PANEL  (node-type legend / info)
    // ═════════════════════════════════════════════════════════════════════════
    private JPanel createSidePanel() {
        JPanel side = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 220));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.setColor(new Color(0xE2E8F0));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
                g2.dispose();
            }
        };
        side.setOpaque(false);
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(new EmptyBorder(40, 32, 40, 32));

        // Icon graphic (chain links, purely drawn)
        JPanel chainIcon = createChainGraphic();
        chainIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        side.add(chainIcon);
        side.add(Box.createVerticalStrut(28));

        JLabel title = styledLabel("Node Types", new Font("Georgia", Font.BOLD, 17), TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        side.add(title);
        side.add(Box.createVerticalStrut(6));

        JLabel subtitle = styledLabel("Each node represents a stage", new Font("SansSerif", Font.PLAIN, 12), TEXT_LIGHT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        side.add(subtitle);
        side.add(Box.createVerticalStrut(4));
        JLabel subtitle2 = styledLabel("in the supply chain network", new Font("SansSerif", Font.PLAIN, 12), TEXT_LIGHT);
        subtitle2.setAlignmentX(Component.CENTER_ALIGNMENT);
        side.add(subtitle2);

        side.add(Box.createVerticalStrut(28));
        side.add(new JSeparator() {{ setMaximumSize(new Dimension(Integer.MAX_VALUE, 1)); setForeground(new Color(0xE2E8F0)); }});
        side.add(Box.createVerticalStrut(24));

        String[][] nodeInfo = {
                {"Supplier",   "🔵", "Raw material source",    "#DBEAFE"},
                {"Factory",    "🟢", "Manufacturing plant",    "#DCFCE7"},
                {"Warehouse",  "🟡", "Storage & distribution", "#FEF9C3"},
                {"Retailer",   "🩷", "End-point of sale",      "#FCE7F3"},
        };

        for (String[] info : nodeInfo) {
            side.add(createNodeTypeCard(info[0], info[1], info[2]));
            side.add(Box.createVerticalStrut(12));
        }

        side.add(Box.createVerticalGlue());

        JLabel tip = styledLabel("💡  Health: 0–100  ·  Capacity: units", new Font("SansSerif", Font.ITALIC, 11), TEXT_LIGHT);
        tip.setAlignmentX(Component.CENTER_ALIGNMENT);
        side.add(tip);

        // Wrapper with margin
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setBorder(new EmptyBorder(40, 40, 40, 20));
        wrap.add(side, BorderLayout.CENTER);
        return wrap;
    }

    private JPanel createNodeTypeCard(String type, String emoji, String desc) {
        JPanel card = new JPanel(new BorderLayout(10, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(INPUT_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        card.setBorder(new EmptyBorder(10, 14, 10, 14));

        JLabel em = new JLabel(emoji);
        em.setFont(new Font("SansSerif", Font.PLAIN, 18));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel tl = new JLabel(type);
        tl.setFont(new Font("SansSerif", Font.BOLD, 13));
        tl.setForeground(TEXT_DARK);

        JLabel dl = new JLabel(desc);
        dl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        dl.setForeground(TEXT_LIGHT);

        text.add(tl);
        text.add(dl);

        card.add(em, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);
        return card;
    }

    // ═════════════════════════════════════════════════════════════════════════
    // FORM AREA
    // ═════════════════════════════════════════════════════════════════════════
    private JPanel createFormArea() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setOpaque(false);

        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Shadow
                for (int i = 6; i >= 0; i--) {
                    float alpha = 0.04f * (7 - i);
                    g2.setColor(new Color(0, 0, 0, (int)(alpha * 255)));
                    g2.fillRoundRect(i, i, getWidth() - i * 2, getHeight() - i * 2, 28, 28);
                }
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                // Top accent stripe
                GradientPaint gp = new GradientPaint(0, 0, ACCENT, getWidth(), 0, new Color(0x7C3AED));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), 6, 6, 6);
                g2.fillRect(0, 3, getWidth(), 3);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(40, 48, 40, 48));

        // Header
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel pageTitle = new JLabel("Add New Node");
        pageTitle.setFont(new Font("Georgia", Font.BOLD, 30));
        pageTitle.setForeground(TEXT_DARK);

        JLabel pageSub = new JLabel("Register a new node into the supply chain graph");
        pageSub.setFont(new Font("SansSerif", Font.PLAIN, 14));
        pageSub.setForeground(TEXT_MID);
        pageSub.setBorder(new EmptyBorder(6, 0, 0, 0));

        header.add(pageTitle);
        header.add(pageSub);
        card.add(header, BorderLayout.NORTH);

        // Form grid
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(new EmptyBorder(32, 0, 32, 0));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(10, 0, 10, 0);
        g.weightx = 1.0;

        // Row 0: ID + Name
        g.gridx = 0; g.gridy = 0; g.gridwidth = 1; g.weightx = 0.45;
        g.insets = new Insets(10, 0, 10, 16);
        form.add(createFieldBlock("Node ID", "e.g. N001", idField = fancyField()), g);

        g.gridx = 1; g.weightx = 0.55;
        g.insets = new Insets(10, 16, 10, 0);
        form.add(createFieldBlock("Node Name", "e.g. Mumbai Warehouse", nameField = fancyField()), g);

        // Row 1: Type (full width)
        g.gridx = 0; g.gridy = 1; g.gridwidth = 2; g.weightx = 1.0;
        g.insets = new Insets(10, 0, 10, 0);
        form.add(createTypeSelector(), g);

        // Row 2: Capacity + Health
        g.gridx = 0; g.gridy = 2; g.gridwidth = 1; g.weightx = 0.5;
        g.insets = new Insets(10, 0, 10, 16);
        form.add(createFieldBlock("Capacity", "Max units", capacityField = fancyField()), g);

        g.gridx = 1;
        g.insets = new Insets(10, 16, 10, 0);
        form.add(createFieldBlock("Health Score", "0 – 100", healthField = fancyField()), g);

        card.add(form, BorderLayout.CENTER);

        // Footer: button
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(8, 0, 0, 0));

        JButton addButton = createAddButton();
        footer.add(addButton, BorderLayout.CENTER);

        card.add(footer, BorderLayout.SOUTH);

        GridBagConstraints oc = new GridBagConstraints();
        oc.fill = GridBagConstraints.BOTH;
        oc.weightx = 1.0;
        oc.weighty = 1.0;
        outer.add(card, oc);

        return outer;
    }

    private JPanel createFieldBlock(String label, String placeholder, JTextField field) {
        JPanel block = new JPanel();
        block.setOpaque(false);
        block.setLayout(new BoxLayout(block, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(TEXT_MID);
        lbl.setBorder(new EmptyBorder(0, 4, 6, 0));

        field.putClientProperty("JTextField.placeholderText", placeholder);

        block.add(lbl);
        block.add(field);
        return block;
    }

    private JPanel createTypeSelector() {
        JPanel block = new JPanel();
        block.setOpaque(false);
        block.setLayout(new BoxLayout(block, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel("Node Type");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(TEXT_MID);
        lbl.setBorder(new EmptyBorder(0, 4, 6, 0));

        String[] types = {"Supplier", "Factory", "Warehouse", "Retailer"};
        typeBox = new JComboBox<>(types);
        typeBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        typeBox.setBackground(INPUT_BG);
        typeBox.setForeground(TEXT_DARK);
        typeBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        typeBox.setPreferredSize(new Dimension(Integer.MAX_VALUE, 48));
        typeBox.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(INPUT_BORDER, 1, true),
                new EmptyBorder(4, 12, 4, 12)
        ));
        ((JLabel) typeBox.getRenderer()).setHorizontalAlignment(SwingConstants.LEFT);

        // Color each item
        typeBox.setRenderer(new DefaultListCellRenderer() {
            private final Color[] rowColors = {
                    NODE_SUPPLIER, NODE_FACTORY, NODE_WAREHOUSE, NODE_RETAILER
            };
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel c = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                c.setFont(new Font("SansSerif", Font.PLAIN, 14));
                c.setBorder(new EmptyBorder(8, 14, 8, 14));
                if (!isSelected && index >= 0) c.setBackground(rowColors[index]);
                else if (!isSelected) c.setBackground(INPUT_BG);
                return c;
            }
        });

        block.add(lbl);
        block.add(typeBox);
        return block;
    }

    private JButton createAddButton() {
        JButton addButton = new JButton("Add Node to Supply Chain") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = getModel().isPressed()
                        ? new GradientPaint(0, 0, new Color(0x1D4ED8), getWidth(), 0, new Color(0x6D28D9))
                        : getModel().isRollover()
                          ? new GradientPaint(0, 0, new Color(0x3B82F6), getWidth(), 0, new Color(0x7C3AED))
                          : new GradientPaint(0, 0, ACCENT, getWidth(), 0, new Color(0x7C3AED));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        addButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        addButton.setForeground(Color.WHITE);
        addButton.setContentAreaFilled(false);
        addButton.setBorderPainted(false);
        addButton.setFocusPainted(false);
        addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addButton.setPreferredSize(new Dimension(0, 52));
        addButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id       = idField.getText().trim();
                String name     = nameField.getText().trim();
                String type     = (String) typeBox.getSelectedItem();
                int    capacity = Integer.parseInt(capacityField.getText().trim());
                int    health   = Integer.parseInt(healthField.getText().trim());

                Node node = new Node(id, name, type, capacity, health);

                graph.addNode(node);

                NodeDAO dao = new NodeDAO();
                dao.insertNode(node);

                showSuccessDialog(name, type);

                idField.setText("");
                nameField.setText("");
                capacityField.setText("");
                healthField.setText("");
            }
        });

        return addButton;
    }

    // ═════════════════════════════════════════════════════════════════════════
    // STATUS BAR
    // ═════════════════════════════════════════════════════════════════════════
    private JPanel createStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(0xF1F5F9));
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0xE2E8F0)),
                new EmptyBorder(8, 32, 8, 32)
        ));
        bar.setPreferredSize(new Dimension(0, 36));

        JLabel left = new JLabel("Supply Chain Management System  ·  v1.0");
        left.setFont(new Font("SansSerif", Font.PLAIN, 11));
        left.setForeground(TEXT_LIGHT);

        JLabel right = new JLabel("Graph persisted to database  ●");
        right.setFont(new Font("SansSerif", Font.PLAIN, 11));
        right.setForeground(SUCCESS_GREEN);

        bar.add(left, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    // ═════════════════════════════════════════════════════════════════════════
    // SUCCESS DIALOG (custom styled)
    // ═════════════════════════════════════════════════════════════════════════
    private void showSuccessDialog(String name, String type) {
        JDialog d = new JDialog(this, "Success", true);
        d.setUndecorated(true);
        d.setSize(360, 200);
        d.setLocationRelativeTo(this);

        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setPaint(new GradientPaint(0, 0, ACCENT, getWidth(), 0, new Color(0x7C3AED)));
                g2.fillRoundRect(0, 0, getWidth(), 6, 6, 6);
                g2.fillRect(0, 3, getWidth(), 3);
                g2.setColor(new Color(0xE2E8F0));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(28, 32, 24, 32));

        JLabel icon = new JLabel("✔  Node Added");
        icon.setFont(new Font("SansSerif", Font.BOLD, 18));
        icon.setForeground(SUCCESS_GREEN);
        icon.setAlignmentX(CENTER_ALIGNMENT);

        JLabel info = new JLabel(name + " (" + type + ") saved to graph.");
        info.setFont(new Font("SansSerif", Font.PLAIN, 13));
        info.setForeground(TEXT_MID);
        info.setAlignmentX(CENTER_ALIGNMENT);
        info.setBorder(new EmptyBorder(8, 0, 20, 0));

        JButton ok = new JButton("OK") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, ACCENT, getWidth(), 0, new Color(0x7C3AED)));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        ok.setFont(new Font("SansSerif", Font.BOLD, 13));
        ok.setForeground(Color.WHITE);
        ok.setContentAreaFilled(false);
        ok.setBorderPainted(false);
        ok.setFocusPainted(false);
        ok.setPreferredSize(new Dimension(120, 38));
        ok.setMaximumSize(new Dimension(120, 38));
        ok.setAlignmentX(CENTER_ALIGNMENT);
        ok.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        ok.addActionListener(ev -> d.dispose());

        p.add(icon);
        p.add(info);
        p.add(ok);

        d.setContentPane(p);
        d.setVisible(true);
    }

    // ═════════════════════════════════════════════════════════════════════════
    // HELPERS
    // ═════════════════════════════════════════════════════════════════════════
    private JTextField fancyField() {
        JTextField f = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(INPUT_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        f.setFont(new Font("SansSerif", Font.PLAIN, 14));
        f.setForeground(TEXT_DARK);
        f.setBackground(INPUT_BG);
        f.setOpaque(false);
        f.setPreferredSize(new Dimension(0, 48));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        f.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(INPUT_BORDER, 1, true),
                new EmptyBorder(4, 14, 4, 14)
        ));
        f.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                f.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(ACCENT, 2, true),
                        new EmptyBorder(3, 13, 3, 13)
                ));
            }
            @Override public void focusLost(FocusEvent e) {
                f.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(INPUT_BORDER, 1, true),
                        new EmptyBorder(4, 14, 4, 14)
                ));
            }
        });
        return f;
    }

    private JLabel styledLabel(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    // Chain graphic drawn with pure Java2D
    private JPanel createChainGraphic() {
        return new JPanel() {
            { setOpaque(false); setPreferredSize(new Dimension(200, 80)); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int[] cx = {24, 58, 92, 126, 160};
                Color[] cols = {new Color(0x3B82F6), new Color(0x10B981),
                        new Color(0xF59E0B), new Color(0xEC4899), new Color(0x8B5CF6)};
                // connector lines
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                for (int i = 0; i < cx.length - 1; i++) {
                    g2.setColor(new Color(0xCBD5E1));
                    g2.drawLine(cx[i] + 16, 40, cx[i + 1] - 16, 40);
                }
                // circles
                for (int i = 0; i < cx.length; i++) {
                    g2.setColor(cols[i]);
                    g2.fillOval(cx[i] - 16, 24, 32, 32);
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("SansSerif", Font.BOLD, 10));
                    FontMetrics fm = g2.getFontMetrics();
                    String[] lbs = {"S","F","W","R","D"};
                    g2.drawString(lbs[i], cx[i] - fm.stringWidth(lbs[i]) / 2, 40 + fm.getAscent() / 2 - 1);
                }
                g2.dispose();
            }
        };
    }

    // ═════════════════════════════════════════════════════════════════════════
    // GRADIENT BACKGROUND PANEL
    // ═════════════════════════════════════════════════════════════════════════
    static class GradientPanel extends JPanel {
        private final Color top, bot;
        GradientPanel(Color top, Color bot) {
            this.top = top; this.bot = bot;
            setOpaque(true);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setPaint(new GradientPaint(0, 0, top, 0, getHeight(), bot));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }
}


