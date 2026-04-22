package com.supplychain.logic;

import com.supplychain.model.*;

import java.util.*;

public class GraphTraversal {

    private Graph graph;

    public GraphTraversal(Graph graph) {
        this.graph = graph;
    }

    // BFS Traversal
    public List<Node> bfs(Node start) {

        List<Node> visited = new ArrayList<>();
        Queue<Node> queue = new LinkedList<>();

        queue.add(start);
        visited.add(start);

        while(!queue.isEmpty()) {

            Node current = queue.poll();

            for(Edge edge : graph.getNeighbors(current)) {

                Node neighbor = edge.getDestination();

                if(!visited.contains(neighbor)) {

                    visited.add(neighbor);
                    queue.add(neighbor);

                }
            }
        }

        return visited;
    }

    // DFS Traversal
    public List<Node> dfs(Node start) {

        List<Node> visited = new ArrayList<>();
        Stack<Node> stack = new Stack<>();

        stack.push(start);

        while(!stack.isEmpty()) {

            Node current = stack.pop();

            if(!visited.contains(current)) {

                visited.add(current);

                for(Edge edge : graph.getNeighbors(current)) {

                    stack.push(edge.getDestination());

                }
            }
        }

        return visited;
    }
}