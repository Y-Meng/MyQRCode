package com.mcy.myviews;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyTileBar extends FrameLayout {
	
	private final FrameLayout fLayout = this;
	private RelativeLayout rLayout;
	private Button btnBack,btnSet;
	private TextView txtTitle;
	private Context mContext;

	//带layout文件
	public MyTileBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		InitView();
	}
    //带layout文件和style文件
	public MyTileBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	private void InitView(){
		rLayout = new RelativeLayout(mContext);
		rLayout.setLayoutParams(
				new RelativeLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, 50));
		rLayout.setBackgroundColor(Color.parseColor("#999999"));
		
		btnBack = new Button(mContext);
		RelativeLayout.LayoutParams bRP = new RelativeLayout.LayoutParams(60, 40);
		bRP.addRule(RelativeLayout.CENTER_VERTICAL);
		bRP.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		bRP.leftMargin = 5;
		btnBack.setLayoutParams(bRP);
		btnBack.setText("返回");
		btnBack.setTextColor(Color.parseColor("#FFFFFF"));
		btnBack.setBackgroundColor(Color.parseColor("#666666"));
		
		btnSet = new Button(mContext);
		RelativeLayout.LayoutParams sRP = new RelativeLayout.LayoutParams(60,40);
		sRP.addRule(RelativeLayout.CENTER_VERTICAL);
		sRP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		sRP.rightMargin = 5;	
		btnSet.setLayoutParams(sRP);
		btnSet.setText("设置");
		btnSet.setTextColor(Color.parseColor("#FFFFFF"));
		btnSet.setBackgroundColor(Color.parseColor("#666666"));
		
		txtTitle = new TextView(mContext);
		RelativeLayout.LayoutParams tRP = 
				new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		tRP.addRule(RelativeLayout.CENTER_IN_PARENT);
		txtTitle.setLayoutParams(tRP);
		txtTitle.setText("title");
		txtTitle.setTextColor(Color.parseColor("#FFFFFF"));
		
		rLayout.addView(btnBack);
		rLayout.addView(txtTitle);
		rLayout.addView(btnSet);
		fLayout.addView(rLayout);
	}
	
	public void setTileText(String title){
		txtTitle.setText(title);
	}
	
	public void setBackListener(OnClickListener l){
		btnBack.setOnClickListener(l);
	}
	public void setSetListener(OnClickListener l){
		btnSet.setOnClickListener(l);
	}
}
