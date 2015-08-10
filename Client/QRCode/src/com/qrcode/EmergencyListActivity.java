package com.qrcode;

import java.sql.SQLException;
import java.util.List;
import com.entity.DBHelper;
import com.entity.EmergencyRecord;
import com.j256.ormlite.dao.Dao;
import com.mcy.myviews.MyListView;
import com.mcy.myviews.MyListView.OnDeleteListener;
import com.qrcode.R;
import com.qrcode.EmergencyView.OnResultListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class EmergencyListActivity extends Activity implements OnDeleteListener,OnItemClickListener{
	
	private Context mContext;
	private DBHelper mHelper;
	private List<EmergencyRecord> mEmergencyRecords;
	private EmergencyRecord mEmergencyRecord;
	private MyListView myListView;
	private MyAdapter myAdapter;
	private int ViewMode = 1;
	private final int FLAG_EXCUTE = 1;
	private final int FLAG_VIEW = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emergeny_list);
		setTitle("应急列表");
		
		mContext = EmergencyListActivity.this;
		mHelper = new DBHelper(mContext);
		//插入测试数据
		//mHelper.InsertTestData();
		
		myListView = (MyListView)findViewById(R.id.myListView1);
		mEmergencyRecords = QueryALL();
		mHelper.close();
		if(mEmergencyRecords!=null){
			myAdapter = new MyAdapter(mEmergencyRecords);
			myListView.setAdapter(myAdapter);
		}
    	myListView.setOnDeleteListener(this);
		myListView.setOnItemClickListener(this);	
	}

	//查询所有记录
	private List<EmergencyRecord> QueryALL() {
		
		try {
			Dao<EmergencyRecord, Integer> mDao = mHelper.getEmergencyDao();
			return mDao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	//查询待处理记录
	private List<EmergencyRecord> QueryNew(){
		try {
			Dao<EmergencyRecord, Integer> mDao = mHelper.getEmergencyDao();
			return mDao.queryBuilder().where().eq("isDone", 0).query();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> av, View v, int position, long arg3) {
		mEmergencyRecord = mEmergencyRecords.get(position);
		final EmergencyView emergencyView = new EmergencyView(mContext,mEmergencyRecord);
		emergencyView.setOnResultListener(new OnResultListener() {
			
			@Override
			public void onResult(boolean result) {
				if(result==true){
					Intent intent = new Intent(EmergencyListActivity.this,EmergencyActivity.class);
					intent.putExtra("record", mEmergencyRecord);
					intent.setFlags(FLAG_EXCUTE);
					startActivity(intent);
				}else{
					Intent intent = new Intent(EmergencyListActivity.this,EmergencyActivity.class);
					intent.putExtra("record", mEmergencyRecord);
					intent.setFlags(FLAG_VIEW);
					startActivity(intent);
				}
			}
		});
		emergencyView.show();
	}

	@Override
	public void onDelete(int index) {

		mHelper = new DBHelper(mContext);
		try {
			mHelper.getEmergencyDao().delete(mEmergencyRecords.get(index));
			mEmergencyRecords.remove(index);
			myAdapter.notifyDataSetChanged();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		mHelper.close();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.emergeny_list, menu);
		return true;
	}

 

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.item_menu_all:
			if(ViewMode!=1){
				mHelper = new DBHelper(mContext);
				mEmergencyRecords = QueryALL();
				mHelper.close();
				if(mEmergencyRecords!=null)
				    myListView.setAdapter(new MyAdapter(mEmergencyRecords));
				ViewMode=1;
			}
			break;

		case R.id.item_menu_new:
			if(ViewMode!=0){
				mHelper = new DBHelper(mContext);
				mEmergencyRecords = QueryNew();
				mHelper.close();
				if(mEmergencyRecords!=null){
				    myListView.setAdapter(new MyAdapter(mEmergencyRecords));
				}else{
					Toast.makeText(mContext, "无待办任务", Toast.LENGTH_LONG).show();
				}
				ViewMode = 0;
			}
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}



	class MyAdapter extends  BaseAdapter{
		
		private List<EmergencyRecord> mRecords;
		
		public MyAdapter(List<EmergencyRecord> Records){
			super();
			this.mRecords = Records;
		}

		@Override
		public int getCount() {
			return mRecords.size();
		}

		@Override
		public Object getItem(int position) {
			return mRecords.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			ViewHolder mHolder = new ViewHolder();
			if(convertView==null){
				LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = mInflater.inflate(R.layout.item_list_emergency_record, null);
				mHolder.txtEmergencyID = (TextView)convertView.findViewById(R.id.item_txt_emergency_id);
				mHolder.txtEmergencySource = (TextView)convertView.findViewById(R.id.item_txt_emergency_source);
				mHolder.txtEmergencyTime = (TextView)convertView.findViewById(R.id.item_txt_emergency_time);
				mHolder.txtEmergencyDescription = (TextView)convertView.findViewById(R.id.item_txt_emergency_description);
				mHolder.txtEmergencyDone = (TextView)convertView.findViewById(R.id.item_txt_emergency_done);
				convertView.setTag(mHolder);
			}else{
				mHolder = (ViewHolder)convertView.getTag();
			}	
			EmergencyRecord record = mRecords.get(position);
			mHolder.txtEmergencyID.setText(String.valueOf(record.getId()));
			mHolder.txtEmergencySource.setText(record.getEmergencySource());
			mHolder.txtEmergencyTime.setText(record.getCreateTime());
			mHolder.txtEmergencyDescription.setText(record.getEmergencyDescription());
			if(record.getIsDone()==1){
				mHolder.txtEmergencyDone.setText("已处理");
			}else{
				mHolder.txtEmergencyDone.setText("未处理");
			}
			return convertView;
		}
		
	}
	
	static class ViewHolder{
		TextView txtEmergencyID;
		TextView txtEmergencySource;
		TextView txtEmergencyTime;
		TextView txtEmergencyDescription;
		TextView txtEmergencyDone;
	}


}
