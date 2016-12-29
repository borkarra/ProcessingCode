package com.example.ra.processingcode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.Math;

public class MainActivity extends AppCompatActivity {

    EditText accelXTextView;
    EditText accelYTextView;
    EditText accelZTextView;
    TextView distTextView;
    TextView curSpeedTextView;
    TextView avgSpeedTextView;
    TextView avgSpeedUnitTextView;
    TextView curSpeedUnitTextView;
    TextView distUnitTextView;
    boolean empirical = false;
    float curSpeedX = 0;
    float curSpeedY = 0;
    float curSpeedZ = 0;
    float curSpeed = 0;
    float avgSpeed = 0;
    float distTraveled = 0;
    int updates = 1; // number of times data has been updated, used for averaging.
    float step = 1; // time between each measurement in seconds
    float kmToMi = (float) 0.621371;
    float msTokmh = (float) 3.6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        accelXTextView = (EditText) findViewById(R.id.accelXTextView);
        accelYTextView = (EditText) findViewById(R.id.accelYTextView);
        accelZTextView = (EditText) findViewById(R.id.accelZTextView);
        curSpeedTextView = (TextView) findViewById(R.id.curSpeedTextView);
        avgSpeedTextView = (TextView) findViewById(R.id.avgSpeedTextView);
        distTextView = (TextView) findViewById(R.id.distTextView);
        curSpeedUnitTextView = (TextView) findViewById(R.id.curSpeedUnitTextView);
        avgSpeedUnitTextView = (TextView) findViewById(R.id.avgSpeedUnitTextView);
        distUnitTextView = (TextView) findViewById(R.id.distUnitTextView);

        Button distBtn = (Button) findViewById(R.id.distBtn);
        distBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                float accelerationX = Float.parseFloat(accelXTextView.getText().toString());
                float accelerationY = Float.parseFloat(accelYTextView.getText().toString());
                float accelerationZ = Float.parseFloat(accelZTextView.getText().toString());
                curSpeedX += accelerationX*step; //in m/s
                curSpeedY += accelerationY*step;
                curSpeedZ += accelerationZ*step;
                float oldSpeed = curSpeed;
                curSpeed = (float) Math.sqrt(Math.pow(curSpeedX,2) + Math.pow(curSpeedY, 2) + Math.pow(curSpeedZ, 2))*msTokmh; // in kmh
                distTraveled += (((oldSpeed + curSpeed)/2)*step)/3600; //speed in kmh, step in sec so convert (WILL CHANGE)
                avgSpeed = (avgSpeed*updates+curSpeed)/(updates+1);
                if (!empirical) {
                    curSpeedTextView.setText(Float.toString(curSpeed));
                    avgSpeedTextView.setText(Float.toString(avgSpeed));
                    distTextView.setText(Float.toString(distTraveled));
                } else {
                    curSpeedTextView.setText(Float.toString(curSpeed*kmToMi));
                    avgSpeedTextView.setText(Float.toString(avgSpeed*kmToMi));
                    distTextView.setText(Float.toString(distTraveled*kmToMi));
                }
                updates += 1;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.units) {
            item.setChecked(!item.isChecked());
            if (item.isChecked()) {
                curSpeedUnitTextView.setText("mi/hr");
                curSpeedTextView.setText(Float.toString(curSpeed*kmToMi));
                avgSpeedUnitTextView.setText("mi/hr");
                avgSpeedTextView.setText(Float.toString(avgSpeed*kmToMi));
                distTextView.setText(Float.toString(distTraveled*kmToMi));
                distUnitTextView.setText("mi");
                empirical = true;
            } else {
                curSpeedUnitTextView.setText("km/hr");
                curSpeedTextView.setText(Float.toString(curSpeed));
                avgSpeedUnitTextView.setText("km/hr");
                avgSpeedTextView.setText(Float.toString(avgSpeed));
                distTextView.setText(Float.toString(distTraveled));
                distUnitTextView.setText("km");
                empirical = false;
            }
            return true;
        }
        if (id == R.id.reset) {
            curSpeed = 0;
            //distTextView.setText(Float.toString(curDist));
        }
        return super.onOptionsItemSelected(item);
    }
}
