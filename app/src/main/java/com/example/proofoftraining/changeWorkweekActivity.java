package com.example.proofoftraining;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import com.example.proofoftraining.model.workweek;
import com.example.proofoftraining.model.workweekParcelable;

import java.util.Date;


public class changeWorkweekActivity extends ActionBarActivity {

    //EXTRA_MESSAGE of the changeWorkweekActivity
    /*public static String EXTRA_MESSAGE_week_ID ="com.example.proofoftraining.week.id";
    public static String EXTRA_MESSAGE_week_start ="com.example.proofoftraining.week.start";
    public static String EXTRA_MESSAGE_week_end ="com.example.proofoftraining.week.end";
    public static String EXTRA_MESSAGE_year_of_training_training ="com.example.proofoftraining.week.year";
    public static String EXTRA_MESSAGE_comment ="com.example.proofoftraining.week.comment";*/
    public static String EXTRA_MESSAGE_workweekParcelable="com.example.proofoftraining.workweek.parcelable";
    public static String EXTRA_MESSAGE_week_new ="com.example.proofoftraining.week.new"; //1 for a new week

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_workweek);

        //get the Object EXTRA_MESSAGE_workweekParcelable from the MainActivity
        Intent intent = getIntent();
        final workweekParcelable workweekParcelable = intent.getParcelableExtra(MainActivity.EXTRA_MESSAGE_workweekParcelable);
        ((EditText) findViewById(R.id.week_ID)).setText(
                String.valueOf(workweekParcelable.getWeek_ID()));


        if (Integer.parseInt(intent.getStringExtra(MainActivity.EXTRA_MESSAGE_week_new))==1)
            this.setTitle("Add a week of work");
        else {
            this.setTitle("Change " + String.valueOf(workweekParcelable.getWeek_ID()) + " week of work");

            ((CalendarView) findViewById(R.id.week_start)).setDate(workweekParcelable.getWeek_start_long());
            ((EditText) findViewById(R.id.year_of_training)).setText(String.valueOf(workweekParcelable.getYear_of_training()));
            ((EditText) findViewById(R.id.comment)).setText(workweekParcelable.getComment());
        }

        final Button button = (Button) findViewById(R.id.save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                //send the the Object EXTRA_MESSAGE_workweekParcelable to the MainActivity
                workweekParcelable.setWeek_start_long(
                        ((CalendarView) findViewById(R.id.week_start)).getDate());
                if (((EditText) findViewById(R.id.year_of_training)).getText().toString().length() != 0) {
                    workweekParcelable.setYear_of_training(
                            Integer.valueOf(((EditText) findViewById(R.id.year_of_training)).getText().toString()));
                    workweekParcelable.setComment(
                            ((EditText) findViewById(R.id.comment)).getText().toString());
                    intent.putExtra(EXTRA_MESSAGE_workweekParcelable, workweekParcelable);
                    setResult(RESULT_OK, intent);
                    finish();
                } else
                    ((EditText) findViewById(R.id.year_of_training)).setError( "" );
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_workweek, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
