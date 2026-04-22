package com.supplychain.model;

import java.util.*;

public class Graph {

    // Adjacency List
    private Map<Node, List<Edge>> adjList;

    public Graph() {
        adjList = new HashMap<>();
    }

    // Add Node
    public void addNode(Node node) {
        adjList.putIfAbsent(node, new ArrayList<>());
    }

    // Add Edge
    public void addEdge(Node source, Node destination, double cost, double time, int capacity) {
        adjList.get(source).add(new Edge(destination, cost, time, capacity));
    }

    // Get neighbors
    public List<Edge> getNeighbors(Node node) {
        return adjList.get(node);
    }

    // Print Graph (for testing)
    public void printGraph() {
        for (Node node : adjList.keySet()) {
            System.out.print(node + " -> ");
            for (Edge edge : adjList.get(node)) {
                System.out.print(edge.getDestination() + " ");
            }
            System.out.println();
        }
    }

    // Get all nodes
    public Set<Node> getAllNodes() {
        return adjList.keySet();
    }
    public Node getNodeByName(String name) {
        for (Node node : adjList.keySet()) {
            if (node.getName().equals(name)) {
                return node;
            }
        }
        return null;
    }
    public Node getNodeById(String id) {

        for (Node node : adjList.keySet()) {

            if (node.getId().equals(id)) {
                return node;
            }

        }

        return null;
    }
}