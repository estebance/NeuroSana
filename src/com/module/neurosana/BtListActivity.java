package com.module.neurosana;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BtListActivity extends Activity 
{

    /*lista de dispositivos bluetooth*/	
	ArrayAdapter<String> bt_list_devices;
	private ListView devices_list; 
	
	/*adaptador bluetooth para buscar archivos*/
	BluetoothAdapter connector; 	
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bt_list);
		
		/*declaramos variables para el listview y adapter que llenara la vista*/
		bt_list_devices = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		devices_list = (ListView)findViewById(R.id.listView1bt_list_activity);
		
		/*declaramos un conector*/		
		connector =  BluetoothAdapter.getDefaultAdapter();	
	    bt_list_devices.clear();
	    /*escanemos los dispositivos*/
	    threadscan();
		/*encontramos un dispositivo bluetooth?*/
	    
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(Receiver_Devices, filter);
        
		/*insertamos dispositivos en el adapter y por consiguiente a la vista*/
		devices_list.setAdapter(bt_list_devices);
		/*si damos click a un dispositivo?*/
		devices_list.setOnItemClickListener(new OnItemClickListener()
				{
					@Override
					public void onItemClick(AdapterView<?> parent, View view,int position , long id) {
						// TODO Auto-generated method stub
						// capturamos toda la informacion nombre y direccion del dispositivo
						String deviceinfo = bt_list_devices.getItem(position).toString();
						// obtenemos la direccion del dispositivo -> es la unica que importa :D
						String devicedir = deviceinfo.substring(deviceinfo.length() - 17); 
						// invocamos la llamada al servidor
						callserver (devicedir);
				}
					
				});		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bt_list, menu);
		return true;
	}
	
	
    public void update(View v)
    {
    bt_list_devices.clear(); 
    threadscan();	
    }	


    private final BroadcastReceiver Receiver_Devices = new BroadcastReceiver() 
    {
    @Override
    public void onReceive(Context context, Intent intent) 
    {		
    String action = intent.getAction();
    if (BluetoothDevice.ACTION_FOUND.equals(action)) 
    {
    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
    System.out.println("Dispositivos capturados" +device.getName());
    bt_list_devices.add(device.getName() + "" + device.getAddress());	
    bt_list_devices.notifyDataSetChanged();
    }
    }
    };	

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
/*hilo para iniciar busqueda de dispositivos bluetooth*/
private void threadscan()
{
	  
	Thread t = new Thread()
	{
		@Override
		public void run()
		{
			connector.startDiscovery(); 
			try 
			{
			Thread.sleep(10000);	
			}
			catch(InterruptedException e)
			{
			e.printStackTrace();	
			}
			/*buscando dispositivos bluetooth*/
		    connector.cancelDiscovery();
		}
	};
	
 t.start();
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

public void callserver (String devicedir)
{
connector.cancelDiscovery();
Intent control_bt_server = new Intent(this , ControlBtActivity.class);
control_bt_server.putExtra("btdir", devicedir);
control_bt_server.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
startActivity(control_bt_server);
finish();
}   


/////////////////////////// detener el broadcast  y reanudarlo ///////////////////////////////////// 
@Override
protected void onDestroy()
{
super.onDestroy();
if (connector != null) {
    connector.cancelDiscovery();
}
this.unregisterReceiver(Receiver_Devices);
}

@Override
protected void onPause()
{
super.onPause();
if (connector != null) {
    connector.cancelDiscovery();
}

}

@Override
protected void onResume()
{
super.onResume();
/*escanemos los dispositivos*/

bt_list_devices.clear();
/*escanemos los dispositivos*/
 threadscan();
/*insertamos dispositivos en el adapter y por consiguiente a la vista*/
devices_list.setAdapter(bt_list_devices);

}


}
