package com.entity;

public class EmergencyRequest {

	private int Mode;
	private double X;
	private double Y;
	private double Buffer;
	private String QrCode;
    private int isMAP;
    private int isZDM;
    private int isHDM;
    private int is3D;
	
	public EmergencyRequest() {
		
	}

	public int getMode() {
		return Mode;
	}
	public void setMode(int mode) {
		Mode = mode;
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


	public double getBuffer() {
		return Buffer;
	}

	public void setBuffer(double buffer) {
		Buffer = buffer;
	}

	public String getQrCode() {
		return QrCode;
	}

	public void setQrCode(String qrCode) {
		QrCode = qrCode;
	}


	public int getIsMAP() {
		return isMAP;
	}


	public void setIsMAP(int isMAP) {
		this.isMAP = isMAP;
	}


	public int getIsZDM() {
		return isZDM;
	}


	public void setIsZDM(int isZDM) {
		this.isZDM = isZDM;
	}


	public int getIsHDM() {
		return isHDM;
	}


	public void setIsHDM(int isHDM) {
		this.isHDM = isHDM;
	}


	public int getIs3D() {
		return is3D;
	}


	public void setIs3D(int is3d) {
		is3D = is3d;
	}

}
