package org.example.gui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import org.example.models.Rental;
import org.example.services.RentalService;
import org.example.services.UserService;
import org.example.sessions.UserSession;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class UserPanelDialog {
    private static final UserService userService = new UserService();
    private static final RentalService rentalService = new RentalService();
    private static Panel userPanel;

    public static void showUserPanelDialog(WindowBasedTextGUI textGUI) {
        String username = UserSession.getInstance().getUsername();
        userPanel = new Panel();
        userPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        TerminalSize screenSize = textGUI.getScreen().getTerminalSize();

        // Wyświetlenie danych użytkownika
        refreshUserPanel(textGUI, username);

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
                refreshUserPanel(textGUI, username);
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

    private static void refreshUserPanel(WindowBasedTextGUI textGUI, String username) {
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
            showEditUserDialog(textGUI, username);
        });
        editButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        userPanel.addComponent(editButton);

        // Wyświetlenie listy aktualnie wypożyczonych samochodów
        Label rentedCarsLabel = new Label("\nTwoje aktualnie wypożyczone samochody:");
        userPanel.addComponent(rentedCarsLabel);

        List<Rental> rentedCars = userService.getUserCurrentRentals(username);
        if (rentedCars.isEmpty()) {
            userPanel.addComponent(new Label("Brak aktualnie wypożyczonych samochodów."));
        } else {
            for (Rental rental : rentedCars) {
                Button carButton = new Button(rental.getCar().brand() + " " + rental.getCar().model(), () -> {
                    showRentalDetailsDialog(textGUI, rental);
                });
                userPanel.addComponent(carButton);
            }
        }
    }

    private static void showRentalDetailsDialog(WindowBasedTextGUI textGUI, Rental rental) {
        TerminalSize screenSize = textGUI.getScreen().getTerminalSize();

        Panel detailsPanel = new Panel();
        detailsPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        // Wyświetlanie szczegółów samochodu
        detailsPanel.addComponent(new Label("Szczegóły samochodu:"));
        detailsPanel.addComponent(new Label("Marka: " + rental.getCar().brand()));
        detailsPanel.addComponent(new Label("Model: " + rental.getCar().model()));
        detailsPanel.addComponent(new Label("Rok: " + rental.getCar().year()));
        detailsPanel.addComponent(new Label("Moc: " + rental.getCar().power() + " KM"));
        detailsPanel.addComponent(new Label("Pojemność silnika: " + rental.getCar().engineCapacity() + " l"));
        detailsPanel.addComponent(new Label("Rodzaj paliwa: " + rental.getCar().fuelType()));
        detailsPanel.addComponent(new Label("Skrzynia biegów: " + rental.getCar().transmission()));
        detailsPanel.addComponent(new Label("Ilość miejsc: " + rental.getCar().seats()));
        detailsPanel.addComponent(new Label("Cena za dzień: " + rental.getCar().pricePerDay() + " PLN"));
        detailsPanel.addComponent(new Separator(Direction.HORIZONTAL));

        // Wyświetlanie szczegółów wypożyczenia
        detailsPanel.addComponent(new Label("Szczegóły wypożyczenia:"));
        detailsPanel.addComponent(new Label("Data wypożyczenia: " + rental.getRentalDate()));
        detailsPanel.addComponent(new Label("Data zwrotu: " + rental.getReturnDate()));

        // Sprawdzenie, czy można anulować wypożyczenie
        LocalDate today = LocalDate.now();
        LocalDate rentalStartDate = rental.getRentalDate();

        if (today.isBefore(rentalStartDate.minusDays(2))) {
            // Jeśli dzisiejsza data jest co najmniej 3 dni przed datą rozpoczęcia wypożyczenia
            Button cancelRentalButton = new Button(" Anuluj wypożyczenie ", () -> {
                boolean success = rentalService.cancelRental(rental.getId());
                if (success) {
                    MessageDialog.showMessageDialog(textGUI, "Sukces", "Wypożyczenie zostało anulowane.");
                    // Odświeżamy panel użytkownika
                    refreshUserPanel(textGUI, UserSession.getInstance().getUsername());
                    textGUI.getActiveWindow().close();
                } else {
                    MessageDialog.showMessageDialog(textGUI, "Błąd", "Nie udało się anulować wypożyczenia.");
                }
            });
            detailsPanel.addComponent(cancelRentalButton);
        }

        BasicWindow detailsWindow = new BasicWindow("Szczegóły Wypożyczenia");
        detailsWindow.setSize(new TerminalSize(screenSize.getColumns() / 3, screenSize.getRows()));
        detailsWindow.setPosition(new TerminalPosition(screenSize.getColumns() / 4 * 3 - 7, 0));
        detailsWindow.setHints(Arrays.asList(Window.Hint.NO_DECORATIONS, Window.Hint.FIXED_POSITION, Window.Hint.FIXED_SIZE));
        detailsWindow.setComponent(detailsPanel.withBorder(Borders.singleLine()));
        detailsWindow.setCloseWindowWithEscape(true);
        textGUI.addWindowAndWait(detailsWindow);
    }
}
