package com.utilities.neurosana;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


import com.module.neurosana.ControlBtActivity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


public class ConnectorBtThread 
{

/* en esta clase indicamos el estado de la conexion*/	
private final Handler my_Handler_to_ui;
private Connect_BtThread my_Connect;
private Management_Connection my_Connected;
private int myState;


/*Estados de la conexion*/
public static final int NO_STATE = 0;       // we're doing nothing
public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
public static final int STATE_CONNECTED = 3;  // now connected to a remote device
public static final int ERROR_CONNECTION = 4;






public ConnectorBtThread( Handler handler ) 
{
    myState = NO_STATE;
    my_Handler_to_ui = handler;
}
	
private synchronized void set_My_State(int state) 
{
     myState = state;
    // Give the new state to the Handler so the UI Activity can update
    // mHandler.obtainMessage(BluetoothChat.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
}


public synchronized int get_My_State() 
{
    return myState;
}	

public synchronized void connect_client(BluetoothDevice device) 
{
    if (myState == STATE_CONNECTING) 
    {
        if (my_Connect != null) {my_Connect.cancel(); my_Connect = null;}
    }
    if (my_Connected != null) {my_Connected.cancel(); my_Connected = null;}

    // Start the thread to connect with the given device
    my_Connect = new Connect_BtThread(device);
    my_Connect.start();
    set_My_State(STATE_CONNECTING);
}


public synchronized void connected_with_server (BluetoothSocket socket) 
{

    if (my_Connect != null) {my_Connect.cancel(); my_Connect = null;}
    if (my_Connected != null) {my_Connected.cancel(); my_Connected = null;}


    my_Connected = new Management_Connection(socket);
    my_Connected.start();
    set_My_State(STATE_CONNECTED);
}


public synchronized void stop() {
 
    if (my_Connect != null) {
        my_Connect.cancel();
        my_Connect = null;
    }

    if (my_Connected != null) {
        my_Connected.cancel();
        my_Connected = null;
    }
    
    set_My_State(NO_STATE);
}
	
    public void setinfo (int data)
    { 
    	   Management_Connection send;
          // sincronizando para poder ser empleado  , si es posible quitarlos
          synchronized (this) 
          {
           if (myState != STATE_CONNECTED) return;
           send = my_Connected;
          }
          
          send.write(data);
    }
    
    
public void send_state_to_ui(int orden)
{
     ////////////////////// responder a la UI ///////////////////////////////////////
     Message msg = my_Handler_to_ui.obtainMessage(ControlBtActivity.ERROR);
     Bundle bundle = new Bundle();
     bundle.putString(ControlBtActivity.STATE, Integer.toString(orden));
     msg.setData(bundle);
     my_Handler_to_ui.sendMessage(msg);   	   
}   
    
   




public class Connect_BtThread extends Thread
{
	
  private final BluetoothSocket socket_information_connect;	
  private final UUID MY_UUID = UUID.fromString("0000-11010000-1000-8000-00805F9B34FD");


	
  public Connect_BtThread (BluetoothDevice device) 
    {  
	  BluetoothSocket socket_connect = null ;
	  
        try 
        {
          
        System.out.println("Informacion sobre la conexion: " + MY_UUID);          
        /* en bt un socket cumple el mismo principio de los socket tcp/ip*/
        socket_connect = device.createInsecureRfcommSocketToServiceRecord(MY_UUID); 
        System.out.println("Conectando: " + MY_UUID);
        	
        }
        catch(IOException e)
        {
        System.out.println("imposible conectar" + e);
     	send_state_to_ui(ERROR_CONNECTION);
     	set_My_State(ERROR_CONNECTION);
        }
        

        socket_information_connect = socket_connect;

    }
    
    /*cuando corre el hilo despues del constructor hemos logrado conectarnos y estamos listos para transmitir informacion*/
    
     @Override
	public void run()
     {   
    	 
    	System.out.println("iniciamos.....");                            
        System.out.println("Conectado iniciamos la comunicacion: " + MY_UUID);  
         
        /*iniciado el socket*/
         try 
         {
         // pueda que funcione es mejor revisar. 
         socket_information_connect.connect();
         }  
         catch (Exception e) 
         {
      	 System.out.println("problemas para realizar una conexion estable:"+e); 
      	 e.printStackTrace();
         try
         {
         socket_information_connect.close();	 
         }
         catch(Exception ex)
         {
         ex.printStackTrace();
         System.out.println("problema al cerrar el socket");
       	 send_state_to_ui(ERROR_CONNECTION);
       	 set_My_State(ERROR_CONNECTION);
         }
         
         }                
    
         connected_with_server(socket_information_connect);
    
    }
     

