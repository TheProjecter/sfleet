package structs;

import java.io.Serializable;

public class ServerCar implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1065720900677295705L;

	private int id;
	
	private int lat;
	
	private int lon;
	
	private int clock;
	
	private double battery;
	
	private Route route;
	
	public ServerCar(int id, int lat, int lon, int clock, double battery, Route route){
		this.id = id;
		this.lat = lat;
		this.lon = lon;
		this.clock = clock;
		this.battery = battery;
		this.route = route;
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

	public int getClock() {
		return clock;
	}

	public void setClock(int clock) {
		this.clock = clock;
	}

	public double getBattery() {
		return battery;
	}

	public void setBattery(double battery) {
		this.battery = battery;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}
}
