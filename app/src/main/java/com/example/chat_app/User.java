package com.example.chat_app;

public class User {
    String email;
    String password;
    String token;


    public User(String email, String password,String token) {
        this.email = email;
        this.password = password;
        this.token=token;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getToken() {
        return token;
    }
}
