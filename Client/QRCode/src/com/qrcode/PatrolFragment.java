package com.qrcode;
import java.util.Random;
import com.qrcode.R;
import com.zxing.activity.CaptureActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class PatrolFragment extends Fragment implements OnClickListener{
	
	private static final int REQUEST_ATTRIBUTES_BY_QRCODE = 1;
	private static final int REQUEST_ATTRIBUTES_BY_MAP = 2;
	
	private View view; 
	private Button btnScanQrcode,btnMapSelect;

	public TextView txtResult;
	public Spinner spinStatus;
	public EditText editDescription;
	public double X,Y;
	
	public PatrolFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view  = inflater.inflate(R.layout.fragment_patrol, container, false);
		InitView();
		return view;
	}

	private void InitView() {

		btnMapSelect = (Button)view.findViewById(R.id.btn_map_select);
		btnMapSelect.setOnClickListener(this);
		btnScanQrcode = (Button)view.findViewById(R.id.btn_scan_qrcode);
		btnScanQrcode.setOnClickListener(this);
		
		txtResult = (TextView)view.findViewById(R.id.txt_result_attributes);
		spinStatus = (Spinner)view.findViewById(R.id.spinner_patrol_status);
		editDescription = (EditText)view.findViewById(R.id.edit_description);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
        switch (v.getId()) {
          case R.id.btn_scan_qrcode:
        	if(txtResult.getText().toString().equals(""))
  			{
  				Intent openCameraIntent = new Intent(getActivity().getApplicationContext(),CaptureActivity.class);
      			startActivityForResult(openCameraIntent, REQUEST_ATTRIBUTES_BY_QRCODE);
  			}
  			else{
  			    AlertDialog.Builder qrbuilder = new Builder(getActivity()); 
  			    qrbuilder.setIcon(android.R.drawable.ic_dialog_info);
  		        qrbuilder.setMessage("二维码已存在，是否重新扫描?"); 
  		        qrbuilder.setTitle("提示"); 
  		        qrbuilder.setPositiveButton("是", 
  		                new android.content.DialogInterface.OnClickListener() { 
  		                    public void onClick(DialogInterface dialog, int which) { 
  		                        
  		                        Intent openCameraIntent = new Intent(getActivity().getApplicationContext(),CaptureActivity.class);
  		            			startActivityForResult(openCameraIntent, REQUEST_ATTRIBUTES_BY_QRCODE);	
  		            			//dialog.dismiss(); 
  		                    } 
  		                }); 
  		        qrbuilder.setNegativeButton("否", 
  		                new android.content.DialogInterface.OnClickListener() { 
  		                    public void onClick(DialogInterface dialog, int which) { 
  		                        //dialog.dismiss(); 
  		                    } 
  		                }); 
  		        qrbuilder.show();
  			}
	          break;
          case R.id.btn_map_select:
        	  Intent openMap = new Intent(getActivity().getApplicationContext(),MapActivity.class);
        	  openMap.setFlags(1);
        	  startActivityForResult(openMap, REQUEST_ATTRIBUTES_BY_MAP);
        	  break;
	      
        default:
	          break;
        }		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==Activity.RESULT_OK){
			switch (requestCode) {			
			case REQUEST_ATTRIBUTES_BY_QRCODE:				
				Bundle bundle = data.getExtras();
				String scanResult = bundle.getString("result");
				try {
//					Geodatabase gdb = new Geodatabase(WebService.GDB);
//					GeodatabaseFeature feature = gdb.getGeodatabaseFeatureTableByLayerId(0).getFeature();
//					仅用于测试
					X = 39287.9+new Random().nextDouble()*100;
					Y = 3832570.0+new Random().nextDouble()*100;
				} catch (Exception e) {
					e.printStackTrace();
				}
				txtResult.setText(scanResult);
				break;
			case REQUEST_ATTRIBUTES_BY_MAP:
				String mapresult = data.getStringExtra("attributtes");
				txtResult.setText(mapresult);
				X = Double.parseDouble(data.getStringExtra("X"));
				Y = Double.parseDouble(data.getStringExtra("Y"));
				break;
				
			default:
				break;
			}
		}
	}

}
