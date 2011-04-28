package messages;

import java.io.Serializable;

public class CarMessage implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5993693991706197110L;

	private int id;
	
	private int lat;
	
	private int lon;
	
	private double battery;

	public CarMessage(int id, int lat, int lon, double battery){
		
		this.id = id;
		this.lat = lat;
		this.lon = lon;
		this.battery = battery;
		
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

	public double getBattery() {
		return battery;
	}

	public void setBattery(double battery) {
		this.battery = battery;
	}
	
}
