package org.example.services;

import org.example.database.DatabaseConnection;
import org.example.models.Car;
import org.example.models.Rental;
import org.example.models.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalService {
    public List<Rental> getAllRentals() {
        List<Rental> rentals = new ArrayList<>();
        String query = "SELECT w.id as rental_id, w.data_wypozyczenia, w.data_zwrotu, " +
                "s.id as car_id, s.marka, s.model, s.rocznik, s.moc, s.pojemnosc_silnika, s.rodzaj_paliwa, s.skrzynia_biegow, s.ilosc_miejsc, s.cena_za_dzien, s.image_url, " +
                "u.id as user_id, u.imie, u.nazwisko, u.login, u.email, u.numer_telefonu, u.rola " +
                "FROM Wypozyczenie w " +
                "JOIN Samochod s ON w.samochod_id = s.id " +
                "JOIN Uzytkownik u ON w.uzytkownik_id = u.id " +
                "WHERE w.status != 'ANULOWANE'";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                // Pobieranie danych wypożyczenia
                int rentalId = resultSet.getInt("rental_id");
                LocalDate rentalDate = resultSet.getDate("data_wypozyczenia").toLocalDate();
                LocalDate returnDate = resultSet.getDate("data_zwrotu").toLocalDate();

                // Pobieranie danych samochodu
                Car car = new Car(
                        resultSet.getInt("car_id"),
                        resultSet.getString("marka"),
                        resultSet.getString("model"),
                        resultSet.getInt("rocznik"),
                        resultSet.getInt("moc"),
                        resultSet.getDouble("pojemnosc_silnika"),
                        resultSet.getString("rodzaj_paliwa"),
                        resultSet.getString("skrzynia_biegow"),
                        resultSet.getInt("ilosc_miejsc"),
                        resultSet.getDouble("cena_za_dzien"),
                        resultSet.getString("image_url")
                );

                // Pobieranie danych użytkownika
                User user = new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("imie"),
                        resultSet.getString("nazwisko"),
                        resultSet.getString("login"),
                        resultSet.getString("email"),
                        resultSet.getString("numer_telefonu"),
                        resultSet.getString("rola")
                );

                Rental rental = new Rental(rentalId, rentalDate, returnDate, car, user);
                rentals.add(rental);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rentals;
    }

    public static List<Rental> getConflictingRentals(int carId, LocalDate dateFrom, LocalDate dateTo) {
        List<Rental> conflictingRentals = new ArrayList<>();
        String query = "SELECT * FROM Wypozyczenie WHERE samochod_id = ? AND status != 'ANULOWANE' AND " +
                "((? <= data_zwrotu) AND (? >= data_wypozyczenia))";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, carId);
            preparedStatement.setDate(2, Date.valueOf(dateFrom)); // dateFrom
            preparedStatement.setDate(3, Date.valueOf(dateTo));   // dateTo

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int rentalId = resultSet.getInt("id");
                LocalDate rentalDateFrom = resultSet.getDate("data_wypozyczenia").toLocalDate();
                LocalDate rentalDateTo = resultSet.getDate("data_zwrotu").toLocalDate();

                Rental rental = new Rental(rentalId, rentalDateFrom, rentalDateTo, null);
                conflictingRentals.add(rental);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conflictingRentals;
    }


    // Metody do wypożyczania samochodu przez użytkownika
    public boolean rentCar(int carId, int userId, LocalDate rentalDate, LocalDate returnDate) {
        List<Rental> conflictingRentals = getConflictingRentals(carId, rentalDate, returnDate);
        if (!conflictingRentals.isEmpty()) {
            // Nie można wypożyczyć samochodu w tym terminie
            return false;
        }

        String query = "INSERT INTO Wypozyczenie (samochod_id, uzytkownik_id, data_wypozyczenia, data_zwrotu) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, carId);
            preparedStatement.setInt(2, userId);
            preparedStatement.setDate(3, Date.valueOf(rentalDate));
            preparedStatement.setDate(4, Date.valueOf(returnDate));
            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean cancelRental(int rentalId) {
        String query = "UPDATE Wypozyczenie SET status = 'ANULOWANE' WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, rentalId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
