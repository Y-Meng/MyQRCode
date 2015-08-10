package com.qrcode;

import java.util.List;

import com.esri.core.map.Feature;
import com.qrcode.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AttributeView extends Dialog {

	private Feature mFeature;
	private List<Feature> mFeatures;
	private TextView txtAttributes;
	private Button btnOK,btnRe;
	public String strAttributes = "";
	private DialogResult Result;
	
	public AttributeView(Context context){
		super(context);
	}
	public AttributeView(Context context, List<Feature> features) {
		super(context);
		this.mFeatures = features;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Ñ¡Ôñ½á¹û");
		setContentView(R.layout.dialog_attribute);
		
		txtAttributes = (TextView)findViewById(R.id.txt_Attribute);
		btnOK = (Button)findViewById(R.id.btn_Attribute_ok);
		btnRe = (Button)findViewById(R.id.btn_Attribute_re);
		btnOK.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Result.onResult(true);
				dismiss();
			}
		});
		btnRe.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Result.onResult(false);
				dismiss();
			}
		});
		for (Feature feature : mFeatures) {
			mFeature = feature;
			strAttributes += mFeature.getAttributes().toString();
		}
		if(!strAttributes.equals("")){
			strAttributes = strAttributes.substring(1, strAttributes.length()-1);
			strAttributes = strAttributes.replace("}{",";\n\n" );
			strAttributes = strAttributes.replace(",", "\n");
	 
		}
		txtAttributes.setText(strAttributes);
	}

	public void setDialogResult(DialogResult mDialogResult) {
		this.Result = mDialogResult;
	}

	
	public interface DialogResult{
		public void onResult(boolean isOK);
	}
}
