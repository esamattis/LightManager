package epeli.lightmanager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;

import android.R.bool;
import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Main extends Activity implements SensorEventListener{

	boolean monitorActive;
	TextView tv;
	TextView colorvalue;
	LightManager lights;
	
	private SensorManager mSensorManager;
    private Sensor mAccelerometer;

	protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }	    
    

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        monitorActive = false;
        lights = new LightManager();

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        
        tv = (TextView) findViewById(R.id.teksti);
        colorvalue = (TextView) findViewById(R.id.colorvalue);
        tv.setText(":D");
        
        
        
        Button buttonblue = (Button) findViewById(R.id.buttonblue);
        Button buttonred = (Button) findViewById(R.id.buttonred);
        Button buttongreen = (Button) findViewById(R.id.buttongreen);
        Button buttonmonitor = (Button) findViewById(R.id.monitor);
        
        
        
        buttongreen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				for (int i=32; i<46; i++) {
					lights.setHSV(i, 100f, 100f, 100f );
				}
			}
		});
        buttonred.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				for (int i=32; i<46; i++) {
					lights.setHSV(i, 0f, 100f, 100f );
				}
			}
		});        
        buttonblue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				for (int i=32; i<46; i++) {
					lights.setHSV(i, 244f, 100f, 100f );
				}
			}
		});  
        buttonmonitor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				monitorActive = !monitorActive;
				Log.d("instanssi", "settig monitor to " + monitorActive);
			}
		});
        
        
        
    }





	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}


	public int mapTo0to255(float i, float min, float max) {
		int out = (int) ((i-min)/(max-min) * 255.0);
		if (out < 0 ) out = 0;
		else if (out > 255) out = 255;
//		Log.d("instanssi", "Transforming " + i +  " to " + out);
		return out; 
	}
	
	
	public float mapTo0To1(float i, float min, float max) {
		float out = (i-min) / (max-min);
		Log.d("instanssi", "mapping " + i + " to " + out);
		return out;
	}
	
	
	public int mapTo32to45(float i, float min, float max) {
		return (int) (32.0 + (i-min)/(max-min) * 14.0);
		
	}
	
	
	@Override
	public void onSensorChanged(SensorEvent e) {
//		Log.d("instanssi", "SAAN " + e.values[0] + "," + e.values[1] + "," + e.values[2] );
		
		int r = mapTo0to255(e.values[0], -10, 10);
		int g = mapTo0to255(e.values[1], -10, 10);
		int b = mapTo0to255(e.values[2], -10, 10);
		 
		
		
		if (monitorActive){
			tv.setBackgroundColor(Color.rgb(r,g,b));
			float hue = mapTo0To1(e.values[2], -20f, 20f);
			for (int i=32; i<46; i++) {
				lights.setHSV(i, hue*100f, 100f, 100f);
			}
	//		setLight(33, mapTo0to255(e.values[1], -10, 10), 0, 0);
	//		setLight(32, 0, mapTo0to255(e.values[0], -10, 10), 0);
	//		setLight(34, 0, 0, mapTo0to255(e.values[2], -10, 10));
		}
		
	}    
    
}
