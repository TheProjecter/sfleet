package structs;

import java.io.Serializable;
import java.util.LinkedList;

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
	
	private long informationTime;
	
	private long timeToUpdate;
	
	private double velocity;
	
	private LinkedList<Integer> latestcarseen;
	
	public ServerCar(int id, int lat, int lon, int clock, double battery, Route route, long informationTime, long timeToUpdate, double velocity){
		this.id = id;
		this.lat = lat;
		this.lon = lon;
		this.clock = clock;
		this.battery = battery;
		this.route = route;
		this.informationTime = informationTime;
		this.timeToUpdate = timeToUpdate;
		this.setVelocity(velocity);
		this.latestcarseen = new LinkedList<Integer>();
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

	public void setInformationTime(long informationTime) {
		this.informationTime = informationTime;
	}

	public long getInformationTime() {
		return informationTime;
	}

	public void setTimeToUpdate(long timeToUpdate) {
		this.timeToUpdate = timeToUpdate;
	}

	public long getTimeToUpdate() {
		return timeToUpdate;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	public double getVelocity() {
		return velocity;
	}

	public void setLatestcarseen(LinkedList<Integer> latestcarseen) {
		this.latestcarseen = latestcarseen;
	}

	public LinkedList<Integer> getLatestcarseen() {
		return latestcarseen;
	}
}
