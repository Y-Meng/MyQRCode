package com.qrcode;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import com.config.services.MyApp;
import com.config.services.WebService;
import com.entity.DBHelper;
import com.entity.PatrolRecord;
import com.j256.ormlite.dao.Dao;
import com.mcy.media.MediaFragment;
import com.qrcode.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PatrolActivity extends Activity implements OnClickListener{


    private ProgressDialog progressDialog;
    private TextView txtTabPatrol,txtTabMedia;
	private Button save,upload;
	private FragmentManager fm;
	private FragmentTransaction ft;
	private MediaFragment mediaFragment;
	private PatrolFragment patrolFragment;
	
	private MyApp myapp;
	private DBHelper mDBHelper = null;
	private Dao<PatrolRecord, Integer> mPatrolDao = null;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/*StrictMode.setThreadPolicy(new StrictMode
		.ThreadPolicy.Builder()
		.detectDiskReads()
		.detectDiskWrites()
		.detectNetwork()
		.penaltyLog()
		.build());
		--------在主线程中访问网络的声明--------------*/
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patrol);
		InitView();
		Log.e("patrol", "activity created");
	}
	private void InitView() {
		myapp=(MyApp)getApplication();
		fm = getFragmentManager();
		patrolFragment = (PatrolFragment)fm.findFragmentById(R.id.patrol_patrol);
		mediaFragment =  (MediaFragment)fm.findFragmentById(R.id.patrol_media);
		txtTabPatrol = (TextView)findViewById(R.id.txt_tab_patrol);
		txtTabPatrol.setBackgroundColor(getResources().getColor(R.color.silver_gray));
		txtTabMedia = (TextView)findViewById(R.id.txt_tab_media);
		ft = fm.beginTransaction();
		ft.show(patrolFragment).hide(mediaFragment);
		ft.commit();
		
		save = (Button)findViewById(R.id.btn_save);
		upload=(Button)findViewById(R.id.btn_uploading);
		
		String qrcode=myapp.getcode();
		if(!qrcode.isEmpty())
		{
			patrolFragment.txtResult.setText(qrcode);
		}			

		save.setOnClickListener(this);
		upload.setOnClickListener(this);
		txtTabMedia.setOnClickListener(this);
		txtTabPatrol.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_uploading:
			if(patrolFragment.txtResult.getText().toString().equals(""))
			{
				Toast.makeText(this, "请先扫描二维码获得信息", Toast.LENGTH_LONG).show();			
			}
			else 
			{
				progressDialog = new ProgressDialog(PatrolActivity.this);
				progressDialog.setTitle("正在上报，请稍后...");
				progressDialog.show();
				SaveRecord(1);
				UploadTask mUploadTask = new UploadTask();
				mUploadTask.execute((Void)null);
			}
			break;
		case R.id.btn_save:
			if(patrolFragment.txtResult.getText().toString().equals(""))
			{
				Toast.makeText(this, "请先扫描二维码获得信息", Toast.LENGTH_LONG).show();			
			}else{
				SaveRecord(0);
				ResetStatus();
			}

			break;
		case R.id.txt_tab_patrol:
			ft = fm.beginTransaction();
			ft.hide(mediaFragment).show(patrolFragment);
			txtTabMedia.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
			txtTabPatrol.setBackgroundColor(getResources().getColor(R.color.silver_gray));
			ft.commit();
			break;
		case R.id.txt_tab_media:
			ft = fm.beginTransaction();
			ft.hide(patrolFragment).show(mediaFragment);
			txtTabPatrol.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
			txtTabMedia.setBackgroundColor(getResources().getColor(R.color.silver_gray));
			ft.commit();
			break;
		}
	}
	
    private void SaveRecord(int isSubmit) {
        PatrolRecord patrolRecord = new PatrolRecord();
        //添加记录详情
        patrolRecord.setUserName(myapp.getname());
        patrolRecord.setQrcode(patrolFragment.txtResult.getText().toString());
        patrolRecord.setStatus(patrolFragment.spinStatus.getSelectedItem().toString());
        patrolRecord.setDetail(patrolFragment.editDescription.getText().toString());
        patrolRecord.setAudioName(mediaFragment.getStrAudioName());
        patrolRecord.setImgName(mediaFragment.getStrPhotoName());
        patrolRecord.setVideoName(mediaFragment.getStrVideoName());
        patrolRecord.setLocationX(patrolFragment.X);
        patrolRecord.setLocationY(patrolFragment.Y);
        String time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA).format(System.currentTimeMillis());
        patrolRecord.setCreateTime(time);
        switch (isSubmit) {
		case 1:
	        patrolRecord.setIsSubmit(true);
			break;
		case 0:
	        patrolRecord.setIsSubmit(false);
			break;
		default:
			break;
		}

        if(mDBHelper==null)
            mDBHelper = new DBHelper(getApplicationContext());
        if(mPatrolDao==null){
			try {
				mPatrolDao = mDBHelper.getPatrolRecordDao();
				mPatrolDao.create(patrolRecord);
				Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
			} catch (SQLException e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_SHORT).show();
			}
        }
        mDBHelper=null;
        mPatrolDao=null;	
	}

	class UploadTask extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected Boolean doInBackground(Void... params) {
		   return Upload();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if(result){
				ResetStatus();
				progressDialog.dismiss();
				//Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_LONG).show();
			}else{
                ResetStatus();
                progressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "上传失败，请重新上传", Toast.LENGTH_LONG).show();
			}
			super.onPostExecute(result);
		}
    } 
	private void ResetStatus() {

		mediaFragment.setStrAudioPath("");
		mediaFragment.setStrPhotoPath("");
		mediaFragment.setStrVideoPath("");
		mediaFragment.setStrAudioName("");
		mediaFragment.setStrPhotoName("");
		mediaFragment.setStrVideoName("");
		mediaFragment.textAudio.setText("");
		mediaFragment.textPhoto.setText("");
		mediaFragment.textVideo.setText("");

		patrolFragment.txtResult.setText("");
        patrolFragment.editDescription.setText("");
	}
   
	private Boolean Upload() {
	
		/* 参数：
         string username
         string qrcode
         string status
         string detail
         string picname
         string b64strPic
         string videoname
         byte[] video
         string audioname
         byte[] audio
		*/
			String username=myapp.getname();
			//String qrcode=resultview.getText().toString().split(",")[0].split(":")[1];
			String qrcode = patrolFragment.txtResult.getText().toString();
			String status = patrolFragment.spinStatus.getSelectedItem().toString();
			String detial = patrolFragment.editDescription.getText().toString();
            String imgB64 = "";
            byte[] byteAudio = new byte[0];
            byte[] byteVideo = new byte[0];
            try{
            	//照片
            	if(!mediaFragment.getStrPhotoPath().equals("")){
        			FileInputStream fis=new FileInputStream(mediaFragment.getStrPhotoPath());
        			ByteArrayOutputStream baos=new ByteArrayOutputStream();
        			int count = 0;
           			byte[] buffer = new byte[8129];
        			while((count = fis.read(buffer))>=0){
        				baos.write(buffer, 0, count);
        			}
        			imgB64 = new String(Base64.encode(baos.toByteArray())); //进行Base64编码 
        			fis.close();
            	}

    			//录像
    			if(!mediaFragment.getStrVideoPath().equals("")){
    				FileInputStream fisVideo = new FileInputStream(mediaFragment.getStrVideoPath());
    				ByteArrayOutputStream baosVideo = new ByteArrayOutputStream();
    				byte[] buffer = new byte[1024];
    				int count = 0;
    				while((count = fisVideo.read(buffer))>=0){
    					baosVideo.write(buffer,0,count);
    				}
    				byteVideo = baosVideo.toByteArray();
    				fisVideo.close();
    			}else{
    				byteVideo = null;
    			}
    			
    			//录音
    			if(!mediaFragment.getStrAudioPath().equals("")){
    				FileInputStream fisAudio = new FileInputStream(mediaFragment.getStrAudioPath());
    				ByteArrayOutputStream baosAudio = new ByteArrayOutputStream();
    				byte[] buffer = new byte[1024];
    				int count = 0;
    				while((count = fisAudio.read(buffer))>=0){
    					baosAudio.write(buffer,0,count);
    				}
    				byteVideo = baosAudio.toByteArray();
    				fisAudio.close();
    			}else{
    				byteAudio = null;
    			}
    			
            }catch(Exception e){
    			e.printStackTrace();
    		}
    		
			SoapObject rpc = new SoapObject(WebService.NAMESPACE, WebService.MethodName_Upload);
			rpc.addProperty("username", username);
			rpc.addProperty("qrcode", qrcode);
			rpc.addProperty("status",status);
			rpc.addProperty("detial",detial);
			rpc.addProperty("picname",mediaFragment.getStrPhotoName());
			rpc.addProperty("b64strPic",imgB64);
			rpc.addProperty("videoname",mediaFragment.getStrVideoName());
			rpc.addProperty("video",byteVideo);
			rpc.addProperty("audioname",mediaFragment.getStrAudioName());
			rpc.addProperty("audio",byteAudio);
			
			SoapSerializationEnvelope envelope =new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;  
			(new MarshalBase64()).register(envelope);
	        envelope.dotNet = true;  
	  
	        HttpTransportSE transport = new HttpTransportSE(myapp.getURL());  
	        transport.debug=true;
	        try {  
	        	
	            transport.call(WebService.SoapAction_Upload, envelope); 
	            
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        if(envelope.bodyIn instanceof SoapFault){
	        	Log.e("soap", "false");
	        	return false;
	        }else{
		        SoapObject object = (SoapObject)envelope.bodyIn;
		        String result = object.getProperty(0).toString();
				if(result.equals("true"))
				{
					return true;
				}else {
					return false;
				}
	        }
		}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.patrol, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_view_patrolrecord:
	        startActivity(new Intent(PatrolActivity.this,PatrolListActivity.class));			
			break;
		default:
			break;
		}

		return true;
	}
	
	//-------------------------分割线-------------------------------------
	
	/*
	private boolean connectWebService(String imageBuffer) {
	    
    	SoapObject soapObject = new SoapObject(WebService.NAMESPACE, WebService.MethodName_UploadImg);             
        soapObject.addProperty("bytestr", imageBuffer);
    	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);   
    	envelope.setOutputSoapObject(soapObject);   
    	envelope.bodyOut = soapObject;   
    	(new MarshalBase64()).register(envelope);
    	envelope.dotNet = true;   
    	envelope.encodingStyle = SoapSerializationEnvelope.ENC;
    	HttpTransportSE httpTranstation = new HttpTransportSE(WebService.URL);   
        try {
        	httpTranstation.call(WebService.SoapAction_UploadImg, envelope);
        	Object result = envelope.getResponse();
        	Log.i("connectWebService", result.toString());
        	if(result.toString()=="文件上传成功")
        		return true;
        	else
        		return false;
        } catch (Exception e) {
        	e.printStackTrace();
        	return false;
        }   
	}
	*/
	/*	
	private void UploadImage() {
		 
		try{
			FileInputStream fis=new FileInputStream(picPath);
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			byte[] buffer = new byte[8129];
			int count = 0;
			while((count = fis.read(buffer))>=0){
				baos.write(buffer, 0, count);
			}
			String uploadBuffer = new String(Base64.encode(baos.toByteArray())); //进行Base64编码 
			connectWebService(uploadBuffer);//调用webservice
			Log.i("connectWebService","start");
			fis.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	*/
	
