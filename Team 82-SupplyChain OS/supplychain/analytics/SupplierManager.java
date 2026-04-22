package com.supplychain.analytics;

import com.supplychain.model.Node;
import com.supplychain.model.Graph;
import com.supplychain.analytics.TrieNode.Trie;
import java.util.*;

/**
 * Supplier Manager for intelligent supplier operations
 * Handles search (Trie), blacklist checking (Bloom Filter), and risk assessment
 */
public class SupplierManager {
    private Graph graph;
    private Trie supplierTrie;
    private BloomFilter blacklist;
    private AnalyticsEngine analyticsEngine;
    private Set<String> suppliers;
    
    public SupplierManager(Graph graph, AnalyticsEngine analyticsEngine) {
        this.graph = graph;
        this.analyticsEngine = analyticsEngine;
        this.supplierTrie = new Trie();
        this.blacklist = new BloomFilter(1000);
        this.suppliers = new HashSet<>();
        
        // Initialize with existing suppliers from graph
        for (Node node : graph.getAllNodes()) {
            addSupplier(node);
        }
    }
    
    /**
     * Add a supplier to the manager
     */
    public void addSupplier(Node node) {
        supplierTrie.insert(node.getName());
        suppliers.add(node.getName());
    }
    
    /**
     * Search suppliers by prefix using Trie
     * @param prefix partial supplier name
     * @return list of matching supplier names
     */
    public List<String> searchSupplier(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return new ArrayList<>(suppliers);
        }
        return supplierTrie.search(prefix);
    }
    
    /**
     * Add supplier to blacklist
     */
    public void addToBlacklist(String supplierName) {
        blacklist.add(supplierName.toLowerCase());
        System.out.println("Added " + supplierName + " to blacklist");
    }
    
    /**
     * Check if supplier is blacklisted using Bloom Filter
     */
    public boolean isBlacklisted(String supplierName) {
        return blacklist.mightContain(supplierName.toLowerCase());
    }
    
    /**
     * Get all suppliers as nodes with their risk scores
     */
    public List<SupplierRiskInfo> getAllSuppliersWithRisk() {
        List<SupplierRiskInfo> riskInfos = new ArrayList<>();
        for (Node node : graph.getAllNodes()) {
            double risk = analyticsEngine.getSupplierRisk(node);
            boolean blacklisted = isBlacklisted(node.getName());
            riskInfos.add(new SupplierRiskInfo(node, risk, blacklisted));
        }
        riskInfos.sort((a, b) -> Double.compare(b.riskScore, a.riskScore));
        return riskInfos;
    }
    
    /**
     * Get node by supplier name
     */
    public Node getSupplierByName(String name) {
        for (Node node : graph.getAllNodes()) {
            if (node.getName().equalsIgnoreCase(name)) {
                return node;
            }
        }
        return null;
    }
    
    /**
     * Get high-risk suppliers
     */
    public List<Node> getHighRiskSuppliers() {
        return analyticsEngine.getHighRiskSuppliers();
    }
    
    /**
     * Get supplier risk score
     */
    public double getSupplierRisk(String supplierName) {
        Node node = getSupplierByName(supplierName);
        if (node == null) return 0;
        return analyticsEngine.getSupplierRisk(node);
    }
    
    /**
     * Inner class for supplier risk information
     */
    public static class SupplierRiskInfo {
        public Node node;
        public double riskScore;
        public boolean isBlacklisted;
        
        public SupplierRiskInfo(Node node, double riskScore, boolean isBlacklisted) {
            this.node = node;
            this.riskScore = riskScore;
            this.isBlacklisted = isBlacklisted;
        }
    }
}
