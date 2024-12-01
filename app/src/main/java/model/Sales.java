package model;

import androidx.annotation.NonNull;

public class Sales {

   private int SaleID;
   private int RepresentativeID ;
   private int Month;
   private int Year;
   private float Amount;
   private float Commission;



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

    public float getCommission() {
        return Commission;
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

    public void setCommission(float commission) {
        Commission = commission;
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

    @NonNull
    @Override
    public String toString() {
        return "Sales{" +
                "SaleID=" + SaleID +
                ", RepresentativeID=" + RepresentativeID +
                ", Month=" + Month +
                ", Year=" + Year +
                ", Amount=" + Amount +
                ", Commission=" + Commission +
                '}';
    }
}
