package com.mcy.myviews;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class MyListView extends ListView implements OnTouchListener ,OnGestureListener{

	private OnDeleteListener deleteListener = null;
	private GestureDetector gestureDetector;
	private Button btnDelete;
	private ViewGroup itemLayout;
	private Context mContext;
	private int selectedItem;
	private boolean isDeleteShow;
	
	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		gestureDetector = new GestureDetector(context, this);
		setOnTouchListener(this);
	}

	private Button CreatDeleteButton(Context c) {

		Button btn = new Button(c);
		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
		p.addRule(RelativeLayout.CENTER_VERTICAL);
		p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		p.rightMargin = 5;
		btn.setLayoutParams(p);
		btn.setText("É¾³ý");
		btn.setTextColor(Color.parseColor("#ffffff"));
		btn.setBackgroundColor(Color.parseColor("#cc3333"));
		return btn;
	}

	//ÉèÖÃÉ¾³ý°´Å¥¼àÌý
	public void setOnDeleteListener(OnDeleteListener listener){
		deleteListener = listener;
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if(isDeleteShow){
			itemLayout.removeView(btnDelete);
			btnDelete = null;
			isDeleteShow = false;
			return false;
		}else{
			return gestureDetector.onTouchEvent(event);
		}
		
	}

	@Override
	public boolean onDown(MotionEvent e) {

		if(!isDeleteShow){
			selectedItem = pointToPosition((int)e.getX(), (int)e.getY());
		}
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		if(!isDeleteShow&&velocityX<0&&Math.abs(velocityX)>2*Math.abs(velocityY)){
			btnDelete = CreatDeleteButton(mContext);
			btnDelete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					itemLayout.removeView(btnDelete);
					btnDelete = null;
					isDeleteShow = false;
					if(deleteListener!=null)
					{
						deleteListener.onDelete(selectedItem);		
					}
				}
			});
			
			itemLayout = (ViewGroup)getChildAt(selectedItem-getFirstVisiblePosition());
			itemLayout.addView(btnDelete);
			isDeleteShow = true;
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	//×Ô¶¨ÒåÉ¾³ý¼àÌý½Ó¿Ú
	public interface OnDeleteListener{
		void onDelete(int index);
	}
}
