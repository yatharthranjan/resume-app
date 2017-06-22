package com.kings.yatharth.yatharthresume.model;

/**
 * Created by yatharth on 21/06/17.
 */

public class User {

    private String username;
    private String name;
    private String email;
    private String password;

    public User(String username, String password, String name, String email){
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
