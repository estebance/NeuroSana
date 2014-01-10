package com.activities.neurosana;


import java.text.DecimalFormat;
import java.util.Arrays;

import com.androidplot.Plot;
import com.androidplot.util.Redrawer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;
import com.module.neurosana.R;
import com.utilities.neurosana.ReadEdf;

import android.os.Bundle;
import android.view.Menu;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;


public class ChartActivity extends Activity 
{
 private static final int X_RANGE_SIZE = 100;	
 private XYPlot myHistoryPlot = null;
 private SimpleXYSeries chanel_a_HistorySeries = null;
 private SimpleXYSeries chanel_b_HistorySeries = null;
 private SimpleXYSeries chanel_c_HistorySeries = null;
 private SimpleXYSeries chanel_d_HistorySeries = null;
 private SimpleXYSeries chanel_e_HistorySeries = null;
 private SimpleXYSeries chanel_f_HistorySeries = null;
 private SimpleXYSeries chanel_g_HistorySeries = null;
 private SimpleXYSeries chanel_h_HistorySeries = null;
 private SimpleXYSeries chanel_i_HistorySeries = null;
 private SimpleXYSeries chanel_j_HistorySeries = null;
 private SimpleXYSeries chanel_k_HistorySeries = null;
 private SimpleXYSeries chanel_l_HistorySeries = null;
 private SimpleXYSeries chanel_m_HistorySeries = null;
 private SimpleXYSeries chanel_n_HistorySeries = null;
 private Redrawer redrawer;

 
 ReadEdf my_edf_data = null; 
 String[] edf_header_chanel = null;
 int[] chanel_number = new int[14];
 int size_signal;
 int size_limit = 0;
 int break_for = 0; 
 double[] signal_a , signal_b , signal_c , signal_d , signal_e , signal_f , signal_g , signal_h , signal_i , signal_j , signal_k , signal_l , signal_m , signal_n; 
	
