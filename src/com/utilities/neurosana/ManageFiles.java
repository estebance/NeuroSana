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
	 File f = new File(Environment.getExternalStorageDirectory() + "/EEGsaved/");	 
	 if (f.exists())
	 {
	 return true; 
	 }
	 else
	 {
	 return false;	 
	 }
 }
	
	
	
}
