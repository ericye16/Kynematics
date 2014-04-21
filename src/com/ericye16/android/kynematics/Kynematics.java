package com.ericye16.android.kynematics;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * It's so much easier to define an API and work towards it than the other way
 * around.
 * 
 * Kynematics aspires to be an open-source inertial navigation system  (INS)
 * implemented using gyroscopes, magnetic field sensors, accelerometers,
 * and possibly some other input such as cameras.
 * 
 * This file contains a (hopefully) nicely-wrapped API to determining the
 * phone's position and rotation, to be used by other apps.
 * 
 * @author ericye16 <me@ericye16.com>
 * @version 0.1
 *
 */
public class Kynematics {
	final private Context context;
	private boolean isRunning = false;
	final private float[] position = new float[] {0, 0, 0};
	final private float[] velocity = new float[] {0, 0, 0};
	final private float[] lastAcceleration = new float[] {0, 0, 0};
	final private float[] rotationMatrix = new float[] 
			{0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0
			}; //length = 4 x 4 = 16
	final private SensorManager sensorManager;
	final private Sensor rotationVectorSensor;
	final private Sensor linearAccelerationSensor;
	private KynRunner kynRunner;
	
	/**
	 * Constructor for the Kynematics class. Note that it does not start
	 * running until <code>start</code> is called.
	 * @param context
	 */
	public Kynematics(Context context) {
		this.context = context;
		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		kynRunner = new KynRunner(context, position, velocity, lastAcceleration, 
				rotationMatrix, linearAccelerationSensor, rotationVectorSensor);
		reset();
	}
	
	/**
	 * Checks if Kynematics is running. Must be true for most library functions.
	 * @return if it is running.
	 */
	public boolean isRunning() {
		return isRunning;
	}
	
	/**
	 * Resets the rotation and position state variables.
	 * <p>
	 * Initializes the necessary variables and states, etc.
	 * This must be called only when the phone is not moving in the desired
	 * inertial frame in order to avoid incorrect initial conditions.
	 * <p>
	 * Cannot be called while running.
	 * @throws IllegalStateException if called while running.
	 */
	public void reset() {
		if (isRunning)
			throw new IllegalStateException("Cannot be reset while running.");
		//reset position and velocity
		for (int i = 0; i < 3; i++) {
			position[i] = 0;
			velocity[i] = 0;
		}
		//zero out rotation matrix
		for (int i = 0; i < 16; i++) {
			rotationMatrix[i] = 0;
		}
	}
	
	/**
	 * Starts Kynematics.
	 * @throws IllegalStateException if it is already running.
	 */
	public void start() {
		if (isRunning)
			throw new IllegalStateException("Kynematics cannot be started because it is already running.");
		
		sensorManager.registerListener(kynRunner, linearAccelerationSensor, SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(kynRunner, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
		isRunning = true;
	}
	
	/**
	 * Stops Kynematics. Note that this must be done before the object
	 * is garbage-collected in order to avoid running the sensors too long
	 * and using too much power.
	 * @throws IllegalStateException if it is not running.
	 */
	public void stop() {
		if (!isRunning)
			throw new IllegalStateException("Kynematics cannot be stopped because it is not running.");
		
		sensorManager.unregisterListener(kynRunner, linearAccelerationSensor);
		sensorManager.unregisterListener(kynRunner, rotationVectorSensor);
		isRunning = false;
	}
	
	/**
	 * Gets the current position of the phone. Contains 3 elements, which
	 * correspond to the x-, y- and z-axes respectively.
	 * @return
	 */
	public float[] getPosition() {
		return position;
	}
	
	/**
	 * Gets the current velocity of the phone. Contains 3 elements, which correspond
	 * to the x-, y- and z-axes respectively.
	 * @return
	 */
	public float[] getVelocity() {
		return velocity;
	}
	
	/**
	 * Gets the last set of acceleration values. Contains 3 elements, which correspond
	 * to the x-, y- and z-axes respectively.
	 * @return
	 */
	public float[] getLastAcceleration() {
		return lastAcceleration;
	}
	
	/**
	 * Gets the current rotation matrix of the phone, according to the phone's
	 * rotation vector sensor. A 4-by-4 matrix contained in a 16-element array,
	 * with elements laid out according to 
	 * {@link android.hardware.SensorManager#getRotationMatrix(float[], float[], float[], float[]) getRotationMatrix}.
	 * @return
	 */
	public float[] getRotationMatrix() {
		return rotationMatrix;
	}
	
	/**
	 * Sets the position of the phone. Since this is not thread-safe,
	 * it is highly recommended that Kynematics not be running while this is called.
	 * This is reset on
	 * 
	 * @param position the position vector to use. Must have 3 elements,
	 * representing x,y,z respectively.
	 */
	public void setPosition(float[] position) {
		if (position.length != 3)
			throw new IllegalArgumentException("Input vector when inputting must have 3 elements.");
		for (int i = 0; i < 3; i++) {
			this.position[i] = position[i];
		}
	}
	
}
