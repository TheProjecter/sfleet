package SmartFleet.monitor;

import java.util.ArrayList;

import SmartFleet.station.Flight;

import com.google.android.maps.GeoPoint;

public class MyStation {

	private int id;

	private GeoPoint mylocation;

	private int nWaitPassengers;
	
	private double AverageWaitTime;

	public MyStation(int id, GeoPoint location, int nWaitP, double avgwt){
		
		this.id = id;
		this.mylocation = location;
		this.setnWaitPassengers(nWaitP);
		this.setAverageWaitTime(avgwt);
		
	}
	
	public void setMylocation(GeoPoint mylocation) {
		this.mylocation = mylocation;
	}
	
	public GeoPoint getMylocation() {
		return mylocation;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public void setnWaitPassengers(int nWaitPassengers) {
		this.nWaitPassengers = nWaitPassengers;
	}

	public int getnWaitPassengers() {
		return nWaitPassengers;
	}

	public void setAverageWaitTime(double averageWaitTime) {
		AverageWaitTime = averageWaitTime;
	}

	public double getAverageWaitTime() {
		return AverageWaitTime;
	}
	
	
}
