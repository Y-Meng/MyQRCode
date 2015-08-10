package com.entity;

import java.io.Serializable;
import com.j256.ormlite.field.DatabaseField;

public class LocationRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@DatabaseField(generatedId = true)
	int id;
	@DatabaseField
	String UserName;
	@DatabaseField
	double X;
	@DatabaseField
	double Y;
	@DatabaseField
	double Latitude;
	@DatabaseField
	double Longitide;
	@DatabaseField
	String LocateTime;

	public LocationRecord() {
		
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

	public double getX() {
		return X;
	}

	public void setX(double x) {
		X = x;
	}

	public double getY() {
		return Y;
	}

	public void setY(double y) {
		Y = y;
	}

	public double getLatitude() {
		return Latitude;
	}

	public void setLatitude(double latitude) {
		Latitude = latitude;
	}

	public double getLongitide() {
		return Longitide;
	}

	public void setLongitide(double longitide) {
		Longitide = longitide;
	}

	public String getLocateTime() {
		return LocateTime;
	}

	public void setLocateTime(String locateTime) {
		LocateTime = locateTime;
	}
}
