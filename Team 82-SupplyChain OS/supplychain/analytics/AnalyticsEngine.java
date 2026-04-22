package com.supplychain.analytics;

import com.supplychain.model.*;
import com.supplychain.logic.RecoveryPlanner;
import java.util.*;

/**
 * Analytics Engine for calculating network metrics, loss, risk scores, and alerts
 */
public class AnalyticsEngine {
    private Graph graph;
    private MaxHeap lossHeap;
    private SegmentTree historicalLoss;
    private List<String> alerts;
    private static final int HEALTH_THRESHOLD = 50;
    private static final int CAPACITY_THRESHOLD = 30;
    
    public AnalyticsEngine(Graph graph) {
        this.graph = graph;
        this.lossHeap = new MaxHeap();
        this.historicalLoss = new SegmentTree(100); // Track up to 100 time periods
        this.alerts = new ArrayList<>();
    }
    
    /**
     * Calculate total network cost (sum of all edge costs)
     */
    public double calculateTotalNetworkCost() {
        double totalCost = 0;
        for (Node node : graph.getAllNodes()) {
            List<Edge> edges = graph.getNeighbors(node);
            if (edges != null) {
                for (Edge edge : edges) {
                    totalCost += edge.getCost();
                }
            }
        }
        return totalCost;
    }
    
    /**
     * Calculate estimated loss per hour based on disrupted nodes
     */
    public double calculateLossPerHour(Set<Node> disruptedNodes) {
        double loss = 0;
        for (Node node : disruptedNodes) {
            if (node.getHealth() < HEALTH_THRESHOLD) {
                double nodeLoss = (node.getCapacity() * (100 - node.getHealth())) / 100.0;
                loss += nodeLoss;
                lossHeap.insert(node, nodeLoss);
            }
        }
        return loss;
    }
    
    /**
     * Get highest loss node using Max Heap
     */
    public MaxHeap.HeapNode getHighestLossNode() {
        return lossHeap.peek();
    }
    
    /**
     * Get top K nodes with highest losses
     */
    public List<MaxHeap.HeapNode> getTopLossyNodes(int k) {
        return lossHeap.getTopK(k);
    }
    
    /**
     * Calculate supplier risk score based on capacity, health, and connectivity
     * Risk Score = (1 - health/100) * 0.4 + (1 - capacity/maxCapacity) * 0.3 + (1 - connectivity/maxDegree) * 0.3
     */
    public double getSupplierRisk(Node node) {
        if (node == null) return 0;
        
        // Health factor (0-100)
        double healthFactor = (100 - node.getHealth()) / 100.0;
        
        // Capacity factor (normalized)
        int maxCapacity = getMaxCapacityInNetwork();
        double capacityFactor = (1.0 - (double)node.getCapacity() / Math.max(1, maxCapacity));
        
        // Connectivity factor (degree in graph)
        int degree = graph.getNeighbors(node) != null ? graph.getNeighbors(node).size() : 0;
        int maxDegree = getMaxDegreeInNetwork();
        double connectivityFactor = (1.0 - (double)degree / Math.max(1, maxDegree));
        
        // Weighted risk score (0-1, higher = more risky)
        return (healthFactor * 0.4) + (capacityFactor * 0.3) + (connectivityFactor * 0.3);
    }
    
    /**
     * Get all high-risk suppliers (risk score > 0.6)
     */
    public List<Node> getHighRiskSuppliers() {
        List<Node> riskySupplers = new ArrayList<>();
        for (Node node : graph.getAllNodes()) {
            if (getSupplierRisk(node) > 0.6) {
                riskySupplers.add(node);
            }
        }
        riskySupplers.sort((a, b) -> Double.compare(getSupplierRisk(b), getSupplierRisk(a)));
        return riskySupplers;
    }
    
    /**
     * Check for alerts based on node health and capacity
     */
    public List<String> checkAlerts() {
        alerts.clear();
        for (Node node : graph.getAllNodes()) {
            if (node.getHealth() < HEALTH_THRESHOLD) {
                alerts.add("⚠️ HEALTH ALERT: " + node.getName() + " health is " + node.getHealth() + "%");
            }
            if (node.getCapacity() < CAPACITY_THRESHOLD) {
                alerts.add("⚠️ CAPACITY ALERT: " + node.getName() + " capacity is low (" + node.getCapacity() + ")");
            }
        }
        return alerts;
    }
    
    /**
     * Add historical loss data point
     */
    public void recordLoss(int timeIndex, long loss) {
        historicalLoss.update(timeIndex, loss);
    }
    
    /**
     * Get total historical loss over a range of time periods
     */
    public long getHistoricalLossSum(int startTime, int endTime) {
        return historicalLoss.rangeSum(startTime, endTime);
    }
    
    /**
     * Get total historical loss across all recorded periods
     */
    public long getTotalHistoricalLoss() {
        return historicalLoss.getTotal();
    }
    
    private int getMaxCapacityInNetwork() {
        int max = 0;
        for (Node node : graph.getAllNodes()) {
            max = Math.max(max, node.getCapacity());
        }
        return max;
    }
    
    private int getMaxDegreeInNetwork() {
        int max = 0;
        for (Node node : graph.getAllNodes()) {
            int degree = graph.getNeighbors(node) != null ? graph.getNeighbors(node).size() : 0;
            max = Math.max(max, degree);
        }
        return max;
    }
    
    public List<String> getAlerts() {
        return alerts;
    }
    
    public void clearAlerts() {
        alerts.clear();
    }
}
