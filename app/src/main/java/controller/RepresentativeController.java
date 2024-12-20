package controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import model.DatabaseHelper;
import model.SalesRepresentative;

public class RepresentativeController {
    private final DatabaseHelper dbHelper;

    public RepresentativeController(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void addRepresentative(SalesRepresentative representative) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", representative.getName());
        values.put("PhoneNumber", representative.getPhoneNumber());
        values.put("Email", representative.getEmail());
        values.put("ImageUri", String.valueOf(representative.getImageUri()));
        values.put("RegionID", representative.getRegionID());

        db.insert("SalesRepresentative", null, values);
        db.close();
    }

    public List<SalesRepresentative> getRepresentatives() {
        List<SalesRepresentative> representatives = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query="SELECT * FROM SalesRepresentative";
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()) {
            do {
                SalesRepresentative salesRep = new SalesRepresentative();
                salesRep.setRepresentativeID(cursor.getInt(0));
                salesRep.setName(cursor.getString(1));
                salesRep.setPhoneNumber(cursor.getString(2));
                salesRep.setEmail(cursor.getString(3));
                salesRep.setImageUri(Uri.parse(cursor.getString(4)));
                salesRep.setRegionID(cursor.getInt(5));

                representatives.add(salesRep);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return representatives;
    }

    public SalesRepresentative getRepresentativeById(int representativeID) {
        SalesRepresentative representative= new SalesRepresentative();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query="SELECT * FROM SalesRepresentative WHERE RepresentativeID=?";
        Cursor cursor = db.rawQuery(query,new String[]{String.valueOf(representativeID)});

        if (cursor.moveToFirst()) {
            do {
                representative.setRepresentativeID(cursor.getInt(0));
                representative.setName(cursor.getString(1));
                representative.setPhoneNumber(cursor.getString(2));
                representative.setEmail(cursor.getString(3));
                representative.setImageUri(Uri.parse(cursor.getString(4)));
                representative.setRegionID(cursor.getInt(5));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return representative;
    }

    public List<SalesRepresentative> getRepresentativesByRegion(int regionID) {
        List<SalesRepresentative> representatives = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query="SELECT * FROM SalesRepresentative WHERE RegionID=?";
        Cursor cursor = db.rawQuery(query,new String[]{String.valueOf(regionID)});

        if (cursor.moveToFirst()) {
            do {
                SalesRepresentative salesRep = new SalesRepresentative();
                salesRep.setRepresentativeID(cursor.getInt(0));
                salesRep.setName(cursor.getString(1));
                salesRep.setPhoneNumber(cursor.getString(2));
                salesRep.setEmail(cursor.getString(3));
                salesRep.setImageUri(Uri.parse(cursor.getString(4)));
                salesRep.setRegionID(cursor.getInt(5));
                representatives.add(salesRep);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return representatives;
    }

    public void updateRepresentative(SalesRepresentative representative) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", representative.getName());
        values.put("PhoneNumber", representative.getPhoneNumber());
        values.put("Email", representative.getEmail());
        values.put("ImageUri", String.valueOf(representative.getImageUri()));
        values.put("RegionID", representative.getRegionID());

        db.update("SalesRepresentative", values,
                "RepresentativeID=?",
                new String[]{String.valueOf(representative.getRepresentativeID())});
        db.close();
    }

    public void deleteRepresentative(int representativeID) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("SalesRepresentative",
                "RepresentativeID=?",
                new String[]{String.valueOf(representativeID)});
        db.close();
    }

}
