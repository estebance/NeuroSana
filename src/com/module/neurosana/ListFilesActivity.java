package com.module.neurosana;

import java.io.File;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListFilesActivity extends Activity 
{
  /*lista de archivos*/
   ListView file_list;	
   ArrayAdapter<String> adapter_list_file;
   
   /*directorio del archivo*/
   File directory;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_files);
		
		/* obtener el directorio y crear la ruta de archivos*/ 
		
		directory = new File(Environment.getExternalStorageDirectory() + "/EEGsaved");
		File[] files = directory.listFiles();
		
	    /* capturar la list view para cargar los archivos */
		 adapter_list_file = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1); 
		 for (int i = 0; i < files.length; i++)
	     {
	            File file = files[i];
	            if (file.isDirectory())
	                adapter_list_file.add(file.getName() + "/");
	            else
	                adapter_list_file.add(file.getName());
	     }
			
		file_list=(ListView)findViewById(R.id.listView1list_files_activity);
		file_list.setAdapter(adapter_list_file);
		
		/*listener que captura cuando se pulse sobre un archivo*/
		file_list.setOnItemClickListener(new OnItemClickListener()
		  {
            @Override
			public void onItemClick(AdapterView<?> parent, View view , int position , long id) 
            {
             //   
                
             String fileselected  = adapter_list_file.getItem(position).toString();
             
             /* capturamos URI  del archivo */ 
             
        	  File file_selected = new File(directory, fileselected);
        	  Uri uri_file = Uri.fromFile(file_selected);
        	  String fileuri = uri_file.toString(); 
              System.out.println("seleccionado" + uri_file);
            
                 	 
        	 /* repondemos con el intent */ 
        	 
        	    Intent data = new Intent();
        	    data.putExtra("fileuri", fileuri);
        	    setResult(RESULT_OK, data);
                finish();
				
			}
           });
		
		
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_files, menu);
		return true;
	}



}
