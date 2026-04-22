package com.supplychain.ui;

import com.supplychain.model.Graph;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

public class MainMenuUI extends JFrame {

    // ── Palette ───────────────────────────────────────────────────────────
    private static final Color BG_TOP        = new Color(0xF0F7FF);
    private static final Color BG_BOT        = new Color(0xE8F4EC);
    private static final Color CARD_BG       = new Color(0xFFFFFF);
    private static final Color ACCENT_BLUE   = new Color(0x2563EB);
    private static final Color ACCENT_GREEN  = new Color(0x16A34A);
    private static final Color ACCENT_AMBER  = new Color(0xD97706);
    private static final Color ACCENT_RED    = new Color(0xDC2626);
    private static final Color ACCENT_VIOLET = new Color(0x7C3AED);
    private static final Color ACCENT_TEAL   = new Color(0x0D9488);
    private static final Color ACCENT_INDIGO = new Color(0x4338CA);
    private static final Color ACCENT_CYAN   = new Color(0x0891B2);
    private static final Color TEXT_DARK     = new Color(0x1E293B);
    private static final Color TEXT_MID      = new Color(0x475569);
    private static final Color TEXT_LIGHT    = new Color(0x94A3B8);
    private static final Color DIVIDER       = new Color(0xE2E8F0);

    public MainMenuUI(Graph graph) {
        setTitle("Supply Chain System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, BG_TOP, getWidth(), getHeight(), BG_BOT));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(37, 99, 235, 14));
                for (int x = 0; x < getWidth(); x += 36)
                    for (int y = 0; y < getHeight(); y += 36)
                        g2.fillOval(x-1, y-1, 3, 3);
            }
        };
        root.setOpaque(false);
        setContentPane(root);

        root.add(buildHeader(graph),         BorderLayout.NORTH);
        root.add(buildScrollBody(graph),     BorderLayout.CENTER);
        root.add(buildFooter(),              BorderLayout.SOUTH);

