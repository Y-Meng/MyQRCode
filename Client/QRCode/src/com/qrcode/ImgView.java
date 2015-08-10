package com.qrcode;

import com.qrcode.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.view.View.OnClickListener;

public class ImgView extends Dialog  implements OnClickListener,OnTouchListener{
	
	private String strIMG;
	private byte[] binIMG;
	private Bitmap bitIMG;
	private ImageView imgData;
	private Button btnSave,btnPrint;
	//private BitmapDrawable bitmapDrawable;
	
	private String imgPath;
	private boolean fromFile = false;
	
	private PointF startPoint = new PointF();
	private int mode = 0,single = 1,multi = 2;
	private Matrix matrix = new Matrix();
	private Matrix currentMatrix = new Matrix();
	private float startDistance;//起始时两点间距离
	private PointF midPoint;//两点中心点
	
	
	public ImgView(Context context,String strImg) {
		super(context);
        strIMG = strImg;
	}
	
	public ImgView(Context context,String path,Boolean frompath){
		super(context);
		imgPath = path;
		fromFile=true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("返回数据：");
		setContentView(R.layout.dialog_image);
		imgData =  (ImageView)findViewById(R.id.img_data);
		btnSave = (Button)findViewById(R.id.btn_save);
		btnSave.setOnClickListener(this);
		btnPrint = (Button)findViewById(R.id.btn_export);
		btnPrint.setOnClickListener(this);
		
		if(fromFile==true){
//			BitmapFactory.Options options = new BitmapFactory.Options();
//			options.inSampleSize = 2;
			bitIMG = BitmapFactory.decodeFile(imgPath);
		}else{
			binIMG =  Base64.decode(strIMG,Base64.DEFAULT);
			bitIMG = BitmapFactory.decodeByteArray(binIMG, 0, binIMG.length);
		}
		if (bitIMG!=null){
			Log.e("bitmap","view");
		    imgData.setImageBitmap(bitIMG);
		    imgData.setOnTouchListener(this);
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_save:
			bitIMG.recycle();
			dismiss();
			break;
			
		case R.id.btn_export:
			bitIMG.recycle();
			dismiss();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		imgData.setScaleType(ScaleType.MATRIX);
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			
			mode = single;
			currentMatrix.set(imgData.getImageMatrix());
			startPoint.set(event.getX(),event.getY());
			Log.e("touch", "down");
			break;

		case MotionEvent.ACTION_POINTER_DOWN://在已有触点的情况下，新的点击
			mode = multi;
			startDistance = getDistance(event);
			midPoint = getMidPoint(event);
			currentMatrix.set(imgData.getImageMatrix());
			break;
		case MotionEvent.ACTION_MOVE:
			if(mode==single){
				float x = event.getX()-startPoint.x;
				float y = event.getY()-startPoint.y;
				matrix.set(currentMatrix);
				matrix.postTranslate(x, y);
				Log.e("touch", "single move:"+String.valueOf(x)+","+String.valueOf(y));
			}else if(mode==multi){
				float endDistance = getDistance(event);
				if(endDistance>10f){
					float sclace = endDistance/startDistance;
					matrix.set(currentMatrix);
					matrix.postScale(sclace, sclace,midPoint.x,midPoint.y);
				}
				Log.e("touch", "mutiple move");
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			mode = 0;
			break;
		default:
			break;
		}
		imgData.setImageMatrix(matrix);
		Log.e("touch", "set img");
		return true;
	}

	private PointF getMidPoint(MotionEvent event) {
		
        float midx = (event.getX(0)+event.getX(1))/2;
        float midy = (event.getY(0)+event.getY(1))/2;
		return new PointF(midx,midy);
	}

	private float getDistance(MotionEvent event) {

        float dx = event.getX(1)-event.getX(0);
        float dy = event.getY(1)-event.getY(0);
        return (float) Math.sqrt(dx*dx+dy*dy);
	}
}
