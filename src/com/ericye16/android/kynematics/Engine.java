package com.ericye16.android.kynematics;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.text.format.Time;

public class Engine implements SensorEventListener {
	private boolean isCollecting = false;
	private final Activity activity;
	private SensorManager sensorManager;
	private Sensor accel;
	private BufferedOutputStream dataStream;
	
	public Engine(Activity activity) {
		this.activity = activity;
		sensorManager = (SensorManager) activity.getSystemService(Activity.SENSOR_SERVICE);
		accel = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
	}
	
	public boolean isCollecting() {
		return isCollecting;
	}
	
	public void startOrStop() throws IOException {
		if (isCollecting) {
			stop();
		} else {
			start();			
		}
		isCollecting = !isCollecting;
	}
	
	private void start() throws IOException {
		if (!isExternalStorageWritable()) throw new IOException();
		Time time = new Time();
		time.setToNow();
		String timeString = time.toString();
		File dataFile = new File(Environment.getExternalStorageDirectory(), timeString + ".csv");
		dataStream = new BufferedOutputStream(new FileOutputStream(dataFile));
		sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	private void stop() {
		sensorManager.unregisterListener(this);
		try {
			dataStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* Checks if external storage is available for read and write */
	private boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int arg1) {
		
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		switch (sensorEvent.sensor.getType()) {
		case Sensor.TYPE_LINEAR_ACCELERATION:
			break;
		default:
			break;
		}
	}
}
