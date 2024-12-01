package model;


import androidx.annotation.NonNull;

public class Region {

    private int RegionID;
    private String RegionName;



    //Getters and Setters


    public int getRegionID() {
        return RegionID;
    }

    public String getRegionName() {
        return RegionName;
    }

    public void setRegionID(int regionID) {
        RegionID = regionID;
    }

    public void setRegionName(String regionName) {
        RegionName = regionName;
    }

    @NonNull
    @Override
    public String toString() {
        return RegionID+" - " + RegionName;
    }
}
