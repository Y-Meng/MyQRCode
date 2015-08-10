package com.qrcode;

import com.entity.PatrolRecord;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

public class PatrolView extends Dialog {

	private PatrolRecord mRecord;
	private TextView txtID,txtUser,txtQRcode,txtStatus,txtDetail,txtNote;
	private TextView txtImg,txtVideo,txtAudio,txtTime,txtIssubmit;
	
	public PatrolView(Context context,PatrolRecord record) {
		super(context);
        mRecord = record;
	}

	public PatrolView(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	public PatrolView(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("记录详情");
		setContentView(R.layout.dialog_partol_record);
		InitView();
	}

	private void InitView() {
		txtID = (TextView)findViewById(R.id.txt_pr_id);
		txtUser = (TextView)findViewById(R.id.txt_pr_user);
		txtQRcode = (TextView)findViewById(R.id.txt_pr_qrcode);
		txtStatus = (TextView)findViewById(R.id.txt_pr_status);
		txtDetail = (TextView)findViewById(R.id.txt_pr_detail);
		txtNote = (TextView)findViewById(R.id.txt_pr_note);
		txtImg = (TextView)findViewById(R.id.txt_pr_img);
		txtVideo = (TextView)findViewById(R.id.txt_pr_video);
		txtAudio = (TextView)findViewById(R.id.txt_pr_audio);
		txtTime = (TextView)findViewById(R.id.txt_pr_time);
		txtIssubmit = (TextView)findViewById(R.id.txt_pr_issubmit);
		
		txtID.setText(String.valueOf(mRecord.getId()));
		txtQRcode.setText(mRecord.getQrcode());
		txtUser.setText(mRecord.getUserName());
		txtStatus.setText(mRecord.getStatus());
		txtDetail.setText(mRecord.getDetail());
		txtNote.setText("无备注信息");
		txtImg.setText(mRecord.getImgName());
		txtAudio.setText(mRecord.getAudioName());
		txtVideo.setText(mRecord.getVideoName());
		txtTime.setText(mRecord.getCreateTime());
		if(mRecord.isIsSubmit())
			txtIssubmit.setText("是");
		else
			txtIssubmit.setText("否");
	}

}
