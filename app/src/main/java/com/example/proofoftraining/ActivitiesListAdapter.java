package com.example.proofoftraining;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.proofoftraining.model.activity;

import java.util.List;

/**
 * Created by bokajs on 01.04.2015.
 * taken from https://github.com/yacekmm/jacek-droid/tree/master/ListViewDemo
 */
public class ActivitiesListAdapter extends ArrayAdapter<activity> {

    protected static final String LOG_TAG = ActivitiesListAdapter.class.getSimpleName();

    private List<activity> activity;
    private int layoutResourceId;
    private Context context;

    public ActivitiesListAdapter(Context context, int layoutResourceId, List<activity> activity) {
        super(context, layoutResourceId, activity);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ActivitiesHolder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new ActivitiesHolder();
        holder.activities = activity.get(position);
        holder.removeActivityButton = (ImageButton)row.findViewById(R.id.activity_delete);
        holder.removeActivityButton.setTag(holder.activities);

        holder.hours = (TextView)row.findViewById(R.id.activity_hours);
        setHoursTextListeners(holder);
        holder.activity = (TextView)row.findViewById(R.id.activity_activity);
        setActivityTextChangeListener(holder);

        row.setTag(holder);

        setupItem(holder);
        return row;
    }

    private void setupItem(ActivitiesHolder holder) {
        holder.hours.setText(String.valueOf(holder.activities.getHours()));
        holder.activity.setText(String.valueOf(holder.activities.getActivity()));
    }

    public static class ActivitiesHolder {
        activity activities;
        TextView hours;
        TextView activity;
        ImageButton removeActivityButton;
    }

    private void setActivityTextChangeListener(final ActivitiesHolder holder) {
        holder.activity.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                holder.activities.setActivity(s.toString());
                update_activities(holder);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    private void setHoursTextListeners(final ActivitiesHolder holder) {
        holder.hours.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    holder.activities.setHours(Integer.valueOf(s.toString()));
                }catch (NumberFormatException e) {
                    Log.e(LOG_TAG, "error reading activity hour: " + s.toString());
                }
                update_activities(holder);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });
    }
    public List<activity> getActivity() {
        return activity;
    }

    private void update_activities(final ActivitiesHolder holder) {
        if (MainActivity.db.getActivity(holder.activities.getActivity_ID())!=null)
            MainActivity.db.updateActivity(holder.activities);
        else
            MainActivity.db.createActivity(holder.activities);
    }
}
