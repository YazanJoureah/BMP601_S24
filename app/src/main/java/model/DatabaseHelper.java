package model;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//This class will handle the creation and management of the SQLite database.
public class DatabaseHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "SalesDB.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Region Table
        db.execSQL("CREATE TABLE Region (" +
                "RegionID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "RegionName TEXT NOT NULL" +
                ");");
        //Add Regions
        db.execSQL("INSERT INTO Region (RegionName) VALUES ('CoastArea');");
        db.execSQL("INSERT INTO Region (RegionName) VALUES ('NorthArea');");
        db.execSQL("INSERT INTO Region (RegionName) VALUES ('SouthArea');");
        db.execSQL("INSERT INTO Region (RegionName) VALUES ('EastArea');");
        db.execSQL("INSERT INTO Region (RegionName) VALUES ('LebanonArea');");

        // Create SalesRepresentative Table
        db.execSQL("CREATE TABLE SalesRepresentative (" +
                "RepresentativeID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Name TEXT NOT NULL," +
                "PhoneNumber TEXT," +
                "Email TEXT," +
                "RegionID INTEGER," +
                "FOREIGN KEY (RegionID) REFERENCES Region(RegionID)" +
                ");");

        // Create Sales Table
        db.execSQL("CREATE TABLE Sales (" +
                "SaleID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "RepresentativeID INTEGER," +
                "Month INTEGER NOT NULL," +
                "Year INTEGER NOT NULL," +
                "RegionID INTEGER NOT NULL,"+
                "Amount DECIMAL(10, 2) NOT NULL," +
                "FOREIGN KEY (RepresentativeID) REFERENCES SalesRepresentative(RepresentativeID)" +
                ");");

        // Create Commission Table
        db.execSQL("CREATE TABLE Commission (" +
                "CommissionID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "RepresentativeID INTEGER," +
                "Month INTEGER NOT NULL," +
                "Year INTEGER NOT NULL," +
                "Amount DECIMAL(10, 2) NOT NULL," +
                "FOREIGN KEY (RepresentativeID) REFERENCES SalesRepresentative(RepresentativeID)," +
                "UNIQUE (RepresentativeID, Month, Year)" +
                ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Commission");
        db.execSQL("DROP TABLE IF EXISTS Sales");
        db.execSQL("DROP TABLE IF EXISTS SalesRepresentative");
        db.execSQL("DROP TABLE IF EXISTS Region");
        onCreate(db);
    }
}
