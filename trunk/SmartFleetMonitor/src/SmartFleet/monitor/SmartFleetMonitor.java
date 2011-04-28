package SmartFleet.monitor;

import java.util.HashMap;
import java.util.Map;

import structs.ServerCar;
import structs.ServerStation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class SmartFleetMonitor extends MapActivity {
	
	private MapView mapView;
	private MapController mapc;
	
	private boolean showcars;
	private boolean showstations;
	
	private Map<Integer, ServerStation> stationlist;
	private Map<Integer, ServerCar> carlist;
	
	private int myport = 5002;
	
	// Need handler for callbacks to the UI thread
	final Handler mHandler = new Handler();
	// Create runnable for posting
	final Runnable mUpdateResults = new Runnable() {
		public void run() {
			mapView.invalidate();
		}
	};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		this.setContentView(R.layout.main);
    	this.mapView = (MapView) findViewById(R.id.mapview);
    	
    	this.setShowcars(true);
    	this.setShowstations(true);
    	
    	this.stationlist = new HashMap<Integer, ServerStation>();
    	this.carlist = new HashMap<Integer, ServerCar>();  
       
    	this.mapc = mapView.getController();
    	//mapView.setBuiltInZoomControls(true);
		this.mapView.setSatellite(false);
		this.mapc.setZoom(12);
		this.mapc.setCenter(new GeoPoint(38736830, -9138181));
		this.mapc.animateTo(new GeoPoint(38736830, -9138181));
        this.mapView.getOverlays().add(new DrawOverlay(this));
        
        MonitorListener.setMainActivity(this, this.myport);
	    final Intent monitorListenerService = new Intent(this, MonitorListener.class);
		startService(monitorListenerService);
    }

    public void callStationInfo(ServerStation s){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setCancelable(true);
    	builder.setTitle("StationID: " + s.getId());
    	builder.setInverseBackgroundForced(false);
    	builder.setMessage("Latitude: " + s.getLat()/1E6 + "\n" +
    					   "Longitude: " + s.getLon()/1E6 + "\n\n");/* +
    					   "On Wait Clients: " + s.getnWaitPassengers() + "\n" +
    					   "Avg. Waiting Time: " + s.getAverageWaitTime() + " min.");*/
    	
    	builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    	  public void onClick(DialogInterface dialog, int which) {
    	    dialog.dismiss();
    	  }
    	});
    	/*builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
    	  public void onClick(DialogInterface dialog, int which) {
    	    dialog.dismiss();
    	  }
    	});*/
    	AlertDialog alert = builder.create();
    	alert.show();

    }
    
    public void callCarInfo(ServerCar s){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setCancelable(true);
    	builder.setTitle("CarID: " + s.getId());
    	builder.setInverseBackgroundForced(false);
    	builder.setMessage("Latitude: " + s.getLat()/1E6 + "\n" +
				   		   "Longitude: " + s.getLon()/1E6 + "\n\n");// +
				   		  // "Battery: " + (int)s.getPercentageBattery() + "%\n");
    	
    	builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    	  public void onClick(DialogInterface dialog, int which) {
    	    dialog.dismiss();
    	  }
    	});
    	/*builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
    	  public void onClick(DialogInterface dialog, int which) {
    	    dialog.dismiss();
    	  }
    	});*/
    	AlertDialog alert = builder.create();
    	alert.show();

    }
    
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public Map<Integer, ServerStation> getStationlist() {
		return stationlist;
	}

	public void setStationlist(Map<Integer, ServerStation> stationlist) {
		this.stationlist = stationlist;
	}

	public Map<Integer, ServerCar> getCarlist() {
		return carlist;
	}

	public void setCarlist(Map<Integer, ServerCar> carlist) {
		this.carlist = carlist;
	}
	
	public void ShowCars(View v){
		this.setShowcars(!this.showcars);
		this.mapView.invalidate();
	}
	
	public void ShowStations(View v){
		this.setShowstations(!this.showstations);
		this.mapView.invalidate();
	}

	public void setShowcars(boolean showcars) {
		this.showcars = showcars;
	}

	public boolean isShowcars() {
		return showcars;
	}

	public void setShowstations(boolean showstations) {
		this.showstations = showstations;
	}

	public boolean isShowstations() {
		return showstations;
	}
	
	
	
}