        setVisible(true);
    }

    // ── Navbar ────────────────────────────────────────────────────────────
    private JPanel buildHeader(Graph graph) {
        JPanel h = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(255, 255, 255, 220));
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(DIVIDER);
                g.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
            }
        };
        h.setOpaque(false);
        h.setBorder(new EmptyBorder(12, 48, 12, 48));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setOpaque(false);
        left.add(new JLabel(buildChainIcon()));
        JPanel titles = new JPanel(new GridLayout(2, 1, 0, 1));
        titles.setOpaque(false);
        JLabel t = new JLabel("SupplyChain OS");
        t.setFont(new Font("Georgia", Font.BOLD, 20));
        t.setForeground(TEXT_DARK);
        JLabel s = new JLabel("Graph-Powered Logistics Intelligence");
        s.setFont(new Font("SansSerif", Font.PLAIN, 11));
        s.setForeground(TEXT_MID);
        titles.add(t); titles.add(s);
        left.add(titles);

        // Right: Dashboard shortcut + status
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        JButton dashNav = navBtn("Analytics Dashboard");
        dashNav.addActionListener(e -> new DashboardUI(graph));
        right.add(dashNav);

        JLabel status = new JLabel("  ●  System Active  ");
        status.setFont(new Font("SansSerif", Font.BOLD, 11));
        status.setForeground(ACCENT_GREEN);
        status.setBackground(new Color(240, 253, 244));
        status.setOpaque(true);
        status.setBorder(new CompoundBorder(
                new LineBorder(new Color(187, 247, 208), 1, true),
                new EmptyBorder(5, 10, 5, 10)));
        right.add(status);

        h.add(left, BorderLayout.WEST);
        h.add(right, BorderLayout.EAST);
        return h;
    }

    // ── Scrollable body ───────────────────────────────────────────────────
    private JScrollPane buildScrollBody(Graph graph) {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);

        body.add(buildHero(graph));
        body.add(buildStatsRow(graph));
        body.add(buildDashboardBanner(graph));
        body.add(sectionHeader("Node Management",           "Add, edit, and remove nodes in your supply chain"));
        body.add(buildActionRow(graph, "node"));
        body.add(Box.createVerticalStrut(6));
        body.add(sectionHeader("Edge & Connectivity",       "Manage connections between supply chain nodes"));
        body.add(buildActionRow(graph, "edge"));
        body.add(Box.createVerticalStrut(6));
        body.add(sectionHeader("Visualisation & Analytics", "Explore and analyse your network"));
        body.add(buildActionRow(graph, "analytics"));
        body.add(Box.createVerticalStrut(48));

        JScrollPane scroll = new JScrollPane(body);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    // ── Hero ──────────────────────────────────────────────────────────────
    private JPanel buildHero(Graph graph) {
        JPanel hero = new JPanel(new BorderLayout(40, 0)) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, new Color(0x1E293B), getWidth(), getHeight(), new Color(0x0F4C81)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 255, 255, 8));
                g2.fillOval(getWidth()-280, -60, 360, 360);
                g2.fillOval(getWidth()-140, getHeight()-70, 180, 180);
                g2.setColor(new Color(37, 99, 235, 35));
                g2.fillOval(-50, getHeight()-100, 200, 200);
            }
        };
        hero.setOpaque(false);
        hero.setBorder(new EmptyBorder(48, 64, 48, 64));
        hero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel badge = new JLabel("  SUPPLY CHAIN MANAGEMENT SYSTEM  ");
        badge.setFont(new Font("SansSerif", Font.BOLD, 10));
        badge.setForeground(new Color(147, 197, 253));
        badge.setBackground(new Color(255, 255, 255, 18));
        badge.setOpaque(true);
        badge.setBorder(new CompoundBorder(
                new LineBorder(new Color(147, 197, 253, 55), 1, true),
                new EmptyBorder(4, 10, 4, 10)));
        badge.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel h1 = new JLabel("<html><div style='color:white;font-size:22px'>"
                + "Intelligent Logistics<br>Network Control</div></html>");
        h1.setFont(new Font("Georgia", Font.BOLD, 24));
        h1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("<html><div style='color:#94A3B8;font-size:12px'>"
                + "Manage suppliers, factories, warehouses and retailers<br>"
                + "through an interactive graph-based operations platform.</div></html>");
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnRow.setOpaque(false);
        btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton vBtn = heroBtn("Visualize Network",    ACCENT_BLUE, true);
        JButton rBtn = heroBtn("Recovery Planner",     null,        false);
        JButton dBtn = heroBtn("Open Dashboard  →",    new Color(0x7C3AED), true);

        vBtn.addActionListener(e -> new GraphVisualizerUI(graph));
        rBtn.addActionListener(e -> new RecoveryUI(graph));
        dBtn.addActionListener(e -> new DashboardUI(graph));

        btnRow.add(vBtn); btnRow.add(rBtn); btnRow.add(dBtn);

        left.add(badge);
        left.add(Box.createVerticalStrut(12));
        left.add(h1);
        left.add(Box.createVerticalStrut(10));
        left.add(sub);
        left.add(Box.createVerticalStrut(20));
        left.add(btnRow);

        // Mini network
        JPanel net = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                int[][] nodes = {{w/2-70,h/2-55},{w/2+55,h/2-70},{w/2-90,h/2+35},
                        {w/2+75,h/2+25},{w/2+5,h/2+75}};
                Color[] nc = {ACCENT_GREEN,ACCENT_BLUE,ACCENT_AMBER,ACCENT_TEAL,ACCENT_VIOLET};
                int[][] edges = {{0,1},{0,2},{1,3},{2,4},{3,4},{1,4}};
                g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                for (int[] e : edges) {
                    g2.setColor(new Color(255,255,255,25));
                    g2.drawLine(nodes[e[0]][0],nodes[e[0]][1],nodes[e[1]][0],nodes[e[1]][1]);
                }
                String[] labels = {"SUP","FAC","WH","RET","RET"};
                for (int i = 0; i < nodes.length; i++) {
                    int nx=nodes[i][0], ny=nodes[i][1];
                    g2.setColor(new Color(nc[i].getRed(),nc[i].getGreen(),nc[i].getBlue(),55));
                    g2.fillOval(nx-17,ny-17,34,34);
                    g2.setColor(nc[i]);
                    g2.fillOval(nx-9,ny-9,18,18);
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("SansSerif",Font.BOLD,7));
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(labels[i], nx-fm.stringWidth(labels[i])/2, ny+3);
                }
            }
        };
        net.setOpaque(false);
        net.setPreferredSize(new Dimension(260, 180));

        hero.add(left, BorderLayout.CENTER);
        hero.add(net,  BorderLayout.EAST);
        return hero;
    }

    // ── Stats row ─────────────────────────────────────────────────────────
    private JPanel buildStatsRow(Graph graph) {
        int nodes = graph.getAllNodes().size();
        int edges = 0, suppliers = 0, warehouses = 0;
        for (com.supplychain.model.Node n : graph.getAllNodes()) {
            edges += graph.getNeighbors(n).size();
            if ("Supplier".equals(n.getType()))  suppliers++;
            if ("Warehouse".equals(n.getType())) warehouses++;
        }

        JPanel row = new JPanel(new GridLayout(1, 4, 0, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 92));
        row.add(statCard("Total Nodes",  String.valueOf(nodes),      "Active in graph",  ACCENT_BLUE));
        row.add(statCard("Connections",  String.valueOf(edges),      "Edges mapped",     ACCENT_GREEN));
        row.add(statCard("Suppliers",    String.valueOf(suppliers),  "Source nodes",     ACCENT_AMBER));
        row.add(statCard("Warehouses",   String.valueOf(warehouses), "Storage nodes",    ACCENT_VIOLET));
        return row;
    }

    private JPanel statCard(String label, String value, String sub, Color accent) {
        JPanel card = new JPanel(new BorderLayout(12, 0)) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(255,255,255,190));
                g.fillRect(0,0,getWidth(),getHeight());
                g.setColor(DIVIDER);
                g.drawRect(0,0,getWidth()-1,getHeight()-1);
                g.setColor(accent);
                g.fillRect(0,0,4,getHeight());
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(14, 18, 14, 14));

        JLabel val = new JLabel(value);
        val.setFont(new Font("Georgia", Font.BOLD, 28));
        val.setForeground(accent);

        JPanel txt = new JPanel(new GridLayout(2,1,0,2));
        txt.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setForeground(TEXT_DARK);
        JLabel sl = new JLabel(sub);
        sl.setFont(new Font("SansSerif", Font.PLAIN, 10));
        sl.setForeground(TEXT_LIGHT);
        txt.add(lbl); txt.add(sl);

        card.add(val, BorderLayout.WEST);
        card.add(txt, BorderLayout.CENTER);
        return card;
    }

    // ── Dashboard banner ──────────────────────────────────────────────────
    private JPanel buildDashboardBanner(Graph graph) {
        JPanel banner = new JPanel(new BorderLayout(20, 0)) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, new Color(0x4C1D95), getWidth(), 0, new Color(0x1E3A5F)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                // decorative dots
                g2.setColor(new Color(255,255,255,12));
                for (int x = 0; x < getWidth(); x += 24)
                    for (int y = 0; y < getHeight(); y += 24)
                        g2.fillOval(x-1, y-1, 3, 3);
            }
        };
        banner.setOpaque(false);
        banner.setBorder(new EmptyBorder(18, 64, 18, 64));
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JPanel left = new JPanel(new GridLayout(2,1,0,3));
        left.setOpaque(false);
        JLabel title = new JLabel("Analytics Dashboard & Supplier Intelligence");
        title.setFont(new Font("Georgia", Font.BOLD, 15));
        title.setForeground(Color.WHITE);
        JLabel sub = new JLabel("Real-time metrics · Supplier risk scoring · Alerts · Blacklist management · Search");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 11));
        sub.setForeground(new Color(196, 181, 253));
        left.add(title); left.add(sub);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        JButton dashBtn = new JButton("Open Analytics Dashboard  →") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(0x6D28D9) : new Color(0x7C3AED));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
                super.paintComponent(g);
            }
        };
        dashBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        dashBtn.setForeground(Color.WHITE);
        dashBtn.setOpaque(false);
        dashBtn.setContentAreaFilled(false);
        dashBtn.setBorderPainted(false);
        dashBtn.setFocusPainted(false);
        dashBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        dashBtn.setBorder(new EmptyBorder(10, 22, 10, 22));
        dashBtn.addActionListener(e -> new DashboardUI(graph));
        right.add(dashBtn);

        banner.add(left,  BorderLayout.CENTER);
        banner.add(right, BorderLayout.EAST);
        return banner;
    }

    // ── Section header ────────────────────────────────────────────────────
    private JPanel sectionHeader(String title, String sub) {
        JPanel p = new JPanel(new BorderLayout(0, 3));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(22, 64, 8, 64));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Georgia", Font.BOLD, 15));
        t.setForeground(TEXT_DARK);
        JLabel s = new JLabel(sub);
        s.setFont(new Font("SansSerif", Font.PLAIN, 11));
        s.setForeground(TEXT_LIGHT);

        JPanel txt = new JPanel(new GridLayout(2,1,0,2));
        txt.setOpaque(false);
        txt.add(t); txt.add(s);

        JSeparator sep = new JSeparator();
        sep.setForeground(DIVIDER);
        p.add(txt, BorderLayout.CENTER);
        p.add(sep, BorderLayout.SOUTH);
        return p;
    }

    // ── Action row (small cards) ──────────────────────────────────────────
    private JPanel buildActionRow(Graph graph, String section) {
        JButton addNodeBtn      = makeCard("Add Node",        "Node",     "Register a new supply node",   ACCENT_BLUE);
        JButton editNodeBtn     = makeCard("Edit Node",       "Edit",     "Modify node attributes",       ACCENT_VIOLET);
        JButton deleteNodeBtn   = makeCard("Delete Node",     "Del Node", "Remove a node permanently",    ACCENT_AMBER);
        JButton addEdgeBtn      = makeCard("Add Edge",        "Edge",     "Connect two existing nodes",   ACCENT_GREEN);
        JButton deleteEdgeBtn   = makeCard("Delete Edge",     "Del Edge", "Disconnect two nodes",         ACCENT_RED);
        JButton viewGraphBtn    = makeCard("View Graph",      "View",     "Browse adjacency list",        ACCENT_INDIGO);
        JButton visualizeBtn    = makeCard("Visualize Graph", "Canvas",   "Interactive graph canvas",     ACCENT_TEAL);
        JButton recoveryEdgeBtn = makeCard("Recovery Planner","Recovery", "Find optimal recovery paths",  ACCENT_CYAN);

        editNodeBtn.addActionListener(e     -> new EditNodeUI());
        deleteNodeBtn.addActionListener(e   -> new DeleteNodeUI());
        deleteEdgeBtn.addActionListener(e   -> new DeleteEdgeUI());
        addNodeBtn.addActionListener(e      -> new AddNodeUI(graph));
        addEdgeBtn.addActionListener(e      -> new AddEdgeUI(graph));
        viewGraphBtn.addActionListener(e    -> new ViewGraphUI(graph));
        visualizeBtn.addActionListener(e    -> new GraphVisualizerUI(graph));
        recoveryEdgeBtn.addActionListener(e -> new RecoveryUI(graph));

        JPanel row = new JPanel(new GridLayout(1, 0, 14, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(0, 64, 0, 64));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        switch (section) {
            case "node":
                row.add(addNodeBtn); row.add(editNodeBtn); row.add(deleteNodeBtn);
                break;
            case "edge":
                row.add(addEdgeBtn); row.add(deleteEdgeBtn);
                break;
            case "analytics":
                row.add(viewGraphBtn); row.add(visualizeBtn); row.add(recoveryEdgeBtn);
                break;
        }
        return row;
    }

    // ── Hero button ───────────────────────────────────────────────────────
    private JButton heroBtn(String text, Color accent, boolean solid) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(solid ? accent : new Color(255,255,255,28));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(!solid);
        btn.setBorder(solid
                ? new EmptyBorder(9, 18, 9, 18)
                : BorderFactory.createCompoundBorder(
                new LineBorder(new Color(255,255,255,70), 1, true),
                new EmptyBorder(8, 17, 8, 17)));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ── Nav button ────────────────────────────────────────────────────────
    private JButton navBtn(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 11));
        btn.setForeground(ACCENT_VIOLET);
        btn.setBackground(new Color(237, 233, 254));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(196, 181, 253), 1, true),
                new EmptyBorder(5, 14, 5, 14)));
        return btn;
    }

    // ── Small card factory ────────────────────────────────────────────────
    private JButton makeCard(String tooltip, String label, String desc, Color accent) {
        JButton btn = new JButton() {
            private float hover = 0f;
            {
                setPreferredSize(new Dimension(160, 100));
                setMinimumSize(new Dimension(130, 88));
                setContentAreaFilled(false);
                setBorderPainted(false);
                setFocusPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setToolTipText(tooltip);
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { animate(true); }
                    @Override public void mouseExited (MouseEvent e) { animate(false); }
                    void animate(boolean in) {
                        Timer t = new Timer(16, null);
                        t.addActionListener(ev -> {
                            hover = in ? Math.min(1f,hover+0.1f) : Math.max(0f,hover-0.1f);
                            repaint();
                            if ((in&&hover>=1f)||(!in&&hover<=0f)) ((Timer)ev.getSource()).stop();
                        });
                        t.start();
                    }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w=getWidth(), h=getHeight(), arc=14;
                // shadow
                int sh=(int)(3+hover*6);
                for (int i=sh;i>0;i--) {
                    g2.setColor(new Color(0,0,0,(int)((0.035f*(1-(float)i/sh)+0.008f)*255)));
                    g2.fill(new RoundRectangle2D.Float(i/2f,i,w-i,h-i/2f,arc,arc));
                }
                // body
                Color bg = interpolate(CARD_BG, new Color(
                        clamp(CARD_BG.getRed()  +(accent.getRed()  -200)/7),
                        clamp(CARD_BG.getGreen()+(accent.getGreen()-200)/7),
                        clamp(CARD_BG.getBlue() +(accent.getBlue() -200)/7)), hover);
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0,0,w,h,arc,arc));
                // top stripe
                int strH=(int)(3+hover*2);
                g2.setColor(accent);
                g2.fill(new RoundRectangle2D.Float(0,0,w,strH+arc,arc,arc));
                g2.fillRect(0,strH,w,arc);
                // border
                g2.setColor(new Color(accent.getRed(),accent.getGreen(),accent.getBlue(),(int)(35+hover*70)));
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f,0.5f,w-1f,h-1f,arc,arc));
                // label
                g2.setFont(new Font("Georgia",Font.BOLD,13));
                g2.setColor(TEXT_DARK);
                FontMetrics fm=g2.getFontMetrics();
                g2.drawString(label,(w-fm.stringWidth(label))/2,h/2+3);
                // desc
                g2.setFont(new Font("SansSerif",Font.PLAIN,10));
                g2.setColor(TEXT_MID);
                FontMetrics fm2=g2.getFontMetrics();
                g2.drawString(desc,(w-fm2.stringWidth(desc))/2,h/2+18);
                // dot
                int dotR=(int)(4+hover*2);
                g2.setColor(new Color(accent.getRed(),accent.getGreen(),accent.getBlue(),160));
                g2.fillOval(w-dotR*2-6,h-dotR*2-6,dotR*2,dotR*2);
                g2.dispose();
            }
        };
        return btn;
    }

    // ── Footer ────────────────────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel f = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(255,255,255,160));
                g.fillRect(0,0,getWidth(),getHeight());
                g.setColor(DIVIDER);
                g.drawLine(0,0,getWidth(),0);
            }
        };
        f.setOpaque(false);
        f.setBorder(new EmptyBorder(10, 48, 10, 48));
        JLabel copy = new JLabel("© 2025 SupplyChain OS — Graph-Powered Logistics Intelligence");
        copy.setFont(new Font("SansSerif", Font.PLAIN, 11));
        copy.setForeground(TEXT_LIGHT);
        JLabel ver = new JLabel("v1.0.0");
        ver.setFont(new Font("SansSerif", Font.PLAIN, 11));
        ver.setForeground(TEXT_LIGHT);
        f.add(copy, BorderLayout.WEST);
        f.add(ver,  BorderLayout.EAST);
        return f;
    }

    // ── Chain icon ────────────────────────────────────────────────────────
    private ImageIcon buildChainIcon() {
        int sz = 40;
        BufferedImage img = new BufferedImage(sz, sz, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(37,99,235,30)); g.fillOval(0,0,sz,sz);
        g.setColor(ACCENT_BLUE); g.setStroke(new BasicStroke(2.2f));
        g.drawOval(1,1,sz-2,sz-2);
        int[] nx={9,20,31}, ny={28,11,28};
        g.setColor(new Color(37,99,235,120)); g.setStroke(new BasicStroke(1.8f));
        for (int i=0;i<2;i++) g.drawLine(nx[i],ny[i],nx[i+1],ny[i+1]);
        g.drawLine(nx[0],ny[0],nx[2],ny[2]);
        g.setColor(ACCENT_BLUE);
        for (int i=0;i<3;i++) g.fillOval(nx[i]-4,ny[i]-4,8,8);
        g.dispose();
        return new ImageIcon(img);
    }

    // ── Utilities ─────────────────────────────────────────────────────────
    private Color interpolate(Color a, Color b, float t) {
        return new Color(
                clamp((int)(a.getRed()  +(b.getRed()  -a.getRed())  *t)),
                clamp((int)(a.getGreen()+(b.getGreen()-a.getGreen())*t)),
                clamp((int)(a.getBlue() +(b.getBlue() -a.getBlue()) *t)));
    }
    private int clamp(int v) { return Math.max(0, Math.min(255, v)); }
}