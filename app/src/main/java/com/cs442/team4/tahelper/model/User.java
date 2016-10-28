package com.cs442.team4.tahelper.model;

/**
 * Created by neo on 25-10-2016.
 */

public class User {

    private String name;
    private String username;
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

}
