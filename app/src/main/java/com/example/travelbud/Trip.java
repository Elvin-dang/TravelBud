package com.example.travelbud;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Trip {
    private String key;

    private String name;
    private Date startDate;
    private Date endDate;
    private List<TravelBudUser> travelers = new ArrayList<>();

    private String kickoffPoint;
    private List<ChecklistItem> checkList = new ArrayList<>();
    private List<Destination> destinations = new ArrayList<>();

    //TODO: to be implemented
    private Object chat;
    private Object Budget;

    public Trip() {
        this.key = UUID.randomUUID().toString();
    }

    public Trip(String name, List<TravelBudUser> travelers,
                String kickoffPoint, List<ChecklistItem> checkList,
                List<Destination> destinations) {
        this();
        this.name = name;
        this.startDate = null;
        this.endDate = null;
        this.travelers = travelers;
        this.kickoffPoint = kickoffPoint;
        this.checkList = checkList;
        this.destinations = destinations;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setTravelers(List<TravelBudUser> travelers) {
        this.travelers = travelers;
    }

    public void setKickoffPoint(String kickoffPoint) {
        this.kickoffPoint = kickoffPoint;
    }

    public void setCheckList(List<ChecklistItem> checkList) {
        this.checkList = checkList;
    }

    public void setDestinations(List<Destination> destinations) {
        this.destinations = destinations;
    }

    public void setChat(Object chat) {
        this.chat = chat;
    }

    public void setBudget(Object budget) {
        Budget = budget;
    }

    public String getName() {
        return name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public List<TravelBudUser> getTravelers() {
        return travelers;
    }

    public String getKickoffPoint() {
        return kickoffPoint;
    }

    public List<ChecklistItem> getCheckList() {
        return checkList;
    }

    public List<Destination> getDestinations() {
        return destinations;
    }

    public Object getChat() {
        return chat;
    }

    public Object getBudget() {
        return Budget;
    }

}
