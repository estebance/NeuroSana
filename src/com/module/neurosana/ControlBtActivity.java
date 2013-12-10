package com.module.neurosana;

import java.io.File;

import com.utilities.neurosana.ConnectorBtThread;
import com.utilities.neurosana.ManageFiles;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ControlBtActivity extends Activity 
{

String Bt_Dir;
String fileuri = null ;
private BluetoothAdapter BluetoothAdapterConn = null;
ConnectorBtThread connection = null ;

public static final int COMANDO_ENTRANTE = 1;
public static final int STATE_ENTRANTE = 2; 
public static final String COMAND = "comando";
public static final String STATE = "estado";

/*posibles estados de la conexion*/
public static final int NO_STATE = 0;
public static final int STATE_CONNECTING = 1; 
public static final int STATE_CONNECTED = 2;  
public static final int ERROR_CONNECTION = 3;
public static final int ERROR_DATA = 4;
public static final int BUSSY = 5; 
public static  final int CONNECTION_CLOSE= 6;
public static final int FREE = 7;
public static final int BUSSY_FILE = 8;
public static final int BUSSY_VERIFICAR = 9 ; 

/*posibles ordenes que lleguen del servidor*/

private static final int COMANDO_SALIR = 1;
private static final int COMANDO_ENVIAR = 2;
private static final int COMANDO_CANCELAR=3;
private static final int COMANDO_INICIAR=4;
private static final int COMANDO_TERMINADO=5;
private static final int COMANDO_CANCELADO=6;
private static final int COMANDO_INICIADO=7;
private static final int COMANDO_FINALIZADO=8;
private static final int COMANDO_FALLA = 10 ;
private static final int COMANDO_VERIFICAR = 11;
private static final int COMANDO_VERIFICADO = 12; 
private static final int COMANDO_ERROR = -1;
private static final int COMANDO_ERROR_VERIFICAR = -2 ;

/*Elementos de la vista*/

TextView state_connection_view;
Button capture , cancel , save , exit , verify ;

///

private int is_verify_sensor = 0 ; 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control_bt);
		
		/* capturamos los elementos de la vista*/
		
		state_connection_view = (TextView)findViewById(R.id.textView1bt_control_activity);
		capture = (Button)findViewById(R.id.button1bt_control_activity);
		cancel = (Button)findViewById(R.id.button2bt_control_activity);
		save = (Button)findViewById(R.id.button4bt_control_activity);
		exit = (Button)findViewById(R.id.button3bt_control_activity);
		verify = (Button)findViewById(R.id.button5bt_control_activity);
	
	    /* capturamos del intent la informacion concerniente a la direccion del dispositivo*/
	
		Bundle extra_info = getIntent().getExtras();
		Bt_Dir = extra_info.getString("btdir"); 		
		BluetoothAdapterConn = BluetoothAdapter.getDefaultAdapter();
		BluetoothDevice device_to_conn = BluetoothAdapterConn.getRemoteDevice(Bt_Dir);
		
		/* Capturamos la informacion del dispositivo BLUETOOTH al que nos vamos a conectar*/
		
		connection = new ConnectorBtThread(Handler_responses_server);
		connection.connect_client(device_to_conn);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.control_bt, menu);
		return true;
	}
	
    private final Handler Handler_responses_server = new Handler() 
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) 
            {
            case COMANDO_ENTRANTE:            	
               String llego_orden = msg.getData().getString(COMAND);
               command_method(Integer.parseInt(llego_orden));
            break;
            case STATE_ENTRANTE:
               String llego_estado = msg.getData().getString(STATE);
               state_method(Integer.parseInt(llego_estado));
            break;
           
            }
        }
    };
	
	/*se procede a realizar una captura de los datos*/
	public void command_capture(View v)
	{	
		if (connection.get_My_State() != ConnectorBtThread.STATE_CONNECTED) 
        {
          Toast.makeText(this, R.string.not_connected, Toast.LENGTH_LONG).show();
          return;
        }
        
        else
        {
         connection.setinfo(COMANDO_INICIAR);
        }	
	}
	//
	
	/*con la bandera que se paso es posible retornar a la activity principal con la URI*/
	public void return_to_first_activity(View v)
	{
		connection.setinfo(COMANDO_SALIR);
		if(fileuri != null)
		{
		    Intent data = new Intent();
		    data.putExtra("fileuri", fileuri);
	        setResult(RESULT_OK, data);			
		}
		
        this.finish();		
	}
	//
	
	/*comando para cancelar captura de datos*/
	public void command_cancel(View v)
	{
		if (connection.get_My_State() != ConnectorBtThread.STATE_CONNECTED) 
        {
          Toast.makeText(this, R.string.not_connected, Toast.LENGTH_LONG).show();
          return;
        }
        
        else
        {
         connection.setinfo(COMANDO_CANCELAR);
        }		
	}
	//
	
	/*comando para iniciar captura de datos */
	public void command_save(View v)
	{
        if (connection.get_My_State() != ConnectorBtThread.STATE_CONNECTED) 
        {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_LONG).show();
            return;
        }
        
        else
        {
        	if(verify_or_create_directory())
        	{
        	connection.setinfo(COMANDO_ENVIAR);
        	}	
        	else
        	{
        	Toast.makeText(this, R.string.directory_error, Toast.LENGTH_LONG).show();
        	}    
        }		
	}
	//
	
	
	
	
	/*obtencion de direccion del archivo para regresar a sana*/
	public void get_uri_file( String fileselected)
	{
		File directory = new File(Environment.getExternalStorageDirectory() + "/EEGsaved");
		File file_selected = new File(directory, fileselected);
  	    Uri uri_file = Uri.fromFile(file_selected);
  	    fileuri = uri_file.toString(); 
        System.out.println("seleccionado" + uri_file);
      
	}
	//
		
	/*verificar creacion de directorio*/
	private boolean verify_or_create_directory()
	{
	 boolean state = false;	
	 System.out.println("verificamos directorio");	  
	 ManageFiles file = new ManageFiles();
	 if(file.verify_sdcard())
	 {
	  if(file.verify_directory())
      {
	   state = true;    
	  }
	  else
	  {
      if(file.makedirectory())
	  {
	  state = true;
	  }   
	  }
	}

	return state; 
   }	
   //
	
  /*tratamiento de las respuestas del servidor */
  public void command_method(int command_from_server)
  {
  
  System.out.println("capturamos la respuesta del server en la vista" +command_from_server);
  
  switch (command_from_server) 
  {
  case COMANDO_TERMINADO:   
	    Toast.makeText(this, R.string.finished_acquiring, Toast.LENGTH_LONG).show();
	   	String nombre_archivo = connection.get_namefile(); 
	   	get_uri_file(nombre_archivo); 	   		  
  break;
  
  case COMANDO_CANCELADO:
	    Toast.makeText(this, R.string.cancel_acquiring, Toast.LENGTH_LONG).show(); 
  break;

  case COMANDO_INICIADO:
	    Toast.makeText(this, R.string.init_acquiring, Toast.LENGTH_LONG).show(); 
  break;  
  
  case COMANDO_FINALIZADO:
	    Toast.makeText(this, R.string.finished_acquiring, Toast.LENGTH_LONG).show(); 
  break;  
  
  case COMANDO_ERROR: 
	  Toast.makeText(this, R.string.error_acquiring, Toast.LENGTH_LONG).show(); 
  break;
  
  case COMANDO_VERIFICADO:
	 Toast.makeText(this, R.string.is_verify, Toast.LENGTH_LONG).show();   
     is_verify_sensor = 1 ;
  break;
  
  case COMANDO_ERROR_VERIFICAR:
   Toast.makeText(this, R.string.error_verify, Toast.LENGTH_LONG).show();   
  break; 
  
  
  }   
    
  }
  
  /* tratamiento de los estados del servidor */ 
  public void state_method(int state_from_connection)
  {
  System.out.println("capturamos la respuesta de un estado en la vista" +state_from_connection);
  
  switch (state_from_connection) 
  {
  case NO_STATE:            	
    state_connection_view.setText(R.string.free);
    disable_buttons();
  break;
  
  case STATE_CONNECTING:
     state_connection_view.setText(R.string.connecting);
     disable_buttons();
  break;
  
  case STATE_CONNECTED:
	  state_connection_view.setText(R.string.connected);
	  enable_buttons();
  break; 
      	  
  case ERROR_CONNECTION:
	  state_connection_view.setText(R.string.error_connection);
	  disable_buttons();
  break;
   
  case BUSSY:
	  state_connection_view.setText(R.string.bussy);
	  disable_buttons();
	  enable_cancel();
  break;  
  
  
  case BUSSY_FILE:
	  state_connection_view.setText(R.string.bussy);
	  disable_buttons();
  break;	
  
  case BUSSY_VERIFICAR:
	  state_connection_view.setText(R.string.bussy);
	  disable_buttons(); 
  break; 
  
  
  
  } 
}
//  

public void disable_buttons()
{	
capture.setEnabled(false);
save.setEnabled(false);
cancel.setEnabled(false);
verify.setEnabled(false);
}


//
public void enable_buttons()
{

 verify.setEnabled(true);	
	
if(is_verify_sensor == 1 )
{
 capture.setEnabled(true);
 save.setEnabled(true);
 cancel.setEnabled(true);	
}	
}

public void enable_cancel()
{
cancel.setEnabled(true);	
}

@Override
public void onBackPressed() 
{
connection.setinfo(COMANDO_SALIR);
if(fileuri != null)
{
    Intent data = new Intent();
    data.putExtra("fileuri", fileuri);
    setResult(RESULT_OK, data);			
}
this.finish();	
}

}
