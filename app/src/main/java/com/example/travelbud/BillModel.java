package com.example.travelbud;

public class BillModel {
    private String description;
    private double amount;
    private String payer;

    public BillModel(String description, double amount, String payer) {
        this.description = description;
        this.amount = amount;
        this.payer = payer;
    }

    public BillModel() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }
}
