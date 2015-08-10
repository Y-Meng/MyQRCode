package com.entity;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

public class PatrolRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@DatabaseField(generatedId = true)
	int id;
	@DatabaseField
	int taskID;
	@DatabaseField
	String UserName;
	@DatabaseField
	String Qrcode;
	@DatabaseField
	String Status;
	@DatabaseField
	String Detail;
	@DatabaseField
	String ImgName;
	@DatabaseField
	String AudioName;
	@DatabaseField
	String VideoName;
	@DatabaseField
	String CreateTime;
	@DatabaseField
	boolean IsSubmit;
	@DatabaseField
	double LocationX;
	@DatabaseField
	double LocationY;
	
	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public PatrolRecord() {
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getQrcode() {
		return Qrcode;
	}

	public void setQrcode(String qrcode) {
		Qrcode = qrcode;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getDetail() {
		return Detail;
	}

	public void setDetail(String detail) {
		Detail = detail;
	}

	public String getImgName() {
		return ImgName;
	}

	public void setImgName(String imgName) {
		ImgName = imgName;
	}

	public String getAudioName() {
		return AudioName;
	}

	public void setAudioName(String audioName) {
		AudioName = audioName;
	}

	public String getVideoName() {
		return VideoName;
	}

	public void setVideoName(String videoName) {
		VideoName = videoName;
	}

	public boolean isIsSubmit() {
		return IsSubmit;
	}

	public void setIsSubmit(boolean isSubmit) {
		IsSubmit = isSubmit;
	}

	public double getLocationX() {
		return LocationX;
	}

	public void setLocationX(double locationX) {
		LocationX = locationX;
	}

	public double getLocationY() {
		return LocationY;
	}

	public void setLocationY(double locationY) {
		LocationY = locationY;
	}
}
