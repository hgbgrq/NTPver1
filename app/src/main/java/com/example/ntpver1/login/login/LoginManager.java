package com.example.ntpver1.login.login;

public class LoginManager {

    private LoginManager() {}

    private static LoginManager loginManager;
    User user;

    public boolean login(String email, String pw) {
        //Test

        return true;
    }

    public User getUser() {
        return user;
    }

    public static LoginManager getInstance() {
        if (loginManager == null) {
            loginManager = new LoginManager();
        }

        return loginManager;
    }
}
