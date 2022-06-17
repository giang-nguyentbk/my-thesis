package com.example.myapp.login;

import java.io.Serializable;

public class User implements Serializable {
    private String fullName;
    private String username;
    private boolean role;

    public User(String fullName, String username, boolean role) {
        this.fullName = fullName;
        this.username = username;
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isRole() {
        return role;
    }

    public void setRole(boolean role) {
        this.role = role;
    }
}
