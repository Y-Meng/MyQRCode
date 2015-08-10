package com.qrcode;

import com.entity.EmergencyRecord;
import com.qrcode.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EmergencyView extends Dialog implements android.view.View.OnClickListener{

	private EmergencyRecord mRecord;
	private Button btnOK,btnCancel;
	private TextView txtID,txtSource,txtTime;
	private TextView txtDescription,txtLocation;
	private TextView txtIsDone;
	private OnResultListener onResultListener;
	
	public void setOnResultListener(OnResultListener onResultListener) {
		this.onResultListener = onResultListener;
	}

	public EmergencyView(Context context,EmergencyRecord record) {
		super(context);
        this.mRecord = record;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("选择结果");
		setContentView(R.layout.dialog_emergency_view);
		InitView();
	}	
	
	private void InitView() {
		
		txtID = (TextView)findViewById(R.id.d_emergency_txt_id);
		txtSource = (TextView)findViewById(R.id.d_emergency_txt_source);
		txtTime = (TextView)findViewById(R.id.d_emergency_txt_time);
		
		txtDescription = (TextView)findViewById(R.id.d_emergency_txt_description);
		txtLocation = (TextView)findViewById(R.id.d_emergency_txt_location);
		txtIsDone = (TextView)findViewById(R.id.d_emergency_txt_isdone);
		
		btnOK = (Button)findViewById(R.id.d_emergency_btn_ok);
		btnCancel = (Button)findViewById(R.id.d_emergency_btn_back);
		btnOK.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		
		txtID.setText(String.valueOf(mRecord.getId()));
		txtSource.setText(mRecord.getEmergencySource());
		txtTime.setText(mRecord.getCreateTime());
		
		txtDescription.setText(mRecord.getEmergencyDescription());
		txtLocation.setText(mRecord.getLocationDescription());
		if(mRecord.getIsDone()==1){
			txtIsDone.setText("完成");
			btnOK.setEnabled(false);
		}else{
			txtIsDone.setText("待办");
			btnCancel.setEnabled(false);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.d_emergency_btn_ok:
			onResultListener.onResult(true);
			dismiss();
			break;
		case R.id.d_emergency_btn_back:
			onResultListener.onResult(false);
			dismiss();
			break;
		default:
			break;
		}
	}
	
	public interface OnResultListener{
		public void onResult(boolean result);
	}

}
