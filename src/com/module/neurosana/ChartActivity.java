package com.module.neurosana;


import java.text.DecimalFormat;
import java.util.Arrays;

import com.androidplot.Plot;
import com.androidplot.util.Redrawer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;
import com.utilities.neurosana.ReadEdf;

import android.os.Bundle;
import android.view.Menu;
import android.app.Activity;
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
 private Redrawer redrawer;

 
 ReadEdf my_edf_data = null; 
 String[] edf_header_chanel = null;
 int[] chanel_number = new int[36];
 int size_signal;
 int size_limit = 0;
 double[] signal_a , signal_b , signal_c , signal_d , signal_e , signal_f , signal_g , signal_h , signal_i , signal_j , signal_k , signal_l , signal_m ; 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart);
		
		
		/* to plot*/
        myHistoryPlot = (XYPlot) findViewById(R.id.xyplot_eeg1);
        chanel_a_HistorySeries = new SimpleXYSeries("Chanel 1");
        chanel_a_HistorySeries.useImplicitXVals();
        chanel_b_HistorySeries = new SimpleXYSeries("Chanel 2");
        chanel_b_HistorySeries.useImplicitXVals();
        chanel_c_HistorySeries = new SimpleXYSeries("Chanel 3");
        chanel_c_HistorySeries.useImplicitXVals();
        chanel_d_HistorySeries = new SimpleXYSeries("Chanel 4");
        chanel_d_HistorySeries.useImplicitXVals();
        chanel_e_HistorySeries = new SimpleXYSeries("Chanel 5");
        chanel_e_HistorySeries.useImplicitXVals();
        chanel_f_HistorySeries = new SimpleXYSeries("Chanel 6");
        chanel_f_HistorySeries.useImplicitXVals();
        chanel_g_HistorySeries = new SimpleXYSeries("Chanel 7");
        chanel_g_HistorySeries.useImplicitXVals();
        chanel_h_HistorySeries = new SimpleXYSeries("Chanel 8");
        chanel_h_HistorySeries.useImplicitXVals();
        chanel_h_HistorySeries = new SimpleXYSeries("Chanel 9");
        chanel_h_HistorySeries.useImplicitXVals();
        chanel_i_HistorySeries = new SimpleXYSeries("Chanel 10");
        chanel_i_HistorySeries.useImplicitXVals();
        chanel_j_HistorySeries = new SimpleXYSeries("Chanel 11");
        chanel_j_HistorySeries.useImplicitXVals();
        chanel_k_HistorySeries = new SimpleXYSeries("Chanel 12");
        chanel_k_HistorySeries.useImplicitXVals();
        chanel_l_HistorySeries = new SimpleXYSeries("Chanel 13");
        chanel_l_HistorySeries.useImplicitXVals();
        chanel_m_HistorySeries = new SimpleXYSeries("Chanel 14");
        chanel_m_HistorySeries.useImplicitXVals();

        
        myHistoryPlot.setRangeBoundaries(3750, 5000, BoundaryMode.FIXED); // revisar     
        myHistoryPlot.setDomainBoundaries(0, X_RANGE_SIZE, BoundaryMode.FIXED); // revisar
        myHistoryPlot.addSeries(chanel_a_HistorySeries,new LineAndPointFormatter(Color.rgb(100, 100, 200), null, null, null));
        myHistoryPlot.addSeries(chanel_b_HistorySeries,new LineAndPointFormatter(Color.rgb(100, 200, 100), null, null, null));
        myHistoryPlot.addSeries(chanel_c_HistorySeries,new LineAndPointFormatter(Color.rgb(200, 100, 100), null, null, null));
        myHistoryPlot.addSeries(chanel_d_HistorySeries,new LineAndPointFormatter(Color.rgb(100, 100, 200), null, null, null));
        myHistoryPlot.addSeries(chanel_e_HistorySeries,new LineAndPointFormatter(Color.rgb(100, 200, 100), null, null, null));
        myHistoryPlot.addSeries(chanel_f_HistorySeries,new LineAndPointFormatter(Color.rgb(200, 100, 100), null, null, null));
        myHistoryPlot.addSeries(chanel_g_HistorySeries,new LineAndPointFormatter(Color.rgb(100, 100, 200), null, null, null));
        myHistoryPlot.addSeries(chanel_h_HistorySeries,new LineAndPointFormatter(Color.rgb(100, 200, 100), null, null, null));
        myHistoryPlot.addSeries(chanel_i_HistorySeries,new LineAndPointFormatter(Color.rgb(200, 100, 100), null, null, null));
        myHistoryPlot.addSeries(chanel_j_HistorySeries,new LineAndPointFormatter(Color.rgb(100, 100, 200), null, null, null));
        myHistoryPlot.addSeries(chanel_k_HistorySeries,new LineAndPointFormatter(Color.rgb(100, 200, 100), null, null, null));
        myHistoryPlot.addSeries(chanel_l_HistorySeries,new LineAndPointFormatter(Color.rgb(200, 100, 100), null, null, null));
        myHistoryPlot.addSeries(chanel_m_HistorySeries,new LineAndPointFormatter(Color.rgb(100, 100, 200), null, null, null));
                
        
        myHistoryPlot.setDomainStepMode(XYStepMode.INCREMENT_BY_VAL);
        myHistoryPlot.setDomainStepValue(X_RANGE_SIZE/100);
        myHistoryPlot.setTicksPerRangeLabel(3); // revisar
        myHistoryPlot.setDomainLabel("Domain");
        myHistoryPlot.getDomainLabelWidget().pack();
        myHistoryPlot.setRangeLabel("Range");
        myHistoryPlot.getRangeLabelWidget().pack();

        myHistoryPlot.setRangeValueFormat(new DecimalFormat("#"));
        myHistoryPlot.setDomainValueFormat(new DecimalFormat("#"));		
		
        redrawer = new Redrawer(Arrays.asList(new Plot[]{myHistoryPlot}),100, false);
		
        /*final to plot*/
		
        /*to read edf*/
		my_edf_data = new ReadEdf();
		my_edf_data.init_parser_file();
		edf_header_chanel = my_edf_data.get_label_chanel();
		my_edf_data.get_signal();
		run_data_charge();
		
	}
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chart, menu);
		return true;
	}

	
	public void duerme(int tie)
	{
		
	try {
		Thread.sleep(tie);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
	}
	
	@Override
    public void onResume() {
        super.onResume();
        redrawer.start();
    }

    @Override
    public void onPause() {
        redrawer.pause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        redrawer.finish();
        super.onDestroy();
    }

    
    
    
    
    public void other()
    {
    /*	
		int increment = 50 ;
	    int resta = 0 ;
		
	   for(int a =0; a < edf_header_chanel.length ; a++)
	   {
		System.out.println(edf_header_chanel[a]);   
		chanel_number[a] = a ;   
	   }   
	   
	   size_signal = my_edf_data.get_size_data();
	   System.out.println("tamano senal"+size_signal);   
	  
	   while(true){
	   for(int i = 0 ; i < size_signal ; i+=increment)
	   {	   
	   resta = size_signal - size_limit;
	   if(resta < 50)
	   {
		increment = resta;   
	   }
	   size_limit += increment; 
	   signal =  my_edf_data.my_signal_part(i, chanel_number[1], size_limit);   
	   
	   System.out.println("el limite de tamaño es_:"+size_limit);	 
	   System.out.println("el incremento es_:"+increment);

	   for(int b =  0 ; b < signal.length ; b++)
	   {
		 System.out.println("value_:"+signal[b]);  
	   }
	   duerme(2000);
	   }}
	      	*/
    }
    
public void run_data_charge()
{

	 new Thread(new Runnable(){
	        public void run() 
	        {
	            // get rid the oldest sample in history:
	         //   if (chanel_a_HistorySeries.size() > X_RANGE_SIZE) {
	         /*       chanel_a_HistorySeries.removeFirst();
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
	                chanel_m_HistorySeries.removeFirst();*/
	         //   }
        	
	    	    int increment = 20 ;
	    	    int resta = 0 ;
	    		
	    	   for(int a =0; a < edf_header_chanel.length ; a++)
	    	   {
	    		System.out.println(edf_header_chanel[a]);   
	    		chanel_number[a] = a ;   
	    	   }   
	    	   
	    	   size_signal = my_edf_data.get_size_data();
	    	   System.out.println("tamano senal"+size_signal);   
	    	   for(int i = 0 ; i < size_signal ; i+=increment)
	    	   {	   
	    	   resta = size_signal - size_limit;
	    	   if(resta < 20)
	    	   {
	    		increment = resta;   
	    	   }
	    	   size_limit += increment; 
	    	   signal_a =  my_edf_data.my_signal_part(i, chanel_number[2], size_limit);   
	    	   signal_b =  my_edf_data.my_signal_part(i, chanel_number[3], size_limit);   
	    	   signal_c =  my_edf_data.my_signal_part(i, chanel_number[4], size_limit);   
	    	   signal_d =  my_edf_data.my_signal_part(i, chanel_number[5], size_limit);   
	    	   signal_e =  my_edf_data.my_signal_part(i, chanel_number[6], size_limit);   
	    	   signal_f =  my_edf_data.my_signal_part(i, chanel_number[7], size_limit);   
	    	   signal_g =  my_edf_data.my_signal_part(i, chanel_number[8], size_limit);   
	    	   signal_h =  my_edf_data.my_signal_part(i, chanel_number[9], size_limit);   
	    	   signal_i =  my_edf_data.my_signal_part(i, chanel_number[10], size_limit);   
	    	   signal_j =  my_edf_data.my_signal_part(i, chanel_number[11], size_limit);  
	    	   signal_k =  my_edf_data.my_signal_part(i, chanel_number[12], size_limit);   
	    	   signal_l =  my_edf_data.my_signal_part(i, chanel_number[13], size_limit);   
	    	   signal_m =  my_edf_data.my_signal_part(i, chanel_number[14], size_limit);   
	    	   
	    	   System.out.println("el limite de tamaño es_:"+size_limit);	 
	    	   System.out.println("el incremento es_:"+increment);

	    	   for(int b =  0 ; b < signal_a.length ; b++)
	    	   {
	    		   if (chanel_a_HistorySeries.size() > X_RANGE_SIZE) {
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
	                chanel_m_HistorySeries.removeFirst();}
	    		   
		            chanel_a_HistorySeries.addLast(null, signal_a[b]);
		            chanel_b_HistorySeries.addLast(null, signal_b[b]);
		            chanel_c_HistorySeries.addLast(null, signal_c[b]);
		            chanel_d_HistorySeries.addLast(null, signal_d[b]);
		            chanel_e_HistorySeries.addLast(null, signal_e[b]);
		            chanel_f_HistorySeries.addLast(null, signal_f[b]);
		            chanel_g_HistorySeries.addLast(null, signal_g[b]);
		            chanel_h_HistorySeries.addLast(null, signal_h[b]);
		            chanel_i_HistorySeries.addLast(null, signal_i[b]);
		            chanel_j_HistorySeries.addLast(null, signal_j[b]);
		            chanel_k_HistorySeries.addLast(null, signal_k[b]);
		            chanel_l_HistorySeries.addLast(null, signal_l[b]);
		            chanel_m_HistorySeries.addLast(null, signal_m[b]);		    		   
		    		  	    	   		   
	    		 //System.out.println("value_:"+signal[b]);  
	    	   }
	    	   duerme(1000);
	    	   }
	        	
	        	
	        	
	        }
	    }).start();	
	
}    
    
   
}
