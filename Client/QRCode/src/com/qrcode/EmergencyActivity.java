package com.qrcode;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.config.services.MyApp;
import com.config.services.WebService;
import com.entity.DBHelper;
import com.entity.EmergencyRecord;
import com.entity.EmergencyRequest;
import com.entity.EmergencyResult;
import com.entity.EmergencyResults;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.qrcode.R;
import com.zxing.activity.CaptureActivity;

public class EmergencyActivity extends Activity implements OnClickListener{

	public static final int TO_GET_QRCODE = 0;
	private EmergencyRequest mRequest;
	private String requestStr = "";
	
	private LinearLayout root;
	private CheckBox checkMap,checkHDM,checkZDM,check3D;
	private MyApp myapp;
	private TextView resultview,txtDetail;
	private Button btnSave,btnDataRequest;
	private ImageButton btnTakeQrcode;
	private ProgressBar pBarRequest;
	
	private View dataview1,dataview2;
	private View buttonView;
	private LinearLayout dataLayout;
	
	private EmergencyRecord mEmergencyRecord;
	private String strDataMapFileName = "";
	private String strDataMapSavePath = "";
	
	private String strDataHDMFileName = "";	
	private String strDataHDMSavePath = "";
	
	private String strDataZDMFileName = "";
	private String strDataZDMSavePath = "";
	
	private String strData3DFileName = "";
	private String strData3DSavePath = "";
	
