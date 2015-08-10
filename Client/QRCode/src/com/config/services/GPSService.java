package com.config.services;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.entity.DBHelper;
import com.entity.LocationRecord;
import com.j256.ormlite.dao.Dao;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class GPSService extends Service {

	LocationListener listener;
	LocationManager manager;
	Location location;
	String provider;
	TimerTask queryTask;
	private MyApp myapp;
	private Timer mTimer;
	private TimerTask mTimerTask;
	final String tag = "gps service";

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		
		myapp = (MyApp)getApplication();
		//定时器
		mTimer = new Timer();
		mTimerTask = new TimerTask() {
			
			@Override
			public void run() {
				 if(location!=null){
					DBHelper mDbHelper = new DBHelper(getApplicationContext()); 
					LocationRecord locationRecord = new LocationRecord();
					
					locationRecord.setLatitude(location.getLatitude());
					locationRecord.setLongitide(location.getLongitude());
					locationRecord.setUserName(myapp.getname());
					Date date = new Date(System.currentTimeMillis());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
					String time = sdf.format(date);
					locationRecord.setLocateTime(time);
					try {
						Dao<LocationRecord, Integer> mDao = mDbHelper.getLocationRecordDao();
						mDao.create(locationRecord);
						Log.e("location", "saved");
					} catch (SQLException e) {
						e.printStackTrace();
					}
				 }
			}
		};
		// 初始化位置监听
		listener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
		 
			}

			@Override
			public void onProviderEnabled(String provider) {

			}

			@Override
			public void onProviderDisabled(String provider) {
				Toast.makeText(getApplicationContext(), "无服务",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onLocationChanged(Location location) {
				sendLocation(location);
			}
		};
		manager = (LocationManager) getSystemService(LOCATION_SERVICE);
		location = null;
		Criteria criteria = new Criteria();
		//criteria.setAccuracy(Criteria.ACCURACY_HIGH);//表明所要求的经纬度的精度
		criteria.setAltitudeRequired(true); // 高度信息是否需要提供
		criteria.setBearingRequired(false); // 压力（气压？）信息是否需要提供
		criteria.setCostAllowed(false); // 是否会产生费用
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);// 最大需求标准
		provider = manager.getBestProvider(criteria, true);
		
		Location loc = manager.getLastKnownLocation(provider);
		if(loc!=null){
			sendLocation(loc);
		}
		manager.requestLocationUpdates(provider, 1000, 1, listener);//绑定监听，貌似不太好使
		
		SharedPreferences sp = getSharedPreferences("UserInfo", MODE_PRIVATE);
		int period = sp.getInt("period", 5);
		mTimer.schedule(mTimerTask, period*60*1000);
		//--------------取消线程----------
		//locThread mThread = new locThread();
		//mThread.run();
		//---------------2014.4.25------------
		super.onCreate();
		Log.e(tag, "create");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.e(tag, "destoryed");
		super.onDestroy();
		manager.removeUpdates(listener);
		mTimer.cancel();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e(tag, "start");
		return super.onStartCommand(intent, flags, startId);
	}

	private void sendLocation(Location location) {
		if(location!=null){
			Intent mIntent = new Intent();
			//接收器接收时需要过滤，设置action
			mIntent.setAction("locationuser");
			mIntent.putExtra("location", location);
			sendBroadcast(mIntent);
			Log.e(tag, "broadcast sended");
		}else{
			Log.e(tag, "location null");
		}
		
	}
	/*----------------发送定位信息线程-----------------
	class locThread extends Thread {

		public void run() {
			while (true) {
				try {
					sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (provider != null) {
					location = manager.getLastKnownLocation(provider);
				} else {
					Toast.makeText(getApplicationContext(), "无服务",
							Toast.LENGTH_SHORT).show();
				}

				Intent mIntent = new Intent();
				mIntent.setAction("locationuser");
				mIntent.putExtra("location", location);
				sendBroadcast(mIntent);
			}
		}
	}*/

}
