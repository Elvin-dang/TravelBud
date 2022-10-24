package com.example.travelbud;

import java.util.Date;

public class ChecklistItem {
    private String name;
    private Integer amount;
    private Boolean checked;
    private Date dueDate;
    private String category;

    public ChecklistItem(String name, Integer amount, Boolean checked,
                         String category) {
        this.name = name;
        this.amount = amount;
        this.checked = checked;
        this.dueDate = null;
        this.category = category;
    }

    public ChecklistItem() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}



