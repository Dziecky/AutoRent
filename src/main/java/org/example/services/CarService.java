package org.example.services;

import org.example.database.DatabaseConnection;
import org.example.models.Car;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CarService {
    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM Samochod";

        return getCars(cars, query);
    }

    public List<Car> getFilteredCars(String brand, String model, String yearFromStr, String yearToStr, String transmission, String fuelType, String dateFromStr, String dateToStr) {
        List<Car> allCars = getAllCars();
        List<Car> filteredCars = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        for (Car car : allCars) {
            boolean matches = true;

            if (brand != null && !brand.isEmpty() && !car.brand().equalsIgnoreCase(brand)) {
                matches = false;
            }
            if (model != null && !model.isEmpty() && !car.model().equalsIgnoreCase(model)) {
                matches = false;
            }
            if (yearFromStr != null && !yearFromStr.isEmpty() && car.year() < Integer.parseInt(yearFromStr)) {
                matches = false;
            }
            if (yearToStr != null && !yearToStr.isEmpty() && car.year() > Integer.parseInt(yearToStr)) {
                matches = false;
            }
            if (transmission != null && !transmission.isEmpty() && !car.transmission().equalsIgnoreCase(transmission)) {
                matches = false;
            }
            if (fuelType != null && !fuelType.isEmpty() && !car.fuelType().equalsIgnoreCase(fuelType)) {
                matches = false;
            }
            if (dateFromStr != null && !dateFromStr.isEmpty() && dateToStr != null && !dateToStr.isEmpty()) {
                try {
                    LocalDate availabilityDateFrom = LocalDate.parse(dateFromStr, formatter);
                    LocalDate availabilityDateTo = LocalDate.parse(dateToStr, formatter);
                    if (!isCarAvailable(car, availabilityDateFrom, availabilityDateTo)) {
                        matches = false;
                    }
                } catch (DateTimeParseException e) {
                    System.err.println("Invalid date format: " + e.getMessage());
                    matches = false;
                }
            }

            if (matches) {
                filteredCars.add(car);
            }
        }

        return filteredCars;
    }

    private boolean isCarAvailable(Car car, LocalDate availabilityDateFrom, LocalDate availabilityDateTo) {
        String query = "SELECT COUNT(*) AS count FROM Wypozyczenie WHERE samochod_id = ? AND data_zwrotu >= ? AND data_wypozyczenia <= ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, car.id());
            preparedStatement.setDate(2, java.sql.Date.valueOf(availabilityDateFrom));
            preparedStatement.setDate(3, java.sql.Date.valueOf(availabilityDateTo));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("count") == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private List<Car> getCars(List<Car> cars, String query) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            createCarList(cars, resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cars;
    }

    private void createCarList(List<Car> filteredCars, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String carBrand = resultSet.getString("marka");
            String carModel = resultSet.getString("model");
            int year = resultSet.getInt("rocznik");
            int power = resultSet.getInt("moc");
            double engineCapacity = resultSet.getDouble("pojemnosc_silnika");
            String fuelType = resultSet.getString("rodzaj_paliwa");
            String transmission = resultSet.getString("skrzynia_biegow");
            int seats = resultSet.getInt("ilosc_miejsc");
            double pricePerDay = resultSet.getDouble("cena_za_dzien");

            filteredCars.add(new Car(id, carBrand, carModel, year, power, engineCapacity, fuelType, transmission, seats, pricePerDay));
        }
    }
}
