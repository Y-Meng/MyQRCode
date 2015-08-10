package com.qrcode;

import java.sql.SQLException;
import java.util.List;
import com.entity.DBHelper;
import com.entity.PatrolRecord;
import com.j256.ormlite.dao.Dao;
import com.mcy.myviews.MyListView;
import com.mcy.myviews.MyListView.OnDeleteListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PatrolListActivity extends Activity {

	private MyListView listView;
	private List<PatrolRecord> records;
	private Dao<PatrolRecord, Integer> mDao;
	private DBHelper mHelper;
	private Context context;
	private MyAdapter myAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patrol_list);
		setTitle("巡查记录");
		context = this;
		mHelper = new DBHelper(context);
		listView = (MyListView)findViewById(R.id.list_patrol_records);
		listView.setOnDeleteListener(new OnDeleteListener() {
			
			@Override
			public void onDelete(int index) {
				// TODO Auto-generated method stub
				try {
					mHelper.getPatrolRecordDao().delete(records.get(index));
					records.remove(index);
					myAdapter.notifyDataSetChanged();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		
		try {
			mDao = mHelper.getPatrolRecordDao();
			records = mDao.queryForAll();
			myAdapter = new MyAdapter(records);
			listView.setAdapter(myAdapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View v,
						int index, long arg3) {
					// TODO Auto-generated method stub
					PatrolRecord record = records.get(index);
					new PatrolView(context, record).show();
				}
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.patrol_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_view_all:
			try {
				mDao = mHelper.getPatrolRecordDao();
				records = mDao.queryForAll();
				myAdapter = new MyAdapter(records);
				listView.setAdapter(myAdapter);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case R.id.menu_view_submit:
			try {
				mDao = mHelper.getPatrolRecordDao();
				records = mDao.queryBuilder().where().eq("IsSubmit", true).query();
				if(null!=records){
					myAdapter = new MyAdapter(records);
					listView.setAdapter(myAdapter);
				}
				    
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mHelper.close();
	}
	
  class MyAdapter extends  BaseAdapter{
		
		private List<PatrolRecord> mRecords;
		
		public MyAdapter(List<PatrolRecord> Records){
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
				LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = mInflater.inflate(R.layout.item_list_patrol_record, null);
				mHolder.txtPatrolUser = (TextView)convertView.findViewById(R.id.txt_patrol_user);
				mHolder.txtPatrolTime = (TextView)convertView.findViewById(R.id.txt_patrol_time);
				mHolder.txtPatrolQrcode = (TextView)convertView.findViewById(R.id.txt_patrol_qrcode);
				mHolder.txtPatrolSubmit = (TextView)convertView.findViewById(R.id.txt_patrol_submit);
				convertView.setTag(mHolder);
			}else{
				mHolder = (ViewHolder)convertView.getTag();
			}	
			PatrolRecord record = mRecords.get(position);
			mHolder.txtPatrolUser.setText(String.valueOf(record.getUserName()));
			mHolder.txtPatrolTime.setText(record.getCreateTime());
			mHolder.txtPatrolQrcode.setText(record.getQrcode());
			if(record.isIsSubmit()==true){
				mHolder.txtPatrolSubmit.setText("已上报");
			}else{
				mHolder.txtPatrolSubmit.setText("");
			}
			return convertView;
		}
		
	}
	
	static class ViewHolder{
		TextView txtPatrolUser;
		TextView txtPatrolTime;
		TextView txtPatrolQrcode;
		TextView txtPatrolSubmit;
	}

}
