package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/autorent";
    private static final String USER = "dziecky";
    private static final String PASSWORD = "JDA1.GkhsAzSmV1.";
    private static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());

    private static Connection connection;

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Nie udało się połączyć z bazą danych", e);
            }
        }
        return connection;
    }
}
