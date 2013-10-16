package com.utilities.neurosana;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class ConnectorBtThread extends Thread
{
	private BluetoothSocket Socket_information = null;   
	private final BluetoothDevice Device_bt;
	private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FD");
	private final InputStream incoming_data;
	private  OutputStream outgoing_data;
	

    byte[] bit;
    
    public ConnectorBtThread (BluetoothDevice device) 
    {    	
        BluetoothSocket socketemporal = null;
        Device_bt = device;    
        InputStream temporal_input = null;
        OutputStream temporal_output = null;
        
        try 
        {
           System.out.println("Informacion sobre la conexion: " + MY_UUID);
           /* en bt un socket cumple el mismo principio de los socket tcp/ip*/
           socketemporal = device.createRfcommSocketToServiceRecord(MY_UUID);
 
           Socket_information = socketemporal;
           temporal_input = Socket_information.getInputStream();
           temporal_output = Socket_information.getOutputStream(); 
           
           System.out.println("Conectando: " + MY_UUID);
        } 
        
        catch (IOException e) 
        {
        System.out.println("error:"+e);	
        
        }    
       
            incoming_data = temporal_input;
            outgoing_data = temporal_output;
        }
    
    /*cuando corre el hilo despues del constructor hemos logrado conectarnos y estamos listos para transmitir informacion*/
    
     public void run()
     {   
    	System.out.println("iniciamos.....");
        try 
        {
         Socket_information.connect();        
         System.out.println("Conectado iniciamos la comunicacion: " + MY_UUID);  
         
         /*iniciado el socket gestionamos esa conexion con el servidor por tanto nos remitimos a esa clas*/

         byte[] buffer = new byte[1024]; 
         int bytes;
         while (true) 
         {
         try 
         {
         // pueda que funcione es mejor revisar. 

          //DataInputStream incoming = new DataInputStream(incoming_data);
          //int size_data = incoming.available();
          //byte[] file_byte = new byte[size_data];
          
      	 bytes = incoming_data.read(buffer);
      	 System.out.println("bytes:"+bytes+"buffer:"+buffer);
      	 System.out.println(buffer.toString());
      	   
          //FileOutputStream  fileOuputStream = new FileOutputStream("escribir una ruta");
          //fileOuputStream.write(filebyte);
          //fileOuputStream.close();
         }  
         catch (IOException e) 
         {
      	 System.out.println("inputstream error en hilo comunicacion connector:"+e);   
         break;
         }
         }       
         } 
      
        catch (IOException connectException) 
        {        
     	
     	 System.out.println("error es necesario cerrar:" +connectException);     	
     	 
     	 try 
          {
             Socket_information.close();
          }     
         catch (IOException closeException) 
          {
             System.out.println("error al cerrar el socket:" + closeException);
          }
         
        }
    }
    
    /* Debemos llamarte cuando decidamos enviar una solicitud */
    public void write(String solicitud) 
     {
      try 
      {
      DataOutputStream flujo= new DataOutputStream(outgoing_data);
      flujo.writeUTF(solicitud);
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
