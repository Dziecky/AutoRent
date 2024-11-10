package org.example.gui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import org.example.sessions.UserSession;
import org.example.services.AuthenticationService;

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

            if (UserSession.getInstance().isAuthenticated()) {
                Button userPanelButton = new Button(" Zalogowany jako: " + UserSession.getInstance().getUsername(), () -> {
                    UserPanelDialog.showUserPanelDialog(textGUI);
                });
                userPanelButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
                mainPanel.addComponent(userPanelButton);
            } else {
                Button loginButton = new Button(" Zaloguj się ", () -> {
                    LoginDialog.showLoginDialog(textGUI);
                    if (UserSession.getInstance().isAuthenticated()) {
                        try {
                            updateMainMenuWithUserPanel(textGUI);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                loginButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
                mainPanel.addComponent(loginButton);
            }

            Button browseCarsButton = new Button(" Przeglądaj dostępne samochody ", () -> {
                MessageDialog.showMessageDialog(textGUI, "Przeglądanie", "Funkcja w budowie");
            });
            browseCarsButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
            mainPanel.addComponent(browseCarsButton);


            Button exitButton = new Button(" Zakończ ", window::close);
            exitButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
            mainPanel.addComponent(exitButton);

            window.setComponent(mainPanel.withBorder(Borders.singleLine()));
            textGUI.addWindowAndWait(window);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void updateMainMenuWithUserPanel(WindowBasedTextGUI textGUI) throws IOException {
        mainPanel.removeAllComponents();

        Button userPanelButton = new Button(" Zalogowany jako: " + UserSession.getInstance().getUsername(), () -> {
            UserPanelDialog.showUserPanelDialog(textGUI);
        });
        userPanelButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        mainPanel.addComponent(userPanelButton);

        Button browseCarsButton = new Button(" Przeglądaj dostępne samochody ", () -> {
            MessageDialog.showMessageDialog(textGUI, "Przeglądanie", "Funkcja w budowie");
        });
        browseCarsButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        mainPanel.addComponent(browseCarsButton);

        Button exitButton = new Button(" Zakończ ", textGUI.getActiveWindow()::close);
        exitButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        mainPanel.addComponent(exitButton);

        textGUI.updateScreen();
    }
}
