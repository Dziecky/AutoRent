package org.example.gui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import org.example.sessions.UserSession;

import java.io.IOException;
import java.util.Arrays;

public class MainMenu {

    private static Panel mainPanel;

    public static void runMainMenu() {
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory().setInitialTerminalSize(new TerminalSize(120, 36));
        try (Screen screen = terminalFactory.createScreen()) {
            screen.startScreen();

            TerminalSize screenSize = screen.getTerminalSize();
            WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
            BasicWindow window = new BasicWindow("Wypożyczalnia Samochodów");

            window.setSize(new TerminalSize(screenSize.getColumns() / 4 + 7, screenSize.getRows()));
            window.setPosition(new TerminalPosition(0, 0));
            window.setHints(Arrays.asList(Window.Hint.NO_DECORATIONS, Window.Hint.FIXED_POSITION, Window.Hint.FIXED_SIZE));

            mainPanel = new Panel();
            mainPanel.setLayoutManager(new GridLayout(1));

            // Inicjalizacja głównego menu
            updateMainMenu(textGUI, window);

            window.setComponent(mainPanel.withBorder(Borders.singleLine()));
            textGUI.addWindowAndWait(window);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateMainMenu(WindowBasedTextGUI textGUI, BasicWindow window) throws IOException {
        mainPanel.removeAllComponents();

        if (UserSession.getInstance().isAuthenticated()) {
            // Przycisk panelu użytkownika
            Button userPanelButton = new Button(" Zalogowany jako: " + UserSession.getInstance().getUsername(), () -> {
                UserPanelDialog.showUserPanelDialog(textGUI);
            });
            userPanelButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
            mainPanel.addComponent(userPanelButton);

            // Przycisk wylogowania
            Button logoutButton = new Button(" Wyloguj się ", () -> {
                UserSession.getInstance().logout();
                try {
                    updateMainMenu(textGUI, window);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            logoutButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
            mainPanel.addComponent(logoutButton);
            if (UserSession.getInstance().getRole().equals("OWNER")) {
                // Przycisk dodawania samochodów
                Button addCarButton = new Button(" Dodaj samochód ", () -> {
                    AddCarDialog.showAddCarDialog(textGUI);
                });
                addCarButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
                mainPanel.addComponent(addCarButton);

                // Przycisk przeglądania wypożyczeń
                Button viewRentalsButton = new Button(" Przeglądaj wypożyczenia ", () -> {
                    RentalsDialog.showRentalsDialog(textGUI);
                });
                viewRentalsButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
                mainPanel.addComponent(viewRentalsButton);
            }
        } else {
            // Przycisk logowania
            Button loginButton = new Button(" Zaloguj się ", () -> {
                LoginDialog.showLoginDialog(textGUI);
                try {
                    updateMainMenu(textGUI, window);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            loginButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
            mainPanel.addComponent(loginButton);
        }

        // Przyciski wspólne dla obu stanów
        Button browseCarsButton = new Button(" Przeglądaj dostępne samochody ", () -> {
            CarsDialog.showAvailableCarsDialog(textGUI);
        });
        browseCarsButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        mainPanel.addComponent(browseCarsButton);

        // Użyj przekazanego okna
        Button exitButton = new Button(" Zakończ ", window::close);
        exitButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        mainPanel.addComponent(exitButton);

        textGUI.updateScreen();
    }
}
