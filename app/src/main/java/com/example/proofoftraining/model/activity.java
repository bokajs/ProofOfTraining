package com.example.proofoftraining.model;

/**
 * Created by bokajs on 06.02.2015.
 */
public class activity {

    //variables declarer
    long activity_ID, day_ID;
    int hours;
    String activity;

    //constructor
    public activity(long activity_ID, long day_ID) {
        this.activity_ID = activity_ID;
        this.day_ID = day_ID;
    }

    //Getter and Setter
    public long getActivity_ID() {
        return activity_ID;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public long getDay_ID() {
        return day_ID;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}
