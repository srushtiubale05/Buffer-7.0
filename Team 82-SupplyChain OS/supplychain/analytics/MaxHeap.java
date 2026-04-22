package com.supplychain.analytics;

import com.supplychain.model.Node;
import java.util.*;

/**
 * Max Heap implementation for tracking nodes with highest loss
 */
public class MaxHeap {
    private List<HeapNode> heap;
    
    public static class HeapNode implements Comparable<HeapNode> {
        Node node;
        double loss;
        
        public HeapNode(Node node, double loss) {
            this.node = node;
            this.loss = loss;
        }
        
        @Override
        public int compareTo(HeapNode other) {
            return Double.compare(other.loss, this.loss); // Max heap
        }
        
        public Node getNode() { return node; }
        public double getLoss() { return loss; }
    }
    
    public MaxHeap() {
        heap = new ArrayList<>();
    }
    
    public void insert(Node node, double loss) {
        heap.add(new HeapNode(node, loss));
        heapifyUp(heap.size() - 1);
    }
    
    public HeapNode peek() {
        if (heap.isEmpty()) return null;
        return heap.get(0);
    }
    
    public HeapNode poll() {
        if (heap.isEmpty()) return null;
        HeapNode max = heap.get(0);
        heap.set(0, heap.get(heap.size() - 1));
        heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) heapifyDown(0);
        return max;
    }
    
    public List<HeapNode> getTopK(int k) {
        List<HeapNode> result = new ArrayList<>();
        MaxHeap tempHeap = new MaxHeap();
        tempHeap.heap = new ArrayList<>(this.heap);
        
        for (int i = 0; i < Math.min(k, tempHeap.heap.size()); i++) {
            HeapNode node = tempHeap.poll();
            if (node != null) result.add(node);
        }
        return result;
    }
    
    private void heapifyUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap.get(index).compareTo(heap.get(parent)) > 0) {
                Collections.swap(heap, index, parent);
                index = parent;
            } else {
                break;
            }
        }
    }
    
    private void heapifyDown(int index) {
        while (2 * index + 1 < heap.size()) {
            int largest = index;
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            
            if (heap.get(left).compareTo(heap.get(largest)) > 0) largest = left;
            if (right < heap.size() && heap.get(right).compareTo(heap.get(largest)) > 0) largest = right;
            
            if (largest != index) {
                Collections.swap(heap, index, largest);
                heapifyDown(largest);
            } else {
                break;
            }
        }
    }
    
    public boolean isEmpty() { return heap.isEmpty(); }
    public int size() { return heap.size(); }
}
