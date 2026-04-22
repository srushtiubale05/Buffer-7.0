package com.supplychain.analytics;

import java.util.*;

/**
 * Trie data structure for supplier name autocomplete search
 */
public class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    List<String> suppliers = new ArrayList<>();
    boolean isEndOfWord = false;
    
    public static class Trie {
        TrieNode root;
        
        public Trie() {
            root = new TrieNode();
        }
        
        public void insert(String supplier) {
            TrieNode node = root;
            for (char c : supplier.toLowerCase().toCharArray()) {
                node.children.putIfAbsent(c, new TrieNode());
                node = node.children.get(c);
                if (!node.suppliers.contains(supplier)) {
                    node.suppliers.add(supplier);
                }
            }
            node.isEndOfWord = true;
        }
        
        public List<String> search(String prefix) {
            TrieNode node = root;
            for (char c : prefix.toLowerCase().toCharArray()) {
                if (!node.children.containsKey(c)) {
                    return new ArrayList<>();
                }
                node = node.children.get(c);
            }
            return new ArrayList<>(node.suppliers);
        }
        
        public boolean contains(String word) {
            TrieNode node = root;
            for (char c : word.toLowerCase().toCharArray()) {
                if (!node.children.containsKey(c)) {
                    return false;
                }
                node = node.children.get(c);
            }
            return node.isEndOfWord;
        }
    }
}