	private boolean isSaved = false;
	private SharedPreferences mSP=null;
	private Context context = EmergencyActivity.this;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emergency);
		myapp = (MyApp)getApplication();
		
		Intent data = getIntent();
		mEmergencyRecord = (EmergencyRecord)data.getSerializableExtra("record");
		
		int flag = data.getFlags();
		
		pBarRequest = (ProgressBar)findViewById(R.id.pbar_data_request);
		root = (LinearLayout)findViewById(R.id.layout_root);
		dataLayout = (LinearLayout)findViewById(R.id.checkLayout);
		resultview = (TextView)findViewById(R.id.textresultview);
		txtDetail = (TextView)findViewById(R.id.txt_emergency_detail);
		txtDetail.setText(mEmergencyRecord.getEmergencyID()
				         +"\n"+mEmergencyRecord.getEmergencySource()
				         +"\n"+mEmergencyRecord.getLocationDescription()
				         +"\n"+mEmergencyRecord.getEmergencyDescription());
		
		dataview1 = findViewById(R.id.tableRow2);
		dataview2 = findViewById(R.id.tableRow4);
		buttonView = findViewById(R.id.btnView);
		checkMap = (CheckBox)findViewById(R.id.check_map);
		checkZDM = (CheckBox)findViewById(R.id.check_zdm);
		checkHDM = (CheckBox)findViewById(R.id.check_hdm);
		check3D = (CheckBox)findViewById(R.id.check_3d);
		
		String qrcode = myapp.getcode();
		if(!qrcode.isEmpty())
		{
			resultview.setText(qrcode);
		}
		
		btnDataRequest = (Button)findViewById(R.id.btn_data_request);
		btnDataRequest.setOnClickListener(this);
		btnTakeQrcode=(ImageButton)findViewById(R.id.takeQrcode);
		btnTakeQrcode.setOnClickListener(this);
		btnSave=(Button)findViewById(R.id.btnSave);
		btnSave.setOnClickListener(this);
		
		initView(flag);
		
		mSP = getPreferences(MODE_PRIVATE);
		registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}

	private void initView(int flag) {
 
		switch (flag) {
		case 1:
			viewExcuteView();
			break;
		case 0:
			viewNormalView();
			break;
		default:
			break;
		}
		
	}

	private void viewNormalView() {
		isSaved = true;
		buttonView.setVisibility(View.GONE);
		btnTakeQrcode.setVisibility(View.GONE);
		dataLayout.setVisibility(View.VISIBLE);
		TextView txtMAP = (TextView)findViewById(R.id.txtErMAP);
		if(!mEmergencyRecord.getData_map_file_name().equals("")){
			txtMAP.setText(mEmergencyRecord.getData_map_file_name()+"  >");
			strDataMapSavePath = mEmergencyRecord.getData_map_save_path();
			txtMAP.setOnClickListener(this);
		}
		
		TextView txtHDM = (TextView)findViewById(R.id.txtErHDM);
		if(!mEmergencyRecord.getData_hdm_file_name().equals("")){
			txtHDM.setText(mEmergencyRecord.getData_hdm_file_name()+"  >");
			strDataHDMSavePath = mEmergencyRecord.getData_hdm_save_path();
			txtHDM.setOnClickListener(this);
		}
		
		TextView txtZDM = (TextView)findViewById(R.id.txtErZDM);
		if(!mEmergencyRecord.getData_zdm_file_name().equals("")){
		    txtZDM.setText(mEmergencyRecord.getData_zdm_file_name()+"  >");
		    strDataZDMSavePath = mEmergencyRecord.getData_zdm_save_path();
			txtZDM.setOnClickListener(this);
		}
		
		TextView txt3D = (TextView)findViewById(R.id.txtEr3D);
		if(!mEmergencyRecord.getData_3d_file_name().equals("")){
			txt3D.setText(mEmergencyRecord.getData_3d_file_name()+"  >");
			strData3DSavePath = mEmergencyRecord.getData_3d_save_path();
			txt3D.setOnClickListener(this);
		}
	}

	private void viewExcuteView() {
		buttonView.setVisibility(View.GONE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (resultCode == Activity.RESULT_OK && requestCode== TO_GET_QRCODE ) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			//CSXG
			if(scanResult.equals(resultview.getText().toString())){
				myapp.setcode(scanResult);
				Toast.makeText(getApplicationContext(), "二维码匹配成功", Toast.LENGTH_LONG).show();
				dataview1.setVisibility(View.VISIBLE);
				dataview2.setVisibility(View.VISIBLE);
				buttonView.setVisibility(View.VISIBLE);
			}else{
				Toast.makeText(getApplicationContext(), "二维码不匹配请重新扫描", Toast.LENGTH_LONG).show();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.takeQrcode:
			if(resultview.getText().toString().equals(""))
			{
				Intent openCameraIntent = new Intent(EmergencyActivity.this,CaptureActivity.class);
    			startActivityForResult(openCameraIntent, TO_GET_QRCODE);
			}
			else{
			 AlertDialog.Builder qrbuilder = new Builder(EmergencyActivity.this); 
			 qrbuilder.setIcon(android.R.drawable.ic_dialog_info);
		        qrbuilder.setMessage("是否核对二维码?"); 
		        qrbuilder.setTitle("提示"); 
		        qrbuilder.setPositiveButton("核对", 
		                new android.content.DialogInterface.OnClickListener() { 
		                    public void onClick(DialogInterface dialog, int which) { 
		                        dialog.dismiss(); 
		                        Intent openCameraIntent = new Intent(EmergencyActivity.this,CaptureActivity.class);
		            			startActivityForResult(openCameraIntent, TO_GET_QRCODE);		            			
		                    } 
		                }); 
		        qrbuilder.setNegativeButton("跳过", 
		                new android.content.DialogInterface.OnClickListener() { 
		                    public void onClick(DialogInterface dialog, int which) { 
		                    	
		                    	//测试需求
		                    	dataview1.setVisibility(View.VISIBLE);
		                    	dataview2.setVisibility(View.VISIBLE);
		                    	buttonView.setVisibility(View.VISIBLE);
		                        dialog.dismiss(); 
		                    } 
		                }); 
		        		qrbuilder.create().show();
			}
		    break;

		case R.id.btnSave://保存应急记录
		
				long time = System.currentTimeMillis();
				String finishTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.CHINA).format(new Date(time));
				mEmergencyRecord.setFinishTime(finishTime);
				mEmergencyRecord.setIsDone(1);
                mEmergencyRecord.setData_map_file_name(strDataMapFileName);
                mEmergencyRecord.setData_map_save_path(strDataMapSavePath);
                
                mEmergencyRecord.setData_hdm_file_name(strDataHDMFileName);
                mEmergencyRecord.setData_hdm_save_path(strDataHDMSavePath);
                
                mEmergencyRecord.setData_zdm_file_name(strDataZDMFileName);
                mEmergencyRecord.setData_zdm_save_path(strDataZDMSavePath);
                
                mEmergencyRecord.setData_3d_file_name(strData3DFileName);
                mEmergencyRecord.setData_3d_save_path(strData3DSavePath);
                
				DBHelper mDbHelper = new DBHelper(EmergencyActivity.this);
				try {
					Dao<EmergencyRecord, Integer> mDao = mDbHelper.getEmergencyDao();
					mDao.update(mEmergencyRecord);
					isSaved = true;
					Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_LONG).show();
				} catch (SQLException e) {
					e.printStackTrace();
				}finally{
					mDbHelper.close();
				}	
			
			break;

		case R.id.btn_data_request:
			if(resultview.getText().toString().equals(""))
			{
				Toast.makeText(this, "请先扫描二维码信息", Toast.LENGTH_LONG).show();			
			}
			else 
			{
				pBarRequest.setVisibility(ProgressBar.VISIBLE);
				mRequest = new EmergencyRequest();
				if(checkMap.isChecked()){
					mRequest.setIsMAP(1);
				}
				
				if(checkZDM.isChecked()){
					mRequest.setIsZDM(1);
				}
				
				if(checkHDM.isChecked()){
			        mRequest.setIsHDM(1);
				}
				
				if(check3D.isChecked()){
		            mRequest.setIs3D(1);
				}

				mRequest.setMode(1);
	            mRequest.setX(mEmergencyRecord.getEmergencyLocationX());
	            mRequest.setY(mEmergencyRecord.getEmergencyLocationY());
	            mRequest.setQrCode(resultview.getText().toString());
	            mRequest.setBuffer(50);
	            
	            requestStr = new Gson().toJson(mRequest,EmergencyRequest.class);
				TaskRequestData mDataRequest = new TaskRequestData();
				mDataRequest.execute(requestStr);
				
			}
			break;
		case R.id.txtEr3D:
			tryOpenFile(strData3DSavePath);
			break;
		case R.id.txtErHDM:
			tryOpenFile(strDataHDMSavePath);
			break;
		case R.id.txtErMAP:
			tryOpenFile(strDataMapSavePath);
			break;
		case R.id.txtErZDM:
			tryOpenFile(strDataZDMSavePath);
			break;
		}
	}
	
	private void tryOpenFile(String strDataMapSavePath2) {

//		Toast.makeText(getApplicationContext(), "未发现可用程序", Toast.LENGTH_LONG).show();
		ImgView imgView = new ImgView(context, 
				Environment.getExternalStorageDirectory()+"/qrcode/download/zdm.png",true);
		imgView.show();
	}

	//动态添加数据信息显示View
	private void CreateDataItemView(EmergencyResult result,int id) {
		
		final EmergencyResult mResult = result;
		final String url = result.getFileURL();
		final int mid  = id;
		//填充布局并设置id
		LayoutInflater inflater = getLayoutInflater();
		RelativeLayout item = (RelativeLayout)inflater.inflate(R.layout.item_data_result, null);
		item.setId(mid);
		
		TextView datatype = (TextView)item.findViewById(R.id.txt_data_type);
		TextView dataname = (TextView)item.findViewById(R.id.txt_data_name);
		final Button btnDownload = (Button)item.findViewById(R.id.btn_data_download);
		final Button btnOpen = (Button)item.findViewById(R.id.btn_data_open);
		
		datatype.setText(mResult.getDataType());
		dataname.setText(mResult.getFileName());
		//调用系统下载功能，下载文件
		btnDownload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DownloadManager dm = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
				Uri uri = Uri.parse(url);
				Request request = new Request(uri);
				request.setAllowedNetworkTypes(Request.NETWORK_MOBILE|Request.NETWORK_WIFI);
				request.setNotificationVisibility(Request.VISIBILITY_VISIBLE);
				request.setVisibleInDownloadsUi(true);
				request.setDestinationInExternalPublicDir("/qrcode/download/", mResult.getFileName());
			    //记录下载编号ID，并与view id绑定
				long DL_ID = dm.enqueue(request);
			    mSP.edit().putInt(String.valueOf(DL_ID), mid).commit();
			    
			    Toast.makeText(getApplicationContext(), "文件已加入下载队列", Toast.LENGTH_SHORT).show();
			    checkDataType(mResult);
			    btnDownload.setVisibility(View.GONE);
//			    btnOpen.setVisibility(View.VISIBLE);
			}

			private void checkDataType(EmergencyResult result) {
				// TODO Auto-generated method stub
				String dataType = result.getDataType(); 
			    if(dataType.equals("MAP")){
			    	result.setDataType("基础平面图");
			    	strDataMapFileName = result.getFileName();
			    	strDataMapSavePath = Environment.getExternalStorageDirectory()
			    			+"\\qrcode\\download\\"+strDataMapFileName;
			    	
			    }else if(dataType.equals("ZDM")){
			    	result.setDataType("纵断面面图");
			    	strDataZDMFileName = result.getFileName();
			    	strDataZDMSavePath = Environment.getExternalStorageDirectory()
			    			+"\\qrcode\\download\\"+strDataZDMFileName;
			    	
			    }else if(dataType.equals("HDM")){
			    	result.setDataType("横断面面图");
			    	strDataHDMFileName = result.getFileName();
			    	strDataHDMSavePath = Environment.getExternalStorageDirectory()
			    			+"\\qrcode\\download\\"+strDataHDMFileName;
			    	
			    }else if(dataType.equals("3D")){
			    	result.setDataType("三维数据");
			    	strData3DFileName = result.getFileName();
			    	strData3DSavePath = Environment.getExternalStorageDirectory()
			    			+"\\qrcode\\download\\"+strData3DFileName;
			    }
			}
		});
		//打开文件
		btnOpen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				String path = Environment.getExternalStorageDirectory()+"/qrcode/download/"+mResult.getFileName();
