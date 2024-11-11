package org.example.services;

import org.example.database.DatabaseConnection;
import org.example.models.Car;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarService {
    public List<Car> getAvailableCars() {
        List<Car> availableCars = new ArrayList<>();
        String query = "SELECT * FROM Samochod WHERE id NOT IN (SELECT samochod_id FROM Wypozyczenie WHERE data_zwrotu IS NULL)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String brand = resultSet.getString("marka");
                String model = resultSet.getString("model");
                int year = resultSet.getInt("rocznik");
                int power = resultSet.getInt("moc");
                double engineCapacity = resultSet.getDouble("pojemnosc_silnika");
                String fuelType = resultSet.getString("rodzaj_paliwa");
                String transmission = resultSet.getString("skrzynia_biegow");
                int seats = resultSet.getInt("ilosc_miejsc");
                double pricePerDay = resultSet.getDouble("cena_za_dzien");

                availableCars.add(new Car(id, brand, model, year, power, engineCapacity, fuelType, transmission, seats, pricePerDay));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return availableCars;
    }
}
