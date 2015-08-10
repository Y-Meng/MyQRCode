package com.entity;

import com.j256.ormlite.field.DatabaseField;

public class DownloadInfo {
	
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private String url;
	@DatabaseField
	private String path;
	@DatabaseField
	private int thid;
	@DatabaseField
	private long done;

	public DownloadInfo() {
	}

	public DownloadInfo(String url,int thid,long i){
		this.url = url;
		this.thid = thid;
		this.done = i;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getThid() {
		return thid;
	}

	public void setThid(int thid) {
		this.thid = thid;
	}

	public long getDone() {
		return done;
	}

	public void setDone(long done) {
		this.done = done;
	}
	
	
}
