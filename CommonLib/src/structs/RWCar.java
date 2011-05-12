package structs;

import java.io.Serializable;

public class RWCar implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2200354606893369003L;

	private int id;
	
	private String ip;
	
	private int port;
	
	private double height;
	
	private double max_battery;
	
	private double battery;
	
	private int lat;
	
	private int log;
	
	private int clock;
	
	private Route route;
	
	private int distance;
	
	private double velocity;
	
	public RWCar(){
		this.max_battery = 36000;
	}
	
	public RWCar(int id, double battery, double height, int clock, int distance){
		this.id = id;
		this.battery = battery;
		this.height = height;
		this.clock = clock;
		this.distance = distance;
	}
	
	public RWCar(String ip, int port){
		this.ip = ip;
		this.port = port;
	}
	
	public RWCar(int id, double battery, int lat, int log, int clock, Route route, double velocity){
		this.id = id;
		this.battery = battery;
		this.lat = lat;
		this.log = log;
		this.route = route;
		this.clock = clock;
		this.velocity = velocity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getHeight() {
		return height;
	}

	public void setLat(int lat) {
		this.lat = lat;
	}

	public int getLat() {
		return lat;
	}

	public void setLog(int log) {
		this.log = log;
	}

	public int getLog() {
		return log;
	}

	public void setBattery(double battery) {
		this.battery = battery;
	}

	public double getBattery() {
		return battery;
	}
	
	public double getPercentageBattery() {

		double res = (this.battery / this.max_battery) * 100;
		return res;
	}

	public void setClock(int clock) {
		this.clock = clock;
	}

	public int getClock() {
		return clock;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public Route getRoute() {
		return route;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getDistance() {
		return distance;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	public double getVelocity() {
		return velocity;
	}
	
	
	
}
