package org.example.gui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import org.example.services.UserService;
import org.example.sessions.UserSession;

import java.util.Arrays;
import java.util.List;

public class UserPanelDialog {
    private static final UserService userService = new UserService();
    private static Panel userPanel;

    public static void showUserPanelDialog(WindowBasedTextGUI textGUI) {
        String username = UserSession.getInstance().getUsername();
        userPanel = new Panel();
        userPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        TerminalSize screenSize = textGUI.getScreen().getTerminalSize();

        // Wyświetlenie danych użytkownika
        refreshUserPanel(username);

        // Utworzenie okna panelu użytkownika
        BasicWindow userWindow = new BasicWindow("Panel Użytkownika");
        setSizeConf(screenSize, userWindow, userPanel);
        userWindow.setCloseWindowWithEscape(true);
        textGUI.addWindowAndWait(userWindow);
    }

    private static void setSizeConf(TerminalSize screenSize, BasicWindow userWindow, Panel userPanel) {
        userWindow.setSize(new TerminalSize(screenSize.getColumns(), screenSize.getRows()));
        userWindow.setPosition(new TerminalPosition(screenSize.getColumns() / 4 + 7, 0));
        userWindow.setHints(Arrays.asList(Window.Hint.NO_DECORATIONS, Window.Hint.FIXED_POSITION, Window.Hint.FIXED_SIZE));
        userWindow.setComponent(userPanel.withBorder(Borders.singleLine()));
    }

    private static void showEditUserDialog(WindowBasedTextGUI textGUI, String username) {
        Panel editPanel = new Panel();
        editPanel.setLayoutManager(new GridLayout(2));
        TerminalSize screenSize = textGUI.getScreen().getTerminalSize();

        TextBox nameBox = new TextBox().setText(userService.getUserName(username));
        TextBox surnameBox = new TextBox().setText(userService.getUserSurname(username));
        TextBox emailBox = new TextBox().setText(userService.getUserEmail(username));
        TextBox phoneBox = new TextBox().setText(userService.getUserPhone(username));

        editPanel.addComponent(new Label("Imię:"));
        editPanel.addComponent(nameBox);
        editPanel.addComponent(new Label("Nazwisko:"));
        editPanel.addComponent(surnameBox);
        editPanel.addComponent(new Label("Email:"));
        editPanel.addComponent(emailBox);
        editPanel.addComponent(new Label("Numer telefonu:"));
        editPanel.addComponent(phoneBox);

        Button saveButton = new Button(" Zapisz ", () -> {
            if (userService.updateUser(username, nameBox.getText(), surnameBox.getText(), emailBox.getText(), phoneBox.getText())) {
                MessageDialog.showMessageDialog(textGUI, "Sukces", "Dane zostały zaktualizowane");
                refreshUserPanel(username);
                textGUI.getActiveWindow().close();
            } else {
                MessageDialog.showMessageDialog(textGUI, "Błąd", "Nie udało się zaktualizować danych");
            }
        });

        Button cancelButton = new Button(" Anuluj ", () -> textGUI.getActiveWindow().close());
        editPanel.addComponent(saveButton);
        editPanel.addComponent(cancelButton);

        BasicWindow editWindow = new BasicWindow("Edytuj Dane Użytkownika");
        setSizeConf(screenSize, editWindow, editPanel);
        textGUI.addWindowAndWait(editWindow);
    }

    private static void refreshUserPanel(String username) {
        userPanel.removeAllComponents();

        // Wyświetlenie zaktualizowanych danych użytkownika
        Label usernameLabel = new Label("Login: " + username);
        Label nameLabel = new Label("Imię: " + userService.getUserName(username));
        Label surnameLabel = new Label("Nazwisko: " + userService.getUserSurname(username));
        Label emailLabel = new Label("Email: " + userService.getUserEmail(username));
        Label phoneLabel = new Label("Numer telefonu: " + userService.getUserPhone(username));

        userPanel.addComponent(usernameLabel);
        userPanel.addComponent(nameLabel);
        userPanel.addComponent(surnameLabel);
        userPanel.addComponent(emailLabel);
        userPanel.addComponent(phoneLabel);

        // Przycisk do edycji danych użytkownika
        Button editButton = new Button(" Edytuj dane ", () -> {
            showEditUserDialog((WindowBasedTextGUI) userPanel.getTextGUI(), username);
        });
        editButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        userPanel.addComponent(editButton);

        // Wyświetlenie listy aktualnie wypożyczonych samochodów
        Label rentedCarsLabel = new Label("\nTwoje aktualnie wypożyczone samochody:");
        userPanel.addComponent(rentedCarsLabel);

        List<String> rentedCars = userService.getUserRentedCars(username);
        if (rentedCars.isEmpty()) {
            userPanel.addComponent(new Label("Brak aktualnie wypożyczonych samochodów."));
        } else {
            for (String car : rentedCars) {
                userPanel.addComponent(new Label("- " + car));
            }
        }
    }
}
