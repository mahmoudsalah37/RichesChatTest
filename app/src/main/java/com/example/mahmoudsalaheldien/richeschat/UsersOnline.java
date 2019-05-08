package com.example.mahmoudsalaheldien.richeschat;

public class UsersOnline {
    public String username;
    public String isonline;
    public String userID;
    public String profile;

    public UsersOnline(String userID, String username, String isonline, String profile) {
        this.username = username;
        this.isonline = isonline;
        this.userID = userID;
        this.profile = profile;
    }
}
