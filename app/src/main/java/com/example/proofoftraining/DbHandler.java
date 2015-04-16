package com.example.proofoftraining;

import android.database.sqlite.SQLiteDatabase;

import com.example.proofoftraining.model.DbHelper;

/**
 * without function, but not unimportant
 * Created by bokaj on 15.04.2015.
 */
public class DbHandler implements Runnable {
    private DbHelper myDbHelper = null;
    private MainActivity Activity = null;
    private String[] sqlInputData = null;
    private String[] sqlOutputData = null;
    private boolean isBusy = false;
    private int currentSqlCommand = -1;

    public DbHandler(DbHelper myDbHelper,MainActivity myActivity) {
        this.myDbHelper = myDbHelper;
        this.Activity = myActivity;
    }
    public synchronized boolean setNewSqlAction(int newSqlCommand,String[] sqlData) {
        if (this.isBusy) {
            return false;
        }
        this.isBusy = true;
        this.currentSqlCommand = newSqlCommand;
        this.sqlInputData = sqlData;
        return true;
    }
    @Override
    public void run() {
        sqlOutputData = null;
        SQLiteDatabase db = this.myDbHelper.getWritableDatabase();

                //long newUserId = DbHelper.insertNewUser(db,this.sqlInputData[0]);
               // this.sqlOutputData = new String[] {String.valueOf(newUserId)};

        this.myDbHelper.close();
        this.Activity.runOnUiThread(new Runnable() {
            public void run() {
                //Activity.handleSqlResponse(currentSqlCommand, sqlOutputData);
            }
        });
        this.isBusy = false;
    }
    public boolean isBusy() {
        return isBusy;
    }
}
