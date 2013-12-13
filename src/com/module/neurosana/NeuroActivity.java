package com.module.neurosana;

import com.utilities.neurosana.ManageFiles;
import com.utilities.neurosana.ReadEdf;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NeuroActivity extends Activity 
{
	
	/*Verificar SD  y Directorio  y obtencion datos*/
	ManageFiles verify_data; 
	private String result_file_uri = "";
	private Uri uri;
	
	/*mostrar el toast  */
	Context context;
	Resources res;
	CharSequence text_show = "";
	int duration_toast = Toast.LENGTH_LONG;
	
	/* esta variable identifica la llamada  que se hace a startactivityforresult*/
	private final static int CALLID = 0;
	
	/*caracteristicas de la vista*/
	TextView file_directory_view;
	Button button_view_eeg;
	
	/*adaptador para bluetooth saber si esta encendido*/
	BluetoothAdapter connector; 
	private final static int REQUEST_ENABLE_BT = 1;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_neuro);
		
		/*traer boton view*/
		
		button_view_eeg = (Button)findViewById(R.id.button3neuro_activity);
		button_view_eeg.setEnabled(false);
		
		/*Adapter para encender bluetooth*/
		connector =  BluetoothAdapter.getDefaultAdapter();
		
		/*mostrar el toast  */
		context = getApplicationContext();
		res = getResources();
	
		/* creamos un objeto de la clase Managefiles para verificar sd y directorio*/
	    verify_data = new ManageFiles();
	    
	   /* Capturamos el textview que nos muestra la ruta del archivo cargado*/
	    
	    file_directory_view = (TextView)findViewById(R.id.textView2neuro_activity);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
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
	        	uri = Uri.parse(result_file_uri);
	        	/*Imprimimos  el file uri en el textview */
	            file_directory_view.setText(result_file_uri);
	            /*activamos el boton de visualizar edf*/
	            button_view_eeg.setEnabled(true);
	            
	        	
	        }
	    }
	    
	   if(requestCode == REQUEST_ENABLE_BT)
	   {
		   
	        if (resultCode == RESULT_OK)
	        {
	    		Intent listdevices = new Intent(this, BtListActivity.class);
	        	startActivity(listdevices);	
	        }   
	        
	        else
	        {
	        
	   		 text_show = res.getString(R.string.bluetooth_turn_off);
			 Toast toast = Toast.makeText(context, text_show, duration_toast);
			 toast.show();	
	        	
	        }
		   
	    	
	   }
	}
	
	
	
	public void search_file (View v)
	{	
	
	boolean sdcard_verify = verify_data.verify_sdcard();
	boolean directory_verify = verify_data.verify_directory();

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
	
	public void capture_data(View v)
	{
		boolean bluetooth = verify_bluetooth();
		if(bluetooth)
		{
			
		 boolean turn_on_bluetooth = turnonbluetooth();
		 if(turn_on_bluetooth)
		 {
		 Intent listdevices = new Intent(this, BtListActivity.class);
		 startActivityForResult(listdevices , CALLID);
		 }
			
		}

	}
	
	public void view_eeg(View v)
	{
	Intent view_file = new Intent(this, ChartActivity.class);
	view_file.putExtra("data_direction", result_file_uri);
	startActivity(view_file);		
	}
	
	
	public boolean verify_bluetooth()
	{
	boolean verify = false; 
	if (connector == null) 
	{
	text_show = res.getString(R.string.bluetooth_availability);
	Toast toast = Toast.makeText(context, text_show, duration_toast);
	toast.show();
	}		
	else
	{
	verify = true; 	
	}
		
	return verify;	
	}
	
	
	public boolean turnonbluetooth()
	{
	boolean verify = false; 	

	/*si el conector no esta habilitado?*/
	if (!connector.isEnabled()) 
	{		 
	  Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	  startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
	}
	else
	{
	 verify = true; 	
	}
	
	return verify;
	}
	
	
	@Override
	public void onBackPressed() 
	{
	  Intent data = new Intent();
	  setResult(RESULT_OK, data);
	  data.setDataAndType(uri, "text/plain");
	  //data.putExtra("uri", result_file_uri);
	  finish();
	}	

}
