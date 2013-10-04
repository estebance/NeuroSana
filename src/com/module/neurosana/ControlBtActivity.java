package com.module.neurosana;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ControlBtActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control_bt);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.control_bt, menu);
		return true;
	}

}
