package com.mcy.media;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;
import com.qrcode.R;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

public class MediaActivity extends Activity implements OnClickListener{
	
	private static final int REQUSET_CODE_PHOTO_ADD = 1;
	private static final int REQUSET_CODE_VIDEO_ADD = 2;
	private static final int REQUSET_CODE_AUDIO_ADD = 3;
	private static final int REQUSET_CODE_PHOTO_TAKE = 4;
	private static final int REQUSET_CODE_VIDEO_TAKE = 5;
	private static final int REQUSET_CODE_AUDIO_TAKE = 6;
	
	private String strPhotoPath = "";
	private String strVideoPath = "";
	private String strAudioPath = "";
	
	private EditText textPhoto,textVideo,textAudio;
	private ImageButton btnPhotoAdd,btnVideoAdd,btnAudioAdd;
	private ImageButton btnPhotoTake,btnVideoTake,btnAudioTake;
	private Button btnUpload;
	private ProgressBar proBar;
	
	//private MyApp myApp;
	//private MediaUpload mMediaUpload;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_media);		
		InitView();
	}

	private void InitView() {
		
		textPhoto = (EditText)findViewById(R.id.edit_pic);
		textVideo = (EditText)findViewById(R.id.edit_video);
		textAudio = (EditText)findViewById(R.id.edit_sound);
		
		btnPhotoAdd = (ImageButton)findViewById(R.id.img_btn_pic_add);
		btnPhotoTake = (ImageButton)findViewById(R.id.img_btn_pic_take);
		btnVideoAdd = (ImageButton)findViewById(R.id.img_btn_video_add);
		btnVideoTake = (ImageButton)findViewById(R.id.img_btn_video_take);
		btnAudioAdd = (ImageButton)findViewById(R.id.img_btn_sound_add);
		btnAudioTake= (ImageButton)findViewById(R.id.img_btn_sound_take);
		btnUpload = (Button)findViewById(R.id.btn_media_upload);
		
		//proBar = (ProgressBar)findViewById(R.id.progressBar_upload);
		
		btnPhotoAdd.setOnClickListener(this);
		btnPhotoTake.setOnClickListener(this);
		btnAudioAdd.setOnClickListener(this);
		btnAudioTake.setOnClickListener(this);
		btnVideoAdd.setOnClickListener(this);
		btnVideoTake.setOnClickListener(this);
		btnUpload.setOnClickListener(this);
		
		//myApp = (MyApp)getApplication();
		//mMediaUpload = new MediaUpload();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public void onClick(View v) {
        switch (v.getId()) {
		case R.id.img_btn_pic_add:
			AddMedia(REQUSET_CODE_PHOTO_ADD);
			break;
        case R.id.img_btn_video_add:
        	AddMedia(REQUSET_CODE_VIDEO_ADD);
			break;
        case R.id.img_btn_sound_add:
        	AddMedia(REQUSET_CODE_AUDIO_ADD);
	        break;
        case R.id.img_btn_pic_take:
        	TakeMedia(REQUSET_CODE_PHOTO_TAKE);
	        break;
        case R.id.img_btn_video_take:
        	TakeMedia(REQUSET_CODE_VIDEO_TAKE);
	        break;
        case R.id.img_btn_sound_take:
        	TakeMedia(REQUSET_CODE_AUDIO_TAKE);
	        break;
        case R.id.btn_media_upload:
        	proBar.setVisibility(View.VISIBLE);
        	//mMediaUpload.execute((Void)null);
        	break;
		}		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		Cursor cursor;
		Uri uriAdded;
		
		if(resultCode==RESULT_OK){
			switch (requestCode) {
			case REQUSET_CODE_PHOTO_ADD:
				
				uriAdded = data.getData();
				cursor = this.getContentResolver().query(uriAdded, null, null, null, null);
				if(cursor.moveToNext()){
					//_data:对应文件绝对路径
					strPhotoPath = cursor.getString(cursor.getColumnIndex("_data"));
					textPhoto.setText(strPhotoPath);
				}
				
				break;
			case REQUSET_CODE_VIDEO_ADD:
				
				uriAdded = data.getData();
				cursor = this.getContentResolver().query(uriAdded, null, null, null, null);
				if(cursor.moveToNext()){
					strVideoPath = cursor.getString(cursor.getColumnIndex("_data"));
					textVideo.setText(strVideoPath);
				}
				
				break;
			case REQUSET_CODE_AUDIO_ADD:
				
				uriAdded = data.getData();
				cursor = this.getContentResolver().query(uriAdded, null, null, null, null);
				if(cursor.moveToNext()){
					strAudioPath = cursor.getString(cursor.getColumnIndex("_data"));
					textAudio.setText(strAudioPath);
				}
				
				break;
			case REQUSET_CODE_PHOTO_TAKE:
				
				textPhoto.setText(strPhotoPath);
				
				break;
			case REQUSET_CODE_VIDEO_TAKE:
				
				textVideo.setText(strVideoPath);
				
				break;
			case REQUSET_CODE_AUDIO_TAKE:
				
				strAudioPath = data.getStringExtra("audiopath");
				textAudio.setText(strAudioPath);
				break;
			}
		}
	}

	private void TakeMedia(int code) {
		Intent intent;
		switch (code) {
		case REQUSET_CODE_PHOTO_TAKE:
			
			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			strPhotoPath = Environment.getExternalStorageDirectory().toString()+"/qrcode/pics/";
			String picName = (String) DateFormat.format("yyyyMMddhhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
			File outDir = new File(strPhotoPath);
			if(!outDir.exists()){
				outDir.mkdirs();
			}
			strPhotoPath += picName;
			outDir = new File(strPhotoPath);
			Uri uri = Uri.fromFile(outDir);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			startActivityForResult(intent, REQUSET_CODE_PHOTO_TAKE);
			
			break;
		case REQUSET_CODE_VIDEO_TAKE:
			
			intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			strVideoPath = Environment.getExternalStorageDirectory().toString()+"/qrcode/video/";
			File dir = new File(strVideoPath);
			if(!dir.exists()){
				dir.mkdirs();
			}
			String videoName = "video_"+(String) DateFormat.format("yyyyMMddmmss",Calendar.getInstance(Locale.CHINA))+".3gp";
			strVideoPath += videoName;
			intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(strVideoPath)) );
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
			startActivityForResult(intent, REQUSET_CODE_VIDEO_TAKE);
			
			break;
		case REQUSET_CODE_AUDIO_TAKE:
			
            intent = new Intent(MediaActivity.this,AudioActivity.class);
            startActivityForResult(intent, REQUSET_CODE_AUDIO_TAKE);
			
			break;
		}
	}

	private void AddMedia(int code) {
		
		Intent intent = new Intent();
		
		switch (code) {	
		case REQUSET_CODE_PHOTO_ADD:
			
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent, REQUSET_CODE_PHOTO_ADD);
			
			break;
		case REQUSET_CODE_VIDEO_ADD:
			
			intent.setType("video/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent, REQUSET_CODE_VIDEO_ADD);
			
			break;
		case REQUSET_CODE_AUDIO_ADD:
			
			intent.setType("audio/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent, REQUSET_CODE_AUDIO_ADD);
			
			break;
		}
	}
	
	/*
	class MediaUpload extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected Boolean doInBackground(Void... params) {
			return Upload();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			
			if(result){
			   proBar.setVisibility(View.GONE);
			   Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_LONG).show();
		    }else{
		       proBar.setVisibility(View.GONE);
			   Toast.makeText(getApplicationContext(), "上传失败，请重新上传", Toast.LENGTH_LONG).show();
		    } 
			super.onPostExecute(result);
		}
		
		private Boolean Upload() {
			
			String username = myApp.getname();
			String filename1 = strVideoPath;
			String filename2 = strAudioPath;
			byte[] data1 = null ,data2 = null;
			FileInputStream fis;
			try {
				if(!filename1.equals("")){
					fis = new FileInputStream(filename1);
					ByteArrayOutputStream baos=new ByteArrayOutputStream();
					byte[] buffer = new byte[8129];
					int count = 0;
					while((count = fis.read(buffer))>=0){
						baos.write(buffer, 0, count);
					}
					data1 = baos.toByteArray();
					fis.close();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if(!filename2.equals("")){
					fis = new FileInputStream(filename2);
					ByteArrayOutputStream baos=new ByteArrayOutputStream();
					byte[] buffer = new byte[8129];
					int count = 0;
					while((count = fis.read(buffer))>=0){
						baos.write(buffer, 0, count);
					}
					data2 = baos.toByteArray();
					fis.close();	
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			SoapObject request = new SoapObject(WebService.NAMESPACE, WebService.MethodName_UploadMedia);
			request.addProperty("username",username);
			request.addProperty("filename1",filename1);
			request.addProperty("data1",data1);
			request.addProperty("filename2",filename2);
			request.addProperty("data2",data2);
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyIn = request;
	    	(new MarshalBase64()).register(envelope);
	    	envelope.dotNet = true;   
	    	HttpTransportSE transport = new HttpTransportSE(WebService.URL);
	    	try {
				transport.call(WebService.SoapAction_UploadMedia, envelope);
				SoapObject response = new SoapObject();
				response = (SoapObject)envelope.bodyOut;
				if("true" == response.toString()){
					return true;
				}else{
					return false;
				}
			} catch (Exception e) {
                e.printStackTrace();
                return false;
			}
		}
	}*/
}