//	public static void word(Context context, File file, CheckInfo info) {
//		try {
//		//模板文件
//		    InputStream tempInputStream = context.getAssets().open("QR.dot");
//		        POIFSFileSystem fs = new POIFSFileSystem(tempInputStream);
//		    HWPFDocument doc = new HWPFDocument(fs);
//		    Range range = doc.getRange();
//		       for (int i = 0; i < range.numCharacterRuns(); i++) {
//		               CharacterRun run = range.getCharacterRun(i);
//		        String text = run.text();
//		             //替换自定义标记
//		        text = replaceText(text, info);
//		        run.replaceText(text, true);
//		    }
//		    doc.write(new FileOutputStream(file));
//		} catch (Exception e) {
//		    Log.d(TAG, "", e);
//		}
//	}
//	private static String replaceText(String text,CheckInfo info){
//		if(null == text || "".equals(text)){
//			return text;
//		}
//		if(text.contains("##A##")){
//			return info.getCompanyName();
//		}else{
//			return text;
//		}		
//	}
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//		if(resultCode==Activity.RESULT_OK && requestCode == TO_SELECT_PHOTO){
//			//imageView不设null, 第一次上传成功后，第二次在选择上传的时候会报错。
//			imageView.setImageBitmap(null);
//			picPath = data.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);
//			Log.i(TAG, "最终选择的图片="+picPath);
////			txt.setText("文件路径"+picPath);
//			Bitmap bm = BitmapFactory.decodeFile(picPath);
//			imageView.setImageBitmap(bm);
//	}
//		super.onActivityResult(requestCode, resultCode, data);
//	}
}
