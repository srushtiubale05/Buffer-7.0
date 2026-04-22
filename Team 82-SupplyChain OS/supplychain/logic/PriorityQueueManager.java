package com.supplychain.logic;

import java.util.PriorityQueue;

public class PriorityQueueManager<T extends Comparable<T>> {

    private PriorityQueue<T> pq;

    public PriorityQueueManager() {
        pq = new PriorityQueue<>();
    }

    public void add(T item) {
        pq.add(item);
    }

    public T poll() {
        return pq.poll();
    }

    public boolean isEmpty() {
        return pq.isEmpty();
    }

    public int size() {
        return pq.size();
    }
}