package SmartFleet.monitor;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
	
	private ArrayList<MyStation> stationlist;
	private ArrayList<MyCar> carlist;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		this.setContentView(R.layout.main);
    	this.mapView = (MapView) findViewById(R.id.mapview);
    	
    	this.setShowcars(true);
    	this.setShowstations(true);
    	
    	this.stationlist = new ArrayList<MyStation>();
    	this.carlist = new ArrayList<MyCar>();
    	
    	//POPULATE MONITOR FOR TESTING
    	MyStation IST = new MyStation(0, new GeoPoint(38736830, -9138181), 3, 2);
    	MyStation TP = new MyStation(1, new GeoPoint(38736320, -9301917), 10, 10);
    	MyStation AIRPORT = new MyStation(2, new GeoPoint(38765775, -9133007), 40, 30);
    	
    	MyCar Cenas = new MyCar(4, 26000, 200, new GeoPoint(38736320, -9301917), new GeoPoint(38736830, -9138181));
    	MyCar Cenas2 = new MyCar(8, 4000, 0, new GeoPoint(38765775, -9133007), new GeoPoint(38765775, -9133007));
    	
    	this.stationlist.add(IST);
    	this.stationlist.add(TP);
    	this.stationlist.add(AIRPORT);
    	
    	this.carlist.add(Cenas);
    	this.carlist.add(Cenas2);
      
       
    	this.mapc = mapView.getController();
    	//mapView.setBuiltInZoomControls(true);
		this.mapView.setSatellite(false);
		this.mapc.setZoom(12);
		this.mapc.setCenter(new GeoPoint(38736830, -9138181));
		this.mapc.animateTo(new GeoPoint(38736830, -9138181));
        this.mapView.getOverlays().add(new DrawOverlay(this));
    }

    public void callStationInfo(MyStation s){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setCancelable(true);
    	builder.setTitle("StationID: " + s.getId());
    	builder.setInverseBackgroundForced(false);
    	builder.setMessage("Latitude: " + s.getMylocation().getLatitudeE6()/1E6 + "\n" +
    					   "Longitude: " + s.getMylocation().getLongitudeE6()/1E6 + "\n\n" +
    					   "On Wait Clients: " + s.getnWaitPassengers() + "\n" +
    					   "Avg. Waiting Time: " + s.getAverageWaitTime() + " min.");
    	
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
    
    public void callCarInfo(MyCar s){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setCancelable(true);
    	builder.setTitle("CarID: " + s.getId());
    	builder.setInverseBackgroundForced(false);
    	builder.setMessage("Latitude: " + s.getMyLocation().getLatitudeE6()/1E6 + "\n" +
				   		   "Longitude: " + s.getMyLocation().getLongitudeE6()/1E6 + "\n\n" +
				   		   "Battery: " + (int)s.getPercentageBattery() + "%\n" +
    					   "Heigh: " + s.getHeight() + "m\n");
    	
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

	public ArrayList<MyStation> getStationlist() {
		return stationlist;
	}

	public void setStationlist(ArrayList<MyStation> stationlist) {
		this.stationlist = stationlist;
	}

	public ArrayList<MyCar> getCarlist() {
		return carlist;
	}

	public void setCarlist(ArrayList<MyCar> carlist) {
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