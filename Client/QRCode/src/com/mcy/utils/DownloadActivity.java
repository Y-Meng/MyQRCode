package com.mcy.utils;

import java.io.File;
import com.qrcode.R;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class DownloadActivity extends Activity {
	
	private Context context;
	private LinearLayout root;
	private String result;
	private String[] downloadList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);
		setTitle("下载任务");
		
		context = this;
		root = (LinearLayout)findViewById(R.id.root);
		result =getIntent().getStringExtra("result");
		downloadList = result.split(";");
		
		for (String filename : downloadList) {
			createDownloadView(filename);
		} 
	}

	//动态添加数据信息展示view
	private void createDownloadView(String strUrl){
		LayoutInflater inflater = this.getLayoutInflater();
		LinearLayout child = (LinearLayout)inflater.inflate(R.layout.item_download, null);
		ProgressBar probar = (ProgressBar)child.findViewById(R.id.download_proBar);
		Button btnControl = (Button)child.findViewById(R.id.download_btn_control);
		Button btnDelete = (Button)child.findViewById(R.id.download_btn_delete);
		
		MyListener listener = new MyListener(probar,strUrl);
		btnControl.setOnClickListener(listener);
		btnDelete.setOnClickListener(listener);
		
		root.addView(child);
	}
	
	
	private  class MyListener implements OnClickListener{
		
		private ProgressBar proBar;
		private String dataUrl;
		private String saveDir;
		private long filelen;
		private  Downloader downloader;
		@SuppressLint("HandlerLeak")
		private Handler handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case -1:
					try{
						downloader.download(1);
					}catch(Exception e){
						e.printStackTrace();
						Toast.makeText(context, "下载异常", Toast.LENGTH_LONG).show();
					}
					break;
				case 0:
					filelen = msg.getData().getLong("filelength");
					proBar.setMax(100);
					break;
				case 1:
					long done = msg.getData().getLong("done");
					
					int pro = (int)(100*done/filelen);
					proBar.setProgress(pro);
					
					if(done==filelen){
                         Toast.makeText(context,dataUrl+"下载完成", Toast.LENGTH_LONG).show();
                         root.removeView((View)proBar.getParent());
					}
					break;
				case 2:
					root.removeView((View)proBar.getParent());
					break;

				default:
					break;
				}
			}
		};
		
		public MyListener(ProgressBar progressBar,String url){
			this.proBar = progressBar;
			this.dataUrl = url;
			saveDir = Environment.getExternalStorageDirectory().toString()+"\\qrcode\\download";
			downloader = new Downloader(context, handler, dataUrl, saveDir);
			handler.sendEmptyMessage(-1);
		}

		
		
		@Override
		public void onClick(View v) {
            switch (v.getId()) {
			case R.id.download_btn_control:
				Button b = (Button)v;
				if("||".equals(b.getText())){
					downloader.pause();
					b.setText(">");
				}else{
					downloader.resume();
					b.setText("||");
				}
				break;
			case R.id.download_btn_delete:
				File file = new File(saveDir+dataUrl.substring(dataUrl.lastIndexOf("/")+1));
				if(file.exists())
					file.delete();
				downloader.delete();
				handler.sendEmptyMessage(2);
				break;
			default:
				break;
			}			
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.download, menu);
		return true;
	}

}
