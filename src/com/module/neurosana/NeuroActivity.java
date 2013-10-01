package com.module.neurosana;

import com.utilities.neurosana.ManageFiles;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class NeuroActivity extends Activity 
{
	ManageFiles verify_data; 
	private String result_file_uri = "";
	
	
	/* esta variable identifica la llamada  que se hace a startactivityforresult*/
	private final static int CALLID = 0;
	
	TextView file_directory_view;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_neuro);
	
		/* creamos un objeto de la clase Managefiles*/
	    verify_data = new ManageFiles();
	    
	   /* Capturamos el textview que nos muestra la ruta del archivo cargado*/
	    
	    file_directory_view = (TextView)findViewById(R.id.textView2neuro_activity);
	   // file_directory_view.setText(result_file_uri);
	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.neuro, menu);
		return true;
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == CALLID)
	    {
	        if (resultCode == RESULT_OK)
	        {
	        	result_file_uri = data.getExtras().getString("fileuri");
	        	/*Imprimimos  el file uri en el textview */
	            file_directory_view.setText(result_file_uri);
	        	
	        }
	    }
	}
	
	
	
	public void search_file (View v)
	{	
	int duration_toast = Toast.LENGTH_LONG;
	CharSequence text_show = "";
	boolean sdcard_verify = verify_data.verify_sdcard();
	boolean directory_verify = verify_data.verify_directory();
	Resources res = getResources();
	/* Verificamos el contexto para mostrar el toast  */
	 Context context = getApplicationContext();
	/* condicionales para realizar el intent a donde son listados los archivos del directorio */	 
	if (sdcard_verify)
	{
		if(directory_verify)
		{
			/* startActivity for result para que nos retorne la uri del archivo a cargar */
			Intent listfiles = new Intent(this, ListFilesActivity.class);
	    	startActivityForResult(listfiles , CALLID);			
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
