package com.entity;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "PatrolTasks")
public class Task implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@DatabaseField(generatedId = true)
	int id;
	@DatabaseField
	String TaskID;
	@DatabaseField
	String TaskType;
	@DatabaseField
	String UserName;
	@DatabaseField
	String Description;
	@DatabaseField
	String TaskDetail;
	@DatabaseField
	String TaskStatus;
	@DatabaseField
	String Time;
	
	public Task()
	{
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTaskStatus() {
		return TaskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		TaskStatus = taskStatus;
	}
	public String getTaskID() {
		return TaskID;
	}
	public void setTaskID(String taskID) {
		TaskID = taskID;
	}
	public String getTaskType() {
		return TaskType;
	}
	public void setTaskType(String taskType) {
		TaskType = taskType;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}

	public String getTaskDetail() {
		return TaskDetail;
	}

	public void setTaskDetail(String taskDetail) {
		TaskDetail = taskDetail;
	}

	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}
	
	
}
