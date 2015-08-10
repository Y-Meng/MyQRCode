package com.qrcode.query;

import java.sql.SQLException;
import java.util.TimerTask;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.config.services.MyApp;
import com.config.services.WebService;
import com.entity.DBHelper;
import com.entity.EmergencyRecord;
import com.entity.Task;
import com.entity.Tasks;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;

public class QueryTask extends TimerTask {
	
    private MyApp myApp;
    private Handler handler;
    private Tasks mTasks;
    private DBHelper mDbHelper;
    private Dao<Task, Integer> mTaskDao = null;
    private Dao<EmergencyRecord, Integer> mEmergencyDao = null;
    
	public QueryTask(MyApp myApp,Handler handler) {
		 mDbHelper = new DBHelper(myApp);
		 this.myApp = myApp;
		 this.handler = handler;
	}

	@Override
	public void run() {
		
		//------------查询网络任务----------------------
		String username=myApp.getname();
		SoapObject rpc = new SoapObject(WebService.NAMESPACE, WebService.MethodName_TaskQuery);
		rpc.addProperty("username", username);
		SoapSerializationEnvelope envelope =new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = rpc;  
		(new MarshalBase64()).register(envelope);
        envelope.dotNet = true;  
        HttpTransportSE transport = new HttpTransportSE(myApp.getURL());  
        transport.debug=true;
		try {  
            // 调用WebService  
            transport.call(WebService.SoapAction_TaskQuery, envelope);  
            SoapObject object = (SoapObject)envelope.bodyIn;
            String data = object.getProperty(0).toString().trim();
            Gson gson = new Gson();
            if(!data.equals("null")){
            	mTasks = gson.fromJson(data, Tasks.class);
            	//将查询到任务插入数据库
            	//---测试需要先注释-----
            	mTaskDao = mDbHelper.getTaskDao();
            	for (Task task  : mTasks.getTasks()){
					mTaskDao.create(task);
				}
				
            	handler.sendEmptyMessage(1);
                Log.e("gson", "gettasks");
            }else{
            	
            	mTasks = null;
            	Log.e("taskquery", "无新任务！");
            }
 
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
		//-------------------查询本地任务记录统计------------------
        try {
			mEmergencyDao = mDbHelper.getEmergencyDao();
			int emergencyNum = mEmergencyDao.queryForAll().size();
			int patrolNum = mTaskDao.queryForAll().size();
			Message msg = new Message();
			msg.what = 0;
			msg.arg1 = patrolNum;
			msg.arg2 = emergencyNum;
			handler.sendMessage(msg);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
