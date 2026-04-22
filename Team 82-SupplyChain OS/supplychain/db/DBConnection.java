package com.supplychain.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/supplychain";
    private static final String USER = "root";
    private static final String PASSWORD = "Ssu@2005"; // change if needed

    public static Connection getConnection() {

        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}