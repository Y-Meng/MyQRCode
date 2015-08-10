package com.mcy.media;
import java.io.File;
import java.util.Calendar;
import java.util.Locale;
import com.qrcode.R;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

public class MediaFragment extends Fragment implements OnClickListener{
	
	private static final int REQUSET_CODE_PHOTO_ADD = 1;
	private static final int REQUSET_CODE_VIDEO_ADD = 2;
	private static final int REQUSET_CODE_AUDIO_ADD = 3;
	private static final int REQUSET_CODE_PHOTO_TAKE = 4;
	private static final int REQUSET_CODE_VIDEO_TAKE = 5;
	private static final int REQUSET_CODE_AUDIO_TAKE = 6;
	
	private String strPhotoPath = "";
	private String strVideoPath = "";
	private String strAudioPath = "";
	private String strPhotoName = "";
	private String strVideoName = "";
	private String strAudioName = "";
	
	private View view; 
	public EditText textPhoto,textVideo,textAudio;
	private ImageButton btnPhotoAdd,btnVideoAdd,btnAudioAdd;
	private ImageButton btnPhotoTake,btnVideoTake,btnAudioTake;

	public MediaFragment() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.fragment_media, container, false);
		InitView();
		Log.e("fragment", "fragment created");
		return view;
	}
	private void InitView() {
		
		textPhoto = (EditText)view.findViewById(R.id.edit_pic);
		textVideo = (EditText)view.findViewById(R.id.edit_video);
		textAudio = (EditText)view.findViewById(R.id.edit_sound);
		
		btnPhotoAdd = (ImageButton)view.findViewById(R.id.img_btn_pic_add);
		btnPhotoTake = (ImageButton)view.findViewById(R.id.img_btn_pic_take);
		btnVideoAdd = (ImageButton)view.findViewById(R.id.img_btn_video_add);
		btnVideoTake = (ImageButton)view.findViewById(R.id.img_btn_video_take);
		btnAudioAdd = (ImageButton)view.findViewById(R.id.img_btn_sound_add);
		btnAudioTake= (ImageButton)view.findViewById(R.id.img_btn_sound_take);
		
		btnPhotoAdd.setOnClickListener(this);
		btnPhotoTake.setOnClickListener(this);
		btnAudioAdd.setOnClickListener(this);
		btnAudioTake.setOnClickListener(this);
		btnVideoAdd.setOnClickListener(this);
		btnVideoTake.setOnClickListener(this);
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
		}		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Cursor cursor;
		Uri uriAdded;
		
		getActivity();
		if(resultCode==Activity.RESULT_OK){
			switch (requestCode) {
			case REQUSET_CODE_PHOTO_ADD:
				
				uriAdded = data.getData();
				cursor = this.getActivity().getContentResolver().query(uriAdded, null, null, null, null);
				if(cursor.moveToNext()){
					//_data:对应文件绝对路径
					strPhotoPath = cursor.getString(cursor.getColumnIndex("_data"));
					strPhotoName = new File(strPhotoPath).getName();
					textPhoto.setText(strPhotoName);
				}
				
				break;
			case REQUSET_CODE_VIDEO_ADD:
				
				uriAdded = data.getData();
				cursor = this.getActivity().getContentResolver().query(uriAdded, null, null, null, null);
				if(cursor.moveToNext()){
					strVideoPath = cursor.getString(cursor.getColumnIndex("_data"));
					strVideoName = new File(strVideoPath).getName();
					textVideo.setText(strVideoName);
				}
				
				break;
			case REQUSET_CODE_AUDIO_ADD:
				
				uriAdded = data.getData();
				cursor = this.getActivity().getContentResolver().query(uriAdded, null, null, null, null);
				if(cursor.moveToNext()){
					strAudioPath = cursor.getString(cursor.getColumnIndex("_data"));
					strAudioName = new File(strAudioPath).getName();
					textAudio.setText(strAudioName);
				}
				
				break;
			case REQUSET_CODE_PHOTO_TAKE:
				
				textPhoto.setText(strPhotoName);
				
				break;
			case REQUSET_CODE_VIDEO_TAKE:
				
				textVideo.setText(strVideoName);
				
				break;
			case REQUSET_CODE_AUDIO_TAKE:
				
				strAudioPath = data.getStringExtra("audiopath");
				strAudioName = data.getStringExtra("audioname");
				textAudio.setText(strAudioName);
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
			strPhotoName = "img_"+(String) DateFormat.format("yyyyMMddhhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
			File outDir = new File(strPhotoPath);
			if(!outDir.exists()){
				outDir.mkdirs();
			}
			strPhotoPath += strPhotoName;
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
			strVideoName = "video_"+(String) DateFormat.format("yyyyMMddmmss",Calendar.getInstance(Locale.CHINA))+".3gp";
			strVideoPath += strVideoName;
			intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(strVideoPath)) );
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
			startActivityForResult(intent, REQUSET_CODE_VIDEO_TAKE);
			
			break;
		case REQUSET_CODE_AUDIO_TAKE:
			
            intent = new Intent(this.getActivity().getApplicationContext(),AudioActivity.class);
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

	public String getStrPhotoPath() {
		return strPhotoPath;
	}

	public void setStrPhotoPath(String strPhotoPath) {
		this.strPhotoPath = strPhotoPath;
	}

	public String getStrVideoPath() {
		return strVideoPath;
	}

	public void setStrVideoPath(String strVideoPath) {
		this.strVideoPath = strVideoPath;
	}

	public String getStrAudioPath() {
		return strAudioPath;
	}

	public void setStrAudioPath(String strAudioPath) {
		this.strAudioPath = strAudioPath;
	}

	public String getStrPhotoName() {
		return strPhotoName;
	}

	public void setStrPhotoName(String strPhotoName) {
		this.strPhotoName = strPhotoName;
	}

	public String getStrVideoName() {
		return strVideoName;
	}

	public void setStrVideoName(String strVideoName) {
		this.strVideoName = strVideoName;
	}

	public String getStrAudioName() {
		return strAudioName;
	}

	public void setStrAudioName(String strAudioName) {
		this.strAudioName = strAudioName;
	}	
	
}
