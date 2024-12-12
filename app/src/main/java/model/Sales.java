package model;

import androidx.annotation.NonNull;

public class Sales {

   private int SaleID;
   private int RepresentativeID ;
   private int Month;
   private int Year;
   private int RegionID;
   private float Amount;




   //Getters And Setters


    public int getSaleID() {
        return SaleID;
    }

    public int getRepresentativeID() {
        return RepresentativeID;
    }

    public float getAmount() {
        return Amount;
    }


    public int getMonth() {
        return Month;
    }

    public int getYear() {
        return Year;
    }

    public void setRepresentativeID(int representativeID) {
        RepresentativeID = representativeID;
    }

    public void setAmount(float amount) {
        Amount = amount;
    }

    public void setMonth(int month) {
        Month = month;
    }

    public void setYear(int year) {
        Year = year;
    }

    public void setSaleID(int saleID) {
        SaleID = saleID;
    }

    public void setRegionID(int region) {
        RegionID = region;
    }

    public int getRegionID() {
        return RegionID;
    }

    @NonNull
    @Override
    public String toString() {
        return "Sales{" +
                "SaleID=" + SaleID +
                ", RepresentativeID=" + RepresentativeID +
                ", Month=" + Month +
                ", Year=" + Year +
                ", RegionID=" + RegionID +
                ", Amount=" + Amount +
                '}';
    }
}
