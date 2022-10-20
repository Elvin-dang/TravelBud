package com.example.travelbud.model;

import java.util.List;
import java.util.Map;

public class UserModel {
    private String name;
    private String email;
    private String key;
    private Map<String, String> trips;
    private Map<String, String> friends;


    public UserModel(String name, String email, Map<String, String> trips,Map<String, String> friends, String key) {
        this.name = name;
        this.email = email;
        this.trips = trips;
        this.friends = friends;
        this.key = key;
    }

    //User Registration Constructor
    public UserModel(String name, String email, String key) {
        this.name = name;
        this.email = email;
        this.key = key;
    }

    public UserModel() {

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

    public Map<String, String> getTrips() {
        return trips;
    }

    public void setTrips(Map<String, String> trips) {
        this.trips = trips;
    }

    public Map<String, String> getFriends() {
        return friends;
    }

    public void setFriends(Map<String, String> friends) {
        this.friends = friends;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
