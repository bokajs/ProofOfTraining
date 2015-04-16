package com.example.proofoftraining;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.proofoftraining.model.DbHelper;
import com.example.proofoftraining.model.activity;
import com.example.proofoftraining.model.day;
import com.example.proofoftraining.model.workweek;
import com.example.proofoftraining.model.workweekParcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    // Database Helper
    //public, because the ActivitiesListAdapter work with
    public static DbHelper db;

    //count of week for the Drawer menu
    public static int weeks;
    //the activity list adapter
    private ActivitiesListAdapter activitiesListAdapter;
    //the yet in menus selected objects
    private workweek selected_workweek;
    private day selected_day;

    //EXTRA_MESSAGE of the changeWorkweekActivity
    /*public static String EXTRA_MESSAGE_week_ID ="com.example.proofoftraining.week.id";
    public static String EXTRA_MESSAGE_week_start ="com.example.proofoftraining.week.start";
    public static String EXTRA_MESSAGE_week_end ="com.example.proofoftraining.week.end";
    public static String EXTRA_MESSAGE_year_of_training_training ="com.example.proofoftraining.week.year";
    public static String EXTRA_MESSAGE_comment ="com.example.proofoftraining.week.comment";*/
    public static String EXTRA_MESSAGE_workweekParcelable="com.example.proofoftraining.workweek.parcelable";
    public static String EXTRA_MESSAGE_week_new ="com.example.proofoftraining.week.new"; //1 for a new week
    public static int ACTIVITY_RESULT_REQUEST_SUB = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //set up the DatabaseHelper
        db = new DbHelper(getApplicationContext());
        //set the count of week
        MainActivity.weeks=db.getCountWorkweek();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();


        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //Add tabs to the tab-menu, the day menu
        AddTab("Mon", "Monday",1);
        AddTab("Tue", "Tuesday",2);
        AddTab("Wed", "Wednesday",3);
        AddTab("Thu", "Thursday",4);
        AddTab("Fri", "Friday",5);
        AddTab("Sat", "Saturday",6);
        AddTab("Sun", "Sunday",7);

        // close database connection
        db.closeDB();
    }

    //menu to select the week of work
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();

        //Add Week position -> create changeWorkweekActivity and send information about the workweek
        //try to set selected_workweek from database, if not than add a week
        if ((selected_workweek = db.getWorkweek(position))==null || position==0){
            Intent intent = new Intent(this, changeWorkweekActivity.class);
            workweekParcelable workweekParcelable = new workweekParcelable(db.getCountWorkweek()+1);
            intent.putExtra(EXTRA_MESSAGE_workweekParcelable, workweekParcelable);
            intent.putExtra(EXTRA_MESSAGE_week_new, "1");
            startActivityForResult(intent, ACTIVITY_RESULT_REQUEST_SUB);

            //selected_workweek = new workweek(0);
        }
    }

    //Callback from the changeWorkweekActivity with new workweek object
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final workweekParcelable workweek = data.getParcelableExtra(MainActivity.EXTRA_MESSAGE_workweekParcelable);

        if (db.getWorkweek(workweek.getWeek_ID())!=null)
            db.updateWorkweek(workweek);
        else
            db.createWorkweek(workweek);

        //update count of week, selected_week, mCurrentSelectedPosition and Title
        MainActivity.weeks=db.getCountWorkweek();
        selected_workweek=workweek;
        NavigationDrawerFragment.mCurrentSelectedPosition= (int) selected_workweek.getWeek_ID();
        onSectionAttached(NavigationDrawerFragment.mCurrentSelectedPosition+1);
    }

    public void onSectionAttached(int number) {
        if (number==1) mTitle = "Add a Week!";
        else mTitle = getString(R.string.title_section)+" "+(number-1);
    }

    public void AddTab(String name, String description, final int weekday) {
        ActionBar actionBar = getSupportActionBar();
        final ActionBar.Tab $name = actionBar.newTab();
        $name.setText(name)
                .setContentDescription(description)
                .setTabListener(
                        new ActionBar.TabListener() {
                            @Override
                            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                                if (selected_workweek != null)
                                    if ((selected_day = db.getDay(weekday,selected_workweek.getWeek_ID()))==null) {
                                        selected_day = new day(db.getCountDay()+1, weekday, selected_workweek.getWeek_ID());
                                        db.createDay(selected_day);
                                    }

                                //Set up the Activity List
                                setupActivityListViewAdapter();
                            }

                            @Override
                            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                                //delete activitiesListAdapter
                                activitiesListAdapter=null;
                                selected_day=null;
                            }

                            @Override
                            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

                            }
                        });
        actionBar.addTab($name);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(false);
        //actionBar.setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();


            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       if (id == R.id.action_menu2_away) {
           final PopupMenu popup = new PopupMenu(this, findViewById(R.id.action_menu2_away));
           MenuInflater inflater = popup.getMenuInflater();
           inflater.inflate(R.menu.main2_away, popup.getMenu());

           switch (selected_day.getAbsent_day()) {
               case 1:
                   popup.getMenu().findItem(R.id.action_away_leave).setChecked(true);
                   break;
               case 2:
                   popup.getMenu().findItem(R.id.action_away_sick).setChecked(true);
                   break;
               case 3:
                   popup.getMenu().findItem(R.id.action_away_other).setChecked(true);
                   break;
           }

           //Add present Item to the menu2_away, if an away Item is selected
           if (selected_day.getAbsent_day()!=0) {
               popup.getMenu().add(R.id.group_away, 0, 0, getString(R.string.menu2_away_present));

           }

           popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
               public boolean onMenuItemClick(MenuItem item) {
                   switch (item.getItemId()) {
                       default:
                           selected_day.setAbsent_day(0);
                           break;
                       case R.id.action_away_leave:
                           selected_day.setAbsent_day(1);
                           break;
                       case R.id.action_away_sick:
                           selected_day.setAbsent_day(2);
                           break;
                       case R.id.action_away_other:
                           selected_day.setAbsent_day(3);
                           break;
                   }
                   db.updateDay(selected_day);
                   return true;
               }
           });

           popup.show();

           return true;
       }

       if (id == R.id.action_activity_add) {
           //Ads a new activity
           activity activity = new activity(db.getCountActivities()+1,selected_day.getDay_ID());
           if (selected_day.getAbsent_day()==0)
               activity.setActivity("");
           else
                activity.setActivity(getString(R.string.activity_activity_away));
           activitiesListAdapter.insert(activity,0);
           return true;
       }

       if (id == R.id.action_change_workweek) {
           //change activity
           Intent intent = new Intent(this, changeWorkweekActivity.class);
           workweekParcelable workweekParcelable = (workweekParcelable) selected_workweek;
           /*workweekParcelable workweekParcelable = new workweekParcelable(selected_workweek.getWeek_ID());
           workweekParcelable.setWeek_start(selected_workweek.getWeek_start());
           workweekParcelable.setYear_of_training(selected_workweek.getYear_of_training());
           workweekParcelable.setComment(selected_workweek.getComment());*/
           intent.putExtra(EXTRA_MESSAGE_workweekParcelable, workweekParcelable);
           intent.putExtra(EXTRA_MESSAGE_week_new, "0");
           startActivityForResult(intent, ACTIVITY_RESULT_REQUEST_SUB);
           return true;
       }

        if (id == R.id.action_settings) {
            //SettingsActivity;
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }

        if (id == R.id.action_info) {

            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_WEEK_NUMBER = "week_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int weekNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_WEEK_NUMBER, weekNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

           return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_WEEK_NUMBER));
        }
    }

    //ActivityList
    public void removeActivityOnClickHandler(View v) {
        activity itemToRemove = (activity)v.getTag();
        activitiesListAdapter.remove(itemToRemove);
        db.deleteActivity(itemToRemove.getActivity_ID());
    }
    private void setupActivityListViewAdapter() {
        activitiesListAdapter = new ActivitiesListAdapter(MainActivity.this, R.layout.item_activities, db.getAllActivityByDay_ID(selected_day.getDay_ID()));
        final ListView ActivitiesListView = (ListView)findViewById(R.id.listView_activities);
        ActivitiesListView.setAdapter(activitiesListAdapter);
    }



}
