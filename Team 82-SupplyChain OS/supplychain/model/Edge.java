package com.supplychain.model;

public class Edge {
    private Node destination;
    private double cost;
    private double time;
    private int capacity;

    public Edge(Node destination, double cost, double time, int capacity) {
        this.destination = destination;
        this.cost = cost;
        this.time = time;
        this.capacity = capacity;
    }

    public Node getDestination() {
        return destination;
    }

    public double getCost() {
        return cost;
    }

    public double getTime() {
        return time;
    }

    public int getCapacity() {
        return capacity;
    }
}