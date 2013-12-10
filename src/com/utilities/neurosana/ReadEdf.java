package com.utilities.neurosana;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;


import com.utilities.edf.EDFParser;
import com.utilities.edf.EDFParserResult;

public class ReadEdf 
{

	BufferedInputStream data_source = null;
	EDFParserResult parser_data_edf = null ;
	String direction_data = null; 
	double[][] data_values;
	int size_data; 
	


	
public ReadEdf()
{
}

public int get_size_data()
{
return size_data;	
}



public void set_uri_file(String direction_data)
{
 this.direction_data = direction_data;	
}	


public void  init_parser_file()
{	
  try 
    {
	  data_source = new BufferedInputStream(new FileInputStream(new File(""+direction_data+"")));
      parser_data_edf = EDFParser.parseEDF(data_source);
    } 
  catch (Exception e) 
  {
	e.printStackTrace();
  }		
}

public void get_signal()
{
 data_values = parser_data_edf.getSignal().getValuesInUnits();
 size_data = data_values[0].length;
}

public String[] get_label_chanel()
{
	String chanel_labels[] = parser_data_edf.getHeader().getChannelLabels();
    return chanel_labels;
}


public double[] my_signal_part(int init_capture , int chanel , int limit)
{
System.out.println("for inicia en_:"+init_capture);	
System.out.println("for limite en_:"+limit);	
double[] data_from_chanel = new double[limit-init_capture];
int position = 0 ; 
for(int range = init_capture ; range < limit  ; range++)
{
 data_from_chanel[position] = data_values[chanel][range];
 position++; 
}
return data_from_chanel;
}




	
	
	
}
