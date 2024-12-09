package org.example.services;

import org.example.database.DatabaseConnection;
import org.example.models.Car;
import org.example.models.Rental;
import org.example.models.User;
import org.example.sessions.UserSession;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    public boolean registerUser(String name, String surname, String login, String password, String email, String phone) {
        String query = "INSERT INTO Uzytkownik (imie, nazwisko, login, haslo, email, numer_telefonu, rola) VALUES (?, ?, ?, ?, ?, ?, 'USER')";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, login);
            preparedStatement.setString(4, password);
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, phone);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Nowa metoda do sprawdzania roli użytkownika
    public String getUserRole(String username) {
        String query = "SELECT rola FROM Uzytkownik WHERE login = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("rola");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getUserName(String username) {
        String query = "SELECT imie FROM Uzytkownik WHERE login = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("imie");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean checkIfLoginExists(String login) {
        String query = "SELECT COUNT(*) AS count FROM Uzytkownik WHERE login = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getUserSurname(String username) {
        String query = "SELECT nazwisko FROM Uzytkownik WHERE login = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("nazwisko");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getUserEmail(String username) {
        String query = "SELECT email FROM Uzytkownik WHERE login = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getUserPhone(String username) {
        String query = "SELECT numer_telefonu FROM Uzytkownik WHERE login = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("numer_telefonu");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean updateUser(String username, String name, String surname, String email, String phone) {
        String query = "UPDATE Uzytkownik SET imie = ?, nazwisko = ?, email = ?, numer_telefonu = ? WHERE login = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, phone);
            preparedStatement.setString(5, username);
            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Rental> getUserCurrentRentals(String username) {
        List<Rental> rentals = new ArrayList<>();
        String query = "SELECT w.id as rental_id, w.data_wypozyczenia, w.data_zwrotu, " +
                "s.id as car_id, s.marka, s.model, s.rocznik, s.moc, s.pojemnosc_silnika, s.rodzaj_paliwa, s.skrzynia_biegow, s.ilosc_miejsc, s.cena_za_dzien, s.image_url " +
                "FROM Wypozyczenie w " +
                "JOIN Samochod s ON w.samochod_id = s.id " +
                "JOIN Uzytkownik u ON w.uzytkownik_id = u.id " +
                "WHERE u.login = ? AND w.data_zwrotu >= CURRENT_DATE AND w.status = 'AKTYWNE'";
        return getRentals(username, rentals, query);
    }

    public List<Rental> getUserPastRentals(String username) {
        List<Rental> rentals = new ArrayList<>();
        String query = "SELECT w.id as rental_id, w.data_wypozyczenia, w.data_zwrotu, " +
                "s.id as car_id, s.marka, s.model, s.rocznik, s.moc, s.pojemnosc_silnika, s.rodzaj_paliwa, s.skrzynia_biegow, s.ilosc_miejsc, s.cena_za_dzien, s.image_url " +
                "FROM Wypozyczenie w " +
                "JOIN Samochod s ON w.samochod_id = s.id " +
                "JOIN Uzytkownik u ON w.uzytkownik_id = u.id " +
                "WHERE u.login = ? AND w.data_zwrotu < CURRENT_DATE AND w.status = 'AKTYWNE'";
        return getRentals(username, rentals, query);
    }

    private List<Rental> getRentals(String username, List<Rental> rentals, String query) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
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

                // Pobieranie danych wypożyczenia
                Rental rental = new Rental(
                        resultSet.getInt("rental_id"),
                        resultSet.getDate("data_wypozyczenia").toLocalDate(),
                        resultSet.getDate("data_zwrotu").toLocalDate(),
                        car
                );

                rentals.add(rental);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rentals;
    }

    public int getUserId(String username) {
        String query = "SELECT id FROM Uzytkownik WHERE login = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
