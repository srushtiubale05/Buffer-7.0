SupplyChain OS
Graph-Powered Logistics Intelligence Platform
SupplyChain OS is an intelligent supply chain analysis system built using Java, Swing, MySQL, and advanced Data Structures & Algorithms.
The platform models a real-world logistics network where suppliers, factories, warehouses, and retailers are connected through a directed graph structure. It allows users to visualize networks, simulate disruptions, detect bottlenecks, generate recovery plans, and analyze supplier risks.
The system demonstrates how DSA can be applied to enterprise logistics optimization problems.

Project Theme
Enterprise Systems & Process Optimization
The goal is to create smart systems that help businesses operate efficiently by applying data structures and algorithms to real-world problems such as:
• Supply chain disruptions
• Resource allocation
• Logistics planning
• Network optimization
• Risk analytics

System Overview
The supply chain network is represented as a graph:
Supplier → Factory → Warehouse → Retailer

Multiple suppliers and routes can exist.
Example network:
S1 → F1 → W1 → R1
S2 → F1
S3 → F2 → W3 → R3

This allows the system to analyze:
• Cascading disruptions
• Critical nodes
• Bottlenecks
• Alternative recovery routes

Key Features
1. Supply Chain Network Builder
Users can build and manage supply chain networks.
Supported operations:
• Add Node
• Edit Node
• Delete Node
• Connect Nodes (Edges)
• Remove Connections
Node types supported:
• Supplier
• Factory
• Warehouse
• Retailer
Node attributes:
• Capacity
• Health
• Type
• Name
Edge attributes:
• Cost
• Time
• Capacity

2. Interactive Graph Visualization
The system visualizes the supply chain using an interactive graph canvas.
Features:
• Drag nodes to reposition
• Click nodes to simulate failure
• Click edges to view logistics details
• Visual legend for node types
Graph colors represent:
Color
Meaning
Green
Supplier
Blue
Factory
Purple
Warehouse
Orange
Retailer
Red
Affected Node
Orange Highlight
Bottleneck Node
Black
Critical Node


3. Disruption Simulation
Users can simulate failures in the supply chain.
Steps:
Click any node in the graph
Select Simulate Failure
System runs BFS cascade analysis
Affected nodes are highlighted
Example:
Supplier Failure
   ↓
Factory affected
   ↓
Warehouse affected
   ↓
Retailer affected

Algorithm used:
Breadth First Search (BFS)
Time Complexity:
O(V + E)


4. Bottleneck Detection
Identifies nodes with high dependency load.
These nodes create logistics congestion.
Algorithm:
Graph degree analysis using HashMap
Bottleneck nodes are highlighted in the graph.

5. Critical Node Detection
Detects single points of failure.
These are nodes that multiple parts of the network depend on.
Algorithm:
Incoming edge dependency analysis.
Critical nodes are marked in the graph.

6. Recovery Planner
When disruptions occur, the system generates optimal recovery routes.
Strategies:
• Fastest path
• Cheapest path
• Most reliable path
Users can apply recovery strategies directly.

7. Analytics Dashboard
The analytics module provides supply chain intelligence.
Metrics include:
• Total Network Cost
• Estimated Loss per Hour
• Total Nodes
• High Risk Suppliers
• Historical Loss
Additional modules:
• Supplier Risk Analysis
• Alerts
• Supplier Search

Data Structures & Algorithms Used
This project integrates multiple advanced DSA concepts.

Graph (Adjacency List)
The supply chain is represented as:
Map<Node, List<Edge>>

Benefits:
• Efficient traversal
• Dynamic topology updates
• Supports complex analytics

Breadth First Search (BFS)
Used for:
• Disruption propagation
• Failure cascade detection

Depth First Search (DFS)
Used for:
• Graph connectivity analysis
• Route exploration

Priority Queue
Used in:
PriorityQueueManager
RecoveryPlanner
RouteOptimizer

Applications:
• Shortest path evaluation
• Route optimization

Bloom Filter
Used in Supplier Risk Detection.
Benefits:
• Fast membership checking
• Efficient memory usage

Segment Tree
Used for:
• Efficient range analytics
• Supply metrics aggregation

Trie
Used in:
SupplierManager
TrieNode

Purpose:
• Fast supplier search

Max Heap
Used to track highest risk suppliers.

Project Architecture
com.supplychain
│
├── analytics
│   ├── AnalyticsEngine
│   ├── BloomFilter
│   ├── MaxHeap
│   ├── SegmentTree
│   ├── SupplierManager
│   └── TrieNode
│
├── db
│   ├── DBConnection
│   ├── EdgeDAO
│   └── NodeDAO
│
├── logic
│   ├── BottleneckAnalyzer
│   ├── DisruptionSimulator
│   ├── GraphTraversal
│   ├── PathResult
│   ├── PriorityQueueManager
│   ├── RecoveryPlanner
│   └── RouteOptimizer
│
├── model
│   ├── Graph
│   ├── Node
│   └── Edge
│
├── ui
│   ├── MainMenuUI
│   ├── GraphPanel
│   ├── GraphVisualizerUI
│   ├── DashboardUI
│   ├── AddNodeUI
│   ├── EditNodeUI
│   ├── DeleteNodeUI
│   ├── AddEdgeUI
│   ├── DeleteEdgeUI
│   ├── RecoveryUI
│   └── ViewGraphUI
│
└── main
    └── MainTest


Technology Stack
Programming Language
Java
GUI Framework
Java Swing
Database
MySQL
Concepts Used
• Object Oriented Programming
• Graph Algorithms
• Data Structures
• Database Integration
• Interactive Visualization

How to Run the Project
1. Setup MySQL Database
Create database:
CREATE DATABASE supplychain;

Create tables:
nodes
edges

Populate with sample data.

2. Configure Database
Edit:
DBConnection.java

Set:
username
password
database url


3. Run Application
Run:
MainTest.java

The system will automatically load the network and start the UI.

Demo Video:
https://drive.google.com/file/d/1kRj5mYdTl-TGn-NMAy37KPA72-dfGreY/view?usp=sharing
