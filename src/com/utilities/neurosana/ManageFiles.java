package com.utilities.neurosana;

import java.io.File;

import android.os.Environment;

public class ManageFiles 
{

 public Boolean verify_sdcard()
 {
  if(Environment.getExternalStorageState().equals("mounted"))
   {	  
   return true;	   
   }
  else
   {
   return false; 	  
   }	
 }
 
 
 public Boolean verify_directory()
 {
	 File f = new File(Environment.getExternalStorageDirectory() + "/EEGsaved");	 
	 if (f.exists())
	 {
	 return true; 
	 }
	 else
	 {
	 return false;	 
	 }
 }
 
 public boolean makedirectory()
 {
 	boolean state = false; 
 	File directory_eeg = new File(Environment.getExternalStorageDirectory() + "/EEGsaved");
 	directory_eeg.mkdirs();
 	if(directory_eeg.exists())
 	{
 	state = true; 	
 	}	
     return state;	
 }
	
	
	
}
