package controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import model.DatabaseHelper;
import model.Commission;


public class CommissionController {

    private DatabaseHelper dbHelper;

    public CommissionController(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void addCommission(Commission commission) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("RepresentativeID", commission.getRepresentativeID());
        values.put("Month", commission.getMonth());
        values.put("Year", commission.getYear());
        values.put("Amount", commission.getAmount());
        db.insert("Sales", null, values);
        db.close();
    }

    public List<Commission> getAllCommissions() {
        List<Commission> commissionList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Commission", null);
        if (cursor.moveToFirst()) {
            do {
                Commission commission = new Commission();
                commission.setCommissionID(cursor.getInt(0));
                commission.setRepresentativeID(cursor.getInt(1));
                commission.setMonth(cursor.getInt(2));
                commission.setYear(cursor.getInt(3));
                commission.setAmount(cursor.getFloat(4));
                commissionList.add(commission);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return commissionList;
    }

    public List<Commission> getCommissionsByRepresentativeAndDate(int representativeID,int month,int year){
        List<Commission> commissionList=new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query="SELECT * FROM Sales WHERE RepresentativeID=? AND Month=? AND Year=?";
        Cursor cursor = db.rawQuery(query,new String[]{String.valueOf(representativeID),String.valueOf(month),String.valueOf(year)});

        if (cursor.moveToFirst()) {
            do {
                Commission commission = new Commission();
                commission.setCommissionID(cursor.getInt(0));
                commission.setRepresentativeID(cursor.getInt(1));
                commission.setMonth(cursor.getInt(2));
                commission.setYear(cursor.getInt(3));
                commission.setAmount(cursor.getFloat(4));
                commissionList.add(commission);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return commissionList;
    }
}
