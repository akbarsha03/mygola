package in.shaapps.mygola.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * My Gola
 * --
 * Created by Akbar Sha Ebrahim on 11/8/2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static String TABLE = "mygoala";
    private static String ACTUAL_PRICE = "actual_price";
    private static String RATING = "rating";
    private static String CITY = "city";
    private static String LOCATION = "location";
    private static String DESCRIPTION = "description";
    private static String IMAGE = "image";
    private static String BOOKMARK = "bookmark";
    private static String DISCOUNT = "discount";
    private static String NAME = "name";

    public DbHelper(Context context) {
        super(context, "MY_GOLA", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE + "(id INTEGER PRIMARY KEY,"
                + ACTUAL_PRICE + " TEXT," + RATING + " TEXT," + CITY + " TEXT," + LOCATION + " TEXT,"
                + DESCRIPTION + " TEXT," + IMAGE + " TEXT," + BOOKMARK + " TEXT,"
                + DISCOUNT + " TEXT," + NAME + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);

        // Create tables again
        onCreate(db);
    }

    // Adding new contact
    public void insertData(Activity activity) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE + " where " + NAME + " =?; ";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{activity.getName()});

        // looping through all rows and adding to list
        Log.d("SHA", "count " + cursor.getCount());
        if (cursor != null)
            if (cursor.getCount() == 0) {
                /**
                 * Insert values only if it the data is new
                 */
                ContentValues values = new ContentValues();
                values.put(NAME, activity.getName()); // Contact Name
                values.put(ACTUAL_PRICE, activity.getActualPrice()); // Contact Name
                values.put(RATING, activity.getRating()); // Contact Name
                values.put(CITY, activity.getCity()); // Contact Name
                values.put(LOCATION, activity.getLocation()); // Contact Name
                values.put(DESCRIPTION, activity.getDescription()); // Contact Name
                values.put(IMAGE, activity.getImage()); // Contact Name
                values.put(DISCOUNT, activity.getDiscount()); // Contact Name

                // Inserting Row
                db.insert(TABLE, null, values);
                Log.d("SHA", "row inserted");
            } else
                Log.d("SHA", "row not inserted");
        db.close(); // Closing database connection
    }

    public List<Activity> getBookmarked() {

        List<Activity> activityList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE + " where " + BOOKMARK + " =?; ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{BOOKMARK});

        // looping through all rows and adding to list
        if (cursor != null)
            if (cursor.moveToFirst()) {
                do {
                    Activity activity = new Activity();
                    activity.setActualPrice(cursor.getString(1));
                    activity.setRating(cursor.getString(2));
                    activity.setCity(cursor.getString(3));
                    activity.setLocation(cursor.getString(4));
                    activity.setDescription(cursor.getString(5));
                    activity.setImage(cursor.getString(6));
                    activity.setDiscount(cursor.getString(8));
                    activity.setName(cursor.getString(9));
                    activityList.add(activity);
                } while (cursor.moveToNext());
            }

        return activityList;
    }

    public int addBookmark(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BOOKMARK, BOOKMARK);

        // updating row
        return db.update(TABLE, values, NAME + " =? ",
                new String[]{name});
    }

    public int removeBookmark(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BOOKMARK, "unbookmarked");

        // updating row
        return db.update(TABLE, values, NAME + " =? ",
                new String[]{name});
    }

    public Activity getActivity(String name) {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE + " where " + NAME + " =?; ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{name});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Activity activity = new Activity();
                activity.setActualPrice(cursor.getString(1));
                activity.setRating(cursor.getString(2));
                activity.setCity(cursor.getString(3));
                activity.setLocation(cursor.getString(4));
                activity.setDescription(cursor.getString(5));
                activity.setImage(cursor.getString(6));
                activity.setDiscount(cursor.getString(8));
                activity.setName(cursor.getString(9));
                return activity;
            } while (cursor.moveToNext());
        }

        return null;
    }

    public List<Activity> getAllDetails() {

        List<Activity> activities = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE + "; ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list

        if (cursor != null)
            if (cursor.moveToFirst()) {
                do {

                    Activity activity = new Activity();
                    activity.setActualPrice(cursor.getString(1));
                    activity.setRating(cursor.getString(2));
                    activity.setCity(cursor.getString(3));
                    activity.setLocation(cursor.getString(4));
                    activity.setDescription(cursor.getString(5));
                    activity.setImage(cursor.getString(6));
                    activity.setDiscount(cursor.getString(8));
                    activity.setName(cursor.getString(9));

                    // Adding activity to list
                    activities.add(activity);
                } while (cursor.moveToNext());
            }

        return activities;
    }
}