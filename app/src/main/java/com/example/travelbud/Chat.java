package com.example.travelbud;

public class Chat {
    private String sender;
    private String name;
    private String message;
    private long time;

    public Chat(String sender, String name, String message, long time) {
        this.sender = sender;
        this.name = name;
        this.message = message;
        this.time = time;
    }

    public Chat() {

    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}