package org.example.gui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import org.example.services.AuthenticationService;
import org.example.sessions.UserSession;

import java.util.Arrays;

public class LoginDialog {
    private static final AuthenticationService authenticationService = new AuthenticationService();

    public static void showLoginDialog(WindowBasedTextGUI textGUI) {
        Panel loginPanel = new Panel();
        loginPanel.setLayoutManager(new GridLayout(2));

        TerminalSize screenSize = textGUI.getScreen().getTerminalSize();

        Label usernameLabel = new Label("Login:");
        TextBox usernameBox = new TextBox();
        Label passwordLabel = new Label("Hasło:");
        TextBox passwordBox = new TextBox().setMask('*');

        loginPanel.addComponent(usernameLabel);
        loginPanel.addComponent(usernameBox);
        loginPanel.addComponent(passwordLabel);
        loginPanel.addComponent(passwordBox);

        Button loginButton = new Button(" Zaloguj ", () -> {
            String login = usernameBox.getText();
            String password = passwordBox.getText();

            if (authenticationService.authenticateUser(login, password)) {
                MessageDialog.showMessageDialog(textGUI, "Sukces", "Zalogowano pomyślnie");
                UserSession.getInstance().setAuthenticated(true, login);
                textGUI.getActiveWindow().close();
            } else {
                MessageDialog.showMessageDialog(textGUI, "Błąd", "Nieprawidłowy login lub hasło");
            }
        });

        Button cancelButton = new Button(" Anuluj ", () -> textGUI.getActiveWindow().close());
        loginPanel.addComponent(loginButton);
        loginPanel.addComponent(cancelButton);

        // Przycisk do rejestracji
        Button registerButton = new Button(" Utwórz konto ", () -> {
            RegisterDialog.showRegisterDialog(textGUI);
        });
        registerButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        loginPanel.addComponent(registerButton);

        BasicWindow loginWindow = new BasicWindow("Logowanie");
        // Ustawienia okna logowania
        loginWindow.setSize(new TerminalSize(screenSize.getColumns() / 2, screenSize.getRows()));
        loginWindow.setPosition(new TerminalPosition(screenSize.getColumns() / 2, screenSize.getRows()/3));
        loginWindow.setHints(Arrays.asList(Window.Hint.NO_DECORATIONS, Window.Hint.FIXED_POSITION));

        loginWindow.setComponent(loginPanel.withBorder(Borders.singleLine()));
        textGUI.addWindowAndWait(loginWindow);
    }
}
