package com.ericye16.android.kynematics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.util.Log;

public class Engine implements SensorEventListener {
	private boolean isCollecting = false;
	private final Activity activity;
	private SensorManager sensorManager;
	private Sensor allSensors;
	private BufferedWriter[] dataWriters;
	final static int ACCEL_FILE = 0;
	final static int GYRO_FILE = 1;
	
	public Engine(Activity activity) {
		this.activity = activity;
		sensorManager = (SensorManager) activity.getSystemService(Activity.SENSOR_SERVICE);
		allSensors = sensorManager.getDefaultSensor(Sensor.TYPE_ALL);
		dataWriters = new BufferedWriter[2];
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
		Log.i("Engine.start", "Starting collection");
		if (!isExternalStorageWritable()) throw new IOException();
		GregorianCalendar time = new GregorianCalendar();
		String timeString = "" + time.get(Calendar.YEAR) + time.get(Calendar.MONTH) +
				time.get(Calendar.DATE) + time.get(Calendar.HOUR_OF_DAY) + 
				time.get(Calendar.MINUTE) + time.get(Calendar.SECOND);
		System.out.println(timeString);
		File rootDir = new File(Environment.getExternalStorageDirectory(), timeString);
		rootDir.mkdir();
		dataWriters[ACCEL_FILE] = new BufferedWriter(new FileWriter(new File(rootDir, "accel.csv")));
		dataWriters[GYRO_FILE] = new BufferedWriter(new FileWriter(new File(rootDir, "gyro.csv")));
		sensorManager.registerListener(this, allSensors, SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	private void stop() {
		Log.i("Engine.stop", "Stopping collection");
		sensorManager.unregisterListener(this);
		try {
			for (BufferedWriter writer: dataWriters) {
				writer.close();
			}
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
		Log.d("Engine.onSensorChanged", "sensor value changed");
		switch (sensorEvent.sensor.getType()) {
		case Sensor.TYPE_LINEAR_ACCELERATION:
			writeAccelData(sensorEvent);
			Log.d("Engine.onSensorChanged", "Accel reading");
			break;
		case Sensor.TYPE_ROTATION_VECTOR:
			writeRotationData(sensorEvent);
			Log.d("Engine.onSensorChanged", "Rotation reading");
			break;
		default:
			Log.d("Engine.onSensorChanged", "Not sensor we want");
			break;
		}
	}
	
	private void writeAccelData(SensorEvent sensorEvent) {
		try {
			dataWriters[ACCEL_FILE].write(sensorEvent.timestamp + "," + 
					sensorEvent.values[0] + "," + sensorEvent.values[1] + "," +
					sensorEvent.values[2] + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void writeRotationData(SensorEvent sensorEvent) {
		try {
			dataWriters[GYRO_FILE].write(sensorEvent.timestamp + "," +
					sensorEvent.values[0] + "," + sensorEvent.values[1] + "," + 
					sensorEvent.values[2] + "," + sensorEvent.values[3] + "," +
					sensorEvent.values[3] + "," + sensorEvent.values[4] + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
