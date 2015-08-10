package com.qrcode;

import java.sql.SQLException;
import java.util.ArrayList;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import com.config.services.MyApp;
import com.config.services.WebService;
import com.entity.DBHelper;
import com.entity.Task;
import com.entity.Tasks;
import com.j256.ormlite.dao.Dao;
import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PatrolTaskActivity extends Activity {
	
	private MyApp myapp;
	private MyAdapter mAdapter = null;
	private Context mContext;
	private Tasks tasks;
	
	private int position;
	private int viewMode = 1;
	private final int MENU_SUBMIT = Menu.FIRST;  
    private final int MENU_DELETE = Menu.FIRST+1;
	
	//异步任务线程
	TasksQuery mTasksQuery;
	ListView tasklistview;

	
	@SuppressLint("ShowToast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_query);
		
		mContext = getApplicationContext();		
		myapp=(MyApp)getApplication();
		String username=myapp.getname();
		setTitle("我的任务");
		tasks = new Tasks();
		tasklistview=(ListView)findViewById(R.id.tasklistView);
		registerForContextMenu(tasklistview);
		
		mTasksQuery = new TasksQuery(username);
		mTasksQuery.execute((Void)null);
        
         tasklistview.setOnItemClickListener(new OnItemClickListener() {
         	@Override
         	public void onItemClick(AdapterView<?> arg0, View view, int arg2,
 					long arg3) {
 				
         		Task task = tasks.getTasks().get(arg2);
         		myapp.setCurrentTaskDetail(task.getTaskDetail());
         		myapp.setCurrentTaskID(task.getId());
         		Bundle bundle = new Bundle();
         		bundle.putString("taskdetail", task.getTaskDetail());
 				Intent intent = new Intent(PatrolTaskActivity.this,MapActivity.class);
 				intent.setFlags(2);
 			    intent.putExtras(bundle);
 			    try {
 			    	startActivity(intent);
 	 				finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
 			} 
     	});
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.task_query, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_task_all:
			if(viewMode!=1){
				DBHelper dbHelper = new DBHelper(mContext);
				ArrayList<Task> mList = null;
				try {
					mList = (ArrayList<Task>) dbHelper.getTaskDao().queryForAll();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if(mList!=null){
					tasks.setTasks(mList);	
					mAdapter.notifyDataSetChanged();
					viewMode = 1;
				}
			}
			break;
		case R.id.menu_task_new:
			if(viewMode!=0){
				DBHelper dbHelper = new DBHelper(mContext);
				ArrayList<Task> mList = null;
				try {
					mList = (ArrayList<Task>) dbHelper.getTaskDao()
							.queryBuilder().where().eq("TaskStatus","new").query();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if(mList!=null){
					tasks.setTasks(mList);	
					mAdapter.notifyDataSetChanged();
					viewMode = 0;
				}
			}	
			Log.e("list", "changed");
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}


	//实现上下文菜单
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		switch(item.getItemId()){
		 
		case MENU_SUBMIT:  
            submitTask(position);  
            break;  
        case MENU_DELETE:  
            deleteTask(position);  
            break;  
        default:  
            break; 
		}
		
		return super.onContextItemSelected(item);
	}
	//生成上下文菜单
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		//注册菜单
		if(v==tasklistview)
			position = ((AdapterContextMenuInfo)menuInfo).position;
		
		menu.add(0,MENU_SUBMIT, 0, "提交");  
	    menu.add(0,MENU_DELETE, 0, "删除"); 
		
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	//提交完成任务
	private void deleteTask(int index) {
		
final int mIndex = index;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(PatrolTaskActivity.this);
		builder.setIcon(android.R.drawable.ic_dialog_alert)
		       .setTitle("提示")
		       .setMessage("确定要删除该任务？");
		builder.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Task task = (Task)tasklistview.getAdapter().getItem(mIndex);
				try {
					DBHelper mHelper = new DBHelper(mContext);
					Dao<Task, Integer> mDao = mHelper.getTaskDao();
					mDao.delete(task);
					tasks.setTasks((ArrayList<Task>)mDao.queryForAll());
					Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
					mAdapter = new MyAdapter(tasks);
					tasklistview.setAdapter(mAdapter);
					
				} catch (SQLException e) {
					e.printStackTrace();
					}
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
		
		
	}
    //删除完成任务
	private void submitTask(int index) {
		
		Task task = (Task)tasklistview.getAdapter().getItem(index);	
		task.setTaskStatus("done");
		try {
			Dao<Task, Integer> mDao =  new DBHelper(mContext).getDao(Task.class);
			mDao.update(task);
			tasks.setTasks((ArrayList<Task>)mDao.queryForAll());
			Toast.makeText(mContext, "提交成功", Toast.LENGTH_SHORT).show();
			mAdapter = new MyAdapter(tasks);
			tasklistview.setAdapter(mAdapter);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//need resume
		//new SubmitTask(task.getTaskID()).execute((Void)null);
	}

	class TasksQuery extends AsyncTask<Void, Void, Boolean>{
		 
		public TasksQuery(String username){
		
		}
		@Override
		protected Boolean doInBackground(Void... arg0) {
			/*--------------改从数据库查询----------
			SoapObject rpc = new SoapObject(WebService.NAMESPACE, WebService.MethodName_TaskQuery);
			rpc.addProperty("username", username);
			SoapSerializationEnvelope envelope =new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;  
			(new MarshalBase64()).register(envelope);
	        envelope.dotNet = true;  
	        HttpTransportSE transport = new HttpTransportSE(WebService.URL);  
	        transport.debug=true;
			try {  
	            // 调用WebService  
	            transport.call(WebService.SoapAction_TaskQuery, envelope);  
	            SoapObject object = (SoapObject)envelope.bodyIn;
	            String data = object.getProperty(0).toString().trim();
	            
	            Gson gson = new Gson();
	            if(!data.equals("null")){
	            	tasks = gson.fromJson(data, Tasks.class);
	                Log.e("gson", "gettasks");
	                return true;
	            }else{
	            	tasks = null;
	            	return false;
	            }
		   }catch (Exception e) {  
               e.printStackTrace();  
               return false;
           }  */
		   DBHelper mdDbHelper = new DBHelper(mContext);
		   try {
			Dao<Task, Integer> mDao = mdDbHelper.getTaskDao();
			if(mDao.queryForAll().isEmpty()){
				return false;
			}else{
				tasks.setTasks((ArrayList<Task>)mDao.queryForAll());
				return true;
			}
			
		   } catch (SQLException e) {
			e.printStackTrace();
			return false;
		   }
	    }
		@Override
		protected void onPostExecute(Boolean result) {
			if(result){	  
				mAdapter = new MyAdapter(tasks);
		        tasklistview.setAdapter(mAdapter);
			}else{
				Toast.makeText(mContext, "查询失败", Toast.LENGTH_LONG).show();
			}
			super.onPostExecute(result);
		}
	}
	
	class SubmitTask extends AsyncTask<Void, Void, Boolean>{
		
		String taskID;
		public SubmitTask(String str){
			taskID = str;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			submit(taskID);
			return null;
		}

		private void submit(String taskID) {
			 SoapObject request = new SoapObject(WebService.NAMESPACE, WebService.MethodName_TaskSubmit);
			 request.addProperty("taskID", taskID);
			 SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			 envelope.bodyOut = request;
			 envelope.dotNet = true;
			 new MarshalBase64().register(envelope);
			 HttpTransportSE transport = new HttpTransportSE(myapp.getURL());
			 transport.debug = true;
			 try {
				transport.call(WebService.SoapAction_TaskSubmit, envelope);
			 } catch (Exception e) {
				e.printStackTrace();
			 }
			 SoapObject response = new SoapObject();
			 response = (SoapObject)envelope.bodyIn;
			 if(response.getProperty(0).toString().equals("true")){
				 Log.e("task", "submit success");
			 }else{
				Log.e("task", "fail to submit");
			 }
		}
		
	}

	class MyAdapter extends BaseAdapter{
		private Tasks tasks;
		
		//重写构造函数
		public MyAdapter(ArrayList<String> taskIDList,ArrayList<String> taskTypeList,ArrayList<String> taskList,ArrayList<String> taskTimeList){
			super();
		
		}
       public MyAdapter(Tasks tasks)
       {
    	   this.tasks = tasks;
	   }
		@Override
		public int getCount() {
			 
			return tasks.getTasks().size();
		}

		@Override
		public Object getItem(int position) {
			 
			return tasks.getTasks().get(position);
		}

		@Override
		public long getItemId(int position) {
			 
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder; 
            
            if(convertView == null){  
                LayoutInflater mInflater = (LayoutInflater) mContext  
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
                
                convertView = mInflater.inflate(R.layout.taskquery_listitem, null);  
                
                holder = new ViewHolder();  
                holder.tv_taskID = (TextView)convertView.findViewById(R.id.taskTitle);  
                holder.tv_tasktype = (TextView)convertView.findViewById(R.id.taskType);
                holder.tv_task=(TextView)convertView.findViewById(R.id.task);
                holder.tv_time=(TextView)convertView.findViewById(R.id.taskData);
                holder.img_status = (ImageView)convertView.findViewById(R.id.img_taskstatus);
 
                convertView.setTag(holder);  
            }else{  
                holder = (ViewHolder)convertView.getTag();  
            }  
           
            Task task = tasks.getTasks().get(position);
            holder.tv_taskID.setText("任务编号:"+ task.getTaskID());  
            holder.tv_tasktype.setText(task.getTaskType()); 
            holder.tv_task.setText("  "+task.getDescription());
            holder.tv_time.setText("  "+task.getTime());
            
            if("new".equals(task.getTaskStatus())){
            	holder.img_status.setImageResource(R.drawable.new_task);
            }
              
            return convertView;  
		}
	}
	
    static class ViewHolder{  
        TextView tv_taskID;  
        TextView tv_tasktype;
        TextView tv_task;
        TextView tv_time;
        ImageView img_status;
    }
	
}
