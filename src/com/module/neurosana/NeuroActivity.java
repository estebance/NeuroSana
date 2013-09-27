package com.module.neurosana;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class NeuroActivity extends Activity 
{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_neuro);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.neuro, menu);
		return true;
	}
	
	public void search_file (View v)
	{
    Intent send_to_list_files = new Intent(this,ListFilesActivity.class);
    startActivity(send_to_list_files);
	}

	
	
}
