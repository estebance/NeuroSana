package com.module.neurosana;

import com.utilities.neurosana.ManageFiles;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

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
	int duration_toast = Toast.LENGTH_LONG;
	CharSequence text_show = "";
	boolean sdcard_verify = verify_data.verify_sdcard();
	boolean directory_verify = verify_data.verify_directory();
	Resources res = getResources();
	
	/*
	 * Verificamos el contexto para mostrar el toast  
	 */
	
	 Context context = getApplicationContext();
	 

	 System.out.println(sdcard_verify);
	 System.out.println(directory_verify);
	 
	
	
	/*
	 * condicionales para realizar el intent a donde son listados los archivos del directorio 
	 */
	 
	if (sdcard_verify)
	{
		if(directory_verify)
		{
			 Intent send_to_list_files = new Intent(this,ListFilesActivity.class);
		     startActivity(send_to_list_files);	
			 text_show = "esta funcionando";
			 Toast toast = Toast.makeText(context, text_show, duration_toast);
			 toast.show();
					
		}
		else
		{
		 text_show = res.getString(R.string.folder_availability);
		 Toast toast = Toast.makeText(context, text_show, duration_toast);
		 toast.show();
		}
	}	
	
	else
	{
	text_show = res.getString(R.string.sdcard_availability);
	Toast toast = Toast.makeText(context, text_show, duration_toast);
	toast.show();
	} 
    
    }

	
	
}