 boolean stop = false; 
 
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart);
		
		/*capture extra*/
		Intent intent = getIntent();
		String file_uri = intent.getStringExtra("data_direction");
		String result_file_uri = file_uri.substring(8); 
		
		/* to plot*/
        myHistoryPlot = (XYPlot) findViewById(R.id.xyplot_eeg1);
        
        
        myHistoryPlot.getGraphWidget().setDomainLabelPaint(null);
        myHistoryPlot.getGraphWidget().setRangeLabelPaint(null);
        myHistoryPlot.getGraphWidget().setMarginRight(30);
        myHistoryPlot.getLegendWidget().setMarginBottom(5);
        

        
        chanel_a_HistorySeries = new SimpleXYSeries("AF3");
        chanel_a_HistorySeries.useImplicitXVals();
        chanel_b_HistorySeries = new SimpleXYSeries("F7");
        chanel_b_HistorySeries.useImplicitXVals();
        chanel_c_HistorySeries = new SimpleXYSeries("F3");
        chanel_c_HistorySeries.useImplicitXVals();
        chanel_d_HistorySeries = new SimpleXYSeries("FC5");
        chanel_d_HistorySeries.useImplicitXVals();
        chanel_e_HistorySeries = new SimpleXYSeries("T7");
        chanel_e_HistorySeries.useImplicitXVals();
        chanel_f_HistorySeries = new SimpleXYSeries("P7");
        chanel_f_HistorySeries.useImplicitXVals();
        chanel_g_HistorySeries = new SimpleXYSeries("01");
        chanel_g_HistorySeries.useImplicitXVals();
        chanel_h_HistorySeries = new SimpleXYSeries("02");
        chanel_h_HistorySeries.useImplicitXVals();
        chanel_i_HistorySeries = new SimpleXYSeries("P8");
        chanel_i_HistorySeries.useImplicitXVals();
        chanel_j_HistorySeries = new SimpleXYSeries("T8");
        chanel_j_HistorySeries.useImplicitXVals();
        chanel_k_HistorySeries = new SimpleXYSeries("FC6");
        chanel_k_HistorySeries.useImplicitXVals();
        chanel_l_HistorySeries = new SimpleXYSeries("F4");
        chanel_l_HistorySeries.useImplicitXVals();
        chanel_m_HistorySeries = new SimpleXYSeries("F8");
        chanel_m_HistorySeries.useImplicitXVals();
        chanel_n_HistorySeries = new SimpleXYSeries("AF4");
        chanel_n_HistorySeries.useImplicitXVals();
       
        
        myHistoryPlot.setRangeBoundaries(7500, 10200, BoundaryMode.FIXED);    
        myHistoryPlot.setDomainBoundaries(0, X_RANGE_SIZE, BoundaryMode.FIXED);
        myHistoryPlot.addSeries(chanel_a_HistorySeries,new LineAndPointFormatter(Color.rgb(255, 255,  255), null, null, null));
        myHistoryPlot.addSeries(chanel_b_HistorySeries,new LineAndPointFormatter(Color.rgb(255, 255,    0), null, null, null));
        myHistoryPlot.addSeries(chanel_c_HistorySeries,new LineAndPointFormatter(Color.rgb(255, 102,  255), null, null, null));
        myHistoryPlot.addSeries(chanel_d_HistorySeries,new LineAndPointFormatter(Color.rgb(255, 153,  102), null, null, null));
        myHistoryPlot.addSeries(chanel_e_HistorySeries,new LineAndPointFormatter(Color.rgb(255,   0,  51), null, null, null));
        myHistoryPlot.addSeries(chanel_f_HistorySeries,new LineAndPointFormatter(Color.rgb(204, 153, 255), null, null, null));
        myHistoryPlot.addSeries(chanel_g_HistorySeries,new LineAndPointFormatter(Color.rgb(216, 177, 100), null, null, null));
        myHistoryPlot.addSeries(chanel_h_HistorySeries,new LineAndPointFormatter(Color.rgb(102,   0,  51), null, null, null));
        myHistoryPlot.addSeries(chanel_i_HistorySeries,new LineAndPointFormatter(Color.rgb(153, 255,   0), null, null, null));
        myHistoryPlot.addSeries(chanel_j_HistorySeries,new LineAndPointFormatter(Color.rgb(204,  77,  51), null, null, null));
        myHistoryPlot.addSeries(chanel_k_HistorySeries,new LineAndPointFormatter(Color.rgb(102, 255,  255), null, null, null));
        myHistoryPlot.addSeries(chanel_l_HistorySeries,new LineAndPointFormatter(Color.rgb(  0, 204, 204), null, null, null));
        myHistoryPlot.addSeries(chanel_m_HistorySeries,new LineAndPointFormatter(Color.rgb(204, 255,   0), null, null, null));
        myHistoryPlot.addSeries(chanel_n_HistorySeries,new LineAndPointFormatter(Color.rgb(100, 102, 204), null, null, null));
                 
        
        myHistoryPlot.setDomainStepMode(XYStepMode.INCREMENT_BY_VAL);
        myHistoryPlot.setDomainStepValue(X_RANGE_SIZE/100);
        myHistoryPlot.setTicksPerRangeLabel(3); // revisar
        //myHistoryPlot.setDomainLabel("Domain");
        myHistoryPlot.getDomainLabelWidget().pack();
        //myHistoryPlot.setRangeLabel("Range");
        myHistoryPlot.getRangeLabelWidget().pack();

        myHistoryPlot.setRangeValueFormat(new DecimalFormat("#"));
        myHistoryPlot.setDomainValueFormat(new DecimalFormat("#"));		
		
        redrawer = new Redrawer(Arrays.asList(new Plot[]{myHistoryPlot}),100, false);
		
        /*final to plot*/
		
        /*to read edf*/
		my_edf_data = new ReadEdf();
		my_edf_data.set_uri_file(result_file_uri);
		my_edf_data.init_parser_file();
		edf_header_chanel = my_edf_data.get_label_chanel();
		System.out.println("hola mundo"+edf_header_chanel[0]);
		my_edf_data.get_signal();
		run_capture();
			
	}
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chart, menu);
		return true;
	}

	
	public void duerme(int time)
	{
		
	try 
	{
		Thread.sleep(time);
	} catch (InterruptedException e) 
	{
		e.printStackTrace();
	}
		
	}
	
	@Override
    public void onResume() {
        super.onResume();
        redrawer.start();
    }

    @Override
    public void onPause() 
    {    
    super.onPause();
    redrawer.pause();
    }

    @Override
    public void onDestroy() 
    {
        super.onDestroy();
        stop = true; 
    	redrawer.finish();
    }
    


@Override
public void onBackPressed() 
{
this.finish();
stop = true;	
redrawer.finish();
System.out.println("pulsadoBack");
}
  

