package com.ericye16.android.kynematics;

import java.io.IOException;
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
	TextView rot_status;
	TextView pos_status;
	private Kynematics kynematics;
	private Timer uiTimer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		accel_status = (TextView) findViewById(R.id.accel_status);
		rot_status = (TextView) findViewById(R.id.rotation_status);
		pos_status = (TextView) findViewById(R.id.position_status);
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
						Log.d("uiTimer", "ui updated");
						if (kynematics.isRunning()) {
							updatePos(kynematics.getPosition());
							updateAccel(kynematics.getLastAcceleration());
							updateRot(kynematics.getRotationMatrix());
						}
					}
					
				});
			}
			
		}, 0, 500);
		
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
		} else {
			kynematics.start();
			status.setText(R.string.running);
			button.setText(R.string.stop_button);
		}
	}
	
	public void updateAccel(float[] accel) {
		if (accel.length != 3) {
			Log.e("MainActivity.updateAccel", "accel not correct length");
			return;
		}
		Log.d("MainActivity.updateAccel", "updating");
		accel_status.setText("Accel: (" + accel[0] + "," + accel[1] + "," + 
				accel[2] + ")");
	}
	
	//current not working since I don't know how to properly display a rotation matrix
	public void updateRot(float[] rot) {
		if (rot.length != 16) {
			Log.e("MainActivity.updateRot", "rot not correct length");
			return;
		}
		rot_status.setText("Rot: (" + rot[0] + "," + rot[1] + "," + rot[2] + 
				"," + rot[3] + "," + rot[4] + ")");
	}
	
	public void updatePos(float[] pos) {
		if (pos.length != 3) {
			Log.e("MainActivity.updatePos", "pos not correct length");
			return;
		}
		pos_status.setText("Pos: (" + pos[0] + "," + pos[1] + "," + pos[2] +
				")");
	}

}
