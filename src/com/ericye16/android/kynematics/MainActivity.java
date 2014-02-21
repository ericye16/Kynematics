package com.ericye16.android.kynematics;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	Engine engine;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		engine = new Engine(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void startOrStop(View view) {
		try {
			engine.startOrStop();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		TextView status = (TextView)findViewById(R.id.status);
		Button button = (Button) findViewById(R.id.button);
		if (engine.isCollecting()) {
			status.setText(R.string.running);
			button.setText(R.string.stop_button);
		} else {
			status.setText(R.string.not_running);
			button.setText(R.string.start_button);
		}
	}

}
