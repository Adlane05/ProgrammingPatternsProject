package org.example.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:./Project01/src/main/resources/LibraryDatabase.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}