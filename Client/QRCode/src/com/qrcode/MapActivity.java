package com.qrcode;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.config.services.MyApp;
import com.config.services.WebService;
import com.entity.DBHelper;
import com.entity.PatrolRecord;
import com.entity.PointInfo;
import com.entity.Points;
import com.esri.android.map.FeatureLayer;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.LocationDisplayManager.AutoPanMode;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.ags.ArcGISFeatureLayer.MODE;
import com.esri.android.map.ags.ArcGISFeatureLayer.Options;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.map.event.OnPinchListener;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.popup.Popup;
import com.esri.android.map.popup.PopupContainer;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.LinearUnit;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.geometry.Unit;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.esri.core.tasks.query.QueryParameters;
import com.esri.core.tasks.query.QueryTask;
import com.google.gson.Gson;
import com.mcy.myviews.Compass;
import com.qrcode.AttributeView.DialogResult;

public class MapActivity extends Activity implements OnClickListener{
	
	private Context context;
	private MyApp myApp;
	private Intent callIntent;
	private MapView mMapView;
	private Compass compass;
	private Points mPoints;
	private ArcGISLocalTiledLayer lytpk;
	//private ArcGISDynamicMapServiceLayer lyJG;
	private Geodatabase lygxgdb = null;
	private ArcGISFeatureLayer fLayer;
	private GraphicsLayer gLayer; 
	private GraphicsLayer progressLayer;
	
	private LocationDisplayManager ls ;

	private final static double SEARCH_RADIUS = 20;
    private double X = 40827.686929041425;
    private double Y = 3832659.949836881;
    private ProgressDialog progress;
	private String taskdetail = "";
	private boolean isViewProgress = false;
	private List<PatrolRecord> mRecords;

	private List<Feature> featuresSeclected;
	private Feature featureSelected;
	private String strSelect;
	
