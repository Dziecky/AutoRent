package org.example;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import org.example.database.DatabaseInitializer;

import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // Inicjalizacja bazy danych
        DatabaseInitializer.initializeDatabase();

        // Inicjalizacja terminala Lanterna
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        try (Screen screen = terminalFactory.createScreen()) {
            screen.startScreen();

            // Utworzenie głównego panelu aplikacji
            WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
            BasicWindow window = new BasicWindow("Wypożyczalnia Samochodów");

            // Główny kontener z przyciskami
            Panel mainPanel = new Panel();
            mainPanel.setLayoutManager(new GridLayout(1)); // Jedna kolumna

            // Przycisk do przeglądania dostępnych samochodów
            Button browseCarsButton = new Button("Przeglądaj dostępne samochody", () ->
                    MessageDialog.showMessageDialog(textGUI, "Przeglądanie", "Funkcja w budowie"));
            mainPanel.addComponent(browseCarsButton);

            // Przycisk do logowania
            Button loginButton = new Button("Zaloguj się", () ->
                    MessageDialog.showMessageDialog(textGUI, "Logowanie", "Funkcja w budowie"));
            mainPanel.addComponent(loginButton);

            // Przycisk do rejestracji
            Button registerButton = new Button("Utwórz konto", () ->
                    MessageDialog.showMessageDialog(textGUI, "Rejestracja", "Funkcja w budowie"));
            mainPanel.addComponent(registerButton);

            // Przycisk do zakończenia programu
            Button exitButton = new Button("Zakończ", window::close);
            mainPanel.addComponent(exitButton);

            // Dodanie głównego panelu do okna
            window.setComponent(mainPanel);

            // Ustawienia okna
            window.setHints(Arrays.asList(Window.Hint.CENTERED));

            // Uruchomienie interfejsu GUI
            textGUI.addWindowAndWait(window);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
