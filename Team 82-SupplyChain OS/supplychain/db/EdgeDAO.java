package com.supplychain.db;

import com.supplychain.model.*;

import java.sql.*;

public class EdgeDAO {

    // insert edge
    public void insertEdge(Node source, Node dest,
                           double cost, double time, int capacity) {

        try {

            Connection conn = DBConnection.getConnection();

            String sql = "INSERT INTO edges(source_id,destination_id,cost,time,capacity) VALUES (?,?,?,?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, source.getId());
            ps.setString(2, dest.getId());
            ps.setDouble(3, cost);
            ps.setDouble(4, time);
            ps.setInt(5, capacity);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // load all edges into graph
    public void loadEdges(Graph graph) {

        try {

            Connection conn = DBConnection.getConnection();

            String sql = "SELECT * FROM edges";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String sourceId = rs.getString("source_id");
                String destId = rs.getString("destination_id");

                Node source = graph.getNodeById(sourceId);
                Node dest = graph.getNodeById(destId);

                double cost = rs.getDouble("cost");
                double time = rs.getDouble("time");
                int capacity = rs.getInt("capacity");

                if (source != null && dest != null) {
                    graph.addEdge(source, dest, cost, time, capacity);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteEdge(String sourceId, String destId) {

        try {

            Connection conn = DBConnection.getConnection();

            String sql = "DELETE FROM edges WHERE source_id=? AND destination_id=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, sourceId);
            ps.setString(2, destId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateEdge(String sourceId, String destId,
            double cost, double time, int capacity) {

try {

Connection conn = DBConnection.getConnection();

String sql = "UPDATE edges SET cost=?, time=?, capacity=? WHERE source_id=? AND destination_id=?";

PreparedStatement ps = conn.prepareStatement(sql);

ps.setDouble(1, cost);
ps.setDouble(2, time);
ps.setInt(3, capacity);
ps.setString(4, sourceId);
ps.setString(5, destId);

ps.executeUpdate();

} catch (Exception e) {
e.printStackTrace();
}
}
    
}