package com.mcy.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.sql.SQLException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.entity.DBHelper;
import com.entity.DownloadInfo;
import com.j256.ormlite.dao.Dao;

public class Downloader {

	private long done = (long) 1;
	private long fileLength;
	private boolean isPause = false;
	private String strUrl;
	private String savePath;
	private String filename ;
	private Handler handler;
	private DBHelper dbHelper;
	private Dao<DownloadInfo, Integer> mDao = null;
	
	private int thCount;
	
	public Downloader(Context context,Handler handler,String url,String saveDir) {
		this.handler = handler;
		this.strUrl = url;
		this.savePath = saveDir;
		dbHelper = new DBHelper(context);
		try {
			mDao = dbHelper.getDownloadDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void download(int thCount) throws Exception{

	     new Thread(bigin).start();
	}
	
	Runnable bigin =  new Runnable() {
		public void run(){
			URL url;
			try {
				url = new URL(strUrl);
				HttpClient httpClient = new DefaultHttpClient();
				HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 20*1000);
				HttpPost post = new HttpPost(strUrl);
				HttpResponse response = httpClient.execute(post);
				Log.e("http response", String.valueOf(response.getStatusLine().getStatusCode()));
				if(response.getStatusLine().getStatusCode()==200){
					fileLength = response.getEntity().getContentLength();
					filename = strUrl.substring(strUrl.lastIndexOf("/")+1);
					File file = new File(savePath, filename);
					RandomAccessFile raf = new RandomAccessFile(file, "rws");
					raf.setLength(fileLength);
					raf.close();
					
					Message msg = new Message();
					msg.what = 0;
					msg.getData().putLong("filelength", fileLength);
					handler.sendMessage(msg);
					
					long partlength = (fileLength + thCount-1)/thCount;
					for(int i=0;i<thCount;i++){
						new DownloadThread(url,file,partlength,i).start();
					}
				}else{
					throw new IllegalArgumentException("404 url"+strUrl);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	public void pause(){
		isPause = true;
	}
	
	public void resume(){
		isPause = false;
		synchronized (mDao) {
			//解除锁定，唤醒所有线程
		    mDao.notifyAll();	
		}
	}
	
	public void delete(){
		isPause = true;
		synchronized (mDao) {
		    try {		
				mDao.deleteBuilder().where().eq("url", strUrl);
				Log.e("download", "delete");
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}
		dbHelper.close();
	}
	
	private final class DownloadThread extends Thread{
		private URL url;
		private File file;
		private long partLen;
		private int id;
		
		public DownloadThread(URL url,File file,long partlen,int id){
			this.url = url;
			this.file = file;
			this.partLen = partlen;
			this.id = id;
		}

		@SuppressWarnings("resource")
		@Override
		public void run() {
			DownloadInfo info = null;
			long start;//起始位置、已下载量
			long end;//结束位置
			try {
				info = mDao.queryBuilder().where().eq("url", url.toString())
						                                       .eq("thid", id).queryForFirst();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(info!=null){
				done = info.getDone();
			}else{
				info = new DownloadInfo(url.toString(),id,01);
				try {
					mDao.create(info);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			start = info.getDone();
			end = partLen-1;
			
			try{
				HttpClient httpClient = new DefaultHttpClient();
				HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 20*1000);
				HttpPost post = new HttpPost(url.toString());
				post.setHeader("Range","bytes="+start+"-"+end);//为未完成任务请求起始位置
				HttpResponse response = httpClient.execute(post);
				RandomAccessFile raf = new RandomAccessFile(file, "rws");
				raf.seek(start);
				InputStream in = response.getEntity().getContent();
				byte[] buffer = new byte[1024*10];
				int len;
				while((len = in.read(buffer))!=-1){
					raf.write(buffer,0,len);
					done+=len;
					info.setDone(done);
					mDao.update(info);
					Message msg = new Message();
					msg.what = 1;
					msg.getData().putLong("done", done);
					handler.sendMessage(msg);//每次读取后发送消息，更新进度条
					if(isPause){
						synchronized (mDao) {
							try {
								mDao.wait();//对象进入等待状态，等待其他线程释放对象锁
								//暂停回复后需要重新连接
								httpClient = new DefaultHttpClient();
								HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 20*1000);
								post = new HttpPost(url.toString());
								post.setHeader("Range","bytes="+done+"-"+end);
								response = httpClient.execute(post);
								raf.seek(done);
								in = response.getEntity().getContent();
							} catch (Exception e) {
                                 e.printStackTrace();
                                 in.close();
							}
						}
					}
				}
				in.close();
				raf.close();
				mDao.delete(info);
			}catch(IOException e){
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
}
