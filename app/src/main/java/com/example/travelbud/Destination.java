package com.example.travelbud;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

//@Entity(tableName = "mediaList")
public class Destination {

    private String address;
    double lat;
    double lng;

    public enum Place {TRANSIT, RESIDENCE, LANDSCAPE}

    ;

    public Destination() {

    }

    private String name;
    private String subtitle;
    private String detailed_address;
    //TODO: String for now, User in future.
    private ArrayList<TravelBudUser> participants;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

//    public Location getLoc() {
////        return loc;
//    }

//    public void setLoc(Location loc) {
//        this.loc = loc;
//    }

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
    //    @PrimaryKey(autoGenerate = true)
//    @NonNull
//    @ColumnInfo(name = "mediaItemID")
//    private int mediaItemID;
//
//    @ColumnInfo(name = "mediaItemName")
//    private String mediaItemName;
//
//    @ColumnInfo(name = "mediaItemCity")
//    private String mediaItemCity;
//
//    @ColumnInfo(name = "mediaItemUri")
//    private String mediaItemUri;


//    public Destination(String mediaItemName, String mediaItemCity, String mediaItemUri) {
//        this.mediaItemName = mediaItemName;
//        this.mediaItemCity = mediaItemCity;
//        this.mediaItemUri = mediaItemUri;
//    }

//    public int getMediaItemID() {
//        return mediaItemID;
//    }
//
//    public void setMediaItemID(int mediaItemID) {
//        this.mediaItemID = mediaItemID;
//    }
//
//    public void setMediaItemCity(String mediaItemCity) {
//        this.mediaItemCity = mediaItemCity;
//    }
//
//    public void setMediaItemUri(String mediaItemUri) {
//        this.mediaItemUri = mediaItemUri;
//    }
//
//    public void setMediaItemName(String mediaItemName) {
//        this.mediaItemName = mediaItemName;
//    }
//
//    public String getMediaItemName() {
//        return mediaItemName;
//    }
//
//    public String getMediaItemCity() {
//        return mediaItemCity;
//    }
//
//    public String getMediaItemUri() {
//        return mediaItemUri;
//    }

}
