package com.supplychain.model;

public class Node {
    private String id;
    private String name;
    private String type; // Supplier, Factory, Warehouse, Retailer
    private int capacity;
    private int health;

    public Node(String id, String name, String type, int capacity, int health) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.capacity = capacity;
        this.health = health;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getHealth() {
        return health;
    }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}