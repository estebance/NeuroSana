package com.module.neurosana;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListFilesActivity extends Activity 
{

   ListView file_list;	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_files);
	    /* capturar la list view para cargar los archivos */
		ArrayAdapter<String> adapter_list_file = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1); 
		file_list =(ListView)findViewById(R.id.listView1list_files_activity);
		file_list.setAdapter(adapter_list_file);
		/*listener que capturara cuando se pulse sobre un archivo*/
		  file_list.setOnItemClickListener(new OnItemClickListener()
		  {
            @Override
			public void onItemClick(AdapterView<?> parent, View view , int position , long id) 
            {
			
				
			}
           });
		
		
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_files, menu);
		return true;
	}



}
