package com.supplychain.analytics;

import java.util.BitSet;

/**
 * Bloom Filter for fast blacklist lookup
 */
public class BloomFilter {
    private BitSet bitset;
    private int size;
    private static final int[] SEEDS = {7, 11, 13, 17, 19, 23, 29, 31};
    
    public BloomFilter(int size) {
        this.size = size;
        this.bitset = new BitSet(size);
    }
    
    private int hash(String item, int seed) {
        int hash = 0;
        for (char c : item.toCharArray()) {
            hash = hash * seed + c;
        }
        return Math.abs(hash) % size;
    }
    
    public void add(String item) {
        for (int seed : SEEDS) {
            int index = hash(item, seed);
            bitset.set(index);
        }
    }
    
    public boolean mightContain(String item) {
        for (int seed : SEEDS) {
            int index = hash(item, seed);
            if (!bitset.get(index)) {
                return false;
            }
        }
        return true;
    }
    
    public void clear() {
        bitset.clear();
    }
}
