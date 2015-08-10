package com.mcy.media;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.qrcode.R;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AudioActivity extends Activity implements OnClickListener{
	
	private Handler mHandler;
	private Button btnStartRecord;
	private Button btnStopRecord;
	private TextView txtTime;
	private Timer mtTimer;
	private TimerTask mTimerTask;
	private int lCont = 0;
	private MediaRecorder mMediaRecorder;
	private String strAudioPath;
	private String strAudioName;
	private Intent intentCallIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio);
		intentCallIntent = getIntent();
		InitView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}
	

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_start_record:
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss",Locale.CHINA);
			strAudioName ="audio_" + simpleDateFormat.format(date) + ".arm";
			strAudioPath += strAudioName;
			InitAudio(strAudioPath);
			try {
				mMediaRecorder.prepare();
				mMediaRecorder.start();
				mtTimer.schedule(mTimerTask, 0, 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case R.id.btn_stop_record:
			mMediaRecorder.stop();
			mMediaRecorder.release();
			mtTimer.cancel();
			lCont = 0;
			intentCallIntent.putExtra("audiopath", strAudioPath);
			intentCallIntent.putExtra("audioname", strAudioName);
			setResult(RESULT_OK,intentCallIntent);
			finish();
			break;

		default:
			break;
		}
	}
	
	@SuppressLint("HandlerLeak")
	private void InitView(){
		strAudioPath = Environment.getExternalStorageDirectory()+"/qrcode/audio/";
		File fileAudioPath = new File(strAudioPath);
		if(!fileAudioPath.exists()){
			fileAudioPath.mkdirs();
		}
		txtTime = (TextView)findViewById(R.id.txt_time);
		btnStartRecord = (Button)findViewById(R.id.btn_start_record);
		btnStopRecord = (Button)findViewById(R.id.btn_stop_record);
		
		btnStartRecord.setOnClickListener(this);
		btnStopRecord.setOnClickListener(this);
		
		mHandler = new  Handler(){
			
			@Override
			public void handleMessage(Message msg) {

				if(msg.what == 0){
					lCont++;
					int hh,mm,ss;
					hh = lCont/3600;
					mm = (lCont%3600)/60;
					ss = (lCont%3600)%60;
					txtTime.setText(String.format("%1$02d:%2$02d:%3$02d", hh, mm, ss));
				}					
				super.handleMessage(msg);
			}
			
		};
		
		mtTimer = new Timer();
		mTimerTask = new TimerTask() {
			
			@Override
			public void run() {

				mHandler.sendEmptyMessage(0);
			}
		};
	}

	private void InitAudio(String outFilePath) {

		mMediaRecorder = new MediaRecorder();
		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//设置声音来源麦克风
		mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);//设置输出格式
		mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//设置编码格式
		mMediaRecorder.setOutputFile(outFilePath);//设置输出路径
	}


}