//				if(new File(path).exists()){
//					Toast.makeText(getApplicationContext(), "未发现可用程序", Toast.LENGTH_SHORT).show();
//				}else{
//					Toast.makeText(getApplicationContext(), "下载尚未完成", Toast.LENGTH_SHORT).show();
//				}
//				Log.e("data", "try open" + mResult.getFileName());
				//test
                tryOpenFile("");
			}
		});
		root.addView(item);
	}
	
    class TaskRequestData extends AsyncTask<String, Void, String>{

	@Override
	protected String doInBackground(String... params) {
		
		return RequsetData(params[0]);
	}

	@Override
	protected void onPostExecute(String result) {
		
		HandleResult(result);
	}

	private String RequsetData(String requestStr) {
		Log.e("request", requestStr);
		String result = "";
		try {
			SoapObject request = new SoapObject(WebService.NAMESPACE,WebService.MethodName_RequestData);
			request.addProperty("requestJson",requestStr);
			 
			SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = request;
			envelope.dotNet = true;
			new MarshalBase64().register(envelope);
			
			HttpTransportSE transportSE = new HttpTransportSE(myapp.getURL());
			transportSE.debug = true;
			transportSE.call(WebService.SoapAction_RequsetData, envelope);
			SoapObject response = new SoapObject();
			response = (SoapObject)envelope.bodyIn;
			result = response.getPropertyAsString(0).trim();
			Log.e("data", result);
		} catch (Exception e) {
            Toast.makeText(EmergencyActivity.this, "请求错误", Toast.LENGTH_LONG).show();
            e.printStackTrace();
		}

		return result;
	}
    private void HandleResult(String result){
	   if(!"".equals(result)){
		   Log.e("dataresponse", result);
		   
		   try {
			   Gson gson = new Gson();
			   EmergencyResults results  = new EmergencyResults();
			   results=  gson.fromJson(result, EmergencyResults.class);
			   //Log.e("results",String.valueOf(null==results.getResults()));
			   int i=0;
			   for (EmergencyResult itemResult : results.getResults()) {
					
			    	//动态填充view
			    	CreateDataItemView(itemResult,i);
			    	i++;
			   }
		   } catch (Exception e) {
               e.printStackTrace();
		   }
		  }
	    dataview1.setVisibility(View.GONE);
	    dataview2.setVisibility(View.GONE);
		pBarRequest.setVisibility(ProgressBar.GONE);
	   }
     } 
    
    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(receiver);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK&&isSaved==false){
			Toast.makeText(getBaseContext(), "请先保存记录", Toast.LENGTH_SHORT).show();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	//声明用于接收下载状态信息的receiver
    private BroadcastReceiver receiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			long DL_ID= intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
			int viewID = mSP.getInt(String.valueOf(DL_ID), 5);
			View v = root.findViewById(viewID);
			if(v!=null){
				Button open = (Button) v.findViewById(R.id.btn_data_open);
				open.setVisibility(View.VISIBLE);
			}
		}
    };
   }

