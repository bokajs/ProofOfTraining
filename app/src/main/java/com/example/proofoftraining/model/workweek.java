package com.example.proofoftraining.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by bokajs on 06.02.2015.
 */
public class workweek {

    //format Date for week_start and week_end
    Date date_instance = new Date();
    SimpleDateFormat format_date = new SimpleDateFormat("dd-MM-yyyy");

    //variables declarer
    int year_of_training;
    long week_ID;
    String week_start, week_end;
    String comment;
    private ArrayList<day> days = new ArrayList<day>();

    //constructor
    public workweek(long week_ID) {
        this.week_ID = week_ID;
    }

    //Getter and Setter
    public long getWeek_ID() {
        return week_ID;
    }

    public int getYear_of_training() {
        return year_of_training;
    }

    public void setYear_of_training(int year_of_training) {
        this.year_of_training = year_of_training;
    }

    public String getWeek_start() {
        return week_start;
    }

    public void setWeek_start(String week_start) {
        this.week_start = format_date.format(week_start);
    }

    public String getWeek_end() {
        return week_end;
    }

    public void setWeek_end(String week_end) {
        this.week_end = format_date.format(week_end);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<day> getDays() {
        return days;
    }

    public void setDays(ArrayList<day> days) {
        this.days = days;
    }

    public SimpleDateFormat dateFormat() { return format_date; }
}