    public void cancel() 
    {
        try 
        {
        socket_information_connect.close();
        } 
        catch (IOException e) 
        {
        System.out.println("problema al cerrar el socket");
     	send_state_to_ui(ERROR_CONNECTION);
     	set_My_State(ERROR_CONNECTION);
        }
    }
     
     

     
} 
         
 
public class Management_Connection extends Thread
{ 	

    private final  BluetoothSocket socket_information;
    private InputStream incoming_data;
    private OutputStream outgoing_data;
	
    
    /*Respuestas y solicitudes al servidor*/
    private static final int COMANDO_SALIR = 1;
    private static final int COMANDO_ENVIAR = 2;
    private static final int COMANDO_CANCELAR=3;
    private static final int COMANDO_INICIAR=4; ;
    private static final int COMANDO_TERMINADO=5;
    private static final int COMANDO_CANCELADO=5;
	
    
    String readMessage = null;
    
    public Management_Connection(BluetoothSocket socket) 
    {    	
        InputStream temporal_input = null;
        OutputStream temporal_output = null;
        
        
        try 
        {
           temporal_input = socket.getInputStream();
           temporal_output = socket.getOutputStream();                             	
        }
        catch(Exception e)
        {
        System.out.println("imposible conectar" + e);
     	send_state_to_ui(ERROR_CONNECTION);
     	set_My_State(ERROR_CONNECTION);
        }
        
         socket_information=socket; 
         incoming_data = temporal_input;
         outgoing_data = temporal_output;
    }
    
    /*cuando corre el hilo despues del constructor hemos logrado conectarnos y estamos listos para transmitir informacion*/
    
     @Override
	public void run()
     {   
    	 
    	System.out.println("iniciamos en este momento ya estamos gestionando la conexion.....");                            
         
         /*iniciado el socket gestionamos esa conexion con el servidor por tanto nos remitimos a esa clas*/
         int orden = 0;
         
         while (true) 
         {
         try 
         {
         // pueda que funcione es mejor revisar. 
          orden = incoming_data.read();
          System.out.println(orden);
          if(orden == COMANDO_ENVIAR)
          {
          receivefile();  
          }
          else
          {
          send_to_ui(orden);
          }
         }  
         catch (IOException e) 
         {
      	 System.out.println("inputstream error en hilo comunicacion connector:"+e); 
      	 send_state_to_ui(ERROR_CONNECTION);
      	 set_My_State(ERROR_CONNECTION);
         break;
         }
         }        
 
    }
    
    /* Debemos llamarte cuando decidamos enviar una solicitud */
    public void write(int order) 
     {
      try  
      {  	  
      System.out.println("datos a enviar "+ order);
      outgoing_data.write(order);
      } 
      catch (IOException e) 
      {
 	  System.out.println("error al enviar solicitud:"+e); 
 	  send_state_to_ui(ERROR_CONNECTION);
 	  set_My_State(ERROR_CONNECTION); 
 	  
      }
     
      }
    
    public void cancel() 
    {
      try 
       {
 	    socket_information.close();
       } 
      catch (IOException e) 
      {
        System.out.println("error al cerrar socket:"+e);  
        send_state_to_ui(ERROR_CONNECTION);
      }
    }

    
    
    public void receivefile()
    {
    
    System.out.println("procedemos a recibir un archivo");	
    // pueda que funcione es mejor revisar. 
    byte[] buffer = new byte[1024];
    int bytes =0;
        try 
        {
			bytes = incoming_data.read(buffer);
		    readMessage = new String(buffer, 0, bytes);
	        savefile();
	        System.out.println("leido" + readMessage);
	        
		} 
        catch (IOException e) 
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
			send_state_to_ui(ERROR_CONNECTION);
		} 
       

  
   }
    
    
   public void savefile()
   {
	   
   } 
   
   
   public void send_to_ui(int orden)
   {
    ////////////////////// responder a la UI ///////////////////////////////////////
    Message msg = my_Handler_to_ui.obtainMessage(ControlBtActivity.COMANDO_ENTRANTE);
    Bundle bundle = new Bundle();
    bundle.putString(ControlBtActivity.COMAND, Integer.toString(orden));
    msg.setData(bundle);
    my_Handler_to_ui.sendMessage(msg);   	   
   }


   
    
}}
    	










 