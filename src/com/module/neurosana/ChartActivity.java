package com.module.neurosana;

import java.text.DecimalFormat;
import java.util.Random;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.FloatMath;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

@SuppressLint("FloatMath")
public class ChartActivity extends Activity implements OnTouchListener 
{
	
private XYPlot eeg_chart;
private SimpleXYSeries[] data_chart = null;
private static final int SERIES_SIZE = 200;

private PointF minimo_valor;
private PointF maximo_valor;


	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart);
	
        eeg_chart = (XYPlot) findViewById(R.id.xyplot_eeg1);
        eeg_chart.setOnTouchListener(this);
        
        //Plot layout configurations
        eeg_chart.getGraphWidget().setTicksPerRangeLabel(1);
        eeg_chart.getGraphWidget().setTicksPerDomainLabel(1);
        eeg_chart.getGraphWidget().setRangeValueFormat(new DecimalFormat("#####.##"));
        eeg_chart.getGraphWidget().setDomainValueFormat(new DecimalFormat("#####.##"));
        eeg_chart.getGraphWidget().setRangeLabelWidth(20);
        eeg_chart.getGraphWidget().setPadding(20, 20, 20, 20);
        eeg_chart.setRangeLabel("");
        eeg_chart.setDomainLabel("");
      
        /*configuracion de lineas y valores a graficar*/
        data_chart = new SimpleXYSeries[4];
        
        int scale = 1;
        for (int i = 0; i < 4; i++, scale *= 5) {
            data_chart[i] = new SimpleXYSeries("S" + i);
            populateSeries(data_chart[i], scale);
        }
        
        eeg_chart.addSeries(data_chart[3],new LineAndPointFormatter(Color.rgb(50, 0, 0), null, null , null));
        eeg_chart.addSeries(data_chart[2],new LineAndPointFormatter(Color.rgb(50, 50, 0), null,null, null));
        eeg_chart.addSeries(data_chart[1],new LineAndPointFormatter(Color.rgb(0, 50, 0), null,null, null));
        eeg_chart.addSeries(data_chart[0],new LineAndPointFormatter(Color.rgb(0, 0, 0), null,null, null));
        eeg_chart.redraw();
        eeg_chart.calculateMinMaxVals();
        minimo_valor = new PointF(eeg_chart.getCalculatedMinX().floatValue(), eeg_chart.getCalculatedMinY().floatValue());
        maximo_valor = new PointF(eeg_chart.getCalculatedMaxX().floatValue(), eeg_chart.getCalculatedMaxY().floatValue());        
         
        
	}
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chart, menu);
		return true;
	}

	
    private void populateSeries(SimpleXYSeries series, int max) 
    {
        Random r = new Random();
        for(int i = 0; i < SERIES_SIZE; i++) {
            series.addLast(i, r.nextInt(max));
        }
    }

    // Definition of the touch states
    static final int NONE = 0;
    static final int ONE_FINGER_DRAG = 1;
    static final int TWO_FINGERS_DRAG = 2;
    int mode = NONE;

    PointF firstFinger;
    float distBetweenFingers;
    boolean stopThread = false;

    @Override
    public boolean onTouch(View arg0, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: // Start gesture
                firstFinger = new PointF(event.getX(), event.getY());
                mode = ONE_FINGER_DRAG;
                stopThread = true;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_POINTER_DOWN: // second finger
                distBetweenFingers = spacing(event);
                // the distance check is done to avoid false alarms
                if (distBetweenFingers > 5f) {
                    mode = TWO_FINGERS_DRAG;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == ONE_FINGER_DRAG) {
                    PointF oldFirstFinger = firstFinger;
                    firstFinger = new PointF(event.getX(), event.getY());
                    scroll(oldFirstFinger.x - firstFinger.x);
                    eeg_chart.setDomainBoundaries(minimo_valor.x, maximo_valor.x,
                            BoundaryMode.FIXED);
                    eeg_chart.redraw();

                } else if (mode == TWO_FINGERS_DRAG) {
                    float oldDist = distBetweenFingers;
                    distBetweenFingers = spacing(event);
                    zoom(oldDist / distBetweenFingers);
                    eeg_chart.setDomainBoundaries(minimo_valor.x, maximo_valor.x,
                            BoundaryMode.FIXED);
                    eeg_chart.redraw();
                }
                break;
        }
        return true;
    }

    private void zoom(float scale) {
        float domainSpan = maximo_valor.x - minimo_valor.x;
        float domainMidPoint = maximo_valor.x - domainSpan / 2.0f;
        float offset = domainSpan * scale / 2.0f;

        minimo_valor.x = domainMidPoint - offset;
        maximo_valor.x = domainMidPoint + offset;

        minimo_valor.x = Math.min(minimo_valor.x, data_chart[3].getX(data_chart[3].size() - 3)
                .floatValue());
        maximo_valor.x = Math.max(maximo_valor.x, data_chart[0].getX(1).floatValue());
        clampToDomainBounds(domainSpan);
    }

    private void scroll(float pan) {
        float domainSpan = maximo_valor.x - minimo_valor.x;
        float step = domainSpan / eeg_chart.getWidth();
        float offset = pan * step;
        minimo_valor.x = minimo_valor.x + offset;
        maximo_valor.x = maximo_valor.x + offset;
        clampToDomainBounds(domainSpan);
    }

    private void clampToDomainBounds(float domainSpan) {
        float leftBoundary = data_chart[0].getX(0).floatValue();
        float rightBoundary = data_chart[3].getX(data_chart[3].size() - 1).floatValue();
        // enforce left scroll boundary:
        if (minimo_valor.x < leftBoundary) {
        	minimo_valor.x = leftBoundary;
            maximo_valor.x = leftBoundary + domainSpan;
        } else if (maximo_valor.x > data_chart[3].getX(data_chart[3].size() - 1).floatValue()) {
        	maximo_valor.x = rightBoundary;
        	minimo_valor.x = rightBoundary - domainSpan;
        }
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

}
