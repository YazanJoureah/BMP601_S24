package model;

import androidx.annotation.NonNull;

public class Commission {
    private int CommissionID;
    private int RepresentativeID;
    private int Month;
    private int Year;
    private float Amount;




    //Getters and Setters


    public int getRepresentativeID() {
        return RepresentativeID;
    }

    public float getAmount() {
        return Amount;
    }

    public int getCommissionID() {
        return CommissionID;
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

    public void setCommissionID(int commissionID) {
        CommissionID = commissionID;
    }

    public void setMonth(int month) {
        Month = month;
    }

    public void setYear(int year) {
        Year = year;
    }

    @NonNull
    @Override
    public String toString() {
        return "Commission{" +
                "CommissionID=" + CommissionID +
                ", RepresentativeID=" + RepresentativeID +
                ", Month=" + Month +
                ", Year=" + Year +
                ", Amount=" + Amount +
                '}';
    }
}
