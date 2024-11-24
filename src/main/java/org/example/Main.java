package org.example;

import org.example.database.DatabaseConnection;
import org.example.views.MainMenu;

import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws SQLException {
        // Inicjalizacja bazy danych
        //DatabaseInitializer.initializeDatabase();
        DatabaseConnection.getConnection();

        // Inicjalizacja terminala Lanterna
        MainMenu.runMainMenu();
    }
}
