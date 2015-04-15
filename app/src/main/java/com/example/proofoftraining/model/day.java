package com.example.proofoftraining.model;

import java.util.ArrayList;

/**
 * Created by bokajs on 06.02.2015.
 */
public class day {

    //variables declarer
    long day_ID, week_ID;
    int weekday;
    int leave, sick, other;
    private ArrayList<activity> activities = new ArrayList<activity>();

    //constructor
    public day(long day_ID ,int weekday, long week_ID) {
        this.day_ID = day_ID;
        this.weekday = weekday;
        this.week_ID = week_ID;
    }

    //Getter and Setter
    public long getDay_ID() {
        return day_ID;
    }

    public long getWeek_ID() {
        return week_ID;
    }

    public int getWeekday() {
        return weekday;
    }

    /*public void setWeekday(int weekday) {
        this.weekday = weekday;
    }*/

    public int getLeave() {
        return leave;
    }

    public void setLeave(int leave) {
        this.leave = leave;
    }

    public int getSick() {
        return sick;
    }

    public void setSick(int sick) {
        this.sick = sick;
    }

    public int getOther() {
        return other;
    }

    public void setOther(int other) {
        this.other = other;
    }

    public ArrayList<activity> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<activity> activities) {
        this.activities = activities;
    }
}
