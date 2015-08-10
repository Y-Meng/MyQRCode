package com.entity;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

public class EmergencyRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private String UserName;
	@DatabaseField
	private String EmergencyID;
	@DatabaseField
	private String EmergencySource;
	@DatabaseField
	private String EmergencyDescription;
	@DatabaseField 
	private double EmergencyLocationX;
	@DatabaseField
	private double EmergencyLocationY;
	@DatabaseField
	private String LocationDescription;
	
	
	@DatabaseField
	private String Data_map_file_name;
	@DatabaseField
	private String Data_map_save_path;
	
	@DatabaseField
	private String Data_zdm_file_name;
	@DatabaseField
	private String Data_zdm_save_path;
	
	@DatabaseField 
	private String Data_hdm_file_name;
	@DatabaseField
	private String Data_hdm_save_path;
 
	@DatabaseField 
	private String Data_3d_file_name;
	@DatabaseField
	private String Data_3d_save_path;
 
	@DatabaseField
	private String CreateTime;
	@DatabaseField
	private String FinishTime;
	@DatabaseField
	private int isDone;
	
	public EmergencyRecord() {
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

	public String getEmergencyID() {
		return EmergencyID;
	}

	public void setEmergencyID(String emergencyID) {
		EmergencyID = emergencyID;
	}

	public String getEmergencyDescription() {
		return EmergencyDescription;
	}

	public void setEmergencyDescription(String emergencyDescription) {
		EmergencyDescription = emergencyDescription;
	}

	public String getEmergencySource() {
		return EmergencySource;
	}

	public void setEmergencySource(String emergencySource) {
		EmergencySource = emergencySource;
	}

	public double getEmergencyLocationX() {
		return EmergencyLocationX;
	}

	public void setEmergencyLocationX(double emergencyLocationX) {
		EmergencyLocationX = emergencyLocationX;
	}

	public double getEmergencyLocationY() {
		return EmergencyLocationY;
	}

	public void setEmergencyLocationY(double emergencyLocationY) {
		EmergencyLocationY = emergencyLocationY;
	}
	public String getLocationDescription() {
		return LocationDescription;
	}

	public void setLocationDescription(String locationDescription) {
		LocationDescription = locationDescription;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public String getFinishTime() {
		return FinishTime;
	}

	public void setFinishTime(String finishTime) {
		FinishTime = finishTime;
	}

	public int getIsDone() {
		return isDone;
	}

	public void setIsDone(int isDone) {
		this.isDone = isDone;
	}

	public String getData_map_file_name() {
		return Data_map_file_name;
	}

	public void setData_map_file_name(String data_map_file_name) {
		Data_map_file_name = data_map_file_name;
	}

	public String getData_map_save_path() {
		return Data_map_save_path;
	}

	public void setData_map_save_path(String data_map_save_path) {
		Data_map_save_path = data_map_save_path;
	}

	public String getData_zdm_file_name() {
		return Data_zdm_file_name;
	}

	public void setData_zdm_file_name(String data_zdm_file_name) {
		Data_zdm_file_name = data_zdm_file_name;
	}

	public String getData_zdm_save_path() {
		return Data_zdm_save_path;
	}

	public void setData_zdm_save_path(String data_zdm_save_path) {
		Data_zdm_save_path = data_zdm_save_path;
	}

	public String getData_hdm_file_name() {
		return Data_hdm_file_name;
	}

	public void setData_hdm_file_name(String data_hdm_file_name) {
		Data_hdm_file_name = data_hdm_file_name;
	}

	public String getData_hdm_save_path() {
		return Data_hdm_save_path;
	}

	public void setData_hdm_save_path(String data_hdm_save_path) {
		Data_hdm_save_path = data_hdm_save_path;
	}

	public String getData_3d_file_name() {
		return Data_3d_file_name;
	}

	public void setData_3d_file_name(String data_3d_file_name) {
		Data_3d_file_name = data_3d_file_name;
	}

	public String getData_3d_save_path() {
		return Data_3d_save_path;
	}

	public void setData_3d_save_path(String data_3d_save_path) {
		Data_3d_save_path = data_3d_save_path;
	}
}
