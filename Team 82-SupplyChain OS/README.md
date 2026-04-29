
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
<img width="1919" height="1021" alt="image" src="https://github.com/user-attachments/assets/5fec6bf6-9ba1-40a4-824c-e92624cd2d7f" />
<img width="1919" height="1021" alt="image" src="https://github.com/user-attachments/assets/a37298a6-f96b-468f-b589-c30e7247db6a" />
<img width="1919" height="1019" alt="image" src="https://github.com/user-attachments/assets/2f3378db-d581-4a13-b609-6edd4033b076" />
<img width="1917" height="1040" alt="image" src="https://github.com/user-attachments/assets/61d33d1a-e210-4b0f-aefe-9def34c91bb7" />
<img width="1919" height="1019" alt="image" src="https://github.com/user-attachments/assets/b3fc4e89-3b03-4645-870b-bfe043219c7a" />
<img width="663" height="774" alt="image" src="https://github.com/user-attachments/assets/0f418b22-1dba-450e-b54b-f5aa21643ef0" />
<img width="674" height="694" alt="image" src="https://github.com/user-attachments/assets/c33bfa48-82f0-41fb-a790-dfbf1e7039fe" />
<img width="1919" height="1020" alt="image" src="https://github.com/user-attachments/assets/5131a5ca-9ca4-4820-8a8c-0d2c461729f4" />
<img width="655" height="483" alt="image" src="https://github.com/user-attachments/assets/5e3dd28c-e3ed-453d-850a-818814270dac" />
<img width="790" height="668" alt="image" src="https://github.com/user-attachments/assets/0f816442-7d55-44d5-ad91-5b5aeb1bef2f" />
<img width="885" height="720" alt="image" src="https://github.com/user-attachments/assets/ed9d21d7-8ccb-45a5-84c8-7a527999c17b" />
<img width="888" height="721" alt="image" src="https://github.com/user-attachments/assets/8895d819-79f3-47e2-94c4-76626a6eb339" />
<img width="1919" height="1022" alt="image" src="https://github.com/user-attachments/assets/c0cc5dae-53e4-45f0-858b-6e2824c6bbb2" />
<img width="1919" height="1022" alt="image" src="https://github.com/user-attachments/assets/1be32f15-af5c-433e-ab14-5735188d4f8d" />
<img width="1919" height="1016" alt="image" src="https://github.com/user-attachments/assets/ba462ba3-81d8-43fb-8ee0-bd719d048e41" />





---


