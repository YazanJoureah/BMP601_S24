package controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import model.DatabaseHelper;
import model.Sales;

import java.util.ArrayList;
import java.util.List;
public class SalesController {
    private DatabaseHelper dbHelper;

    public SalesController(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void addSale(Sales sale) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("RepresentativeID", sale.getRepresentativeID());
        values.put("Month", sale.getMonth());
        values.put("Year", sale.getYear());
        values.put("Amount", sale.getAmount());
        values.put("Commission", sale.getCommission());
        db.insert("Sales", null, values);
        db.close();
    }

    public List<Sales> getAllSales() {
        List<Sales> salesList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Sales", null);
        if (cursor.moveToFirst()) {
            do {
                Sales sale = new Sales();
                sale.setSaleID(cursor.getInt(0));
                sale.setRepresentativeID(cursor.getInt(1));
                sale.setMonth(cursor.getInt(2));
                sale.setYear(cursor.getInt(3));
                sale.setAmount(cursor.getFloat(4));
                sale.setCommission(cursor.getFloat(5));
                salesList.add(sale);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return salesList;
    }

    public List<Sales> getSalesByRepresentativeAndDate(int representativeID,int month,int year){
        List<Sales> salesList=new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query="SELECT * FROM Sales WHERE RepresentativeID=? AND Month=? AND Year=?";
        Cursor cursor = db.rawQuery(query,new String[]{String.valueOf(representativeID),String.valueOf(month),String.valueOf(year)});

        if(cursor.moveToFirst()){
            do{
                Sales sale=new Sales();
                sale.setSaleID(cursor.getInt(0));
                sale.setRepresentativeID(cursor.getInt(1));
                sale.setMonth(cursor.getInt(2));
                sale.setYear(cursor.getInt(3));
                sale.setAmount(cursor.getFloat(4));
                sale.setCommission(cursor.getFloat(5));
                salesList.add(sale);
            }while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return salesList;
    }
}