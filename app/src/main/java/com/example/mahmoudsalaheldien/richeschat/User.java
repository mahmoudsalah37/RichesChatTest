package com.example.mahmoudsalaheldien.richeschat;

public class User {
    String  userID;
    String mail;
    String username;
    String nickname;
    String country;
    String status;
    String profile;

    public User(){ }


    public User(String userID, String mail, String username, String nickname, String country, String status, String profile) {
        this.userID = userID;
        this.mail = mail;
        this.username = username;
        this.nickname = nickname;
        this.country = country;
        this.status = status;
        this.profile = profile;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }




}
