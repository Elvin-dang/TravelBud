package com.example.travelbud;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GroupChat implements Serializable {
    private String name;
    private String latestMessage;
    private long time;
    private List<Chat> chatList = new ArrayList<>();
    private String key;

    public GroupChat() {

    }

    public GroupChat(String name, String latestMessage, long time) {
        this.name = name;
        this.latestMessage = latestMessage;
        this.time = time;
        this.chatList = new ArrayList<>();
    }

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(String latestMessage) {
        this.latestMessage = latestMessage;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Exclude
    public List<Chat> getChatList() {
        return chatList;
    }

    @Exclude
    public void setChatList(List<Chat> chatList) {
        this.chatList = chatList;
    }
}
