package com.supplychain.db;

import com.supplychain.model.Node;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NodeDAO {

    // insert node
    public void insertNode(Node node) {

        try {

            Connection conn = DBConnection.getConnection();

            String sql = "INSERT INTO nodes VALUES (?,?,?,?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, node.getId());
            ps.setString(2, node.getName());
            ps.setString(3, node.getType());
            ps.setInt(4, node.getCapacity());
            ps.setInt(5, node.getHealth());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // fetch all nodes
    public List<Node> getAllNodes() {

        List<Node> nodes = new ArrayList<>();

        try {

            Connection conn = DBConnection.getConnection();

            String sql = "SELECT * FROM nodes";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Node node = new Node(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getInt("capacity"),
                        rs.getInt("health")
                );

                nodes.add(node);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return nodes;
    }
    public void updateNode(Node node) {

        try {

            Connection conn = DBConnection.getConnection();

            String sql = "UPDATE nodes SET name=?, type=?, capacity=?, health=? WHERE id=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, node.getName());
            ps.setString(2, node.getType());
            ps.setInt(3, node.getCapacity());
            ps.setInt(4, node.getHealth());
            ps.setString(5, node.getId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteNode(String nodeId) {

        try {

            Connection conn = DBConnection.getConnection();

            String sql = "DELETE FROM nodes WHERE id=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, nodeId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}