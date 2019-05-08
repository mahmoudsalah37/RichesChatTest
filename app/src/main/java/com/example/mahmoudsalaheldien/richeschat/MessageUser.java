package com.example.mahmoudsalaheldien.richeschat;


public class MessageUser {
    String from , message , type;
    boolean seen;

    public MessageUser(String from, String message, boolean seen , String type) {
        this.from = from;
        this.message = message;
        this.seen = seen;
        this.type = type;
    }
}
