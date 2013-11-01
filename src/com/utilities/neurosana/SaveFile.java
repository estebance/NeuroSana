package com.utilities.neurosana;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import android.os.Environment;

public class SaveFile 

{
ManageFiles verify_data;
String name; 
byte[] data;
	
	
public SaveFile(String filename , byte[] buffer)
{
verify_data = new ManageFiles();
data = buffer;
name = filename;
}	
	

public boolean verify_directory()
{
boolean state = false; 
boolean state_directory = false;
boolean sdcard_verify = verify_data.verify_sdcard();
boolean directory_verify = verify_data.verify_directory();
if(sdcard_verify)
{
 if(directory_verify)
 {
  state = savefile();	 
 }
 else 
 {
	state_directory = makedirectory(); 	
	if(state_directory)
	{
	state = savefile();	
	}	
 }
}
	
return state;  
}


public boolean makedirectory()
{
	boolean state = false; 
	File directory_eeg = new File(Environment.getExternalStorageDirectory() + "/EEGsaved/");
	directory_eeg.mkdirs();
	if(directory_eeg.exists())
	{
	state = true; 	
	}	
    return state;	
}


public boolean savefile()
{	
	boolean state = false;
	File directory = new File(Environment.getExternalStorageDirectory() + "/EEGsaved/");
	File data_eeg = new File(directory.getAbsolutePath() , name);
	try
	{
		
	BufferedOutputStream content = new BufferedOutputStream(new FileOutputStream(data_eeg));
	content.write(data);
	content.flush();
	content.close();
	state = true;
	
	}
	
	catch (Exception ex)
	{
	 System.out.println("Error al escribir el archivo en  la tarjeta SD");
	}		
return state; 	
}



}