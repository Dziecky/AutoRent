package org.example.controllers;

import org.example.models.Rental;
import org.example.services.UserService;

import java.util.List;

public class UserController {
    private final UserService userService;

    public UserController() {
        this.userService = new UserService();
    }

    public boolean registerUser(String name, String surname, String login, String password, String email, String phone) {
        return userService.registerUser(name, surname, login, password, email, phone);
    }

    public boolean checkIfLoginExists(String login) {
        return userService.checkIfLoginExists(login);
    }

    public String getUserRole(String username) {
        return userService.getUserRole(username);
    }

    public String getUserName(String username) {
        return userService.getUserName(username);
    }

    public String getUserSurname(String username) {
        return userService.getUserSurname(username);
    }

    public String getUserEmail(String username) {
        return userService.getUserEmail(username);
    }

    public String getUserPhone(String username) {
        return userService.getUserPhone(username);
    }

    public boolean updateUser(String username, String name, String surname, String email, String phone) {
        return userService.updateUser(username, name, surname, email, phone);
    }

    public List<Rental> getUserCurrentRentals(String username) {
        return userService.getUserCurrentRentals(username);
    }

    public List<Rental> getUserPastRentals(String username) {
        return userService.getUserPastRentals(username);
    }

    public int getUserId(String username) {
        return userService.getUserId(username);
    }
}
