package com.ericye16.android.kynematics;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class Engine implements SensorEventListener {
	private boolean isCollecting = false;
	
	public Engine() {
		//I suppose something should happen here
	}
	
	public boolean isCollecting() {
		return isCollecting;
	}
	
	public void startOrStop() {
		if (isCollecting) {
			stop();
		} else {
			start();
		}
	}
	
	private void start() {
		
	}
	
	private void stop() {
		
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		
	}

	@Override
	public void onSensorChanged(SensorEvent arg0) {
		
	}
}
