package org.example.gui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import org.example.services.CarService;
import org.example.models.Car;

import java.util.Arrays;
import java.util.List;

public class AvailableCarsDialog {
    private static CarService carService = new CarService();

    public static void showAvailableCarsDialog(WindowBasedTextGUI textGUI) {
        TerminalSize screenSize = textGUI.getScreen().getTerminalSize();
        List<Car> availableCars = carService.getAvailableCars();
        Panel carsPanel = new Panel();
        carsPanel.setLayoutManager(new GridLayout(2));

        Label titleLabel = new Label("Dostępne samochody:");
        carsPanel.addComponent(titleLabel);
        carsPanel.addComponent(new EmptySpace());

        if (availableCars.isEmpty()) {
            carsPanel.addComponent(new Label("Brak dostępnych samochodów."));
        } else {
            for (Car car : availableCars) {
                Button carButton = new Button(" " + car.brand() + " " + car.model() + " (" + car.year() + ") ", () -> {
                    showCarDetailsDialog(textGUI, car);
                });
                carsPanel.addComponent(carButton);
                carsPanel.addComponent(new Label( car.pricePerDay() + " PLN/dzień"));
            }
        }

        BasicWindow carsWindow = new BasicWindow("Dostępne Samochody");
        carsWindow.setSize(new TerminalSize(screenSize.getColumns(), screenSize.getRows()));
        carsWindow.setPosition(new TerminalPosition(screenSize.getColumns() / 4 + 7, 0));
        carsWindow.setHints(Arrays.asList(Window.Hint.NO_DECORATIONS, Window.Hint.FIXED_POSITION, Window.Hint.FIXED_SIZE));
        carsWindow.setComponent(carsPanel.withBorder(Borders.singleLine()));
        carsWindow.setCloseWindowWithEscape(true);
        textGUI.addWindowAndWait(carsWindow);
    }

    private static void showCarDetailsDialog(WindowBasedTextGUI textGUI, Car car) {
        TerminalSize screenSize = textGUI.getScreen().getTerminalSize();

        Panel carDetailsPanel = new Panel();
        carDetailsPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        carDetailsPanel.addComponent(new Label("Szczegóły samochodu:"));
        carDetailsPanel.addComponent(new Label("Marka: " + car.brand()));
        carDetailsPanel.addComponent(new Label("Model: " + car.model()));
        carDetailsPanel.addComponent(new Label("Rocznik: " + car.year()));
        carDetailsPanel.addComponent(new Label("Moc: " + car.power() + " KM"));
        carDetailsPanel.addComponent(new Label("Pojemność silnika: " + car.engineCapacity() + " l"));
        carDetailsPanel.addComponent(new Label("Rodzaj paliwa: " + car.fuelType()));
        carDetailsPanel.addComponent(new Label("Skrzynia biegów: " + car.transmission()));
        carDetailsPanel.addComponent(new Label("Ilość miejsc: " + car.seats()));
        carDetailsPanel.addComponent(new Label("Cena za dzień: " + car.pricePerDay() + " PLN"));

        BasicWindow carDetailsWindow = new BasicWindow("Szczegóły Samochodu");
        carDetailsWindow.setSize(new TerminalSize(screenSize.getColumns(), screenSize.getRows()));
        carDetailsWindow.setPosition(new TerminalPosition(screenSize.getColumns() / 3 * 2 + 7, 0));
        carDetailsWindow.setHints(Arrays.asList(Window.Hint.NO_DECORATIONS, Window.Hint.FIXED_POSITION, Window.Hint.FIXED_SIZE));
        carDetailsWindow.setComponent(carDetailsPanel.withBorder(Borders.singleLine()));
        carDetailsWindow.setCloseWindowWithEscape(true);
        textGUI.addWindowAndWait(carDetailsWindow);
    }
}
