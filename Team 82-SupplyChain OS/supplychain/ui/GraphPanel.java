package com.supplychain.ui;

import com.supplychain.model.*;
import com.supplychain.logic.*;

import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.RenderingHints;
import java.util.*;

public class GraphPanel extends JPanel {

    private Graph graph;
    private Map<Node, Point> nodePositions;

    private Set<Node> affectedNodes   = new HashSet<>();
    private Set<Node> bottleneckNodes = new HashSet<>();
    private Set<Node> criticalNodes   = new HashSet<>();

    private Node selectedNode = null;
    private Node hoveredNode  = null;
    private Point mousePoint  = null;

    // ── Palette ──────────────────────────────────────────────────────────────
    private static final Color BG           = new Color(0xF1EFE8);
    private static final Color EDGE_COLOR   = new Color(0xB4B2A9);
    private static final Color EDGE_LABEL   = new Color(0x888780);
    private static final Color NODE_TEXT    = Color.WHITE;
    private static final Color NODE_BORDER  = new Color(0xFFFFFF, true);
    private static final Color SUBTEXT      = new Color(0xD3D1C7);

    private static final Color C_SUPPLIER   = new Color(0x1D9E75);
    private static final Color C_FACTORY    = new Color(0x185FA5);
    private static final Color C_WAREHOUSE  = new Color(0x534AB7);
    private static final Color C_RETAILER   = new Color(0xBA7517);

    private static final Color C_AFFECTED   = new Color(0xE24B4A);
    private static final Color C_BOTTLENECK = new Color(0xEF9F27);
    private static final Color C_CRITICAL   = new Color(0x2C2C2A);

    private static final Color LEGEND_BG    = new Color(0xFFFFFF);
    private static final Color LEGEND_BORDER= new Color(0xD3D1C7);

    private static final int   NODE_RADIUS  = 28;
    private static final Font  FONT_NODE    = new Font("Segoe UI", Font.BOLD,   11);
    private static final Font  FONT_TYPE    = new Font("Segoe UI", Font.PLAIN,  10);
    private static final Font  FONT_EDGE    = new Font("Segoe UI", Font.PLAIN,  11);
    private static final Font  FONT_LEGEND  = new Font("Segoe UI", Font.PLAIN,  12);
    private static final Font  FONT_LEG_HDR = new Font("Segoe UI", Font.BOLD,   11);

