package SmartFleet.station;

import java.util.ArrayList;
import java.util.HashMap;

import messages.Station;

import structs.Flight;

import com.google.android.maps.GeoPoint;

public class FlyingStation {

	private GeoPoint mylocation;

	private HashMap<String, GeoPoint> destinations;

	private ArrayList<Flight> flightqueue;
	private ArrayList<Long> waitingTime;
	
	private ArrayList<Station> stations;

	public FlyingStation(GeoPoint location){
		
		this.mylocation = location;
		this.destinations = new HashMap<String, GeoPoint>();
		this.flightqueue = new ArrayList<Flight>();
		this.stations = new ArrayList<Station>();
		this.waitingTime = new ArrayList<Long>();
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

	public void setStations(ArrayList<Station> stations) {
		this.stations = stations;
	}

	public ArrayList<Station> getStations() {
		return stations;
	}

	public void setWaitingTime(ArrayList<Long> waitingTime) {
		this.waitingTime = waitingTime;
	}

	public ArrayList<Long> getWaitingTime() {
		return waitingTime;
	}	
	
}
