package com.example.travelbud;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Trip {

    private String key;
    private String name;
    private String host;
    private Date startDate;
    private Date endDate;
    private List<TravelBudUser> travelers = new ArrayList<>();
    private String kickoffPoint;
    private List<ChecklistItem> checkList = new ArrayList<>();
    private List<Destination> destinations = new ArrayList<>();

    //TODO: to be implemented
    private Object Budget;

    public Trip() {
        this.key = UUID.randomUUID().toString();
    }

    public Trip(String name, List<TravelBudUser> travelers, String host,
                String kickoffPoint, List<ChecklistItem> checkList,
                List<Destination> destinations) {
        this.key = UUID.randomUUID().toString();
        this.name = name;
        this.host = host;
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<TravelBudUser> getTravelers() {
        return travelers;
    }

    public void setTravelers(List<TravelBudUser> travelers) {
        this.travelers = travelers;
    }

    public String getKickoffPoint() {
        return kickoffPoint;
    }

    public void setKickoffPoint(String kickoffPoint) {
        this.kickoffPoint = kickoffPoint;
    }

    public List<ChecklistItem> getCheckList() {
        return checkList;
    }

    public void setCheckList(List<ChecklistItem> checkList) {
        this.checkList = checkList;
    }

    public List<Destination> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<Destination> destinations) {
        this.destinations = destinations;
    }

    public Object getBudget() {
        return Budget;
    }

    public void setBudget(Object budget) {
        Budget = budget;
    }
}