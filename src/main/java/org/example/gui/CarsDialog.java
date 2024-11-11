package org.example.gui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import org.example.models.Car;
import org.example.services.CarService;
import org.example.sessions.UserSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class CarsDialog {
    private static CarService carService = new CarService();
    private static List<Car> cars;
    private static Panel carsPanel;
    private static Panel filtersPanel;
    private static WindowBasedTextGUI textGUI;
    private static LocalDate selectedDate = null;

    public static void showAvailableCarsDialog(WindowBasedTextGUI textGUI) {
        CarsDialog.textGUI = textGUI; // Przechowujemy textGUI do użytku w handlerach
        TerminalSize screenSize = textGUI.getScreen().getTerminalSize();

        // Inicjalizacja paneli
        Panel mainPanel = new Panel();
        mainPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        // Panel filtrowania
        filtersPanel = new Panel();
        filtersPanel.setLayoutManager(new GridLayout(4));

        // Komponenty filtrowania
        TextBox brandFilter = new TextBox();
        TextBox modelFilter = new TextBox();
        TextBox yearFromFilter = new TextBox();
        TextBox yearToFilter = new TextBox();
        ComboBox<String> transmissionFilter = new ComboBox<>("", "Manualna", "Automatyczna");
        ComboBox<String> fuelTypeFilter = new ComboBox<>("", "Benzyna", "Diesel", "Elektryczny", "Hybryda");
        TextBox dateFromFilter = new TextBox();
        TextBox dateToFilter = new TextBox();

        // Dodajemy komponenty filtrowania do panelu
        filtersPanel.addComponent(new Label("Marka:"));
        filtersPanel.addComponent(brandFilter);
        filtersPanel.addComponent(new Label("Model:"));
        filtersPanel.addComponent(modelFilter);
        filtersPanel.addComponent(new Label("Rok od:"));
        filtersPanel.addComponent(yearFromFilter);
        filtersPanel.addComponent(new Label("do:"));
        filtersPanel.addComponent(yearToFilter);
        filtersPanel.addComponent(new Label("Skrzynia biegów:"));
        filtersPanel.addComponent(transmissionFilter);
        filtersPanel.addComponent(new Label("Rodzaj paliwa:"));
        filtersPanel.addComponent(fuelTypeFilter);
        filtersPanel.addComponent(new Label("Data od:"));
        filtersPanel.addComponent(dateFromFilter);
        filtersPanel.addComponent(new Label("do:"));
        filtersPanel.addComponent(dateToFilter);

        Button filterButton = new Button(" Filtruj ", () -> {
            updateCarList(
                    brandFilter.getText(),
                    modelFilter.getText(),
                    yearFromFilter.getText(),
                    yearToFilter.getText(),
                    transmissionFilter.getSelectedItem(),
                    fuelTypeFilter.getSelectedItem(),
                    dateFromFilter.getText(),
                    dateToFilter.getText()
            );
        });
        Button clearButton = new Button(" Wyczyść filtry ", () -> {
            brandFilter.setText("");
            modelFilter.setText("");
            yearFromFilter.setText("");
            yearToFilter.setText("");
            transmissionFilter.setSelectedItem(null);
            fuelTypeFilter.setSelectedItem(null);
            dateFromFilter.setText("");
            dateToFilter.setText("");
            updateCarList("", "", "", null, null, null, null, null);
        });

        filtersPanel.addComponent(filterButton);
        filtersPanel.addComponent(clearButton);

        // Inicjalizacja panelu samochodów
        carsPanel = new Panel();
        carsPanel.setLayoutManager(new GridLayout(2));

        // Dodajemy panele do głównego panelu
        mainPanel.addComponent(filtersPanel.withBorder(Borders.singleLine("Filtry")));
        mainPanel.addComponent(new Separator(Direction.HORIZONTAL));
        mainPanel.addComponent(carsPanel);

        // Pobieramy wszystkie samochody na początku
        cars = carService.getAllCars();
        updateCarList("", "", "", null, null, null, null, null);

        // Tworzymy okno
        BasicWindow carsWindow = new BasicWindow("Dostępne Samochody");
        carsWindow.setSize(new TerminalSize(screenSize.getColumns(), screenSize.getRows()));
        carsWindow.setPosition(new TerminalPosition(0, 0));
        carsWindow.setHints(Arrays.asList(Window.Hint.NO_DECORATIONS, Window.Hint.FIXED_POSITION, Window.Hint.FIXED_SIZE));
        carsWindow.setComponent(mainPanel.withBorder(Borders.singleLine()));
        carsWindow.setCloseWindowWithEscape(true);
        textGUI.addWindowAndWait(carsWindow);
    }

    private static void updateCarList(String brand, String model, String yearFromStr, String yearToStr, String transmission, String fuelType, String dareFromStr, String dateToStr) {
        carsPanel.removeAllComponents();

        List<Car> filteredCars = carService.getFilteredCars(brand, model, yearFromStr, yearToStr, transmission, fuelType, dareFromStr, dateToStr);

        if (filteredCars.isEmpty()) {
            carsPanel.addComponent(new Label("Brak dostępnych samochodów dla wybranych filtrów."));
        } else {
            for (Car car : filteredCars) {
                Button carButton = new Button(" " + car.brand() + " " + car.model() + " (" + car.year() + ") ", () -> {
                    showCarDetailsDialog(textGUI, car);
                });
                carsPanel.addComponent(carButton);
                carsPanel.addComponent(new Label(car.pricePerDay() + " PLN/dzień"));
            }
        }
        try {
            textGUI.updateScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void showCarDetailsDialog(WindowBasedTextGUI textGUI, Car car) {
        TerminalSize screenSize = textGUI.getScreen().getTerminalSize();

        Panel carDetailsPanel = new Panel();
        carDetailsPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        carDetailsPanel.addComponent(new Label("Szczegóły samochodu:"));
        carDetailsPanel.addComponent(new Label("Marka: " + car.brand()));
        carDetailsPanel.addComponent(new Label("Model: " + car.model()));
        carDetailsPanel.addComponent(new Label("Rok: " + car.year()));
        carDetailsPanel.addComponent(new Label("Moc: " + car.power() + " KM"));
        carDetailsPanel.addComponent(new Label("Pojemność silnika: " + car.engineCapacity() + " l"));
        carDetailsPanel.addComponent(new Label("Rodzaj paliwa: " + car.fuelType()));
        carDetailsPanel.addComponent(new Label("Skrzynia biegów: " + car.transmission()));
        carDetailsPanel.addComponent(new Label("Ilość miejsc: " + car.seats()));
        carDetailsPanel.addComponent(new Label("Cena za dzień: " + car.pricePerDay() + " PLN"));

        if (UserSession.getInstance().isAuthenticated()) {
            Button rentButton = new Button(" Wypożycz ", () -> {
                // Logika wypożyczenia
                MessageDialog.showMessageDialog(textGUI, "Wypożyczenie", "Samochód został pomyślnie wypożyczony!");
                textGUI.getActiveWindow().close();
            });
            rentButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
            carDetailsPanel.addComponent(rentButton);
        } else {
            Button loginButton = new Button(" Zaloguj się ", () -> {
                LoginDialog.showLoginDialog(textGUI);
                textGUI.getActiveWindow().close();
                showCarDetailsDialog(textGUI, car);
            });
            loginButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
            carDetailsPanel.addComponent(loginButton);
        }

        BasicWindow carDetailsWindow = new BasicWindow("Szczegóły Samochodu");
        carDetailsWindow.setSize(new TerminalSize(screenSize.getColumns() / 2, screenSize.getRows()));
        carDetailsWindow.setPosition(new TerminalPosition(screenSize.getColumns() / 4, 0));
        carDetailsWindow.setHints(Arrays.asList(Window.Hint.NO_DECORATIONS, Window.Hint.FIXED_POSITION, Window.Hint.FIXED_SIZE));
        carDetailsWindow.setComponent(carDetailsPanel.withBorder(Borders.singleLine()));
        carDetailsWindow.setCloseWindowWithEscape(true);
        textGUI.addWindowAndWait(carDetailsWindow);
    }
}
