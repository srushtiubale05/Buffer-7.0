package com.supplychain.logic;

import com.supplychain.model.*;

import java.util.*;

public class DisruptionSimulator {

    private Graph graph;

    public DisruptionSimulator(Graph graph) {
        this.graph = graph;
    }

    public List<Node> simulateFailure(Node failedNode) {

        List<Node> affectedNodes = new ArrayList<>();

        Queue<Node> queue = new LinkedList<>();

        queue.add(failedNode);
        affectedNodes.add(failedNode);

        while(!queue.isEmpty()) {

            Node current = queue.poll();

            for(Edge edge : graph.getNeighbors(current)) {

                Node next = edge.getDestination();

                if(!affectedNodes.contains(next)) {

                    affectedNodes.add(next);
                    queue.add(next);

                }
            }
        }

        return affectedNodes;
    }
}