package com.supplychain.logic;

import com.supplychain.model.*;

import java.util.*;

public class BottleneckAnalyzer {

    private Graph graph;

    public BottleneckAnalyzer(Graph graph) {
        this.graph = graph;
    }

    // Find nodes with highest outgoing dependency
    public List<Node> findBottlenecks() {

        List<Node> bottlenecks = new ArrayList<>();

        int maxConnections = 0;

        for(Node node : graph.getAllNodes()) {

            int connections = graph.getNeighbors(node).size();

            if(connections > maxConnections) {

                maxConnections = connections;

                bottlenecks.clear();
                bottlenecks.add(node);

            }
            else if(connections == maxConnections) {

                bottlenecks.add(node);

            }
        }

        return bottlenecks;
    }
    public List<Node> findSinglePointsOfFailure(){

        List<Node> criticalNodes = new ArrayList<>();

        Map<Node,Integer> incoming = new HashMap<>();

        for(Node node : graph.getAllNodes()){
            incoming.put(node,0);
        }

        for(Node node : graph.getAllNodes()){

            for(Edge e : graph.getNeighbors(node)){

                Node dest = e.getDestination();

                incoming.put(dest, incoming.get(dest) + 1);

            }
        }

        for(Node node : incoming.keySet()){

            if(incoming.get(node) > 1){
                criticalNodes.add(node);
            }
        }

        return criticalNodes;
    }
    private void dfsWithoutNode(Node current, Node removedNode, Set<Node> visited) {

        visited.add(current);

        for(Edge edge : graph.getNeighbors(current)) {

            Node next = edge.getDestination();

            if(next.equals(removedNode))
                continue;

            if(!visited.contains(next)) {
                dfsWithoutNode(next, removedNode, visited);
            }
        }
    }
    private int countReachableNodes() {

        if(graph.getAllNodes().isEmpty())
            return 0;

        Set<Node> visited = new HashSet<>();

        Node start = graph.getAllNodes().iterator().next();

        dfsWithoutNode(start, null, visited);

        return visited.size();
    }
}