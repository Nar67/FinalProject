package com.tg.narcis.finalproject;


import android.content.Context;

public class User {
    private String username;
    private String password;
    private String score;

    public User(String username, String password, String score) {
        this.username = username;
        this.password = password;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public static String printUser(User user) {
        String s = "Username: " + user.getUsername() + " pass:" + user.getPassword() + " score: " + user.getScore();
        return s;
    }

}
