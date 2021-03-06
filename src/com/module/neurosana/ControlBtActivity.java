package com.module.neurosana;

import com.utilities.neurosana.ConnectorBtThread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class ControlBtActivity extends Activity 
{

String Bt_Dir;
private BluetoothAdapter BluetoothAdapterConn = null;
ConnectorBtThread connection = null ;

public static final int COMANDO_ENTRANTE = 1;
public static final int ERROR = 2; 
public static final String COMAND = "comando";
public static final String STATE = "estado";

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
	
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control_bt);
	
	    /* capturamos del intent la informacion concerniente a la direccion del dispositivo*/
	
		Bundle extra_info = getIntent().getExtras();
		Bt_Dir = extra_info.getString("btdir"); 
		
		BluetoothAdapterConn = BluetoothAdapter.getDefaultAdapter();
		BluetoothDevice device_to_conn = BluetoothAdapterConn.getRemoteDevice(Bt_Dir);
		
		System.out.println("pillado " +device_to_conn.getName());
		System.out.println("dirr"+ device_to_conn.getAddress());
		
		connection = new ConnectorBtThread( Handler_responses_server );
		connection.connect_client(device_to_conn);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.control_bt, menu);
		return true;
	}
	
	
	public void command_capture(View v)
	{	
        if (connection.get_My_State() != ConnectorBtThread.STATE_CONNECTED) 
        {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
	       connection.setinfo(2);
        }
	}
	
    private final Handler Handler_responses_server = new Handler() 
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) 
            {
            case COMANDO_ENTRANTE:
            	
               String llego_orden = msg.getData().getString(COMAND);
               System.out.println("capturamos la respuesta en la vista" +llego_orden);
            break;
            case ERROR:
               String llego_estado = msg.getData().getString(STATE);
               System.out.println("capturamos la respuesta en la vista" +llego_estado); 
            break;
           
            }
        }
    };
	
	
	
}
