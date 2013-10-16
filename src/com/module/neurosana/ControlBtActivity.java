package com.module.neurosana;

import com.utilities.neurosana.ConnectorBtThread;

import android.os.Bundle;
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
		
		//System.out.println("pillado " +device_to_conn.getName());
		//System.out.println("dirr"+ device_to_conn.getAddress());
		
		connection = new ConnectorBtThread(device_to_conn);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.control_bt, menu);
		return true;
	}
	
	
	public void command_capture(View v)
	{
	  connection.write("2");	
	}
	

}
