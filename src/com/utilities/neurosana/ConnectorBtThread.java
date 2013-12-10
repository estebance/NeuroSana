package com.utilities.neurosana;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


import com.module.neurosana.ControlBtActivity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;


public class ConnectorBtThread 
{

/* en esta clase indicamos el estado de la conexion y se realizan los procesos*/	
private final Handler my_Handler_to_ui;
private Connect_BtThread my_Connect;
private Management_Connection my_Connected;
private int myState;
public String name_file = null; 


/*Estados de la conexion*/
public static final int NO_STATE = 0;
public static final int STATE_CONNECTING = 1; 
public static final int STATE_CONNECTED = 2;  
public static final int ERROR_CONNECTION = 3;
public static final int BUSSY = 5;
public static final int CONNECTION_CLOSE= 6;
public static final int FREE = 7;
public static final int BUSSY_FILE = 8;
public static final int BUSSY_VERIFICAR = 9 ; 

public ConnectorBtThread( Handler handler )
{
    my_Handler_to_ui = handler;
    set_My_State(NO_STATE);
}
	
private synchronized void set_My_State(int state) 
{
     myState = state;
     send_state_to_ui(state);
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

    /* vamos a iniciar un proceso de conexion */
    
    my_Connect = new Connect_BtThread(device);
    set_My_State(STATE_CONNECTING);
    my_Connect.start();
    
}


public synchronized void connected_with_server (BluetoothSocket socket) 
{
    my_Connected = new Management_Connection(socket);
    set_My_State(STATE_CONNECTED);
    my_Connected.start();
    
}


public synchronized void stop() 
{
 
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
   Management_Connection send;  /* sincronizando para poder ser empleado */
   synchronized (this) 
   {
     if (myState != STATE_CONNECTED)
     { 
     return;
     }
     else
     {
     send = my_Connected;
     send.write(data);
     }
   }
}
    
    
public void send_state_to_ui(int orden)
{
     ////////////////////// responder a la UI ///////////////////////////////////////
     Message msg = my_Handler_to_ui.obtainMessage(ControlBtActivity.STATE_ENTRANTE);
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
      socket_connect = device.createRfcommSocketToServiceRecord(MY_UUID); /* en bt un socket cumple el mismo principio de los socket tcp/ip*/
      System.out.println("Conectando: " + MY_UUID);
    }
    catch(IOException e)
    {
    System.out.println("no es posible crear el socket" + e);
    e.printStackTrace();
    set_My_State(ERROR_CONNECTION);
    }
        
    socket_information_connect = socket_connect;
}
    
    /*cuando corre el hilo despues del constructor hemos logrado conectarnos y estamos listos para transmitir informacion*/
    
     @Override
public void run()
{      	                          
        /*iniciado el socket*/
         try 
         {
         socket_information_connect.connect();
         if(socket_information_connect.isConnected())
         {
         connected_with_server(socket_information_connect);    	     	 
         }
         }  
         catch (Exception e) 
         {
      	 System.out.println("problemas para realizar una conexion estable:"+e); 
         e.printStackTrace();
      	 set_My_State(ERROR_CONNECTION);
      	 
         try
         {
         socket_information_connect.close();
         set_My_State(CONNECTION_CLOSE);
         }
         catch(Exception ex)
         {
         ex.printStackTrace();
         System.out.println("problema al cerrar el socket");
         }
         
         }                
}
     

public void cancel() 
{
        try 
        {
        socket_information_connect.close();
        set_My_State(CONNECTION_CLOSE);
        } 
        catch (IOException e) 
        {
        System.out.println("problema al cerrar el socket");
     	set_My_State(ERROR_CONNECTION);
        }
}
     
     

     
}       
 
public class Management_Connection extends Thread
{ 	

	/* variables para gestion de la conexion*/
    private BluetoothSocket socket_information;
    private InputStream incoming_data;
    private OutputStream outgoing_data;
    
