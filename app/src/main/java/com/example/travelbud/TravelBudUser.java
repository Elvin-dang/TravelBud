package com.example.travelbud;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.List;

public class TravelBudUser implements Serializable {
    private String username;
    private List<Trip> trips;
    private List<TravelBudUser> friends;

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    private String key;

    public TravelBudUser(String username, List<Trip> trips, List<TravelBudUser> friends) {
        this.username = username;
        this.trips = trips;
        this.friends = friends;
    }

    public TravelBudUser() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    public List<TravelBudUser> getFriends() {
        return friends;
    }

    public void setFriends(List<TravelBudUser> friends) {
        this.friends = friends;
    }
}