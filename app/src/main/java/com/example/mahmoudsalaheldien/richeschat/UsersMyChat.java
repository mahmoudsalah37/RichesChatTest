package com.example.mahmoudsalaheldien.richeschat;

public class UsersMyChat {
    public String name;
    public String online;
    public String userID;
    public String profile;

    public UsersMyChat(String userID, String name, String online, String profile) {
        this.name = name;
        this.online = online;
        this.userID = userID;
        this.profile = profile;
    }
}