public void run_capture()
{
  new Thread(new Runnable() 
  {

  int increment = 20 ;
  int resta = 0 ;	
    	
   public void run()
   {
    
   while(true)
   {  
	   
   if(stop == true)
   { 
    break;  
   }   
	   
   for(int a =0; a < edf_header_chanel.length ; a++)
   {
	System.out.println(edf_header_chanel[a]);   
	chanel_number[a] = a ;   
   }   
   
   size_signal = my_edf_data.get_size_data();
   System.out.println("tamano senal"+size_signal);   
   
   for(int i = 0 ; i < size_signal ; i+=increment)
   {
	   
   if(stop == true)
   {
    break;  
   }	   
	   
   resta = size_signal - size_limit;
   // si los datos que quedan son menores al incremento establecido , set al incremento con este valor 
   if(resta < 20)
   {
	increment = resta;   
   }
   size_limit += increment; 
   signal_a =  my_edf_data.my_signal_part(i, chanel_number[0], size_limit);   
   signal_b =  my_edf_data.my_signal_part(i, chanel_number[1], size_limit);   
   signal_c =  my_edf_data.my_signal_part(i, chanel_number[2], size_limit);   
   signal_d =  my_edf_data.my_signal_part(i, chanel_number[3], size_limit);   
   signal_e =  my_edf_data.my_signal_part(i, chanel_number[4], size_limit);   
   signal_f =  my_edf_data.my_signal_part(i, chanel_number[5], size_limit);   
   signal_g =  my_edf_data.my_signal_part(i, chanel_number[6], size_limit);   
   signal_h =  my_edf_data.my_signal_part(i, chanel_number[7], size_limit);   
   signal_i =  my_edf_data.my_signal_part(i, chanel_number[8], size_limit);   
   signal_j =  my_edf_data.my_signal_part(i, chanel_number[9], size_limit);  
   signal_k =  my_edf_data.my_signal_part(i, chanel_number[10], size_limit);   
   signal_l =  my_edf_data.my_signal_part(i, chanel_number[11], size_limit);   
   signal_m =  my_edf_data.my_signal_part(i, chanel_number[12], size_limit);
   signal_n = my_edf_data.my_signal_part( i, chanel_number[13], size_limit);
   
   System.out.println("el limite de tama??o es_:"+size_limit);	 
   System.out.println("el incremento es_:"+increment);

   for(int b =  0 ; b < signal_a.length ; b++)
   {
	   if (chanel_a_HistorySeries.size() > X_RANGE_SIZE) 
	   {
	    chanel_a_HistorySeries.removeFirst();
        chanel_b_HistorySeries.removeFirst();
        chanel_c_HistorySeries.removeFirst();
        chanel_d_HistorySeries.removeFirst();
        chanel_e_HistorySeries.removeFirst();
        chanel_f_HistorySeries.removeFirst();
        chanel_g_HistorySeries.removeFirst();
        chanel_h_HistorySeries.removeFirst();
        chanel_i_HistorySeries.removeFirst();
        chanel_j_HistorySeries.removeFirst();
        chanel_k_HistorySeries.removeFirst();
        chanel_l_HistorySeries.removeFirst();
        chanel_m_HistorySeries.removeFirst();
        chanel_n_HistorySeries.removeFirst(); 
	   }
	   
        chanel_a_HistorySeries.addLast(null, signal_a[b] + 245); // 7 senal 
        chanel_b_HistorySeries.addLast(null, signal_b[b] + 1000); // 3 senal 
        chanel_c_HistorySeries.addLast(null, signal_c[b] + 1250); // 1 senal 
        chanel_d_HistorySeries.addLast(null, signal_d[b]  + 35 ); // 13 senal 
        chanel_e_HistorySeries.addLast(null, signal_e[b] + 925); // 2 senal
        chanel_f_HistorySeries.addLast(null, signal_f[b] - 150); // 14 senal 
        chanel_g_HistorySeries.addLast(null, signal_g[b] + 295); // 8 senal 
        chanel_h_HistorySeries.addLast(null, signal_h[b] + 275); // 10 senal 
        chanel_i_HistorySeries.addLast(null, signal_i[b] + 200); // 11 senal 
        chanel_j_HistorySeries.addLast(null, signal_j[b] + 135); // 12 senal 
        chanel_k_HistorySeries.addLast(null, signal_k[b] - 125); // senal 9 
        chanel_l_HistorySeries.addLast(null, signal_l[b] + 525); // 4 senal 
        chanel_m_HistorySeries.addLast(null, signal_m[b] + 200);	// 6 senal
        chanel_n_HistorySeries.addLast(null, signal_n[b] + 285); // 5 senal 		    		  	    	   		   
       }
      duerme(1000);     
   }	



 }
 }
 }).start();
 
}   
}
