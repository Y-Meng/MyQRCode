package com.entity;

import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.Environment;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DBHelper extends OrmLiteSqliteOpenHelper {
	
	private static String PATH = Environment.getExternalStorageDirectory() + "/qrcode/"; 
	private final static String DATABASE_NAME= PATH+"data.db";
	private final static  int DATABASE_VERSION = 1;
	
	//数据访问接口
	private Dao<Task,Integer> TaskDao = null;
	private Dao<PatrolRecord,Integer> PatrolRecordDao = null;
	private Dao<LocationRecord,Integer> LocationRecordDao = null;
	private Dao<EmergencyRecord,Integer> EmergencyDao = null;
	private Dao<DownloadInfo,Integer> DownloadInfoDao = null;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public DBHelper(Context context, String databaseName,
			CursorFactory factory, int databaseVersion, int configFileId) {
		super(context, databaseName, factory, databaseVersion, configFileId);
		// TODO Auto-generated constructor stub
	}

	public DBHelper(Context context, String databaseName,
			CursorFactory factory, int databaseVersion, File configFile) {
		super(context, databaseName, factory, databaseVersion, configFile);
		// TODO Auto-generated constructor stub
	}

	public DBHelper(Context arg0, String arg1, CursorFactory arg2, int arg3,
			InputStream arg4) {
		super(arg0, arg1, arg2, arg3, arg4);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		 try {
			    //创建数据表
				TableUtils.createTable(arg1, Task.class);
				TableUtils.createTable(arg1, PatrolRecord.class);
				TableUtils.createTable(arg1, LocationRecord.class);
				TableUtils.createTable(arg1, EmergencyRecord.class);
			} catch (SQLException e) {
				Log.e(DATABASE_NAME, "无法打开数据库");
			}	
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}
	
	//获取数据访问对象
	public  Dao<Task,Integer> getTaskDao() throws SQLException {
				
		if(TaskDao==null)
 		TaskDao = super.getDao(Task.class);
		return TaskDao;
	}
	
	public Dao<PatrolRecord, Integer> getPatrolRecordDao() throws SQLException{
		if(PatrolRecordDao==null)
			PatrolRecordDao = super.getDao(PatrolRecord.class);
		return PatrolRecordDao;		
	}
	
	public Dao<LocationRecord,Integer> getLocationRecordDao() throws SQLException{
		if(LocationRecordDao==null)
			LocationRecordDao = super.getDao(LocationRecord.class);
		return LocationRecordDao;
	}
	
	public Dao<EmergencyRecord, Integer> getEmergencyDao() throws SQLException{
		if(EmergencyDao==null)
			EmergencyDao = super.getDao(EmergencyRecord.class);
		return EmergencyDao;
	}
	
	public Dao<DownloadInfo, Integer> getDownloadDao()throws SQLException{
		if(DownloadInfoDao==null)
			DownloadInfoDao = super.getDao(DownloadInfo.class);
		return DownloadInfoDao;
	}
	
	//插入测试数据
	public void InsertTestData(){
 
		try {
			//TableUtils.clearTable(getConnectionSource(), EmergencyRecord.class);
			//TableUtils.dropTable(getConnectionSource(), EmergencyRecord.class, true);
			//Log.e("testdata", "drop table");
			TableUtils.createTableIfNotExists(getConnectionSource(), EmergencyRecord.class);
			Log.e("testdata","create table");
			EmergencyDao = getEmergencyDao();
			for(int i = 0;i<10;i++)
			{
				EmergencyRecord record = new EmergencyRecord();
				record.setEmergencyID("10000000"+String.valueOf(i));
				record.setEmergencyDescription("上水井盖破损");
				record.setEmergencySource("巡查上报");
				record.setLocationDescription("丰庆华府");
				record.setCreateTime("2014-9-2 17:1"+String.valueOf(i));
				record.setIsDone(1);
				EmergencyDao.create(record);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
