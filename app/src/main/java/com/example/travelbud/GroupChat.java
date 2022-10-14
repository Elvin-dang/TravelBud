package com.example.travelbud;

import java.util.Date;

public class GroupChat {
    public String name, latestMessage;
    public Date time;

    public GroupChat(String name, String latestMessage, Date time) {
        this.name = name;
        this.latestMessage = latestMessage;
        this.time = time;
    }
}
