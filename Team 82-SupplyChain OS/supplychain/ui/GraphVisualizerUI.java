package com.supplychain.ui;

import com.supplychain.model.Graph;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class GraphVisualizerUI extends JFrame {

    public GraphVisualizerUI(Graph graph) {

        setTitle("Supply Chain — Graph Visualizer");
        setSize(1000, 700);
        setMinimumSize(new Dimension(800, 560));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ── Top toolbar ───────────────────────────────────────────────────
        JPanel toolbar = new JPanel(new BorderLayout(0, 0));
        toolbar.setBackground(new Color(0x1D9E75));
        toolbar.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Left: icon + title
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        left.setOpaque(false);
        left.setBorder(new EmptyBorder(12, 10, 12, 0));

        JPanel iconBox = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillRoundRect(0, 0, 36, 36, 8, 8);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.fillOval(14, 14, 8, 8);
                g2.drawLine(18, 14, 18,  6);
                g2.drawLine(18, 22, 18, 30);
                g2.drawLine(18, 18,  8, 12);
                g2.drawLine(18, 18, 28, 12);
            }
        };
        iconBox.setOpaque(false);
        iconBox.setPreferredSize(new Dimension(36, 36));

        JPanel titleBlock = new JPanel(new GridLayout(2, 1, 0, 1));
        titleBlock.setOpaque(false);
        JLabel titleLbl = new JLabel("Supply Chain Graph");
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLbl.setForeground(Color.WHITE);
        JLabel subLbl = new JLabel("Click a node to simulate failure · drag to reposition");
        subLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subLbl.setForeground(new Color(0xB0E8D0));
        titleBlock.add(titleLbl);
        titleBlock.add(subLbl);

        left.add(iconBox);
        left.add(titleBlock);

        // Right: badge pills
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        right.setBorder(new EmptyBorder(12, 0, 12, 14));

        right.add(typeBadge("Supplier",  new Color(0xE1F5EE), new Color(0x0F6E56)));
        right.add(typeBadge("Factory",   new Color(0xE6F1FB), new Color(0x0C447C)));
        right.add(typeBadge("Warehouse", new Color(0xEEEDFE), new Color(0x3C3489)));
        right.add(typeBadge("Retailer",  new Color(0xFAEEDA), new Color(0x854F0B)));

        toolbar.add(left,  BorderLayout.WEST);
        toolbar.add(right, BorderLayout.EAST);

        // ── Graph panel ───────────────────────────────────────────────────
        GraphPanel panel = new GraphPanel(graph);
        panel.setBackground(new Color(0xF1EFE8));

        // ── Status bar ────────────────────────────────────────────────────
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(0xFFFFFF));
        statusBar.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 0, 0, 0, new Color(0xD3D1C7)),
                new EmptyBorder(5, 14, 5, 14)));

        JLabel statusLbl = new JLabel("Ready  ·  " + graph.getAllNodes().size() + " nodes loaded");
        statusLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLbl.setForeground(new Color(0x888780));

        JButton resetBtn = new JButton("Reset simulation");
        resetBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        resetBtn.setForeground(new Color(0x5F5E5A));
        resetBtn.setBackground(new Color(0xF1EFE8));
        resetBtn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(0xD3D1C7), 1, true),
                new EmptyBorder(3, 12, 3, 12)));
        resetBtn.setFocusPainted(false);
        resetBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        resetBtn.addActionListener(e -> {
            panel.showSimulation(new java.util.HashSet<>(),
                    new java.util.HashSet<>(),
                    new java.util.HashSet<>());
            statusLbl.setText("Ready  ·  " + graph.getAllNodes().size() + " nodes loaded");
        });

        statusBar.add(statusLbl, BorderLayout.WEST);
        statusBar.add(resetBtn,  BorderLayout.EAST);

        add(toolbar,   BorderLayout.NORTH);
        add(panel,     BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ── Badge pill helper ─────────────────────────────────────────────────
    private JLabel typeBadge(String text, Color bg, Color fg) {
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
}

