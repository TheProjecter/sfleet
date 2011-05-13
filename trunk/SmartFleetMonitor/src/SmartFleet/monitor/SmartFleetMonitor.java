package SmartFleet.monitor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import structs.Flight;
import structs.RWCar;
import structs.Route;
import structs.ServerCar;
import structs.ServerStation;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
	
	private String serverip = "194.210.228.38";
	private int serverport = 6799;
	
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
		
        this.loadConfiguration();
        
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
        
        //UpdateMonitor.setMainActivity(this, this.serverport, this.serverip);
	    //final Intent monitorListenerService = new Intent(this, UpdateMonitor.class);
		//startService(monitorListenerService);
        
        //POPULATE
        ServerCar car0 = new ServerCar(0, 38765775, -9133007, 0, 36000, new Route(), 1111111, 111111, 10); 
        ServerCar car1 = new ServerCar(1, 38765775, -9133007, 0, 36000, new Route(), 1111111, 111111, 10);
        HashMap<Integer, RWCar> stationcarlist = new HashMap<Integer, RWCar>();
        RWCar rwcar0 = new RWCar();
        rwcar0.setId(car0.getId());
        rwcar0.setBattery(car0.getBattery());
        rwcar0.setClock(car0.getClock());
        rwcar0.setLat(car0.getLat());
        rwcar0.setLog(car0.getLon());
        rwcar0.setVelocity(car0.getVelocity());
        RWCar rwcar1 = new RWCar();
        rwcar1.setId(car1.getId());
        rwcar1.setBattery(car1.getBattery());
        rwcar1.setClock(car1.getClock());
        rwcar1.setLat(car1.getLat());
        rwcar1.setLog(car1.getLon());
        rwcar1.setVelocity(car1.getVelocity());
        stationcarlist.put(0, rwcar0);
        stationcarlist.put(1, rwcar1);
        
        ServerStation station2 = new ServerStation(2, 38765775, -9133007, 0, "", 23, stationcarlist, new ArrayList<Flight>());
    
        this.carlist.put(0, car0);
        this.carlist.put(1, car1);
        this.stationlist.put(2, station2);
    
    }

    public void callStationInfo(ServerStation s){
    	
    	int numberwaiting = 0;
    	double avgtime = s.getAvgwaittime() / 1000;
    	HashMap<Integer, RWCar> carlist = s.getCarsDocked();
    	ArrayList<Flight> flist = s.getFlightqueue();
    	
    	for(Flight f : flist){
    		numberwaiting += f.getNpassengers();
    	}
    		
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setCancelable(true);
    	builder.setTitle("StationID: " + s.getId());
    	builder.setInverseBackgroundForced(false);
    	builder.setMessage("Latitude: " + s.getLat()/1E6 + "\n" +
    					   "Longitude: " + s.getLon()/1E6 + "\n\n" +
    					   "On Wait Clients: " + numberwaiting + "\n" +
    					   "Avg. Waiting Time: " + avgtime + " sec.");
    	/*final CharSequence[] items = {"Red", "Green", "Blue"};
    	builder.setItems(items, new DialogInterface.OnClickListener() {
      	  public void onClick(DialogInterface dialog, int which) {
      	    dialog.dismiss();
      	  }
      	});*/
    	
    	builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    	  public void onClick(DialogInterface dialog, int which) {
    	    dialog.dismiss();
    	  }
    	});
    	
    	final SmartFleetMonitor smf = this;
    	final ServerStation ser = s;
    	
    	builder.setNegativeButton("Cars", new DialogInterface.OnClickListener() {

    		ServerStation serv = ser;
    		
    		public void onClick(DialogInterface dialog, int which) {
    			AlertDialog.Builder builder = new AlertDialog.Builder(smf);
    			builder.setCancelable(true);
    			builder.setTitle("Cars");
    			
    			final CharSequence[] items = new CharSequence[serv.getCarsDocked().size()];
    			    
    			int i = 0;
    			for(Integer c : serv.getCarsDocked().keySet()){
    				items[i] = "Car" + c;
    				i++;
    			}
    			
    			builder.setItems(items, new DialogInterface.OnClickListener() {
    				
    				public void onClick(DialogInterface dialog, int which) {
    					CharSequence s = items[which];
    					int i = Integer.parseInt(""+s.charAt(3));
    					ServerCar sc = smf.carlist.get(i);
    					smf.callCarInfo(sc);
    				}
    			});
    			AlertDialog alert = builder.create();
    	    	alert.show();
    		}
    	});

    	AlertDialog alert = builder.create();
    	alert.show();

    }
    
    public void callCarInfo(ServerCar s){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setCancelable(true);
    	builder.setTitle("CarID: " + s.getId());
    	builder.setInverseBackgroundForced(false);
    	int passengers = 0;
    	for(Flight f : s.getRoute().getRoute()){
    		passengers += f.getNpassengers();
    	}
    	builder.setMessage("Number of Passenger: " + passengers + "\n\n" +
				   		   "Battery: " + (int)s.getBattery() + "\n\n");// +
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
    
    public void callOverallInfo(){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setCancelable(true);
    	builder.setTitle("Overall Statistics");
    	builder.setInverseBackgroundForced(false);
    	builder.setMessage("Total Passengers: " + "\n\n" +
				   		   "Total Kms: " + "\n\n" +
				   		   "Total Battery waste: " + "\n\n" +
				   		   "Average time flying: " + "\n\n" +
				   		   "Lost Vehicles: " + "\n\n" +
				   		   "Distance to be travelled: " + "\n\n");
    	
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

	public void setServerip(String serverip) {
		this.serverip = serverip;
	}

	public String getServerip() {
		return serverip;
	}

	public void setServerport(int serverport) {
		this.serverport = serverport;
	}

	public int getServerport() {
		return serverport;
	}
	
	public void loadConfiguration(){
		
		try {
			 BufferedReader in = new BufferedReader(new FileReader("/mnt/sdcard/config.txt"));
			 
			 this.serverip = in.readLine();
			 this.serverport = Integer.valueOf(in.readLine());
			 
			 Log.d("coisas", this.serverip);
			 Log.d("cenas", "" + this.serverport);
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
}