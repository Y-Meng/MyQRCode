package com.qrcode.query;

import java.util.Random;
import com.qrcode.R;
import com.qrcode.PatrolTaskActivity;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NewTaskNotifier {
	
	private Context context;
	private NotificationManager notificationManager;

	
	public NewTaskNotifier(Context context) {
		this.context = context;
		this.notificationManager = 
				(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	//提示消息
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public void taskNotify(String title,String message){
		 Notification notification = new Notification();
		 notification.icon = R.drawable.task;
		 
		 //设置唤起Activity
		 Intent intent = new Intent(context,PatrolTaskActivity.class);
		 PendingIntent pintent =  PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
		 
		 //sdk16(4.1)以上版本,测试客户端版本为15(4.0)
		 /*Builder builder = new Builder(context);
		 builder.setContentIntent(pintent)
		        .setContentTitle(title)
		        .setContentText(message);
         notification = builder.build();
         notification.flags = Notification.FLAG_AUTO_CANCEL;
         //或者 
         //notification = 
         //		 new Builder(context).setContentIntent(pintent).setContentTitle(title).setContentText(message).build();
         */
		 //4.1以下版本
		 notification.flags = Notification.FLAG_AUTO_CANCEL;
		 notification.setLatestEventInfo(context, title, message,pintent);
		 notification.defaults = Notification.DEFAULT_SOUND;//使用系统默认声音 
         
		 notificationManager.notify(new Random().nextInt(),notification);
        
	}

}
