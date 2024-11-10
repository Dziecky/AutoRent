package org.example.gui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import org.example.services.UserService;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Arrays;

public class RegisterDialog {
    private static final UserService userService = new UserService();

    public static void showRegisterDialog(WindowBasedTextGUI textGUI) {
        TerminalSize screenSize = textGUI.getScreen().getTerminalSize();
        Panel registerPanel = new Panel();
        registerPanel.setLayoutManager(new GridLayout(2));

        Label nameLabel = new Label("Imię:");
        TextBox nameBox = new TextBox();
        Label surnameLabel = new Label("Nazwisko:");
        TextBox surnameBox = new TextBox();
        Label usernameLabel = new Label("Login:");
        TextBox usernameBox = new TextBox();
        Label passwordLabel = new Label("Hasło:");
        TextBox passwordBox = new TextBox().setMask('*');
        Label emailLabel = new Label("Email:");
        TextBox emailBox = new TextBox();
        Label phoneLabel = new Label("Numer telefonu:");
        TextBox phoneBox = new TextBox();

        registerPanel.addComponent(nameLabel);
        registerPanel.addComponent(nameBox);
        registerPanel.addComponent(surnameLabel);
        registerPanel.addComponent(surnameBox);
        registerPanel.addComponent(usernameLabel);
        registerPanel.addComponent(usernameBox);
        registerPanel.addComponent(passwordLabel);
        registerPanel.addComponent(passwordBox);
        registerPanel.addComponent(emailLabel);
        registerPanel.addComponent(emailBox);
        registerPanel.addComponent(phoneLabel);
        registerPanel.addComponent(phoneBox);

        Button registerButton = new Button(" Zarejestruj ", () -> {
            String name = nameBox.getText();
            String surname = surnameBox.getText();
            String login = usernameBox.getText();
            String password = passwordBox.getText();
            String email = emailBox.getText();
            String phone = phoneBox.getText();

            if (name.isEmpty() || surname.isEmpty() || login.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                MessageDialog.showMessageDialog(textGUI, "Błąd", "Wszystkie pola muszą być wypełnione");
            } else if (userService.registerUser(name, surname, login, BCrypt.hashpw(password, BCrypt.gensalt()), email, phone)) {
                MessageDialog.showMessageDialog(textGUI, "Sukces", "Zarejestrowano pomyślnie");
                textGUI.getActiveWindow().close();
            } else {
                MessageDialog.showMessageDialog(textGUI, "Błąd", "Nie udało się zarejestrować użytkownika");
            }
        });

        Button cancelButton = new Button(" Anuluj ", () -> textGUI.getActiveWindow().close());
        registerPanel.addComponent(registerButton);
        registerPanel.addComponent(cancelButton);

        BasicWindow registerWindow = new BasicWindow("Rejestracja");
        registerWindow.setComponent(registerPanel.withBorder(Borders.singleLine()));
        textGUI.addWindowAndWait(registerWindow);
    }
}
