package com.supplychain.main;

import com.supplychain.model.*;
import com.supplychain.db.*;
import com.supplychain.logic.BottleneckAnalyzer;
import com.supplychain.logic.DisruptionSimulator;
import com.supplychain.logic.GraphTraversal;
import com.supplychain.logic.RecoveryPlanner;
import com.supplychain.ui.*;

import java.util.List;

public class MainTest {

    public static void main(String[] args) {

        Graph graph = new Graph();

        NodeDAO nodeDAO = new NodeDAO();
        EdgeDAO edgeDAO = new EdgeDAO();

        // Load nodes from DB
        List<Node> nodes = nodeDAO.getAllNodes();

        for(Node n : nodes){
            graph.addNode(n);
        }

        // Load edges from DB
        edgeDAO.loadEdges(graph);

        // Start UI
        new MainMenuUI(graph);

        // --- TESTING BFS ---
//        GraphTraversal traversal = new GraphTraversal(graph);
//
//        Node start = graph.getNodeById("S1");
//
//        if(start != null){
//
//            List<Node> affected = traversal.bfs(start);
//
//            System.out.println("BFS Traversal from S1:");
//
//            for(Node n : affected){
//                System.out.println(n.getName());
//            }
//
//        }
//        DisruptionSimulator simulator = new DisruptionSimulator(graph);
//
//        Node failedNode = graph.getNodeById("S1");
//
//        List<Node> affected = simulator.simulateFailure(failedNode);
//
//        System.out.println("Failure Impact Simulation:");
//
//        for(Node n : affected){
//            System.out.println(n.getName());
//        }
//        BottleneckAnalyzer analyzer = new BottleneckAnalyzer(graph);
//
//        List<Node> bottlenecks = analyzer.findBottlenecks();
//
//        System.out.println("Bottleneck Nodes:");
//
//        for(Node n : bottlenecks){
//            System.out.println(n.getName());
//        }
//        BottleneckAnalyzer analyzer1 = new BottleneckAnalyzer(graph);
//
//        List<Node> critical = analyzer1.findSinglePointsOfFailure();
//
//        System.out.println("Single Points of Failure:");
//
//        for(Node n : critical){
//            System.out.println(n.getName());
//        }
//
//
//     // ---------------- YOUR PART (MEMBER 3) ----------------
//
//        System.out.println("\n===== RECOVERY TEST =====");
//
//        Node source = graph.getNodeById("S1");   // change if needed
//        Node destination = graph.getNodeById("R1"); // change based on your DB
//
//        if(source != null && destination != null){
//
//            RecoveryPlanner planner = new RecoveryPlanner(graph);
//            planner.generateRecovery(source,destination);
//
//        } else {
//            System.out.println("Source or Destination not found!");
//        }


    }
}