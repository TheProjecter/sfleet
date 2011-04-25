package SmartFleet.station;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.maps.GeoPoint;

public class FlyingStation {

	private GeoPoint mylocation;

	HashMap<String, GeoPoint> destinations;
	//HashMap<Integer, Info> myCars;
	ArrayList<Flight> flightqueue;

	public FlyingStation(GeoPoint location){
		
		this.mylocation = location;
		this.destinations = new HashMap<String, GeoPoint>();
		//this.myCars = new HashMap<Integer, Info>();
		this.flightqueue = new ArrayList<Flight>();
		
	}
	
	public void setMylocation(GeoPoint mylocation) {
		this.mylocation = mylocation;
	}
	
	public GeoPoint getMylocation() {
		return mylocation;
	}
	
	public void setDestinations(HashMap<String, GeoPoint> destlist){
		destinations = destlist;
	}
	
	public HashMap<String, GeoPoint> getDestinations(){
		return destinations;
	}
	
	public void setFlightQueue(ArrayList<Flight> fqueue){
		flightqueue = fqueue;
	}
	
	public ArrayList<Flight> getFlightQueue(){
		return flightqueue;
	}	
	
}
