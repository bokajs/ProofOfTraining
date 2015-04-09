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

import java.util.Date;


public class changeWorkweekActivity extends ActionBarActivity {

    //EXTRA_MESSAGE of the changeWorkweekActivity
    public static String EXTRA_MESSAGE_week_ID ="com.example.proofoftraining.week.id";
    public static String EXTRA_MESSAGE_week_start ="com.example.proofoftraining.week.start";
    public static String EXTRA_MESSAGE_week_end ="com.example.proofoftraining.week.end";
    public static String EXTRA_MESSAGE_year_of_training_training ="com.example.proofoftraining.week.year";
    public static String EXTRA_MESSAGE_comment ="com.example.proofoftraining.week.comment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_workweek);

        //take the EXTRA_MESSAGE from the MainActivity, the workweek attributes
        Intent intent = getIntent();
        ((EditText) findViewById(R.id.week_ID)).setText(intent.getStringExtra(MainActivity.EXTRA_MESSAGE_week_ID));

        if (Integer.parseInt(intent.getStringExtra(MainActivity.EXTRA_MESSAGE_week_new))==1)
            this.setTitle("Add a week of work");
        else {
            this.setTitle("Change " + intent.getStringExtra(MainActivity.EXTRA_MESSAGE_week_ID) + " week of work");

            ((CalendarView) findViewById(R.id.week_start)).setDate(Long.getLong(intent.getStringExtra(MainActivity.EXTRA_MESSAGE_week_start)));
            //((EditText) findViewById(R.id.week_start)).setText(intent.getStringExtra(MainActivity.EXTRA_MESSAGE_week_start));
                //((EditText) findViewById(R.id.week_end)).setText(intent.getStringExtra(MainActivity.EXTRA_MESSAGE_week_end));
            ((EditText) findViewById(R.id.year_of_training)).setText(intent.getStringExtra(MainActivity.EXTRA_MESSAGE_year_of_training_training));
            ((EditText) findViewById(R.id.comment)).setText(intent.getStringExtra(MainActivity.EXTRA_MESSAGE_comment));
        }

        final Button button = (Button) findViewById(R.id.save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                //send the EXTRA_MESSAGE to the MainActivity, the workweek attributes
                intent.putExtra(EXTRA_MESSAGE_week_ID, ((EditText) findViewById(R.id.week_ID)).getText().toString());
                intent.putExtra(EXTRA_MESSAGE_week_start, Long.toString(((CalendarView) findViewById(R.id.week_start)).getDate()));
                    //intent.putExtra(EXTRA_MESSAGE_week_end, ((EditText) findViewById(R.id.week_end)).getText().toString());
                intent.putExtra(EXTRA_MESSAGE_year_of_training_training, ((EditText) findViewById(R.id.year_of_training)).getText().toString());
                intent.putExtra(EXTRA_MESSAGE_comment, ((EditText) findViewById(R.id.comment)).getText().toString());
                setResult(RESULT_OK, intent);
                finish();
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
