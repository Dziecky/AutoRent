package org.example.sessions;

public class UserSession {
    private static UserSession instance;
    private boolean authenticated;
    private String username;

    private UserSession() {
        this.authenticated = false;
        this.username = "";
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

    public void setAuthenticated(boolean authenticated, String username) {
        this.authenticated = authenticated;
        this.username = username;
    }
}
