package com.config.services;

import android.app.Application;

public class MyApp extends Application{
	
	private String userName="";
	private String qrcode="";
	//public static final String URL="http://127.0.0.1:1285/Service1.asmx";//虚拟机默认为自己地址
	//public static final String URL="http://10.0.2.2:60619/Service1.asmx";//虚拟机默认主机地址
	private String URL = "http://192.168.0.116:9998/Service1.asmx";//发布地址
	private String currentTaskDetail="";
	private int currentTaskID = -1;
	
	
	
	public int getCurrentTaskID() {
		return currentTaskID;
	}
	public void setCurrentTaskID(int currentTaskID) {
		this.currentTaskID = currentTaskID;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public String getname() {
		return userName;
	}
	public void setname(String name) {
		userName = name;
	}
	
	public String getcode() {
		return qrcode;
	}
	public void setcode(String code) {
		qrcode = code;
	}
	public String getCurrentTaskDetail() {
		return currentTaskDetail;
	}
	public void setCurrentTaskDetail(String currentTaskDetail) {
		this.currentTaskDetail = currentTaskDetail;
	}
	
}
