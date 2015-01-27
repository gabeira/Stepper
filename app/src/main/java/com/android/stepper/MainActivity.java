package com.android.stepper;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.location.DetectedActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends ActionBarActivity implements SensorEventListener {

    EditText etMsg;
    private SensorManager mSensorManager;
    private Sensor mStepCounterSensor;
    int step = 0;
    DetectedActivity detectedActivity;

    private Sensor mStepDetectorSensor;

    /*
     * Called when the Activity becomes visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {

        super.onResume();

        mSensorManager.registerListener(this, mStepCounterSensor,

                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mStepDetectorSensor,

                SensorManager.SENSOR_DELAY_FASTEST);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this, mStepCounterSensor);
        mSensorManager.unregisterListener(this, mStepDetectorSensor);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etMsg = (EditText) findViewById(R.id.editTextMsg);


        mSensorManager = (SensorManager) getSystemService(getApplicationContext().SENSOR_SERVICE);

        mStepCounterSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mStepDetectorSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);


        Button btConta = (Button) findViewById(R.id.buttonSms);
        btConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Steps:" + step + " type:" + detectedActivity.getType(), Toast.LENGTH_LONG).show();

                step = 0;

            }
        });

		Button btGear = (Button) findViewById(R.id.buttonGear);
		btGear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Criou evento no calendar",Toast.LENGTH_LONG).show();
                createEvent();
			}
		});
        fitness();
    }

    public void fitness() {
        GoogleApiClient client = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(Fitness.API)
//        ...
                .build();

        client.connect();

//        PendingResult<Status> pendingResult = Fitness.SensorsApi.add(client,
//                new SensorRequest.Builder()
//                        .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
//                        .setSamplingDelay(1, TimeUnit.MINUTES)  // sample once per minute
//                        .build(),
//                myStepCountListener);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;
        int value = -1;

        if (values.length > 0) {
            value = (int) values[0];
        }
        //etMsg.setText("" + step++);
        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            etMsg.setText("Step Counter : " + value + " - step: " + step);
        } else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            // For test only. Only allowed value is 1.0 i.e. for step taken
            etMsg.setText("Steps: " + step++);

            Log.i("StepCount", "Step:" + step + "");
        }
    }
    //https://developer.android.com/reference/com/google/android/gms/location/DetectedActivity.html


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }


    private void createEvent(){
        Calendar beginTime = Calendar.getInstance();
        Calendar endTime = beginTime;
        endTime.set(Calendar.DAY_OF_MONTH, 22);

        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setType("vnd.android.cursor.item/event")
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false) // just included for completeness
                .putExtra(CalendarContract.Events.TITLE, "My Awesome Event")
                .putExtra(CalendarContract.Events.DESCRIPTION, "Heading out with friends to do something awesome.")
                .putExtra(CalendarContract.Events.EVENT_LOCATION, "Earth")
//                .putExtra(CalendarContract.Events.RRULE, "FREQ=DAILY;COUNT=10")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
//                .putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_DEFAULT)
//                .putExtra(Intent.EXTRA_EMAIL, "my.friend@example.com")
                ;
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
//            unregisterFitnessDataListener();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
