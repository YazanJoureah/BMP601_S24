package controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import model.DatabaseHelper;
import model.Region;

public class RegionController {

    private DatabaseHelper dbHelper;

    public RegionController(Context context) {
        dbHelper = new DatabaseHelper(context);
    }


    public void addRegion(Region region) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("RegionName", region.getRegionName());

        db.insert("Region", null, values);
        db.close();
    }

    public List<Region> getRegions() {
        List<Region> regions = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query="SELECT * FROM Region";
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()) {
            do {
               Region region = new Region();
               region.setRegionID(cursor.getInt(0));
               region.setRegionName(cursor.getString(1));
               regions.add(region);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return regions;
    }

    public Region getRegionById(int regionID) {
        Region region= new Region();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query="SELECT * FROM Region WHERE RegionID=?";
        Cursor cursor = db.rawQuery(query,new String[]{String.valueOf(regionID)});

        if (cursor.moveToFirst()) {
            do {
                region.setRegionID(cursor.getInt(0));
                region.setRegionName(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return region;
    }
}
