package com.android.stepper;

import android.app.Activity;
import com.google.android.gms.location.DetectedActivity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {

	EditText etMsg;
	private SensorManager mSensorManager;
	private Sensor mStepCounterSensor;
	int step=0;
    DetectedActivity detectedActivity;

    private Sensor mStepDetectorSensor;
	
	/*
	 * Called when the Activity becomes visible.
	 */
	@Override
	protected void onStart() {
		super.onStart();
	}

	protected void onResume() {

	     super.onResume();

	     mSensorManager.registerListener(this, mStepCounterSensor,

	           SensorManager.SENSOR_DELAY_FASTEST);      
	     mSensorManager.registerListener(this, mStepDetectorSensor,

	           SensorManager.SENSOR_DELAY_FASTEST);

	 }

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
				Toast.makeText(getApplicationContext(), "Steps:"+step+ " type:" + detectedActivity.getType(),Toast.LENGTH_LONG).show();
				
				step=0;
				
			}
		});

//		Button btGear = (Button) findViewById(R.id.buttonGear);
//		btGear.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				//sendNotification("gear gear");
//				etMsg.setText("Clicou no fone:");
//
//				//Toast.makeText(getApplicationContext(), "Clicou no fone:"+phoneNumber,Toast.LENGTH_LONG).show();
//			}
//		});
		
	}

	 public void onSensorChanged(SensorEvent event) {
	     Sensor sensor = event.sensor;
	     float[] values = event.values;
	     int value = -1;
	    
	     if (values.length > 0) {
	        value = (int) values[0];
	     }
	     //etMsg.setText("" + step++);
	     if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
	    	  etMsg.setText("Step Counter : " + value+" - step: "+step);
	     } else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
	         // For test only. Only allowed value is 1.0 i.e. for step taken
	    	 etMsg.setText("Steps: " + step++);
	    	 
	    	 Log.i("StepCount","Step:"+step+"");
	     }
	 }
	//https://developer.android.com/reference/com/google/android/gms/location/DetectedActivity.html


	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