    // ── Constructor ──────────────────────────────────────────────────────────
    public GraphPanel(Graph graph) {
        this.graph = graph;
        this.nodePositions = new HashMap<>();
        generatePositions();
        setBackground(BG);

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                selectedNode = getClickedNode(e.getPoint());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                selectedNode = null;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                Node clickedNode = getClickedNode(e.getPoint());

                if (clickedNode != null) {
                    showFailureDialog(clickedNode);
                } else {
                    handleEdgeClick(e.getPoint());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hoveredNode = null;
                mousePoint  = null;
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedNode != null) {
                    nodePositions.put(selectedNode, e.getPoint());
                    repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                Node n = getClickedNode(e.getPoint());
                hoveredNode = n;
                mousePoint  = e.getPoint();
                setCursor(n != null
                        ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
                        : Cursor.getDefaultCursor());
                repaint();
            }
        });
    }

    // ── Failure dialog (replaces raw JOptionPane) ─────────────────────────
    private void showFailureDialog(Node clickedNode) {
        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Simulate Node Failure", true);
        dialog.setUndecorated(false);
        dialog.setSize(340, 180);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(0, 0));

        JPanel body = new JPanel(new BorderLayout(0, 12));
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(24, 28, 20, 28));

        JLabel title = new JLabel("Simulate failure of " + clickedNode.getName() + "?");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(new Color(0x2C2C2A));

        JLabel sub = new JLabel("Affected, bottleneck and critical nodes will be highlighted.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(new Color(0x888780));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        textPanel.setBackground(Color.WHITE);
        textPanel.add(title);
        textPanel.add(sub);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setBackground(Color.WHITE);

        JButton cancelBtn = styledButton("Cancel", new Color(0xF1EFE8), new Color(0x5F5E5A));
        JButton simBtn    = styledButton("Simulate failure", new Color(0xE24B4A), Color.WHITE);

        final int[] choice = {JOptionPane.NO_OPTION};

        cancelBtn.addActionListener(ev -> dialog.dispose());
        simBtn.addActionListener(ev -> {
            choice[0] = JOptionPane.YES_OPTION;
            dialog.dispose();
        });

        btnPanel.add(cancelBtn);
        btnPanel.add(simBtn);

        body.add(textPanel, BorderLayout.CENTER);
        body.add(btnPanel,  BorderLayout.SOUTH);
        dialog.add(body);
        dialog.setVisible(true);

        if (choice[0] == JOptionPane.YES_OPTION) {
            DisruptionSimulator simulator = new DisruptionSimulator(graph);
            BottleneckAnalyzer  analyzer  = new BottleneckAnalyzer(graph);

            List<Node> affected    = simulator.simulateFailure(clickedNode);
            List<Node> bottlenecks = analyzer.findBottlenecks();
            List<Node> critical    = analyzer.findSinglePointsOfFailure();

            showSimulation(
                    new HashSet<>(affected),
                    new HashSet<>(bottlenecks),
                    new HashSet<>(critical));
        }
    }

    // ── Button helper ─────────────────────────────────────────────────────
    private JButton styledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(bg.darker(), 1, true),
                new EmptyBorder(7, 18, 7, 18)));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ── Positions ─────────────────────────────────────────────────────────
    private void generatePositions() {
        int x = 120, y = 120;
        for (Node node : graph.getAllNodes()) {
            nodePositions.put(node, new Point(x, y));
            x += 180;
            if (x > 700) { x = 120; y += 180; }
        }
    }

    // ── Paint ─────────────────────────────────────────────────────────────
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,    RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawEdges(g2);
        drawNodes(g2);
        drawLegend(g2);
        drawTooltip(g2);
    }

    // ── Draw edges ────────────────────────────────────────────────────────
    private void drawEdges(Graphics2D g2) {
        g2.setFont(FONT_EDGE);
        for (Node node : graph.getAllNodes()) {
            Point p1 = nodePositions.get(node);
            for (Edge edge : graph.getNeighbors(node)) {
                Point p2 = nodePositions.get(edge.getDestination());
                if (p1 == null || p2 == null) continue;

                g2.setColor(EDGE_COLOR);
                g2.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                drawArrow(g2, p1, p2);

                int midX = (p1.x + p2.x) / 2;
                int midY = (p1.y + p2.y) / 2;

                String label = "C:" + edge.getCost();
                FontMetrics fm = g2.getFontMetrics();
                int lw = fm.stringWidth(label);

                g2.setColor(new Color(255, 255, 255, 210));
                g2.fillRoundRect(midX - lw / 2 - 4, midY - 9, lw + 8, 16, 6, 6);

                g2.setColor(EDGE_LABEL);
                g2.drawString(label, midX - lw / 2, midY + 3);
            }
        }
        g2.setStroke(new BasicStroke(1f));
    }

    // ── Draw nodes ────────────────────────────────────────────────────────
    private void drawNodes(Graphics2D g2) {
        for (Node node : graph.getAllNodes()) {
            Point p = nodePositions.get(node);
            if (p == null) continue;

            Color fill = getColor(node);

            // shadow / glow ring for critical nodes
            if (criticalNodes.contains(node)) {
                g2.setColor(new Color(0xEF9F27));
                g2.setStroke(new BasicStroke(3f));
                g2.drawOval(p.x - NODE_RADIUS - 4, p.y - NODE_RADIUS - 4,
                        (NODE_RADIUS + 4) * 2, (NODE_RADIUS + 4) * 2);
            }

            // filled circle
            g2.setColor(fill);
            g2.fillOval(p.x - NODE_RADIUS, p.y - NODE_RADIUS,
                    NODE_RADIUS * 2, NODE_RADIUS * 2);

            // inner border (white 40% alpha)
            g2.setColor(new Color(255, 255, 255, 100));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawOval(p.x - NODE_RADIUS, p.y - NODE_RADIUS,
                    NODE_RADIUS * 2, NODE_RADIUS * 2);

            g2.setStroke(new BasicStroke(1f));

            // node name — split long names
            g2.setFont(FONT_NODE);
            g2.setColor(NODE_TEXT);
            FontMetrics fm = g2.getFontMetrics();
            String[] words = node.getName().split(" ");
            if (words.length == 1) {
                int sw = fm.stringWidth(node.getName());
                g2.drawString(node.getName(), p.x - sw / 2, p.y + fm.getAscent() / 2 - 1);
            } else {
                String line1 = words[0];
                String line2 = String.join(" ", Arrays.copyOfRange(words, 1, words.length));
                int sw1 = fm.stringWidth(line1), sw2 = fm.stringWidth(line2);
                g2.drawString(line1, p.x - sw1 / 2, p.y - 3);
                g2.drawString(line2, p.x - sw2 / 2, p.y + fm.getHeight() - 3);
            }

            // type label below node
            g2.setFont(FONT_TYPE);
            g2.setColor(new Color(0x5F5E5A));
            FontMetrics fmT = g2.getFontMetrics();
            int tw = fmT.stringWidth(node.getType());
            g2.drawString(node.getType(), p.x - tw / 2, p.y + NODE_RADIUS + 14);
        }
    }

    // ── Node color ────────────────────────────────────────────────────────
    private Color getColor(Node node) {
        if (criticalNodes.contains(node))   return C_CRITICAL;
        if (bottleneckNodes.contains(node)) return C_BOTTLENECK;
        if (affectedNodes.contains(node))   return C_AFFECTED;
        return getColor(node.getType());
    }

    private Color getColor(String type) {
        switch (type) {
            case "Supplier":  return C_SUPPLIER;
            case "Factory":   return C_FACTORY;
            case "Warehouse": return C_WAREHOUSE;
            case "Retailer":  return C_RETAILER;
            default:          return new Color(0x888780);
        }
    }

    // ── Arrow ─────────────────────────────────────────────────────────────
    private void drawArrow(Graphics2D g2, Point p1, Point p2) {
        int offset = NODE_RADIUS;
        double angle = Math.atan2(p2.y - p1.y, p2.x - p1.x);

        int x1 = (int) (p1.x + offset * Math.cos(angle));
        int y1 = (int) (p1.y + offset * Math.sin(angle));
        int x2 = (int) (p2.x - offset * Math.cos(angle));
        int y2 = (int) (p2.y - offset * Math.sin(angle));

        g2.drawLine(x1, y1, x2, y2);

        double phi = Math.toRadians(22);
        int    barb = 12;
        double theta = Math.atan2(y2 - y1, x2 - x1);

        g2.drawLine(x2, y2,
                (int)(x2 - barb * Math.cos(theta + phi)),
                (int)(y2 - barb * Math.sin(theta + phi)));
        g2.drawLine(x2, y2,
                (int)(x2 - barb * Math.cos(theta - phi)),
                (int)(y2 - barb * Math.sin(theta - phi)));
    }

    // ── Click node ────────────────────────────────────────────────────────
    private Node getClickedNode(Point p) {
        for (Node node : nodePositions.keySet()) {
            if (p.distance(nodePositions.get(node)) <= NODE_RADIUS) return node;
        }
        return null;
    }

    // ── Click edge ────────────────────────────────────────────────────────
    private void handleEdgeClick(Point click) {
        for (Node node : graph.getAllNodes()) {
            Point p1 = nodePositions.get(node);
            for (Edge edge : graph.getNeighbors(node)) {
                Point p2 = nodePositions.get(edge.getDestination());
                if (isPointNearLine(click, p1, p2)) {
                    showEdgeDialog(edge);
                    return;
                }
            }
        }
    }

    private void showEdgeDialog(Edge edge) {
        JDialog d = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Edge details", true);
        d.setSize(280, 180);
        d.setLocationRelativeTo(this);

        JPanel body = new JPanel(new GridLayout(3, 2, 8, 8));
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(20, 24, 20, 24));

        Font lbl = new Font("Segoe UI", Font.PLAIN, 12);
        Font val = new Font("Segoe UI", Font.BOLD,  12);
        Color clbl = new Color(0x888780);
        Color cval = new Color(0x2C2C2A);

        addRow(body, "Cost",     String.valueOf(edge.getCost()),     lbl, val, clbl, cval);
        addRow(body, "Time",     String.valueOf(edge.getTime()),     lbl, val, clbl, cval);
        addRow(body, "Capacity", String.valueOf(edge.getCapacity()), lbl, val, clbl, cval);

        d.add(body);
        d.setVisible(true);
    }

    private void addRow(JPanel p, String label, String value,
                        Font lf, Font vf, Color lc, Color vc) {
        JLabel l = new JLabel(label); l.setFont(lf); l.setForeground(lc);
        JLabel v = new JLabel(value); v.setFont(vf); v.setForeground(vc);
        p.add(l); p.add(v);
    }

    // ── Line detection ────────────────────────────────────────────────────
    private boolean isPointNearLine(Point p, Point p1, Point p2) {
        return Line2D.ptSegDist(p1.x, p1.y, p2.x, p2.y, p.x, p.y) < 10;
    }

    // ── Hover tooltip (drawn on canvas) ───────────────────────────────────
    private void drawTooltip(Graphics2D g2) {
        if (hoveredNode == null || mousePoint == null) return;

        Node n = hoveredNode;
        String status = "Normal";
        if (criticalNodes.contains(n))   status = "Critical";
        else if (bottleneckNodes.contains(n)) status = "Bottleneck";
        else if (affectedNodes.contains(n))   status = "Affected";

        String[] labels = { "Type", "Capacity", "Health", "Status" };
        String[] values = { n.getType(), String.valueOf(n.getCapacity()),
                n.getHealth() + "%", status };

        Font fLabel = new Font("Segoe UI", Font.PLAIN, 11);
        Font fValue = new Font("Segoe UI", Font.BOLD,  11);
        Font fTitle = new Font("Segoe UI", Font.BOLD,  12);

        int pad = 12, rowH = 20, titleH = 22;
        int rows = labels.length;
        int boxW = 180;
        int boxH = pad + titleH + rows * rowH + pad;

        int tx = mousePoint.x + 16;
        int ty = mousePoint.y - boxH / 2;
        if (tx + boxW > getWidth()  - 8) tx = mousePoint.x - boxW - 16;
        if (ty < 8)                       ty = 8;
        if (ty + boxH > getHeight() - 8)  ty = getHeight() - boxH - 8;

        // card background
        g2.setColor(new Color(255, 255, 255, 245));
        g2.fillRoundRect(tx, ty, boxW, boxH, 12, 12);
        g2.setColor(new Color(0xD3D1C7));
        g2.setStroke(new BasicStroke(0.5f));
        g2.drawRoundRect(tx, ty, boxW, boxH, 12, 12);

        // colour accent bar on left edge
        g2.setColor(getColor(n.getType()));
        g2.fillRoundRect(tx, ty, 4, boxH, 4, 4);
        g2.fillRect(tx + 2, ty, 2, boxH); // square right side of bar

        int cx = tx + pad + 6;
        int cy = ty + pad + 13;

        // node name title
        g2.setFont(fTitle);
        g2.setColor(new Color(0x2C2C2A));
        g2.drawString(n.getName(), cx, cy);
        cy += titleH;

        // rows
        for (int i = 0; i < labels.length; i++) {
            g2.setFont(fLabel);
            g2.setColor(new Color(0x888780));
            g2.drawString(labels[i], cx, cy);

            g2.setFont(fValue);
            g2.setColor(new Color(0x2C2C2A));
            FontMetrics fm = g2.getFontMetrics();
            int vw = fm.stringWidth(values[i]);
            g2.drawString(values[i], tx + boxW - pad - vw, cy);
            cy += rowH;
        }

        g2.setStroke(new BasicStroke(1f));
    }

    // ── Legend ────────────────────────────────────────────────────────────
    private void drawLegend(Graphics2D g2) {
        int x = 16, y = 16;
        int pw = 170, ph = 9 * 22 + 28;

        g2.setColor(LEGEND_BG);
        g2.fillRoundRect(x, y, pw, ph, 12, 12);
        g2.setColor(LEGEND_BORDER);
        g2.setStroke(new BasicStroke(0.5f));
        g2.drawRoundRect(x, y, pw, ph, 12, 12);

        int cx = x + 14, cy = y + 18;

        g2.setFont(FONT_LEG_HDR);
        g2.setColor(new Color(0x888780));
        g2.drawString("LEGEND", cx, cy);
        cy += 18;

        drawLegendItem(g2, "Supplier",       C_SUPPLIER,   cx, cy); cy += 22;
        drawLegendItem(g2, "Factory",        C_FACTORY,    cx, cy); cy += 22;
        drawLegendItem(g2, "Warehouse",      C_WAREHOUSE,  cx, cy); cy += 22;
        drawLegendItem(g2, "Retailer",       C_RETAILER,   cx, cy); cy += 22;

        // divider
        g2.setColor(LEGEND_BORDER);
        g2.drawLine(cx, cy - 4, cx + pw - 28, cy - 4);
        cy += 4;

        drawLegendItem(g2, "Affected node",   C_AFFECTED,   cx, cy); cy += 22;
        drawLegendItem(g2, "Bottleneck node", C_BOTTLENECK, cx, cy); cy += 22;
        drawLegendItem(g2, "Critical node",   C_CRITICAL,   cx, cy);

        g2.setStroke(new BasicStroke(1f));
    }

    private void drawLegendItem(Graphics2D g2, String label, Color color, int x, int y) {
        g2.setColor(color);
        g2.fillOval(x, y - 9, 13, 13);
        g2.setColor(new Color(255, 255, 255, 80));
        g2.drawOval(x, y - 9, 13, 13);
        g2.setColor(new Color(0x2C2C2A));
        g2.setFont(FONT_LEGEND);
        g2.drawString(label, x + 19, y + 1);
    }

    // ── Simulation ────────────────────────────────────────────────────────
    public void showSimulation(Set<Node> affected,
                               Set<Node> bottlenecks,
                               Set<Node> critical) {
        this.affectedNodes   = affected;
        this.bottleneckNodes = bottlenecks;
        this.criticalNodes   = critical;
        repaint();
    }
}

