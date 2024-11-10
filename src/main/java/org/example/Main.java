package org.example;

import org.example.database.DatabaseInitializer;
import org.example.gui.MainMenu;


public class Main {
    public static void main(String[] args) {
        // Inicjalizacja bazy danych
        DatabaseInitializer.initializeDatabase();

        // Inicjalizacja terminala Lanterna
        MainMenu.runMainMenu();
    }
}
