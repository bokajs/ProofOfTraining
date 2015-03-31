package com.example.proofoftraining.model;

import com.example.proofoftraining.model.workweek;
import com.example.proofoftraining.model.day;
import com.example.proofoftraining.model.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by bokajs on 06.02.2015.
 */
public class DbHelper extends SQLiteOpenHelper {
    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "proofoftraining";

    // Table Names
    private static final String TABLE_ACTIVITY = "activitys";
    private static final String TABLE_DAY = "days";
    private static final String TABLE_WORKWEEK = "workweeks";

    // Common column names
    //private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    // ACTIVITY Table - column nmaes
    private static final String KEY_ACTIVITY_ID = "activity_id";
    private static final String KEY_HOURS = "hours";
    private static final String KEY_ACTIVITY = "activity";

    // DAY Table - column names
    private static final String KEY_DAY_ID = "day_id";
    private static final String KEY_LEAVE = "leave";
    private static final String KEY_SICK = "sick";
    private static final String KEY_OTHER = "other";
    private static final String KEY_WEEKDAY = "weekday";

    // WORKWEEK Table - column names
    private static final String KEY_WORKWEEK_ID = "workweek_id";
    private static final String KEY_WEEK_START = "week_start";
    private static final String KEY_WEEK_END = "week_end";
    private static final String KEY_YEAR_OF_TRAINING = "year_of_training";
    private static final String KEY_COMMENT = "comment";

    // Table Create Statements
    // activity table create statement
    private static final String CREATE_TABLE_ACTIVITY = "CREATE TABLE "
            + TABLE_ACTIVITY + "(" + KEY_ACTIVITY_ID + " INTEGER PRIMARY KEY," + KEY_HOURS
            + " INTEGER," + KEY_ACTIVITY + " TEXT," + KEY_DAY_ID + " INTEGER,"
            + KEY_CREATED_AT + " DATETIME" + ")";

    // Day table create statement
    private static final String CREATE_TABLE_DAY = "CREATE TABLE " + TABLE_DAY
            + "(" + KEY_DAY_ID + " INTEGER PRIMARY KEY," + KEY_LEAVE + " INTEGER,"
            + KEY_SICK + " INTEGER," + KEY_OTHER + " INTEGER,"+ KEY_WEEKDAY + " INTEGER,"
            + KEY_WORKWEEK_ID + " INTEGER," + KEY_CREATED_AT + " DATETIME" + ")";

