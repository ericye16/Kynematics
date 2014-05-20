package com.ericye16.android.kynematics;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView accel_status;
	TextView vel_status;
	TextView rot_status;
	TextView pos_status;
	Button reset_button;
	private Kynematics kynematics;
	private Timer uiTimer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		accel_status = (TextView) findViewById(R.id.accel_status);
		vel_status = (TextView) findViewById(R.id.vel_status);
		rot_status = (TextView) findViewById(R.id.rotation_status);
		pos_status = (TextView) findViewById(R.id.position_status);
		reset_button = (Button) findViewById(R.id.reset_button);
		kynematics = new Kynematics(this);
		
		//this has got to be the worst way of doing things.
		//yo dawg, I heard you like threads.
		uiTimer = new Timer();
		uiTimer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						updateUI();
					}
					
				});
			}
			
		}, 0, 500);
		
	}
	
	private void updateUI() {
		updatePos();
		updateVel();
		updateAccel();
		updateRot();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void startOrStop(View view) {
		TextView status = (TextView)findViewById(R.id.status);
		Button button = (Button) findViewById(R.id.button);
		if (kynematics.isRunning()) {
			kynematics.stop();
			status.setText(R.string.not_running);
			button.setText(R.string.start_button);
			reset_button.setEnabled(true);
		} else {
			kynematics.start();
			status.setText(R.string.running);
			button.setText(R.string.stop_button);
			reset_button.setEnabled(false);
		}
	}
	
	public void reset(View view) {
		kynematics.reset();
		updateUI();
	}
	
	public void updateAccel() {
		float[] accel = kynematics.getLastAcceleration();
		if (accel.length != 3) {
			Log.e("MainActivity.updateAccel", "accel not correct length");
			return;
		}
		accel_status.setText("Accel: (" + accel[0] + "," + accel[1] + "," + 
				accel[2] + ")");
	}
	
	public void updateRot() {
		float[] rot = kynematics.getRotationMatrix();
		if (rot.length != 16) {
			Log.e("MainActivity.updateRot", "rot not correct length");
			return;
		}
		rot_status.setText("Rot:\n" + 
				rot[0] + "," + rot[1] + "," + rot[2] + "," + rot[3] + "\n" + 
				rot[4] + "," + rot[5] + "," + rot[6] + "," + rot[7] + "\n" + 
				rot[8] + "," + rot[9] + "," + rot[10] + "," + rot[11] + "\n" +
				rot[12] + "," + rot[13] + "," + rot[14] + "," + rot[15]);
	}
	
	public void updatePos() {
		float[] pos = kynematics.getPosition();
		if (pos.length != 3) {
			Log.e("MainActivity.updatePos", "pos not correct length");
			return;
		}
		pos_status.setText("Pos: (" + pos[0] + "," + pos[1] + "," + pos[2] +
				")");
	}
	
	public void updateVel() {
		float[] vel = kynematics.getVelocity();
		vel_status.setText("Vel: (" + vel[0] + "," + vel[1] + "," + vel[2] +
				")");
	}

}
