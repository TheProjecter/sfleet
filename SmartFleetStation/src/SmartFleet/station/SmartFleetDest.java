package SmartFleet.station;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class SmartFleetDest extends MapActivity{

	private MapView mapView;
	private MapController mapc;
	private SelectionOverlay solay;
	
	private static SmartFleetStation sfs;
	
	public static void setMainActivity(SmartFleetStation activity) {
		sfs = activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		this.setContentView(R.layout.selectdestination);
    	this.mapView = (MapView) findViewById(R.id.mapview);
    	
    	this.mapc = mapView.getController();
    	mapView.setBuiltInZoomControls(true);
		this.mapView.setSatellite(false);
		this.mapc.setZoom(16);
		GeoPoint me = sfs.getMylocation();
		this.mapc.setCenter(new GeoPoint(me.getLatitudeE6(), me.getLongitudeE6()));
		this.mapc.animateTo(new GeoPoint(me.getLatitudeE6(), me.getLongitudeE6()));
		this.solay = new SelectionOverlay();
		this.mapView.getOverlays().add(this.solay);
	}

	public void okSelectDestination(View V){

		if(this.solay.getDestination() != null){
			GeoPoint gp = this.solay.getDestination();
			Intent me = this.getIntent();
			me.putExtra("log", gp.getLongitudeE6());
			me.putExtra("lat", gp.getLatitudeE6());
			this.setResult(RESULT_OK, me);
		}else{
			this.setResult(RESULT_CANCELED);
		}
		finish();
    }
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
