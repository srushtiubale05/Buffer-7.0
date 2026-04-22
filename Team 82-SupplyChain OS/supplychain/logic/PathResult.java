package com.supplychain.logic;

import com.supplychain.model.Node;
import java.util.List;

public class PathResult {

    private List<Node> path;
    private double totalValue;

    public PathResult(List<Node> path, double totalValue) {
        this.path = path;
        this.totalValue = totalValue;
    }

    public List<Node> getPath() {
        return path;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void printResult() {
        System.out.print("Path: ");
        for (Node node : path) {
            System.out.print(node.getName() + " -> ");
        }
        System.out.println("END");
        System.out.println("Total Value: " + totalValue);
    }
}