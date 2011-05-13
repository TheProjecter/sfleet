package SmartFleet.monitor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import structs.Flight;
import structs.RWCar;
import structs.Route;
import structs.ServerCar;
import structs.ServerStation;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
	private Map<Integer, ServerCar> missinglist;
	private int total_people = 0;
	private double total_battery = 0;	
	private double total_km = 0;
	private int nflights = 0;
	private double total_time = 0;
	private double straightsum = 0;
	
	private String serverip = "169.254.247.246";
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
		
        //this.loadConfiguration();
        
		this.setContentView(R.layout.main);
    	this.mapView = (MapView) findViewById(R.id.mapview);
    	
    	this.setShowcars(true);
    	this.setShowstations(true);
    	
    	this.stationlist = new HashMap<Integer, ServerStation>();
    	this.carlist = new HashMap<Integer, ServerCar>();  
    	this.missinglist = new HashMap<Integer, ServerCar>();  
    	
    	this.mapc = mapView.getController();
    	//mapView.setBuiltInZoomControls(true);
		this.mapView.setSatellite(false);
		this.mapc.setZoom(12);
		this.mapc.setCenter(new GeoPoint(38736830, -9138181));
		this.mapc.animateTo(new GeoPoint(38736830, -9138181));
        this.mapView.getOverlays().add(new DrawOverlay(this));
        
        UpdateMonitor.setMainActivity(this, this.serverport, this.serverip);
	    final Intent monitorListenerService = new Intent(this, UpdateMonitor.class);
		startService(monitorListenerService);
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
    	
    	String strg = "";
    	for(Integer i : s.getLatestcarseen()){
    		strg += "Car" + i + " ";
    	}
    	
    	builder.setMessage("Number of Passenger: " + passengers + "\n" +
				   		   "Battery: " + (int)((s.getBattery()/36000)*100) + "%\n\n" +
				   		   "Latest cars seen: " + strg + "\n" +
				   		   "Date of info: " + s.getInformationTime());
    	
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
    
    public void callOverallInfo(View v){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setCancelable(true);
    	builder.setTitle("Overall Statistics");
    	builder.setInverseBackgroundForced(false);
    	
    	String missings = "";
    	for(ServerCar sc : this.missinglist.values()){
    		missings += "Car" + sc.getId() + " ";
    	}
    	
    	builder.setMessage("Total Passengers: "+ this.total_people + "\n" +
				   		   "Total Kms: " + String.format("%.2g%n",this.total_km) +"\n" +
				   		   "Total Battery waste: " + (int)this.total_battery + "\n" +
				   		   "Average time flying: " + String.format("%.2g%n", this.total_time/this.nflights) + "\n\n" +
				   		   "Number of Lost Vehicles: " + this.missinglist.size() + "\n" +
				   		   "Lost Vehicles: " + missings + "\n\n" +
				   		   "Distance to be travelled: " + this.straightsum);
    	
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
	
	
	public Map<Integer, ServerCar> getMissinglist() {
		return missinglist;
	}

	public void setMissinglist(Map<Integer, ServerCar> missinglist) {
		this.missinglist = missinglist;
	}

	public int getTotal_people() {
		return total_people;
	}
	
	public void setTotal_people(int totalPeople) {
		total_people = totalPeople;
	}

	public double getTotal_battery() {
		return total_battery;
	}

	public void setTotal_battery(double totalBattery) {
		total_battery = totalBattery;
	}

	public double getTotal_km() {
		return total_km;
	}

	public void setTotal_km(double totalKm) {
		total_km = totalKm;
	}

	public int getNflights() {
		return nflights;
	}

	public void setNflights(int nflights) {
		this.nflights = nflights;
	}

	public double getTotal_time() {
		return total_time;
	}

	public void setTotal_time(double totalTime) {
		total_time = totalTime;
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

	public void setStraightsum(double straightsum) {
		this.straightsum = straightsum;
	}

	public double getStraightsum() {
		return straightsum;
	}
	
	
}