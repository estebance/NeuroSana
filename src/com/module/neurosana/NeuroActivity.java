package com.module.neurosana;

import com.utilities.neurosana.ManageFiles;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class NeuroActivity extends Activity 
{
	ManageFiles verify_data; 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_neuro);
	
		/* creamos un objeto de la clase Managefiles*/
	    verify_data = new ManageFiles();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.neuro, menu);
		return true;
	}
	
	public void search_file (View v)
	{	
		
	boolean sdcard_verify = verify_data.verify_sdcard();
	boolean directory_verify = verify_data.verify_directory();
	
	if (sdcard_verify == true)
	{
		if(directory_verify == true)
		{
			//Intent send_to_list_files = new Intent(this,ListFilesActivity.class);
		    //startActivity(send_to_list_files);
			
			System.out.println("Has capturado un  archivo");	
					
		}
		else
		{
		 System.out.println("Aun no has capturado ningun archivo");	
		}
	}	
	
	else
	{
	System.out.println("no tienes tarjeta sd para almacenar el archivo");	
	} 
    
    }

	
	
}
