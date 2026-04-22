package com.supplychain.ui;

import com.supplychain.model.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class ViewGraphUI extends JFrame {

    private Graph graph;
    private JTextArea displayArea;          // kept — field name unchanged
    private JTextPane styledPane;           // renders colour output

    // ── Palette ───────────────────────────────────────────────────────────
    private static final Color BG_PAGE    = new Color(0xF1EFE8);
    private static final Color BG_CARD    = new Color(0xFFFFFF);
    private static final Color BG_HEADER  = new Color(0x1D9E75);
    private static final Color BG_AREA    = new Color(0xFAFAF8);
    private static final Color BG_BTN     = new Color(0x1D9E75);
    private static final Color BG_BTN_HOV = new Color(0x0F6E56);

    private static final Color FG_TITLE   = Color.WHITE;
    private static final Color FG_SUB     = new Color(0xB0E8D0);
    private static final Color FG_AREA    = new Color(0x2C2C2A);
    private static final Color FG_MUTED   = new Color(0x888780);
    private static final Color FG_BTN     = Color.WHITE;
    private static final Color BD_AREA    = new Color(0xD3D1C7);

    private static final Color C_SUPPLIER  = new Color(0x1D9E75);
    private static final Color C_FACTORY   = new Color(0x185FA5);
    private static final Color C_WAREHOUSE = new Color(0x534AB7);
    private static final Color C_RETAILER  = new Color(0xBA7517);
    private static final Color C_ARROW     = new Color(0xB4B2A9);
    private static final Color C_DEST      = new Color(0xD85A30);

    // ── Constructor ───────────────────────────────────────────────────────
    public ViewGraphUI(Graph graph) {
        this.graph = graph;

        setTitle("View Supply Chain Graph");
        setSize(640, 540);
        setMinimumSize(new Dimension(500, 400));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // hidden — satisfies backend if anything calls displayArea.getText()
        displayArea = new JTextArea();
        displayArea.setEditable(false);

        add(buildHeader(),  BorderLayout.NORTH);
        add(buildCenter(),  BorderLayout.CENTER);
        add(buildFooter(),  BorderLayout.SOUTH);

        setVisible(true);
        displayGraph();
    }

    // ── Header ────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setBackground(BG_HEADER);
        header.setBorder(new EmptyBorder(14, 18, 14, 18));

        JPanel iconBox = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillRoundRect(0, 0, 36, 36, 8, 8);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawRoundRect(5, 9,  10, 16, 3, 3);
                g2.drawRoundRect(21, 5, 10, 10, 3, 3);
                g2.drawRoundRect(21, 20, 10, 10, 3, 3);
                g2.drawLine(15, 15, 21, 10);
                g2.drawLine(15, 21, 21, 25);
            }
        };
        iconBox.setOpaque(false);
        iconBox.setPreferredSize(new Dimension(36, 36));

        JPanel text = new JPanel(new GridLayout(2, 1, 0, 2));
        text.setOpaque(false);
        JLabel t = new JLabel("Supply Chain Graph");
        t.setFont(new Font("Segoe UI", Font.BOLD,  15));
        t.setForeground(FG_TITLE);
        JLabel s = new JLabel("Nodes and their outgoing edges · colour-coded by type");
        s.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        s.setForeground(FG_SUB);
        text.add(t);
        text.add(s);

        header.add(iconBox, BorderLayout.WEST);
        header.add(text,    BorderLayout.CENTER);
        return header;
    }

    // ── Center ────────────────────────────────────────────────────────────
    private JPanel buildCenter() {
        styledPane = new JTextPane();
        styledPane.setEditable(false);
        styledPane.setBackground(BG_AREA);
        styledPane.setMargin(new Insets(14, 16, 14, 16));
        styledPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JScrollPane scroll = new JScrollPane(styledPane);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BG_AREA);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getHorizontalScrollBar().setUnitIncrement(16);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BG_CARD);
        card.setBorder(new LineBorder(BD_AREA, 1, true));
        card.add(scroll, BorderLayout.CENTER);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(BG_PAGE);
        wrap.setBorder(new EmptyBorder(14, 14, 0, 14));
        wrap.add(card, BorderLayout.CENTER);
        return wrap;
    }

    // ── Footer ────────────────────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(BG_PAGE);
        footer.setBorder(new EmptyBorder(10, 14, 14, 14));

        // legend pills
        JPanel pills = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        pills.setOpaque(false);
        pills.add(pill("Supplier",  new Color(0xE1F5EE), C_SUPPLIER));
        pills.add(pill("Factory",   new Color(0xE6F1FB), C_FACTORY));
        pills.add(pill("Warehouse", new Color(0xEEEDFE), C_WAREHOUSE));
        pills.add(pill("Retailer",  new Color(0xFAEEDA), C_RETAILER));

        JButton refreshButton = new JButton("Refresh Graph") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? BG_BTN_HOV : BG_BTN);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
            }
        };
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshButton.setForeground(FG_BTN);
        refreshButton.setOpaque(false);
        refreshButton.setContentAreaFilled(false);
        refreshButton.setBorderPainted(false);
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        refreshButton.setBorder(new EmptyBorder(7, 20, 7, 20));
        refreshButton.addActionListener(e -> displayGraph());

        footer.add(pills,         BorderLayout.WEST);
        footer.add(refreshButton, BorderLayout.EAST);
        return footer;
    }

    // ── Pill label ────────────────────────────────────────────────────────
    private JLabel pill(String text, Color bg, Color fg) {
        JLabel l = new JLabel(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                super.paintComponent(g);
            }
        };
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(fg);
        l.setOpaque(false);
        l.setBorder(new EmptyBorder(3, 10, 3, 10));
        return l;
    }

    // ── Display graph — original logic, colour-coded output ───────────────
    private void displayGraph() {
        StyledDocument doc = styledPane.getStyledDocument();
        try {
            doc.remove(0, doc.getLength());
        } catch (BadLocationException ignored) {}

        // also keep plain text in displayArea for any backend reads
        StringBuilder sb = new StringBuilder();

        try {
            for (Node node : graph.getAllNodes()) {

                // node name — bold, type colour
                SimpleAttributeSet nodeAttr = new SimpleAttributeSet();
                StyleConstants.setForeground(nodeAttr, typeColor(node.getType()));
                StyleConstants.setBold(nodeAttr, true);
                StyleConstants.setFontFamily(nodeAttr, "Segoe UI");
                StyleConstants.setFontSize(nodeAttr, 13);
                String nodeStr = node.toString();
                doc.insertString(doc.getLength(), nodeStr, nodeAttr);

                sb.append(nodeStr).append(" -> ");

                // arrow
                SimpleAttributeSet arrowAttr = new SimpleAttributeSet();
                StyleConstants.setForeground(arrowAttr, C_ARROW);
                StyleConstants.setBold(arrowAttr, false);
                StyleConstants.setFontFamily(arrowAttr, "Segoe UI");
                StyleConstants.setFontSize(arrowAttr, 13);
                doc.insertString(doc.getLength(), "  →  ", arrowAttr);

                // destination nodes
                for (Edge edge : graph.getNeighbors(node)) {
                    String dest = edge.getDestination().toString();

                    SimpleAttributeSet destAttr = new SimpleAttributeSet();
                    StyleConstants.setForeground(destAttr, C_DEST);
                    StyleConstants.setBold(destAttr, false);
                    StyleConstants.setFontFamily(destAttr, "Segoe UI");
                    StyleConstants.setFontSize(destAttr, 13);
                    doc.insertString(doc.getLength(), dest + "   ", destAttr);

                    sb.append(dest).append(" ");
                }

                // newline
                SimpleAttributeSet nlAttr = new SimpleAttributeSet();
                StyleConstants.setForeground(nlAttr, FG_AREA);
                doc.insertString(doc.getLength(), "\n", nlAttr);
                sb.append("\n");
            }
        } catch (BadLocationException ignored) {}

        displayArea.setText(sb.toString());
        styledPane.setCaretPosition(0);
    }

    // ── Type colour ───────────────────────────────────────────────────────
    private Color typeColor(String type) {
        switch (type) {
            case "Supplier":  return C_SUPPLIER;
            case "Factory":   return C_FACTORY;
            case "Warehouse": return C_WAREHOUSE;
            case "Retailer":  return C_RETAILER;
            default:          return FG_AREA;
        }
    }
}

