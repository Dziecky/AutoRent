package org.example.services;

import org.example.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public List<String> getUserRentedCars(String username) {
        String query = "SELECT s.marka, s.model FROM Wypozyczenie w JOIN Samochod s ON w.samochod_id = s.id JOIN Uzytkownik u ON w.uzytkownik_id = u.id WHERE u.login = ? AND w.data_zwrotu IS NULL";
        List<String> rentedCars = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                rentedCars.add(resultSet.getString("marka") + " " + resultSet.getString("model"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rentedCars;
    }
}
