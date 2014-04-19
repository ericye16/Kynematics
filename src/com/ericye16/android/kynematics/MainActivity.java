package com.ericye16.android.kynematics;

import java.io.IOException;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		accel_status = (TextView) findViewById(R.id.accel_status);
		rot_status = (TextView) findViewById(R.id.rotation_status);
		pos_status = (TextView) findViewById(R.id.position_status);
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
	
	public void updateRot(float[] rot) {
		if (rot.length != 5) {
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
