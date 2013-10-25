package com.utilities.neurosana;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import com.module.neurosana.ControlBtActivity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

public class ConnectorBtThread extends Thread
{
	private BluetoothSocket Socket_information = null;   
	private final BluetoothDevice Device_bt;
	private final UUID MY_UUID = UUID.fromString("0000-11010000-1000-8000-00805F9B34FD");
	private final InputStream incoming_data;
	private OutputStream outgoing_data;
	public  final Handler handler_response;
	

    byte[] bit;
    
    public ConnectorBtThread (BluetoothDevice device , Handler handler) 
    {    	
        BluetoothSocket socketemporal = null;
        Device_bt = device; 
        handler_response = handler; 
        InputStream temporal_input = null;
        OutputStream temporal_output = null;
        
        try 
        {
           System.out.println("Informacion sobre la conexion: " + MY_UUID);
           /* en bt un socket cumple el mismo principio de los socket tcp/ip*/
           socketemporal = device.createRfcommSocketToServiceRecord(MY_UUID);
           Socket_information = socketemporal;
 
           Socket_information.connect();
           temporal_input = Socket_information.getInputStream();
           temporal_output = Socket_information.getOutputStream();                      
           System.out.println("Conectando: " + MY_UUID);
        	
        }
        catch(Exception e)
        {
        System.out.println("imposible conectar" + e);
        }
        
         incoming_data = temporal_input;
         outgoing_data = temporal_output;
        }
    
    /*cuando corre el hilo despues del constructor hemos logrado conectarnos y estamos listos para transmitir informacion*/
    
     public void run()
     {   
    	 
    	System.out.println("iniciamos.....");                            
        System.out.println("Conectado iniciamos la comunicacion: " + MY_UUID);  
         
         /*iniciado el socket gestionamos esa conexion con el servidor por tanto nos remitimos a esa clas*/

         byte[] buffer = new byte[1024]; 
         int bytes;
         int a ; 
         while (true) 
         {
         try 
         {
         // pueda que funcione es mejor revisar. 
          bytes = incoming_data.read(buffer);
          System.out.println(bytes);
          handler_response.obtainMessage(ControlBtActivity.COMANDO_ENTRANTE, bytes, -1, buffer).sendToTarget();
          }  
         catch (IOException e) 
         {
      	 System.out.println("inputstream error en hilo comunicacion connector:"+e);   
         break;
         }
         }        
 
    }
    
    /* Debemos llamarte cuando decidamos enviar una solicitud */
    public void write(byte[] bytes) 
     {
    
      DataOutputStream flujo= new DataOutputStream(outgoing_data);	
      try  
      {
      
      System.out.println("datos a enviar "+bytes);
      flujo.writeInt(4);
      flujo.close();
      } 
      catch (IOException e) 
      {
 	  System.out.println("error al enviar solicitud:"+e);    
      }
     
      }
    
    public void cancel() 
      {
      try 
       {
 	    Socket_information.close();
       } 
      catch (IOException e) 
      {
        System.out.println("error al cerrar socket:"+e);  
      }
    }





}


/*
    private static final int COMANDO_SALIR = 1;
	private static final int COMANDO_ENVIAR = 2;
	private static final int COMANDO_CANCELAR=3;
	private static final int COMANDO_INICIAR=4; ;
	private static final int COMANDO_TERMINADO=5;
	private static final int COMANDO_CANCELADO=5;
*/
 