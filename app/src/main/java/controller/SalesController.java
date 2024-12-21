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

    public boolean addSale(Sales sale) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("RepresentativeID", sale.getRepresentativeID());
        values.put("Month", sale.getMonth());
        values.put("Year", sale.getYear());
        values.put("RegionID",sale.getRegionID());
        values.put("Amount", sale.getAmount());
        db.insert("Sales", null, values);
        db.close();
        return false;
    }

    public boolean updateSale(Sales sale) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("RepresentativeID", sale.getRepresentativeID());
        values.put("Month", sale.getMonth());
        values.put("Year", sale.getYear());
        values.put("RegionID",sale.getRegionID());
        values.put("Amount", sale.getAmount());

        db.update("Sales", values,
                "RepresentativeID=? AND Month=? AND Year=? AND RegionID=?",
                new String[]{
                        String.valueOf(sale.getRepresentativeID()),
                        String.valueOf(sale.getMonth()),
                        String.valueOf(sale.getYear()),
                        String.valueOf(sale.getRegionID())});
        db.close();
        return false;
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
                sale.setRegionID(cursor.getInt(4));
                sale.setAmount(cursor.getFloat(5));
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
                sale.setRegionID(cursor.getInt(4));
                sale.setAmount(cursor.getFloat(5));
                salesList.add(sale);
            }while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return salesList;
    }

    public void deleteSale(Sales sale) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("Sales",
                "RepresentativeID=? AND Month=? AND Year=? AND RegionID=?",
                new String[]{
                        String.valueOf(sale.getRepresentativeID()),
                        String.valueOf(sale.getMonth()),
                        String.valueOf(sale.getYear()),
                        String.valueOf(sale.getRegionID())});
        db.close();
    }

    public void deleteAllSale(int currentRepresentativeId, int selectedMonth, int selectedYear) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("Sales",
                "RepresentativeID=? AND Month=? AND Year=? ",
                new String[]{
                        String.valueOf(currentRepresentativeId),
                        String.valueOf(selectedMonth),
                        String.valueOf(selectedYear),
                        });
        db.close();
    }
}
