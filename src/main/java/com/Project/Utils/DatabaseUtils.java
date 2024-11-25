package com.Project.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtils {
    private static final String DB_URL = "jdbc:postgresql://localhost:1405/OnlineMarket";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "1405";

    private static Connection connection = null;

    private DatabaseUtils() {} // Java apparently creates a public constructor if there is none.

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("Database connection established.");
            } catch (SQLException e) {
                System.err.println("Error establishing database connection: " + e.getMessage());
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}
