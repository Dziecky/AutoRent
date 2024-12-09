//package org.example.views;
//
//import com.googlecode.lanterna.TerminalPosition;
//import com.googlecode.lanterna.TerminalSize;
//import com.googlecode.lanterna.gui2.*;
//import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
//import org.example.controllers.CarController;
//import org.example.controllers.RentalController;
//import org.example.controllers.UserController;
//import org.example.models.Car;
//import org.example.models.Rental;
//import org.example.sessions.UserSession;
//
//import java.io.IOException;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.time.format.DateTimeParseException;
//import java.util.Arrays;
//import java.util.List;
//
//public class CarsDialog {
//    private static final CarController carController = new CarController();
//    private static final RentalController rentalController = new RentalController();
//    private static Panel carsPanel;
//    private static WindowBasedTextGUI textGUI;
//
//    public static void showAvailableCarsDialog(WindowBasedTextGUI textGUI) {
//        CarsDialog.textGUI = textGUI;
//        TerminalSize screenSize = textGUI.getScreen().getTerminalSize();
//
//        // Initialize panels
//        Panel mainPanel = new Panel();
//        mainPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
//
//        // Filtering panel
//        Panel filtersPanel = new Panel();
//        filtersPanel.setLayoutManager(new GridLayout(4));
//
//        // Filtering components
//        TextBox brandFilter = new TextBox();
//        TextBox modelFilter = new TextBox();
//        TextBox yearFromFilter = new TextBox();
//        TextBox yearToFilter = new TextBox();
//        ComboBox<String> transmissionFilter = new ComboBox<>("", "Manualna", "Automatyczna");
//        ComboBox<String> fuelTypeFilter = new ComboBox<>("", "Benzyna", "Diesel", "Elektryczny", "Hybryda");
//        TextBox dateFromFilter = new TextBox();
//        TextBox dateToFilter = new TextBox();
//
//        // Add filtering components to panel
//        filtersPanel.addComponent(new Label("Marka:"));
//        filtersPanel.addComponent(brandFilter);
//        filtersPanel.addComponent(new Label("Model:"));
//        filtersPanel.addComponent(modelFilter);
//        filtersPanel.addComponent(new Label("Rok od:"));
//        filtersPanel.addComponent(yearFromFilter);
//        filtersPanel.addComponent(new Label("do:"));
//        filtersPanel.addComponent(yearToFilter);
//        filtersPanel.addComponent(new Label("Skrzynia biegów:"));
//        filtersPanel.addComponent(transmissionFilter);
//        filtersPanel.addComponent(new Label("Rodzaj paliwa:"));
//        filtersPanel.addComponent(fuelTypeFilter);
//        filtersPanel.addComponent(new Label("Data od (YYYY-MM-DD):"));
//        filtersPanel.addComponent(dateFromFilter);
//        filtersPanel.addComponent(new Label("do (YYYY-MM-DD):"));
//        filtersPanel.addComponent(dateToFilter);
//
//        Button filterButton = new Button(" Filtruj ", () -> {
//            updateCarList(
//                    brandFilter.getText(),
//                    modelFilter.getText(),
//                    yearFromFilter.getText(),
//                    yearToFilter.getText(),
//                    transmissionFilter.getSelectedItem(),
//                    fuelTypeFilter.getSelectedItem(),
//                    dateFromFilter.getText(),
//                    dateToFilter.getText()
//            );
//        });
//        Button clearButton = new Button(" Wyczyść filtry ", () -> {
//            brandFilter.setText("");
//            modelFilter.setText("");
//            yearFromFilter.setText("");
//            yearToFilter.setText("");
//            transmissionFilter.setSelectedItem(null);
//            fuelTypeFilter.setSelectedItem(null);
//            dateFromFilter.setText("");
//            dateToFilter.setText("");
//            updateCarList("", "", "", null, null, null, null, null);
//        });
//
//        filtersPanel.addComponent(filterButton);
//        filtersPanel.addComponent(clearButton);
//
//        // Initialize cars panel
//        carsPanel = new Panel();
//        carsPanel.setLayoutManager(new GridLayout(2));
//
//        // Add panels to main panel
//        mainPanel.addComponent(filtersPanel.withBorder(Borders.singleLine("Filtry")));
//        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));
//        mainPanel.addComponent(carsPanel);
//
//        // Get all cars initially
//        updateCarList("", "", "", null, null, null, null, null);
//
//        // Create window
//        BasicWindow carsWindow = new BasicWindow("Dostępne Samochody");
//        carsWindow.setSize(new TerminalSize(screenSize.getColumns(), screenSize.getRows()));
//        carsWindow.setPosition(new TerminalPosition(screenSize.getColumns() / 4 + 7, 0));
//        carsWindow.setHints(Arrays.asList(Window.Hint.NO_DECORATIONS, Window.Hint.FIXED_POSITION, Window.Hint.FIXED_SIZE));
//        carsWindow.setComponent(mainPanel.withBorder(Borders.singleLine()));
//        carsWindow.setCloseWindowWithEscape(true);
//        textGUI.addWindowAndWait(carsWindow);
//    }
//
//    private static void updateCarList(String brand, String model, String yearFromStr, String yearToStr, String transmission, String fuelType, String dateFromStr, String dateToStr) {
//        carsPanel.removeAllComponents();
//
//        List<Car> filteredCars = carController.getFilteredCars(brand, model, yearFromStr, yearToStr, transmission, fuelType, dateFromStr, dateToStr);
//
//        if (filteredCars.isEmpty()) {
//            carsPanel.addComponent(new Label("Brak dostępnych samochodów dla wybranych filtrów."));
//        } else {
//            for (Car car : filteredCars) {
//                Button carButton = new Button(" " + car.brand() + " " + car.model() + " (" + car.year() + ") ", () -> {
//                    showCarDetailsDialog(textGUI, car);
//                });
//                carsPanel.addComponent(carButton);
//                carsPanel.addComponent(new Label(car.pricePerDay() + " PLN/dzień"));
//            }
//        }
//        try {
//            textGUI.updateScreen();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private static void showCarDetailsDialog(WindowBasedTextGUI textGUI, Car car) {
//        TerminalSize screenSize = textGUI.getScreen().getTerminalSize();
//
//        Panel carDetailsPanel = new Panel();
//        carDetailsPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
//
//        carDetailsPanel.addComponent(new Label("Szczegóły samochodu:"));
//        carDetailsPanel.addComponent(new Label("Marka: " + car.brand()));
//        carDetailsPanel.addComponent(new Label("Model: " + car.model()));
//        carDetailsPanel.addComponent(new Label("Rok: " + car.year()));
//        carDetailsPanel.addComponent(new Label("Moc: " + car.power() + " KM"));
//        carDetailsPanel.addComponent(new Label("Pojemność silnika: " + car.engineCapacity() + " l"));
//        carDetailsPanel.addComponent(new Label("Rodzaj paliwa: " + car.fuelType()));
//        carDetailsPanel.addComponent(new Label("Skrzynia biegów: " + car.transmission()));
//        carDetailsPanel.addComponent(new Label("Ilość miejsc: " + car.seats()));
//        carDetailsPanel.addComponent(new Label("Cena za dzień: " + car.pricePerDay() + " PLN"));
//
//        if (UserSession.getInstance().isAuthenticated()) {
//            if (UserSession.getInstance().getRole().equals("USER")) {
//                // Ask for rental dates
//                TextBox dateFromBox = new TextBox();
//                TextBox dateToBox = new TextBox();
//
//                carDetailsPanel.addComponent(new Label("Data od (YYYY-MM-DD):"));
//                carDetailsPanel.addComponent(dateFromBox);
//                carDetailsPanel.addComponent(new Label("Data do (YYYY-MM-DD):"));
//                carDetailsPanel.addComponent(dateToBox);
//
//                Button rentButton = new Button(" Wypożycz ", () -> {
//                    String dateFromStr = dateFromBox.getText();
//                    String dateToStr = dateToBox.getText();
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//                    try {
//                        LocalDate dateFrom = LocalDate.parse(dateFromStr, formatter);
//                        LocalDate dateTo = LocalDate.parse(dateToStr, formatter);
//
//                        if (dateFrom.isAfter(dateTo)) {
//                            MessageDialog.showMessageDialog(textGUI, "Błąd", "Data rozpoczęcia musi być przed datą zakończenia.");
//                            return;
//                        }
//
//                        // Check if the car is available
//                        List<Rental> conflictingRentals = rentalController.getConflictingRentals(car.id(), dateFrom, dateTo);
//
//                        if (!conflictingRentals.isEmpty()) {
//                            StringBuilder message = new StringBuilder("Samochód jest niedostępny w następujących terminach:\n");
//                            for (Rental rental : conflictingRentals) {
//                                message.append("- Od: ").append(rental.getRentalDate())
//                                        .append(" Do: ").append(rental.getReturnDate()).append("\n");
//                            }
//                            MessageDialog.showMessageDialog(textGUI, "Niedostępny", message.toString());
//                        } else {
//                            // Calculate the number of days
//                            long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(dateFrom, dateTo) + 1;
//                            double totalPrice = daysBetween * car.pricePerDay();
//
//                            // Display confirmation window
//                            showConfirmationDialog(textGUI, car, dateFrom, dateTo, daysBetween, totalPrice);
//                        }
//
//                    } catch (DateTimeParseException e) {
//                        MessageDialog.showMessageDialog(textGUI, "Błąd", "Nieprawidłowy format daty. Użyj formatu YYYY-MM-DD.");
//                    }
//                });
//                rentButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
//                carDetailsPanel.addComponent(rentButton);
//            }
//        } else {
//            Button loginButton = new Button(" Zaloguj się ", () -> {
//                LoginDialog.showLoginDialog(textGUI);
//                textGUI.getActiveWindow().close();
//                showCarDetailsDialog(textGUI, car);
//            });
//            loginButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
//            carDetailsPanel.addComponent(loginButton);
//        }
//
//        BasicWindow carDetailsWindow = new BasicWindow("Szczegóły Samochodu");
//        carDetailsWindow.setSize(new TerminalSize(screenSize.getColumns() / 3, screenSize.getRows()));
//        carDetailsWindow.setPosition(new TerminalPosition(screenSize.getColumns() / 4 * 3 - 7, 0));
//        carDetailsWindow.setHints(Arrays.asList(Window.Hint.NO_DECORATIONS, Window.Hint.FIXED_POSITION, Window.Hint.FIXED_SIZE));
//        carDetailsWindow.setComponent(carDetailsPanel.withBorder(Borders.singleLine()));
//        carDetailsWindow.setCloseWindowWithEscape(true);
//        textGUI.addWindowAndWait(carDetailsWindow);
//    }
//
//    private static void showConfirmationDialog(WindowBasedTextGUI textGUI, Car car, LocalDate dateFrom, LocalDate dateTo, long daysBetween, double totalPrice) {
//        Panel confirmationPanel = new Panel();
//        confirmationPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
//
//        confirmationPanel.addComponent(new Label("Potwierdzenie wypożyczenia"));
//        confirmationPanel.addComponent(new Label("Samochód: " + car.brand() + " " + car.model()));
//        confirmationPanel.addComponent(new Label("Od: " + dateFrom.toString()));
//        confirmationPanel.addComponent(new Label("Do: " + dateTo.toString()));
//        confirmationPanel.addComponent(new Label("Ilość dni: " + daysBetween));
//        confirmationPanel.addComponent(new Label("Łączna kwota: " + totalPrice + " PLN"));
//
//        Button payButton = new Button(" Zapłać ", () -> {
//            UserController userController = new UserController();
//            int userId = userController.getUserId(UserSession.getInstance().getUsername());
//
//            if (rentalController.rentCar(car.id(), userId, dateFrom, dateTo)) {
//                MessageDialog.showMessageDialog(textGUI, "Wypożyczenie", "Samochód został pomyślnie wypożyczony!");
//                textGUI.getActiveWindow().close(); // Close confirmation window
//            } else {
//                MessageDialog.showMessageDialog(textGUI, "Błąd", "Nie udało się wypożyczyć samochodu.");
//            }
//        });
//
//        Button cancelButton = new Button(" Anuluj ", () -> {
//            textGUI.getActiveWindow().close(); // Close confirmation window
//        });
//
//        confirmationPanel.addComponent(new EmptySpace(new TerminalSize(0, 1))); // Add some spacing
//        confirmationPanel.addComponent(payButton);
//        confirmationPanel.addComponent(cancelButton);
//
//        BasicWindow confirmationWindow = new BasicWindow("Potwierdzenie");
//        confirmationWindow.setComponent(confirmationPanel.withBorder(Borders.singleLine()));
//        confirmationWindow.setCloseWindowWithEscape(true);
//        textGUI.addWindow(confirmationWindow);
//    }
//}
