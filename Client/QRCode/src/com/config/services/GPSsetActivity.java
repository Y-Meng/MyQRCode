package com.config.services;


import com.qrcode.R;

import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GPSsetActivity extends Activity implements OnClickListener{
	
	private Button btnStart;
	private Button btnStop;
	private TextView tvLatitude;
	private TextView tvLongitude;
	private TextView tvAltitute;
	private EditText editPeriod;
	private Location location;
	private MyReceiver mReceiver;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gpsset);
		setTitle("GPS");
		
		initView();
		
		mReceiver = new MyReceiver();
		registerReceiver(mReceiver, new  IntentFilter("locationuser"));
		Log.e("gps", "receiver registed");
		
	}

	private void initView() {
		 btnStart = (Button)findViewById(R.id.btn_start_gps);
		 btnStop = (Button)findViewById(R.id.btn_stop_gps);
		 tvAltitute = (TextView)findViewById(R.id.txt_gps_altitute);
		 tvLongitude = (TextView)findViewById(R.id.txt_gps_longtitute);
         tvLatitude = (TextView)findViewById(R.id.txt_gps_latitute);
         editPeriod = (EditText)findViewById(R.id.edit_gps_period);
         btnStart.setOnClickListener(this);
         btnStop.setOnClickListener(this);
	}
	private void updateView(Location location) {
		
		 tvAltitute.setText(String.valueOf(location.getAltitude()));
		 tvLatitude.setText(String.valueOf(location.getLatitude()));
		 tvLongitude.setText(String.valueOf(location.getLongitude()));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.gpsset, menu);
		return true;
	}

	class MyReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			 
			Log.e("gps", "receive cast");
			location = (Location) intent.getParcelableExtra("location");
			if(location!=null){
				updateView(location);
			}
			Log.e("gps", "no locatin");
		}
	}

	@Override
	public void onClick(View v) {
		 switch (v.getId()) {
		case R.id.btn_start_gps:
			SharedPreferences sp = getSharedPreferences("UserInfo", MODE_PRIVATE);
			sp.edit().putInt("period",Integer.parseInt(editPeriod.getText().toString())).commit();
			
			Intent service = new Intent(GPSsetActivity.this,GPSService.class);
			startService(service);
			break;
        case R.id.btn_stop_gps:
        	Intent name = new Intent(GPSsetActivity.this,GPSService.class);
			stopService(name);
			Toast.makeText(getApplicationContext(), "定位服务已关闭", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		
	}

	@Override
	protected void onDestroy() {
		 
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}

	@Override
	protected void onResume() {
		 
		super.onResume();
		registerReceiver(mReceiver, new  IntentFilter("locationuser"));
		Log.e("gps", "receiver registed");
	}
	
}
