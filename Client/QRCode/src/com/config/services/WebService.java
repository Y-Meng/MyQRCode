package com.config.services;

import android.os.Environment;

public class WebService {
	
	//地图服务
    public static final String LYBASE = "http://192.168.0.116:6080/arcgis/rest/services/qrmap/LYBASE/MapServer";
    public static final String LYJG = "http://192.168.0.116:6080/arcgis/rest/services/qrmap/LYJG/MapServer";
	public static final String TPK = Environment.getExternalStorageDirectory() + "/qrcode/map/Layers.tpk";
	public static final String GDB= Environment.getExternalStorageDirectory()+"/qrcode/map/luoyang.geodatabase";
	
	//public static final String NAMESPACE="http://qrcode.basicservice/";
	public static final String NAMESPACE="http://tempuri.org/";
	
	//登录
	public static final String MethodName_Login = "UserLogin";
	public static final String SoapAction_Login = NAMESPACE+MethodName_Login;
	//退出登录
	public static final String MethodName_Exit = "UserExit";
	public static final String SoapAction_Exit = NAMESPACE+MethodName_Exit;
	//上报巡查
	public static final String MethodName_Upload = "EventSubmit";
	public static final String SoapAction_Upload =  NAMESPACE+MethodName_Upload;
	//上传定位信息
	public static final String MethodName_Location = "LocationTrace";
	public static final String SoapAction_Location = NAMESPACE+MethodName_Location;
	//任务查询
	public static final String MethodName_TaskQuery = "TaskQuery"; 
	public static final String SoapAction_TaskQuery = NAMESPACE+MethodName_TaskQuery;
	//任务提交
	public static final String MethodName_TaskSubmit = "TaskSubmit"; 
	public static final String SoapAction_TaskSubmit = NAMESPACE+MethodName_TaskSubmit;
	//上传照片
	public static final String MethodName_UploadImg = "UploadImg";
	public static final String SoapAction_UploadImg = NAMESPACE+MethodName_UploadImg;
	//请求数据
	public static final String MethodName_RequestData = "RequestData";
	public static final String SoapAction_RequsetData = NAMESPACE+MethodName_RequestData;
}
