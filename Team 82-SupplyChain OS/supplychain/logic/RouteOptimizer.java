package com.supplychain.logic;

import com.supplychain.model.*;
import java.util.*;

public class RouteOptimizer {

    private Graph graph;

    public RouteOptimizer(Graph graph) {
        this.graph = graph;
    }

    // 🔥 DIJKSTRA ALGORITHM
    public Map<Node, Double> dijkstra(Node source, String type) {

        Map<Node, Double> distance = new HashMap<>();
        Map<Node, Node> parent = new HashMap<>();

        PriorityQueue<NodeDistance> pq = new PriorityQueue<>();

        // Initialize distances
        for (Node node : graph.getAllNodes()) {
            distance.put(node, Double.MAX_VALUE);
        }

        distance.put(source, 0.0);
        pq.add(new NodeDistance(source, 0.0));

        while (!pq.isEmpty()) {
            NodeDistance current = pq.poll();
            Node currentNode = current.node;

            for (Edge edge : graph.getNeighbors(currentNode)) {

                Node neighbor = edge.getDestination();
                double weight = 0;

                // 🔥 Select weight based on type
                switch (type) {
                    case "cost":
                        weight = edge.getCost();
                        break;
                    case "time":
                        weight = edge.getTime();
                        break;
                    case "capacity":
                        weight = 1.0 / edge.getCapacity(); // higher capacity = better
                        break;
                }

                double newDist = distance.get(currentNode) + weight;

                if (newDist < distance.get(neighbor)) {
                    distance.put(neighbor, newDist);
                    parent.put(neighbor, currentNode);
                    pq.add(new NodeDistance(neighbor, newDist));
                }
            }
        }

        return distance;
    }

    // 🚀 FASTEST
    public Map<Node, Double> getFastestPaths(Node source) {
        return dijkstra(source, "time");
    }

    // 💰 CHEAPEST
    public Map<Node, Double> getCheapestPaths(Node source) {
        return dijkstra(source, "cost");
    }

    // 🛡 RELIABLE
    public Map<Node, Double> getReliablePaths(Node source) {
        return dijkstra(source, "capacity");
    }
    
    public PathResult getBestPath(Node source, Node destination, String type) {

        Map<Node, Double> distance = new HashMap<>();
        Map<Node, Node> parent = new HashMap<>();

        PriorityQueue<NodeDistance> pq = new PriorityQueue<>();

        for (Node node : graph.getAllNodes()) {
            distance.put(node, Double.MAX_VALUE);
        }

        distance.put(source, 0.0);
        pq.add(new NodeDistance(source, 0.0));

        while (!pq.isEmpty()) {

            NodeDistance current = pq.poll();
            Node currentNode = current.node;

            for (Edge edge : graph.getNeighbors(currentNode)) {

                Node neighbor = edge.getDestination();
                double weight = 0;

                switch (type) {
                    case "cost":
                        weight = edge.getCost();
                        break;
                    case "time":
                        weight = edge.getTime();
                        break;
                    case "capacity":
                        weight = 1.0 / edge.getCapacity();
                        break;
                }

                double newDist = distance.get(currentNode) + weight;

                if (newDist < distance.get(neighbor)) {
                    distance.put(neighbor, newDist);
                    parent.put(neighbor, currentNode);
                    pq.add(new NodeDistance(neighbor, newDist));
                }
            }
        }

        // 🔥 BUILD PATH
        List<Node> path = new ArrayList<>();
        Node current = destination;

        while (current != null) {
            path.add(0, current);
            current = parent.get(current);
        }

        return new PathResult(path, distance.get(destination));
    }
}

// 🔥 HELPER CLASS
class NodeDistance implements Comparable<NodeDistance> {
    Node node;
    double distance;

    public NodeDistance(Node node, double distance) {
        this.node = node;
        this.distance = distance;
    }

    @Override
    public int compareTo(NodeDistance other) {
        return Double.compare(this.distance, other.distance);
    }
}