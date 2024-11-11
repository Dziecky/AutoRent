package org.example.sessions;

public class UserSession {
    private static UserSession instance;
    private boolean authenticated;
    private String username;
    private String role;

    private UserSession() {
        this.authenticated = false;
        this.username = "";
        this.role = "";
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public void setAuthenticated(boolean authenticated, String username, String role) {
        this.authenticated = authenticated;
        this.username = username;
        this.role = role;
    }

    public void logout() {
        this.authenticated = false;
        this.username = null;
        this.role = null;
    }
}