	CallbackListener<FeatureSet> callback = new CallbackListener<FeatureSet>() {

		@Override
		public void onCallback(FeatureSet objs) {
		}

		@Override
		public void onError(Throwable e) {
			 fLayer.clear();
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArcGISRuntime.setClientId("uK0DxqYT0om1UXa9");
        setContentView(R.layout.activity_map);
        
        context = MapActivity.this;
        myApp = (MyApp)getApplication();
        callIntent = getIntent();
        
        featuresSeclected = new ArrayList<Feature>();
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        if(data!=null)
        	taskdetail = data.getString("taskdetail");
        

        //��ʼ����ͼ
		InitMapView();
		
		//�ؼ�����
		Button zoomIn=(Button)findViewById(R.id.btnZoomIn);
		zoomIn.setOnClickListener(this);
		Button zoomOut=(Button)findViewById(R.id.btnZoomOut);
		zoomOut.setOnClickListener(this);
		Button btnStartPatrol=(Button)findViewById(R.id.btnStartPatrol);
		btnStartPatrol.setOnClickListener(this);
		Button location=(Button)findViewById(R.id.btnLocation);
		location.setOnClickListener(this);
		Button btnShowProgress=(Button)findViewById(R.id.btnShowProgress);
		btnShowProgress.setOnClickListener(this);
		
        if(callIntent.getFlags()==1){
        	taskdetail = myApp.getCurrentTaskDetail();
        	btnStartPatrol.setVisibility(View.GONE);
        }
        
        if(callIntent.getFlags()==0){
        	btnStartPatrol.setVisibility(View.GONE);
        	btnShowProgress.setVisibility(View.GONE);
        }
    }
    
    //��ʼ����ͼ
    private void InitMapView(){
    	mMapView = (MapView)findViewById(R.id.map);
		mMapView.setMapBackground(Color.WHITE, Color.BLACK,100, 0);
		mMapView.setAllowRotationByPinch(false);	
		compass=new Compass(this, null);
		mMapView.addView(compass);
		mMapView.setEsriLogoVisible(false);
		mMapView.enableWrapAround(true);
		mMapView.setMinScale(80000);
		
		gLayer= new GraphicsLayer();//����ͼ��
		progressLayer = new GraphicsLayer();
		lytpk = new ArcGISLocalTiledLayer(WebService.TPK);//������Ƭ
//		lyJG = new ArcGISDynamicMapServiceLayer(WebService.LYJG);//���綯̬����
		try {
			lygxgdb = new Geodatabase(WebService.GDB);//���ص������ݿ�
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mMapView.addLayer(lytpk,0);
		//���ر������ݿ���ͼ��
		if(lygxgdb!=null){
			int i=1;
			for(GeodatabaseFeatureTable featureTable :lygxgdb.getGeodatabaseTables()){
				if(featureTable.hasGeometry()){
					mMapView.addLayer(new FeatureLayer(featureTable),i);
					i+=1;
					//��Ӻ󾮸�ͼ��indexΪ1������ͼ��indexΪ2
				}
			}
		}
		
		//��ʾ��������
		initTaskView();
		//��ͼ״̬���
		mMapView.setOnStatusChangedListener(new OnStatusChangedListener(){

			private static final long serialVersionUID = 1L;

			@Override
			public void onStatusChanged(Object source, STATUS status) {
				if(source==mMapView&&status==STATUS.INITIALIZED)
				    zoomToTask();
			}
		});
		//��ͼ�����¼�identity task
		mMapView.setOnSingleTapListener(new OnSingleTapListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void onSingleTap(float x, float y) {
			
			}
		});
		//��ͼ�����¼�
		mMapView.setOnLongPressListener(new OnLongPressListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public boolean onLongPress(float x, float y) {
				if(lygxgdb==null){
				    //ͨ�������ѯ��λ
				    QueryPress(x,y);
				}else{
				//�ڱ������ݿ��в�ѯ��λ
					GetFeatureByGDB(x,y);
				}
				return false;
			}
		});
		//��ָ��������
		mMapView.setOnPinchListener(new OnPinchListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void prePointersUp(float arg0, float arg1, float arg2, float arg3,
					double arg4) {
				mMapView.getRotationAngle();				
				compass.setRotationAngle(mMapView.getRotationAngle());					
				compass.postInvalidate();
			}
			@Override
			public void prePointersDown(float arg0, float arg1, float arg2, float arg3,
					double arg4) {
			}
			@Override
			public void postPointersUp(float arg0, float arg1, float arg2, float arg3,
					double arg4) {
			}
			@Override
			public void postPointersMove(float arg0, float arg1, float arg2,
					float arg3, double arg4) {
			}
			@Override
			public void postPointersDown(float arg0, float arg1, float arg2,
					float arg3, double arg4) {
			}
			@Override
			public void prePointersMove(float x1, float y1, float x2, float y2,
					double factor) {
			}
		});
        
