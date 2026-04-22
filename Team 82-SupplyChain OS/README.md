
---

# 📦 SupplyChain OS

## Graph-Powered Logistics Intelligence Platform

SupplyChain OS is an intelligent supply chain analysis system built using **Java, Swing, MySQL, and advanced Data Structures & Algorithms**.

The platform models a real-world logistics network where suppliers, factories, warehouses, and retailers are connected through a **directed graph structure**.

It allows users to:

* Visualize networks
* Simulate disruptions
* Detect bottlenecks
* Generate recovery plans
* Analyze supplier risks

The system demonstrates how **DSA can be applied to enterprise logistics optimization problems**.

---

# 🎥 Demo Video

👉 Watch here:
[https://drive.google.com/file/d/1kRj5mYdTl-TGn-NMAy37KPA72-dfGreY/view?usp=sharing](https://drive.google.com/file/d/1kRj5mYdTl-TGn-NMAy37KPA72-dfGreY/view?usp=sharing)

---

# 🧠 Project Theme

## Enterprise Systems & Process Optimization

The goal is to create smart systems that help businesses operate efficiently by applying data structures and algorithms to real-world problems such as:

* Supply chain disruptions
* Resource allocation
* Logistics planning
* Network optimization
* Risk analytics

---

# 🔗 System Overview

The supply chain network is represented as a graph:

```
Supplier → Factory → Warehouse → Retailer
```

### Example Network

```
S1 → F1 → W1 → R1  
S2 → F1  
S3 → F2 → W3 → R3  
```

This allows the system to analyze:

* Cascading disruptions
* Critical nodes
* Bottlenecks
* Alternative recovery routes

---

# ⚙️ Key Features

## 1. Supply Chain Network Builder

Users can build and manage supply chain networks.

### Supported Operations

* Add Node
* Edit Node
* Delete Node
* Connect Nodes (Edges)
* Remove Connections

### Node Types

* Supplier
* Factory
* Warehouse
* Retailer

### Node Attributes

* Capacity
* Health
* Type
* Name

### Edge Attributes

* Cost
* Time
* Capacity

---

## 2. Interactive Graph Visualization

The system visualizes the supply chain using an interactive graph canvas.

### Features

* Drag nodes to reposition
* Click nodes to simulate failure
* Click edges to view logistics details
* Visual legend for node types

### Graph Color Legend

| Color            | Meaning         |
| ---------------- | --------------- |
| Green            | Supplier        |
| Blue             | Factory         |
| Purple           | Warehouse       |
| Orange           | Retailer        |
| Red              | Affected Node   |
| Orange Highlight | Bottleneck Node |
| Black            | Critical Node   |

---

## 3. Disruption Simulation

Users can simulate failures in the supply chain.

### Steps

1. Click any node in the graph
2. Select **Simulate Failure**
3. System runs BFS cascade analysis
4. Affected nodes are highlighted

### Example

```
Supplier Failure  
   ↓  
Factory affected  
   ↓  
Warehouse affected  
   ↓  
Retailer affected  
```

### Algorithm Used

Breadth First Search (BFS)

### Time Complexity

```
O(V + E)
```

---

## 4. Bottleneck Detection

Identifies nodes with high dependency load.

These nodes create logistics congestion.

### Algorithm

Graph degree analysis using **HashMap**

---

## 5. Critical Node Detection

Detects single points of failure.

### Algorithm

Incoming edge dependency analysis

---

## 6. Recovery Planner

When disruptions occur, the system generates optimal recovery routes.

### Strategies

* Fastest path
* Cheapest path
* Most reliable path

Users can apply recovery strategies directly.

---

## 7. Analytics Dashboard

The analytics module provides supply chain intelligence.

### Metrics

* Total Network Cost
* Estimated Loss per Hour
* Total Nodes
* High Risk Suppliers
* Historical Loss

### Additional Modules

* Supplier Risk Analysis
* Alerts
* Supplier Search

---

# 📊 Data Structures & Algorithms Used

## Graph (Adjacency List)

```
Map<Node, List<Edge>>
```

### Benefits

* Efficient traversal
* Dynamic updates
* Supports analytics

---

## BFS

* Disruption propagation
* Failure cascade

---

## DFS

* Connectivity analysis
* Route exploration

---

## Priority Queue

Used in:

* PriorityQueueManager
* RecoveryPlanner
* RouteOptimizer

---

## Bloom Filter

* Fast supplier risk detection

---

## Segment Tree

* Efficient range queries

---

## Trie

* Fast supplier search

---

## Max Heap

* Track highest risk suppliers

---

# 🏗 Project Architecture

```
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
```

---

# 💻 Technology Stack

**Programming Language**
Java

**GUI Framework**
Java Swing

**Database**
MySQL

### Concepts Used

* Object Oriented Programming
* Graph Algorithms
* Data Structures
* Database Integration
* Interactive Visualization

---

# ▶️ How to Run

## 1. Setup Database

```sql
CREATE DATABASE supplychain;
```

Create tables:

```
nodes  
edges  
```

---

## 2. Configure Database

Edit:

```
DBConnection.java
```

Set:

```
username  
password  
database URL  
```

---

## 3. Run Application

```
MainTest.java
```

---

# 
<img width="1919" height="1019" alt="image" src="https://github.com/user-attachments/assets/c9d71d01-b60e-4940-9119-86e641d12574" />
<img width="1919" height="1018" alt="image" src="https://github.com/user-attachments/assets/dea0a76c-92de-4377-9029-b22e5df39c65" />
<img width="1241" height="875" alt="image" src="https://github.com/user-attachments/assets/2d22e1ec-2eea-4ba8-a6b8-09b396b549b1" />
<img width="888" height="720" alt="image" src="https://github.com/user-attachments/assets/c0b9a526-bec2-42f6-ba4c-d7fd3c49ca66" />
<img width="1580" height="1017" alt="image" src="https://github.com/user-attachments/assets/312ef275-5555-4a55-9eba-a979f5445e76" />



---


