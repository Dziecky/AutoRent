package org.example.services;

import org.example.database.DatabaseConnection;
import org.example.sessions.UserSession;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class AuthenticationService {
    public boolean authenticateUser(String login, String password) {
        String query = "SELECT haslo, rola FROM Uzytkownik WHERE login = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("haslo");
                String role = resultSet.getString("rola");
                if (BCrypt.checkpw(password, hashedPassword)) {
                    UserSession.getInstance().setAuthenticated(true, login, role);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
