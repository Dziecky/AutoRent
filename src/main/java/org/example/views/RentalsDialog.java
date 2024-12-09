//package org.example.views;
//
//import com.googlecode.lanterna.TerminalPosition;
//import com.googlecode.lanterna.TerminalSize;
//import com.googlecode.lanterna.gui2.*;
//import org.example.controllers.RentalController;
//import org.example.models.Car;
//import org.example.models.Rental;
//import org.example.models.User;
//
//import java.util.Arrays;
//import java.util.List;
//
//public class RentalsDialog {
//    private static final RentalController rentalController = new RentalController();
//
//    public static void showRentalsDialog(WindowBasedTextGUI textGUI) {
//        TerminalSize screenSize = textGUI.getScreen().getTerminalSize();
//
//        Panel rentalsPanel = new Panel();
//        rentalsPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
//
//        List<Rental> rentals = rentalController.getAllRentals();
//        if (rentals.isEmpty()) {
//            rentalsPanel.addComponent(new Label("Brak wypożyczeń."));
//        } else {
//            for (Rental rental : rentals) {
//                Button rentalButton = new Button("Samochód: " + rental.getCar().brand() + " " + rental.getCar().model() +
//                        ", Użytkownik: " + rental.getUser().getLogin(), () -> {
//                    showRentalDetailsDialog(textGUI, rental);
//                });
//                rentalsPanel.addComponent(rentalButton);
//            }
//        }
//
//        BasicWindow rentalsWindow = new BasicWindow("Wypożyczenia");
//        rentalsWindow.setSize(new TerminalSize(screenSize.getColumns(), screenSize.getRows()));
//        rentalsWindow.setPosition(new TerminalPosition(screenSize.getColumns() / 4 + 7, 0));
//        rentalsWindow.setHints(Arrays.asList(Window.Hint.NO_DECORATIONS, Window.Hint.FIXED_POSITION, Window.Hint.FIXED_SIZE));
//        rentalsWindow.setComponent(rentalsPanel.withBorder(Borders.singleLine()));
//        rentalsWindow.setCloseWindowWithEscape(true);
//        textGUI.addWindowAndWait(rentalsWindow);
//    }
//
//    private static void showRentalDetailsDialog(WindowBasedTextGUI textGUI, Rental rental) {
//        TerminalSize screenSize = textGUI.getScreen().getTerminalSize();
//
//        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(rental.getRentalDate(), rental.getReturnDate()) + 1;
//        double totalPrice = daysBetween * rental.getCar().pricePerDay();
//
//        Panel detailsPanel = new Panel();
//        detailsPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
//
//        // Wyświetlanie szczegółów samochodu
//        Car car = rental.getCar();
//        detailsPanel.addComponent(new Label("Szczegóły samochodu:"));
//        detailsPanel.addComponent(new Label("ID: " + car.id()));
//        detailsPanel.addComponent(new Label("Marka: " + car.brand()));
//        detailsPanel.addComponent(new Label("Model: " + car.model()));
//        detailsPanel.addComponent(new Label("Rok: " + car.year()));
//        detailsPanel.addComponent(new Label("Moc: " + car.power() + " KM"));
//        detailsPanel.addComponent(new Label("Pojemność silnika: " + car.engineCapacity() + " l"));
//        detailsPanel.addComponent(new Label("Rodzaj paliwa: " + car.fuelType()));
//        detailsPanel.addComponent(new Label("Skrzynia biegów: " + car.transmission()));
//        detailsPanel.addComponent(new Label("Ilość miejsc: " + car.seats()));
//        detailsPanel.addComponent(new Label("Cena za dzień: " + car.pricePerDay() + " PLN"));
//        detailsPanel.addComponent(new Separator(Direction.HORIZONTAL));
//
//        // Wyświetlanie szczegółów użytkownika
//        User user = rental.getUser();
//        detailsPanel.addComponent(new Label("Szczegóły użytkownika:"));
//        detailsPanel.addComponent(new Label("Imię: " + user.getName()));
//        detailsPanel.addComponent(new Label("Nazwisko: " + user.getSurname()));
//        detailsPanel.addComponent(new Label("Login: " + user.getLogin()));
//        detailsPanel.addComponent(new Label("Email: " + user.getEmail()));
//        detailsPanel.addComponent(new Label("Numer telefonu: " + user.getPhone()));
//        detailsPanel.addComponent(new Separator(Direction.HORIZONTAL));
//
//        // Wyświetlanie szczegółów wypożyczenia
//        detailsPanel.addComponent(new Label("Data wypożyczenia: " + rental.getRentalDate()));
//        detailsPanel.addComponent(new Label("Data zwrotu: " + rental.getReturnDate()));
//        detailsPanel.addComponent(new Label("Liczba dni: " + daysBetween));
//        detailsPanel.addComponent(new Label("Cena całkowita: " + totalPrice + " PLN"));
//
//        BasicWindow detailsWindow = new BasicWindow("Szczegóły Wypożyczenia");
//        detailsWindow.setSize(new TerminalSize(screenSize.getColumns() / 3, screenSize.getRows()));
//        detailsWindow.setPosition(new TerminalPosition(screenSize.getColumns() / 4 * 3 - 7, 0));
//        detailsWindow.setHints(Arrays.asList(Window.Hint.NO_DECORATIONS, Window.Hint.FIXED_POSITION, Window.Hint.FIXED_SIZE));
//        detailsWindow.setComponent(detailsPanel.withBorder(Borders.singleLine()));
//        detailsWindow.setCloseWindowWithEscape(true);
//        textGUI.addWindowAndWait(detailsWindow);
//    }
//}
