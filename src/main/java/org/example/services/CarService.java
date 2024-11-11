package org.example.services;

import org.example.database.DatabaseConnection;
import org.example.models.Car;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CarService {

    public List<Car> getAllCars() {
        return getFilteredCars(null, null, null, null, null, null, null, null);
    }

    public List<Car> getFilteredCars(String brand, String model, String yearFromStr, String yearToStr, String transmission, String fuelType, String dateFromStr, String dateToStr) {
        List<Car> cars = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM Samochod WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        if (brand != null && !brand.isEmpty()) {
            query.append(" AND marka LIKE ?");
            parameters.add("%" + brand + "%");
        }
        if (model != null && !model.isEmpty()) {
            query.append(" AND model LIKE ?");
            parameters.add("%" + model + "%");
        }
        if (yearFromStr != null && !yearFromStr.isEmpty()) {
            query.append(" AND rocznik >= ?");
            parameters.add(Integer.parseInt(yearFromStr));
        }
        if (yearToStr != null && !yearToStr.isEmpty()) {
            query.append(" AND rocznik <= ?");
            parameters.add(Integer.parseInt(yearToStr));
        }
        if (transmission != null && !transmission.isEmpty()) {
            query.append(" AND skrzynia_biegow = ?");
            parameters.add(transmission);
        }
        if (fuelType != null && !fuelType.isEmpty()) {
            query.append(" AND rodzaj_paliwa = ?");
            parameters.add(fuelType);
        }

        // Filtracja po dostępności w danym przedziale dat
        if (dateFromStr != null && !dateFromStr.isEmpty() && dateToStr != null && !dateToStr.isEmpty()) {
            query.append(" AND id NOT IN (SELECT samochod_id FROM Wypozyczenie WHERE (? <= data_zwrotu AND ? >= data_wypozyczenia))");
            parameters.add(LocalDate.parse(dateFromStr));
            parameters.add(LocalDate.parse(dateToStr));
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            createCarList(cars, resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cars;
    }

    public boolean addCar(Car car) {
        String query = "INSERT INTO Samochod (marka, model, rocznik, moc, pojemnosc_silnika, rodzaj_paliwa, skrzynia_biegow, ilosc_miejsc, cena_za_dzien) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, car.brand());
            preparedStatement.setString(2, car.model());
            preparedStatement.setInt(3, car.year());
            preparedStatement.setInt(4, car.power());
            preparedStatement.setDouble(5, car.engineCapacity());
            preparedStatement.setString(6, car.fuelType());
            preparedStatement.setString(7, car.transmission());
            preparedStatement.setInt(8, car.seats());
            preparedStatement.setDouble(9, car.pricePerDay());

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void createCarList(List<Car> cars, ResultSet resultSet) throws SQLException {
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

            cars.add(new Car(id, carBrand, carModel, year, power, engineCapacity, fuelType, transmission, seats, pricePerDay));
        }
    }
}
