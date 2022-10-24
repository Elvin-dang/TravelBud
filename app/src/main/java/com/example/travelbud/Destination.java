package com.example.travelbud;

import java.util.ArrayList;

//@Entity(tableName = "mediaList")
public class Destination {

    double lat;
    double lng;
    private String address;
    private String name;

    private String subtitle;
    private String detailed_address;
    //TODO: String for now, User in future.
    private ArrayList<TravelBudUser> participants;
    public Destination() {

    }
    public Destination(String address, double lat, double lng, String name, String subtitle,
                       String detailed_address,
                       ArrayList<TravelBudUser> participants) {
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.subtitle = subtitle;
        this.detailed_address = detailed_address;
        this.participants = participants;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDetailed_address() {
        return detailed_address;
    }

    public void setDetailed_address(String detailed_address) {
        this.detailed_address = detailed_address;
    }

    public ArrayList<TravelBudUser> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<TravelBudUser> participants) {
        this.participants = participants;
    }

    public enum Place {TRANSIT, RESIDENCE, LANDSCAPE}


}