    // workweek table create statement
    private static final String CREATE_TABLE_WORKWEEK = "CREATE TABLE "
            + TABLE_WORKWEEK + "(" + KEY_WORKWEEK_ID + " INTEGER PRIMARY KEY,"
            + KEY_WEEK_START + " DATETIME," + KEY_WEEK_END + " DATETIME,"
            + KEY_YEAR_OF_TRAINING + " INTEGER," + KEY_COMMENT + " TEXT,"
            + KEY_CREATED_AT + " DATETIME" + ")";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_ACTIVITY);
        db.execSQL(CREATE_TABLE_DAY);
        db.execSQL(CREATE_TABLE_WORKWEEK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKWEEK);

        // create new tables
        onCreate(db);
    }

    // ------------------------ "activity" table methods ----------------//

    /*
     * Creating a activity
     */
    public long createActivity(activity activity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ACTIVITY_ID, activity.getActivity_ID());
        values.put(KEY_HOURS, activity.getHours());
        values.put(KEY_ACTIVITY, activity.getActivity());
        values.put(KEY_DAY_ID, activity.getDay_ID());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long activity_id = db.insert(TABLE_ACTIVITY, null, values);

        /*/ creating the day, if not exist
        if(getDay(activity.getDay_ID()) == null) {
            day day = new day(activity.getDay_ID(),week_id);
            createDay(day);
        }*/

        return activity_id;
    }

    /*
     * get single activity
     */
    public activity getActivity(long activity_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_ACTIVITY + " WHERE "
                + KEY_ACTIVITY_ID + " = " + activity_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        activity td = new activity(activity_id,c.getInt(c.getColumnIndex(KEY_DAY_ID)));
        td.setHours(c.getInt(c.getColumnIndex(KEY_HOURS)));
        td.setActivity((c.getString(c.getColumnIndex(KEY_ACTIVITY))));

        return td;
    }

    /**
     * getting all activities
     * */
    public List<activity> getAllActivity() {
        List<activity> activities = new ArrayList<activity>();
        String selectQuery = "SELECT * FROM " + TABLE_ACTIVITY;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                activity td = new activity(c.getInt((c.getColumnIndex(KEY_ACTIVITY_ID))),c.getInt(c.getColumnIndex(KEY_DAY_ID)));
                td.setHours(c.getInt(c.getColumnIndex(KEY_HOURS)));
                td.setActivity((c.getString(c.getColumnIndex(KEY_ACTIVITY))));

                // adding to activity list
                activities.add(td);
            } while (c.moveToNext());
        }

        return activities;
    }

    /**
     * getting all activities by day_ID
     * */
    public List<activity> getAllActivityByDay_ID(long day_ID) {
        List<activity> activities = new ArrayList<activity>();
        String selectQuery = "SELECT * FROM " + TABLE_ACTIVITY + " WHERE "
                + KEY_DAY_ID + " = " + day_ID;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                activity td = new activity(c.getInt((c.getColumnIndex(KEY_ACTIVITY_ID))),c.getInt(c.getColumnIndex(KEY_DAY_ID)));
                td.setHours(c.getInt(c.getColumnIndex(KEY_HOURS)));
                td.setActivity((c.getString(c.getColumnIndex(KEY_ACTIVITY))));

                // adding to activity list
                activities.add(td);
            } while (c.moveToNext());
        }

        return activities;
    }

     /*
     * Updating a activity
     */
    public long updateActivity(activity activity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ACTIVITY, activity.getActivity());
        values.put(KEY_HOURS, activity.getHours());
        values.put(KEY_DAY_ID, activity.getDay_ID());

        // updating row
        return db.update(TABLE_ACTIVITY, values, KEY_ACTIVITY_ID + " = ?",
                new String[] { String.valueOf(activity.getActivity_ID()) });
    }

    /*
     * Deleting a activity
     */
    public void deleteActivity(long activity) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ACTIVITY, KEY_ACTIVITY_ID + " = ?",
                new String[] { String.valueOf(activity) });
    }

    // ------------------------ "day" table methods ----------------//

    /*
     * Creating day
     */
    public long createDay(day day) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DAY_ID, day.getDay_ID());
        values.put(KEY_LEAVE, day.getLeave());
        values.put(KEY_SICK, day.getSick());
        values.put(KEY_OTHER, day.getOther());
        values.put(KEY_WEEKDAY, day.getWeekday());
        values.put(KEY_WORKWEEK_ID, day.getWeekday());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long day_id = db.insert(TABLE_DAY, null, values);

        // creating workweek, if not exist
        //if(getWorkweek(day.getWeek_ID()) == null) createWorkweek(new workweek(day.getWeek_ID()));

        return day_id;
    }

    /*
     * get single day
     */
    public day getDay(long day_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_DAY + " WHERE "
                + KEY_DAY_ID + " = " + day_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        day td = new day(day_id,c.getInt(c.getColumnIndex(KEY_WORKWEEK_ID)));
        td.setLeave(c.getInt(c.getColumnIndex(KEY_LEAVE)));
        td.setSick(c.getInt(c.getColumnIndex(KEY_SICK)));
        td.setOther(c.getInt(c.getColumnIndex(KEY_OTHER)));
        td.setWeekday(c.getInt(c.getColumnIndex(KEY_WEEKDAY)));

        return td;
    }

    /**
     * getting all days
     * */
    public List<day> getAllDays() {
        List<day> days = new ArrayList<day>();
        String selectQuery = "SELECT * FROM " + TABLE_DAY;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                day t = new day(c.getInt((c.getColumnIndex(KEY_DAY_ID))),c.getInt(c.getColumnIndex(KEY_WORKWEEK_ID)));
                t.setLeave(c.getInt(c.getColumnIndex(KEY_LEAVE)));
                t.setSick(c.getInt(c.getColumnIndex(KEY_SICK)));
                t.setOther(c.getInt(c.getColumnIndex(KEY_OTHER)));
                t.setWeekday(c.getInt(c.getColumnIndex(KEY_WEEKDAY)));

                // adding to days list
                days.add(t);
            } while (c.moveToNext());
        }
        return days;
    }

    /**
     * getting all days by week_ID
     * */
    public List<day> getAllDaysByWeek_ID(long week_ID) {
        List<day> days = new ArrayList<day>();
        String selectQuery = "SELECT * FROM " + TABLE_DAY + " WHERE "
                + KEY_WORKWEEK_ID + " = " + week_ID;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                day t = new day(c.getInt((c.getColumnIndex(KEY_DAY_ID))),c.getInt(c.getColumnIndex(KEY_WORKWEEK_ID)));
                t.setLeave(c.getInt(c.getColumnIndex(KEY_LEAVE)));
                t.setSick(c.getInt(c.getColumnIndex(KEY_SICK)));
                t.setOther(c.getInt(c.getColumnIndex(KEY_OTHER)));
                t.setWeekday(c.getInt(c.getColumnIndex(KEY_WEEKDAY)));

                // adding to days list
                days.add(t);
            } while (c.moveToNext());
        }
        return days;
    }

    /*
     * Updating a day
     */
    public long updateDay(day day) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LEAVE, day.getLeave());
        values.put(KEY_SICK, day.getSick());
        values.put(KEY_OTHER, day.getOther());
        values.put(KEY_WEEKDAY, day.getWeekday());
        values.put(KEY_WORKWEEK_ID, day.getWeek_ID());

        // updating row
        return db.update(TABLE_DAY, values, KEY_DAY_ID + " = ?",
                new String[] { String.valueOf(day.getDay_ID()) });
    }

    /*
     * Deleting a day
     */
    public void deleteDay(day day, boolean should_delete_all_activities) {
        SQLiteDatabase db = this.getWritableDatabase();

        // before deleting day
        // check if activities under this tag should also be deleted
        if (should_delete_all_activities) {
            // get all activities under this day
            List<activity> allActivity = getAllActivityByDay_ID(day.getDay_ID());

            // delete all activities
            for (activity activity : allActivity) {
                // delete activity
               deleteActivity(activity.getActivity_ID());
            }
        }

        // now delete the day
        db.delete(TABLE_DAY, KEY_DAY_ID + " = ?",
                new String[] { String.valueOf(day.getDay_ID()) });
    }

    // ------------------------ "workweek" table methods ----------------//

    /*
     * Creating workweek
     */
    public long createWorkweek(workweek workweek) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_WORKWEEK_ID, workweek.getWeek_ID());
        values.put(KEY_WEEK_START, workweek.getWeek_start());
        values.put(KEY_WEEK_END, workweek.getWeek_end());
        values.put(KEY_YEAR_OF_TRAINING, workweek.getYear_of_training());
        values.put(KEY_COMMENT, workweek.getComment());
        values.put(KEY_CREATED_AT, getDateTime());

        long id = db.insert(TABLE_WORKWEEK, null, values);

        return id;
    }

    /*
     * get single workweek
     */
    public workweek getWorkweek(long workweek_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_WORKWEEK + " WHERE "
                + KEY_WORKWEEK_ID + " = " + workweek_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        workweek td = new workweek(workweek_id);
        td.setWeek_start(c.getString(c.getColumnIndex(KEY_WEEK_START)));
        td.setWeek_end(c.getString(c.getColumnIndex(KEY_WEEK_END)));
        td.setYear_of_training((c.getInt(c.getColumnIndex(KEY_YEAR_OF_TRAINING))));
        td.setComment(c.getString(c.getColumnIndex(KEY_COMMENT)));

        return td;
    }

    /**
     * getting all workweeks
     * */
    public List<workweek> getAllWorkweeks() {
        List<workweek> workweeks = new ArrayList<workweek>();
        String selectQuery = "SELECT * FROM " + TABLE_WORKWEEK;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                workweek t = new workweek(c.getInt(c.getColumnIndex(KEY_WORKWEEK_ID)));
                t.setWeek_start(c.getString(c.getColumnIndex(KEY_WEEK_START)));
                t.setWeek_end(c.getString(c.getColumnIndex(KEY_WEEK_END)));
                t.setYear_of_training(c.getInt(c.getColumnIndex(KEY_YEAR_OF_TRAINING)));
                t.setComment(c.getString(c.getColumnIndex(KEY_COMMENT)));

                // adding to days list
                workweeks.add(t);
            } while (c.moveToNext());
        }
        return workweeks;
    }

    /*
     * Updating a workweek
     */
    public long updateWorkweek(workweek workweek) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_WEEK_START, workweek.getWeek_start());
        values.put(KEY_WEEK_END, workweek.getWeek_end());
        values.put(KEY_YEAR_OF_TRAINING, workweek.getYear_of_training());
        values.put(KEY_COMMENT, workweek.getComment());
        values.put(KEY_CREATED_AT, getDateTime());

        // updating row
        return db.update(TABLE_ACTIVITY, values, KEY_WORKWEEK_ID + " = ?",
                new String[] { String.valueOf(workweek.getWeek_ID()) });
    }

    /*
     * Deleting a workweek
     */
    public void deleteWorkweek(workweek workweek, boolean should_delete_all_days_and_activities) {
        SQLiteDatabase db = this.getWritableDatabase();

        // before deleting day
        // check if activities under this tag should also be deleted
        if (should_delete_all_days_and_activities) {
            // get all activities under this day
            List<day> allDays = getAllDaysByWeek_ID(workweek.getWeek_ID());

            // delete all days and activities
            for (day day : allDays) {
                // delete day and activities
                deleteDay(day, should_delete_all_days_and_activities);
            }
        }

        // now delete the workweek
        db.delete(TABLE_WORKWEEK, KEY_WORKWEEK_ID + " = ?",
                new String[] { String.valueOf(workweek.getWeek_ID()) });
    }

    /*
         * getting count for workweek
         */
    public int getCountWorkweek() {
        return getCount(TABLE_WORKWEEK);
    }

    // ------------------------ general table methods ----------------//

    /*
         * getting count
         */
    public int getCount(String table) {
        String countQuery = "SELECT * FROM " + table;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /**
     * get datetime
     * */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}