		zoomToTask();
    }
    //��ʼ����ͼ��ʾ��Χ�����ĵ�
    private void zoomToTask(){
    	Log.e("map", "zoom");
//    	Unit mapUnit = mMapView.getSpatialReference().getUnit();
//		double zoomWidth = Unit.convertUnits(
//				SEARCH_RADIUS,
//				Unit.create(LinearUnit.Code.MILE_US),
//				mapUnit);
//		Envelope zoomExtent = new Envelope(new Point(39287.9, 3832370.0),zoomWidth, zoomWidth);
//		mMapView.setExtent(zoomExtent);
		mMapView.zoomTo(new Point(39287.9, 3832370.0), 1);
    }
    //��ʾ��������
    private void initTaskView() {
    	
    	Options o = new Options();
    	o.mode = MODE.ONDEMAND;
    	o.outFields = new String[]{"OBJECTID","SHAPE","OBJCODE","COMPCODE","COMPNAME"};
    	
    	    //�����ʱͼ����ʾ�����б�
    		GraphicsLayer mTaskLayer = new GraphicsLayer();
    		//fLayer = new ArcGISFeatureLayer(WebService.LYJG+"/0",o);
    		//������Ⱦ��ʽ
    		//mTaskLayer.setRenderer(new SimpleRenderer((Symbol) new SimpleMarkerSymbol(Color.RED,
    		//			20, SimpleMarkerSymbol.STYLE.CROSS)));
    		//SimpleMarkerSymbol selectedSymbol = new SimpleMarkerSymbol(Color.RED, 5, STYLE.DIAMOND);
    		//fLayer.setSelectionSymbol(selectedSymbol);
    		//mMapView.addLayer(fLayer);
    		//�����������
    		Log.e("taskdetail",taskdetail);
    		if(!taskdetail.equals("")){
    				
    			//��base64�ַ�ת����ɶ���������
    			byte[] bytes = Base64.decode(taskdetail, TRIM_MEMORY_BACKGROUND);
    			//ת������ͨ�ַ���
    			String jsondetail = new String(bytes);
    			Log.e("json", jsondetail);
    			Gson gson = new Gson();
    			try {
    				mPoints = gson.fromJson(jsondetail, Points.class);
    				Log.e("taskdetail", "getpoints");
    					
    				Polygon polygon = new Polygon();
    				ArrayList<PointInfo> mmPoints = new ArrayList<PointInfo>();
    				mmPoints = mPoints.getPoints();
    				int count = mmPoints.size();
    				Log.e("points", String.valueOf(count));
    				polygon.startPath(mmPoints.get(0).getX(),mmPoints.get(0).getY());
    				
    				if(mmPoints!=null){
    					for(int i=1;i<count;i++)
        				{
    						Point point  = new Point(mmPoints.get(i).getX(),mmPoints.get(i).getY());
    						polygon.lineTo(point);
        				}
    				}
    				Graphic g = new Graphic(polygon, new SimpleLineSymbol(Color.RED,3));	
    				int uid = mTaskLayer.addGraphic(g);
    				Log.e("uid", String.valueOf(uid));
    				//��ѯ��Χ��Ҫ��
    			   // Query q = new Query();
    			   // q.setReturnGeometry(true);
    			   // q.setInSpatialReference(mMapView.getSpatialReference());
    			   // q.setGeometry(mTaskLayer.getGraphic(uid).getGeometry());
    				//q.setSpatialRelationship(SpatialRelationship.INTERSECTS);
    				
    				//fLayer.selectFeatures(q, SELECTION_METHOD.NEW, callback);
    					 
    			} catch (Exception e) {
    				e.printStackTrace();
    			}			
    		}
    		 
    		mMapView.addLayer(mTaskLayer);
	}
	//��ʾ�������
    private void initTaskProgress(){
    	if(isViewProgress==false){
    		int currentTaskID = myApp.getCurrentTaskID();
    		Log.e("taskID", String.valueOf(currentTaskID));
    		if(currentTaskID!=-1){
    			DBHelper dbHelper = new DBHelper(getBaseContext());
    			try {
					 mRecords= dbHelper.getPatrolRecordDao()
							.queryBuilder().orderBy("id",true).where().eq("taskID", currentTaskID).query();
				} catch (SQLException e) {
					e.printStackTrace();
				}finally{
					dbHelper.close();
				}
    		}	
    		if(mRecords!=null&&mRecords.size()>0){
    			 Log.e("progress", "view");
    			 new QueryProgressTask().execute();
    			 isViewProgress = true;
    		}
    	}else{
    		mMapView.removeLayer(progressLayer);
    		isViewProgress = false;
    	}
    }
    
    private void GetFeatureByGDB(float x, float y) {
		// TODO Auto-generated method stub
		//featurelayer ����ֱ��ͨ����Ļ������ݲ���Ҫ��Ids
		//Log.e("xy", String.valueOf(x)+","+String.valueOf(y));
		long[] featuerIds = ((FeatureLayer)mMapView.getLayer(1)).getFeatureIDs(x, y, 20);
		//Log.e("IDs", String.valueOf(featuerIds.length));
		if(featuerIds.length>0){
			//ȡ��һ��
			long featureId = featuerIds[0];
			GeodatabaseFeature gdbFeature = (GeodatabaseFeature)((FeatureLayer)mMapView.getLayer(1)).getFeature(featureId);
			if(callIntent.getFlags()==1){
				Map<String, Object> attributes = gdbFeature.getAttributes();
				String attr = "";
				if(null!=attributes.get("Ψһ����"))
				    attr += ("Ψһ���룺"+attributes.get("Ψһ����").toString()+"\n");
				if(null!=attributes.get("��������"))
				    attr += ("�������ͣ�"+attributes.get("��������").toString()+"\n");
				if(null!=attributes.get("X����"))
				    attr += ("X���꣺"+attributes.get("X����").toString()+"\n");
				if(null!=attributes.get("Y����"))
				    attr += ("Y���꣺"+attributes.get("Y����").toString()+"\n");
				callIntent.putExtra("attributtes",attr);
				callIntent.putExtra("X", attributes.get("X����").toString());
				callIntent.putExtra("Y", attributes.get("Y����").toString());
				setResult(RESULT_OK, callIntent);
				Log.e("mapselect", attr);
				finish();
			}else{
				PopupContainer popupContainer = new PopupContainer(mMapView);
			    Popup popup = ((FeatureLayer)mMapView.getLayer(1)).createPopup(mMapView,0,gdbFeature);
			    Log.e("popup",gdbFeature.toString());
			    popup.setEditable(false);
			    popupContainer.addPopup(popup);
			    PopupDialog popupDialog = new PopupDialog(mMapView.getContext(), popupContainer);
			    popupDialog.show();
			}
		}
	}
	private void QueryPress(float x, float y) {

		Point point = mMapView.toMapPoint(x,y);
		X = point.getX();
		Y = point.getY();
		QueryPressTask mTask = new QueryPressTask();
		mTask.execute(new String[]{""});
	}
 
	private class QueryPressTask extends AsyncTask<String, Void, FeatureResult>{


		@Override
		protected void onPreExecute() {
            progress = new ProgressDialog(MapActivity.this);
            progress.show();
			super.onPreExecute();
		}

		@Override
		protected FeatureResult doInBackground(String... params) {

			//��ѯ
			FeatureResult fr = null;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			//gLayer.addGraphic(new Graphic(new Point(X,Y), new SimpleMarkerSymbol(Color.RED, 5, STYLE.DIAMOND)));
			Log.e("press", String.valueOf(X)+","+String.valueOf(Y));
			mMapView.addLayer(gLayer);
			QueryTask queryTask = new QueryTask(WebService.LYJG+"/0");
			QueryParameters qParameters = new QueryParameters();
			qParameters.setGeometry(
					new Envelope( X-5, Y-5, X+5 , Y+5));
			qParameters.setReturnGeometry(true);
			qParameters.setOutFields(new String[]{"OBJECTID","SHAPE","OBJCODE","COMPCODE","COMPNAME","POINT_X","POINT_Y"});
			try {
				fr = queryTask.execute(qParameters);

			} catch (Exception e) {
				e.printStackTrace();
				Log.e("e", "e");
			}
			return fr;
		}
		@Override
		protected void onPostExecute(FeatureResult result) {
			progress.dismiss();
			if(result!=null){
				for (Object object : result) {
					if(object instanceof Feature){
						featureSelected = (Feature)object;
						featuresSeclected.add(featureSelected);
						Graphic graphic = 
								new Graphic(featureSelected.getGeometry(), 
								           new SimpleMarkerSymbol(Color.GREEN, 5, STYLE.CROSS),
								           featureSelected.getAttributes());
						strSelect = featureSelected.getAttributes().get("OBJECTID").toString();
						//strSelect = featureSelected.getAttributeValue("OBJECTID").toString();
						gLayer.addGraphic(graphic);
						mMapView.addLayer(gLayer);
						Log.e("press", "addlayer");
						Log.e("OID", strSelect);
					}
				}
				if(featuresSeclected.size() != 0){
					final AttributeView attributeView = new AttributeView(context, featuresSeclected);
					attributeView.setDialogResult(new DialogResult() {
						
						@Override
						public void onResult(boolean isOK) {
							 
							if(isOK == true&&callIntent.getFlags()==1){
								callIntent.putExtra("attributtes", attributeView.strAttributes);
								setResult(RESULT_OK, callIntent);
								Log.e("mapselect", "back to call");
								finish();
							}else{
								gLayer.clearSelection();
								featuresSeclected.clear();
								mMapView.removeLayer(gLayer);
								mMapView.refreshDrawableState();
								Log.e("glayer", "remove");
							}						
						}
					});
					attributeView.show();
					Log.e("dialog", "show");
				}
			}
			super.onPostExecute(result);
		}
	}
	
	private class QueryProgressTask extends AsyncTask<Void, Void, Graphic>{

		@Override
		protected Graphic doInBackground(Void... params) {
            Polyline taskLine = new Polyline();
			taskLine.startPath(mRecords.get(0).getLocationX(),mRecords.get(0).getLocationY());
            for(PatrolRecord record:mRecords){
            	if(record.getLocationX()<record.getLocationY()){
            		taskLine.lineTo(record.getLocationX(), record.getLocationY());
            	}
            }
			Graphic taskGraphic = new Graphic(taskLine, new SimpleLineSymbol(Color.BLUE,3));
			return taskGraphic;
		}
		@Override
		protected void onPostExecute(Graphic result) {
			try {
				progressLayer.addGraphic(result);
				int pid = mMapView.addLayer(progressLayer);
				Log.e("pid", String.valueOf(pid));
			} catch (Exception e) {
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}
	}
	
	@Override 
	protected void onDestroy() { 
		super.onDestroy();
    }
	@Override
	protected void onPause() {
		super.onPause();
		mMapView.pause();
    }
	@Override 	
	protected void onResume() {
		super.onResume(); 
		mMapView.unpause();
	}
	
	@Override
	public void onClick(View v) {
		 switch (v.getId()) {
		 
		case R.id.btnZoomIn:
			mMapView.zoomin();
			break;
		case R.id.btnZoomOut:
			mMapView.zoomout();
			break;
		case R.id.btnStartPatrol:
			startActivity(new Intent(context,PatrolActivity.class));
			break;
		case R.id.btnShowProgress:
			initTaskProgress();
			break;
		case R.id.btnLocation:
			ls = mMapView.getLocationDisplayManager();
			ls.setAutoPanMode(AutoPanMode.OFF);
			ls.setAllowNetworkLocation(true);
			ls.setLocationListener(new LocationListener() {

				boolean locationChanged = false;
				// GPS��λ���Zoom����ǰλ��
				public void onLocationChanged(Location loc) {
					if (!locationChanged) {
						
						double locy = loc.getLatitude();						
						double locx = loc.getLongitude();
						Point wgspoint = new Point(locx, locy);
						Point mapPoint = (Point) GeometryEngine
								.project(wgspoint,
										SpatialReference.create(4326),
										mMapView.getSpatialReference());

						Unit mapUnit = mMapView.getLayer(0).getSpatialReference()
								.getUnit();
						double zoomWidth = Unit.convertUnits(
								SEARCH_RADIUS,
								Unit.create(LinearUnit.Code.MILE_US),
								mapUnit);
						Envelope zoomExtent = new Envelope(mapPoint,
								zoomWidth, zoomWidth);
						mMapView.setExtent(zoomExtent);
						locationChanged = true;
					}

				}
				public void onProviderDisabled(String arg0) {
					Toast.makeText(context, "�������ź�", Toast.LENGTH_LONG).show();
				}
				public void onProviderEnabled(String arg0) {
					Toast.makeText(context, "���ڽ��������ź�", Toast.LENGTH_LONG).show();
				}
				public void onStatusChanged(String arg0, int arg1,
						Bundle arg2) {
				}
			});
			ls.start();
			Toast.makeText(context, "��λ��...", Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}	
	}
}