package com.supplychain.analytics;

/**
 * Segment Tree for efficient range sum queries on historical loss data
 */
public class SegmentTree {
    private long[] tree;
    private long[] arr;
    private int n;
    
    public SegmentTree(int size) {
        n = size;
        arr = new long[n];
        tree = new long[4 * n];
    }
    
    public void update(int index, long value) {
        if (index < 0 || index >= n) return;
        arr[index] = value;
        updateTree(0, 0, n - 1, index, value);
    }
    
    private void updateTree(int node, int start, int end, int idx, long val) {
        if (start == end) {
            tree[node] = val;
        } else {
            int mid = (start + end) / 2;
            int leftChild = 2 * node + 1;
            int rightChild = 2 * node + 2;
            
            if (idx <= mid) {
                updateTree(leftChild, start, mid, idx, val);
            } else {
                updateTree(rightChild, mid + 1, end, idx, val);
            }
            tree[node] = tree[leftChild] + tree[rightChild];
        }
    }
    
    public long rangeSum(int l, int r) {
        if (l < 0 || r >= n || l > r) return 0;
        return queryTree(0, 0, n - 1, l, r);
    }
    
    private long queryTree(int node, int start, int end, int l, int r) {
        if (r < start || end < l) {
            return 0;
        }
        if (l <= start && end <= r) {
            return tree[node];
        }
        int mid = (start + end) / 2;
        int leftChild = 2 * node + 1;
        int rightChild = 2 * node + 2;
        
        long p1 = queryTree(leftChild, start, mid, l, r);
        long p2 = queryTree(rightChild, mid + 1, end, l, r);
        return p1 + p2;
    }
    
    public long getTotal() {
        return n > 0 ? rangeSum(0, n - 1) : 0;
    }
}
