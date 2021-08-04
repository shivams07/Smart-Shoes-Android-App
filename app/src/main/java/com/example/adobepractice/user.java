package com.example.adobepractice;

public class user {
    public String name;
    public String steps;
    public String cals;
    public user(){}

    public user(String name, String steps, String cals) {
        this.name = name;
        this.steps = steps;
        this.cals = cals;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getCals() {
        return cals;
    }

    public void setCals(String cals) {
        this.cals = cals;
    }
}
