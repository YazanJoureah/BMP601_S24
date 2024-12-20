package model;

import android.net.Uri;

import androidx.annotation.NonNull;

public class SalesRepresentative {
    private int representativeID;
    private String name;
    private String phoneNumber;
    private String email;
    private Uri imageUri;
    private int regionID;

    public SalesRepresentative(String s, int i) {
        this.representativeID=i;
        this.name=s;
    }

    public SalesRepresentative() {

    }

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

    public Uri getImageUri() {
        return imageUri;
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

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
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
