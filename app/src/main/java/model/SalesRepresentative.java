package model;

import androidx.annotation.NonNull;

public class SalesRepresentative {
    private int representativeID;
    private String name;
    private String phoneNumber;
    private String email;
    private int regionID;

       // Getters and Setters

    public int getRepresentativeID() {
        return representativeID;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public int getRegionID() {
        return regionID;
    }

    public void setRegionID(int regionID) {
        this.regionID = regionID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setRepresentativeID(int representativeID) {
        this.representativeID = representativeID;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
