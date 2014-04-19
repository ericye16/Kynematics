package com.ericye16.android.kynematics;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

class KynRunner implements SensorEventListener {
	
	private Context context;
	final private float[] position;
	final private float[] R;
	final private float[] velocity;
	final private Sensor linearAccelerationSensor;
	final private Sensor rotationVectorSensor;
	private long prev_timestamp = -1;
	
	public KynRunner(Context context, float[] position, float[] velocity, float[] rotationMatrix,
			Sensor linearAccelerationSensor, Sensor rotationSensor) {
		this.context = context;
		this.position = position;
		this.velocity = velocity;
		this.R = rotationMatrix;
		this.linearAccelerationSensor = linearAccelerationSensor;
		this.rotationVectorSensor = rotationSensor;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		Log.d("KynRunner.onAccuracyChanged", "Accuracy on sensor " + sensor.toString() +
				" changed to: " + accuracy + "\n.");
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		Log.d("KynRunner.onSensorChanged", "Event on sensor " + sensorEvent.sensor.toString() + "\n");
		if (sensorEvent.sensor == linearAccelerationSensor) {
			if (prev_timestamp == -1 || (sensorEvent.timestamp - prev_timestamp > 200000684)) { //first run [in a while], ignore sensor and just update timestamp
				prev_timestamp = sensorEvent.timestamp;
			} else {
				long deltaT = sensorEvent.timestamp - prev_timestamp;
				prev_timestamp = sensorEvent.timestamp;
				float[] a = sensorEvent.values;
				velocity[0] = velocity[0] + deltaT * (R[0] * a[0] + R[1] * a[1] + 
						R[2] * a[2]);
				velocity[1] = velocity[1] + deltaT * (R[4] * a[0] + R[5] * a[1] +
						R[6] * a[2]);
				velocity[2] = velocity[2] + deltaT * (R[8] * a[0] + R[9] * a[1] + 
						R[10] * a[2]);
				for (int i = 0; i < 3; i++) {
					position[i] = position[i] + velocity[i] * deltaT;
				}
			}
		} else if (sensorEvent.sensor == rotationVectorSensor) {
			float[] rotationVector = sensorEvent.values;
			SensorManager.getRotationMatrixFromVector(R, rotationVector);
		} else {
			throw new RuntimeException("Sensor should be either rotation sensor or linear sensor.");
		}
	}

}
