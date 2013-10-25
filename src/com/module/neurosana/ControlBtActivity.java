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

public class ControlBtActivity extends Activity 
{

String Bt_Dir;
private BluetoothAdapter BluetoothAdapterConn = null;
ConnectorBtThread connection;

public static final int COMANDO_ENTRANTE = 1;
public static final int ERROR = 2; 

	
	
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
		
		connection = new ConnectorBtThread(device_to_conn , Handler_responses_server );
		connection.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.control_bt, menu);
		return true;
	}
	
	
	public void command_capture(View v)
	{
	  String a = "4";	
	  connection.write(a.getBytes());	
	}
	
    private final Handler Handler_responses_server = new Handler() 
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) 
            {
            case COMANDO_ENTRANTE:
            	
                byte[] datafrombuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String command = new String(datafrombuf);
                
                System.out.println("comando desde el servidor" +command);


                break;
            case ERROR:
                
                break;
           
            }
        }
    };
	
	
	
}
