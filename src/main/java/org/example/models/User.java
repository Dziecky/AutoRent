package org.example.models;

public class User {
    private int id;
    private String name;
    private String surname;
    private String login;
    private String email;
    private String phone;
    private String role;

    public User(int id, String name, String surname, String login, String email, String phone, String role) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    // Gettery

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }
}