    /*Respuestas y solicitudes al servidor*/
    private static final int COMANDO_SALIR = 1;
    private static final int COMANDO_ENVIAR = 2;
    private static final int COMANDO_CANCELAR=3;
    private static final int COMANDO_INICIAR=4; ;
    private static final int COMANDO_TERMINADO=5;
    private static final int COMANDO_CANCELADO=6;
    private static final int COMANDO_INICIADO=7;
    private static final int COMANDO_FINALIZADO=8;	
	private static final int COMANDO_VERIFICAR = 11;
	private static final int COMANDO_VERIFICADO = 12; 
	private static final int COMANDO_ERROR = -1;
	private static final int COMANDO_ERROR_VERIFICAR = -2 ;
    
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
        System.out.println("imposible gestionar los stream" + e);
        e.printStackTrace();
     	set_My_State(ERROR_CONNECTION);
        }
         
        socket_information=socket; 
        incoming_data = temporal_input;
        outgoing_data = temporal_output;
    }
    
    /*cuando corre el hilo despues del constructor estamos listos para transmitir informacion*/
    
     @Override
	public void run()
     {   
    	 int orden = 0;
    	 int respuesta = 0;    	
    	 System.out.println("enviando y recibiendo streams, gestionando la conexion...");                            
              
         while (true) 
         {
         try 
         {
          /* recibiendo respuestas del servidor */
        	 
          orden = incoming_data.read();
          
          
          if(orden == COMANDO_ENVIAR)
          {
          receivefile();           
          }
          
          if(orden == COMANDO_VERIFICAR)
          {
        	set_My_State(BUSSY_VERIFICAR);           
        	try
            {
            	while(true)
                {			
                respuesta = incoming_data.read();
                if(respuesta == COMANDO_VERIFICADO)
                {
                send_to_ui(respuesta);               
                break;
                }
                if(respuesta == COMANDO_ERROR_VERIFICAR)
                {
                send_to_ui(respuesta);
                break;
                }
                

                set_My_State(STATE_CONNECTED); 
                
                } // fin del while 
            	                                     
            }
              
            catch(Exception e)
            {
              set_My_State(NO_STATE);	
              e.printStackTrace();	
            }
        	
        	
          }	  
                    
          if(orden == COMANDO_INICIADO)
          {
        	set_My_State(BUSSY);  
          try
          {
        	while(true)
            {			
            respuesta = incoming_data.read();
            if(respuesta == COMANDO_FINALIZADO)
            {
            send_to_ui(respuesta);
            break;
            }
            if(respuesta == COMANDO_ERROR)
            {
            send_to_ui(respuesta);
            break;
            }
            
            } // fin del while 
        	
            set_My_State(STATE_CONNECTED);            
          }
          
          catch(Exception e)
          {
        	set_My_State(NO_STATE);	
        	e.printStackTrace();	
          }
         }
          
         else
         {
         send_to_ui(orden);
         }
         
        }  
         
         catch (Exception e) 
         {
      	 System.out.println("error en la comunicacion:"+e); 
      	 e.printStackTrace();
      	 set_My_State(ERROR_CONNECTION);
         break;
         }
         }        
 
    }
    
    /* Debemos  este metodo cuando decidamos enviar una solicitud */
    
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
 	  set_My_State(ERROR_CONNECTION); 	  
      }
     
     }
    
    public void cancel() 
    {
      try 
       {
 	    socket_information.close();
 	    set_My_State(CONNECTION_CLOSE);
       } 
      catch (IOException e) 
      {
        System.out.println("error al cerrar socket:"+e);  
        set_My_State(ERROR_CONNECTION);
      }
    }
 
    public void receivefile()
    {
       String read_name = null;
       String name_file;
       BufferedInputStream buffer_stream = null;
       FileOutputStream file_stream;   
       byte[] buffer_file_name = new byte[1024];
       byte[] buffer_file = new byte[64];
       int bytes_file_name =0;
       int bytes_file , respuesta_servidor;;
        try 
        {
        	set_My_State(BUSSY_FILE);
			bytes_file_name = incoming_data.read(buffer_file_name);
		    read_name = new String(buffer_file_name, 0, bytes_file_name);
			
		    if(read_name != null )
		    {
		    name_file = new String(read_name);	
	    	File directory = new File(Environment.getExternalStorageDirectory() + "/EEGsaved/");
	    	File data_eeg = new File(directory.getAbsolutePath() , name_file);
	    	/*ponemos el nombre del archivo*/
	    	set_name_file(name_file);
	 

	    	buffer_stream =new BufferedInputStream(incoming_data);
	    	file_stream = new FileOutputStream(data_eeg);
	    	 
	    	 while ((bytes_file = buffer_stream.read(buffer_file)) != 0)
	    	 {
	    	 System.out.println(bytes_file);	
	    	 if(bytes_file == 1)
	    	 {
	    	 break;
	    	 }
	    	 else
	    	 {
	    	 file_stream.write(buffer_file, 0, bytes_file);
			 file_stream.flush();}
	    	 }
	    	 file_stream.close();
	    	 /*esperar por la respuesta del servidor ante la orden*/
	    	 
	    	 System.out.println("esperando respuesta");
			 respuesta_servidor = incoming_data.read();
		     send_to_ui(respuesta_servidor);
		     set_My_State(STATE_CONNECTED );
			   
		    }		
        } 
        
        catch (IOException e) 
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
			set_My_State(ERROR_CONNECTION);
		} 
       

  
   }

    
}


public void set_name_file(String name)
{
	 name_file = name;   
}

public String get_namefile()
{
	return name_file;   
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

}
    	










 