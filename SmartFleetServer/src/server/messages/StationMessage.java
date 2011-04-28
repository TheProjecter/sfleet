package server.messages;

import java.io.Serializable;

public class StationMessage implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1707569896043429474L;

	private int passengers;
	
	private double waitingtime;
	
	private int id;
	
	private int lat;
	
	private int lon;

	public StationMessage(int id, int lat, int lon, int passengers, double waitingtime){
		
		this.id = id;
		this.passengers = passengers;
		this.waitingtime = waitingtime;
		this.lat = lat;
		this.lon = lon;
		
	}

	public int getPassengers() {
		return passengers;
	}

	public void setPassengers(int passengers) {
		this.passengers = passengers;
	}

	public double getWaitingtime() {
		return waitingtime;
	}

	public void setWaitingtime(double waitingtime) {
		this.waitingtime = waitingtime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLat() {
		return lat;
	}

	public void setLat(int lat) {
		this.lat = lat;
	}

	public int getLon() {
		return lon;
	}

	public void setLon(int lon) {
		this.lon = lon;
	}
	
}
