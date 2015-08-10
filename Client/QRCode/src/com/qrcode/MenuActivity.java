package com.qrcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import com.config.services.GPSService;
import com.config.services.GPSsetActivity;
import com.config.services.MyApp;
import com.config.services.WebService;
import com.mcy.media.MediaActivity;
import com.qrcode.query.NewTaskNotifier;
import com.qrcode.query.QueryTask;
import com.zxing.activity.CaptureActivity;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends Activity {
	
	private String texts[] = null;
    private int images[] = null;
    private MyApp myapp;
    private long exitTime = 0;
    private Location loc = null;
    
    private Timer timer ;
	private QueryTask querytask ;
	//handler应声明为静态的，否则会提示泄露
	private Handler handler;
	private MyReceiver receiver;
	
	private TextView txtPatrolNum,txtEmergencyNum;
	private GridView gridview;
	
	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		myapp=(MyApp)getApplication();
		images=new int[]{
				R.drawable.task, 
				R.drawable.code,
                R.drawable.patrol, 
                R.drawable.emergency, 
                R.drawable.map,
                R.drawable.gps,
                R.drawable.media,
                R.drawable.exit
                };
        texts = new String[]{ 
        		"我的任务",
        		"扫描二维码",
                "巡查",
                "应急", 
                "地图浏览",
                "跟踪设置",
                "多媒体",
                "退出"};
        
        txtPatrolNum = (TextView)findViewById(R.id.menu_txt_patrol);
        txtEmergencyNum = (TextView)findViewById(R.id.menu_txt_emergency);
        gridview= (GridView) findViewById(R.id.gridview);
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < images.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("itemImage", images[i]);
            map.put("itemText", texts[i]);
            lstImageItem.add(map);
        }
        
        SimpleAdapter saImageItems = new SimpleAdapter(this, 
                lstImageItem,// 数据源
                R.layout.item,// 显示布局
                new String[] { "itemImage", "itemText" }, 
                new int[] { R.id.itemImage, R.id.itemText }); 
        gridview.setAdapter(saImageItems);
        gridview.setOnItemClickListener((OnItemClickListener) new ItemClickListener());
        
        //允许在主线程中进行网络访问声明
        /* if (Build.VERSION.SDK_INT >= 11) {
        	StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads  ().detectDiskWrites().detectNetwork().penaltyLog().build());
        	StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        	}
        */ 
        //注册广播接收器
        receiver = new MyReceiver();
        registerReceiver(receiver, new IntentFilter("locationuser"));
        //启动轮询任务并处理结果
        handler = new Handler(){
            /*----------防止泄露的方法是增加弱引用，但这里不改变UI，直接放弃检查即可--------------
        	WeakReference<MenuActivity> refActivity =  new WeakReference<MenuActivity>(MenuActivity.this);
        	MenuActivity aggrieve = refActivity.get();
        	----------------------------------*/
			@Override
			public void handleMessage(Message msg) {
				
				if(msg.what==1){
					//显示推送提醒
					NewTaskNotifier taskNotifier = new NewTaskNotifier(getApplicationContext());
					taskNotifier.taskNotify("新任务", "请及时查看！");
					//反馈位置信息
					if(loc!=null){
						UploadLocation();
						Log.e("location", "submit");
					}else{
						Log.e("location", "null");
					}
				}else{
					txtPatrolNum.setText(String.valueOf(msg.arg1));
					txtEmergencyNum.setText(String.valueOf(msg.arg2));
				}
				super.handleMessage(msg);
			}
        };
        querytask = new QueryTask(myapp, handler);
        timer = new Timer();
        SharedPreferences sp = getSharedPreferences("UserInfo", MODE_PRIVATE);
        int period = sp.getInt("period", 5);
        timer.schedule(querytask, 0, period*60*1000);    
	}
	
	class ItemClickListener implements OnItemClickListener {
        /**
         * 点击项时触发事件
         * 
         * @param parent  发生点击动作的AdapterView
         * @param view 在AdapterView中被点击的视图(它是由adapter提供的一个视图)。
         * @param position 视图在adapter中的位置。
         * @param rowid 被点击元素的行id。
         */
        public void onItemClick(AdapterView<?> parent, View view, int position, long rowid) {
            
            switch (images[position]) {
            case R.drawable.task:
            	startActivity(new Intent(MenuActivity.this, PatrolTaskActivity.class));
                break;
            case R.drawable.code:
            	Intent openCameraIntent = new Intent(MenuActivity.this,CaptureActivity.class);
    			startActivityForResult(openCameraIntent, 0);
    			break;
            case R.drawable.patrol:
                startActivity(new Intent(MenuActivity.this, PatrolActivity.class)); 
                break;
            case R.drawable.emergency:
                startActivity(new Intent(MenuActivity.this, EmergencyListActivity.class));
                break;
            case R.drawable.map:
            	Intent intent = new Intent(MenuActivity.this, MapActivity.class);
            	intent.setFlags(0);
                startActivity(intent);
                break;
            case R.drawable.gps:
            	startActivity(new Intent(MenuActivity.this,GPSsetActivity.class));
            	break;
            case R.drawable.media:
            	startActivity(new Intent(MenuActivity.this,MediaActivity.class));
            	break;
            case R.drawable.exit:
            	AlertDialog.Builder builder = new Builder(MenuActivity.this); 
   			    builder.setIcon(android.R.drawable.ic_dialog_info);
   		        builder.setMessage("确定要退出?"); 
   		        builder.setTitle("提示"); 
   		        builder.setPositiveButton("确认", 
   		                new android.content.DialogInterface.OnClickListener() { 
   		                    public void onClick(DialogInterface dialog, int which) { 
   		                     new TaskCheckout().execute((Void)null);
   		                     dialog.dismiss(); 
   		                     Intent name = new Intent(MenuActivity.this,GPSService.class);
   		      			     stopService(name);
   		      			     timer.cancel();
   		      			     myapp.setname("");
		                     myapp.setcode(""); 
   		                     MenuActivity.this.finish(); 
   		                    }
   		                }); 
   		        builder.setNegativeButton("取消", 
   		                new android.content.DialogInterface.OnClickListener() { 
   		                    public void onClick(DialogInterface dialog, int which) { 
   		                        dialog.dismiss(); 
   		                    } 
   		                }); 
   		        		builder.create().show();
   		        break;
            }
            
        }
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			myapp.setcode(scanResult);
			startActivity(new Intent(MenuActivity.this,PatrolActivity.class));
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
	 
	}
	
	private void exit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
        	new TaskCheckout().execute((Void)null);
        	Intent name = new Intent(MenuActivity.this,GPSService.class);
			stopService(name);
			timer.cancel();
            finish();
            System.exit(0);
        }
		
	}
	
	//用户检查退出
	class TaskCheckout extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected Boolean doInBackground(Void... params) {
			checkout();
			return null;
		}
		
		private void checkout() {
			
			SoapObject requst = new SoapObject(WebService.NAMESPACE,WebService.MethodName_Exit);
			requst.addProperty("username",myapp.getname());
			Log.e("user", myapp.getname());
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = requst;
			envelope.dotNet = true;
			new MarshalBase64().register(envelope);
			HttpTransportSE transport = new HttpTransportSE(myapp.getURL());
			transport.debug = true;
			try {
				transport.call(WebService.SoapAction_Exit, envelope);
				Log.e("user", "exit");
			} catch (Exception e) {
			    e.printStackTrace();
			}
			SoapObject response = (SoapObject)envelope.bodyIn;
			Log.e("exit", response.getPropertyAsString(0));
		}
		
	}	

	//上传定位信息
	private void UploadLocation() {
		String username = myapp.getname(); 
		SoapObject request = new SoapObject(WebService.NAMESPACE,WebService.MethodName_Location);
		request.addProperty("latitude",String.valueOf(loc.getLatitude()));
		request.addProperty("longitude", String.valueOf(loc.getLongitude()));
		request.addProperty("username",username);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyIn = request;
		envelope.dotNet = true;
		//注册envelope
		new MarshalBase64().register(envelope);
		HttpTransportSE httpCilent = new HttpTransportSE(myapp.getURL());
		httpCilent.debug = true;
		try {
			httpCilent.call(WebService.SoapAction_Location, envelope);
			SoapObject response = new SoapObject();
			response = (SoapObject)envelope.bodyOut;
			String result = response.getPropertyAsString(0);
			if(result=="true"){
				Log.e("loadlocation", "success");
			}else{
				Log.e("loadlocation", "fail");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	class MyReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			 
			Log.e("gps", "receive cast");
			loc = (Location) intent.getParcelableExtra("location");
			if(loc==null){
				Log.e("gps", "no locatin");
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(receiver);
		super.onDestroy();
	} 
	
}
