package com.example.adobepractice;

public class trusted_contacts {
    public String primary;
    public String secondary;
    public trusted_contacts(){
    }

    public trusted_contacts(String primary, String secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getSecondary() {
        return secondary;
    }

    public void setSecondary(String secondary) {
        this.secondary = secondary;
    }
}
