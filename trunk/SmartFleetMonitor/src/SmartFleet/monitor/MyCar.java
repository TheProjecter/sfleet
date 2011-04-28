package SmartFleet.monitor;

import com.google.android.maps.GeoPoint;

public class MyCar {
	
	private double battery;

	private int id;

	private double max_battery;

	//private GeoPoint myDestination;

	private int lat;
	
	private int lon;

	public MyCar(int id, double battery, int lat, int lon){
		this.battery = battery;
		this.id = id;
		this.max_battery = 10 * 3600;
		this.lat = lat;
		this.lon = lon;
	}
	
	public double getBattery() {
		return battery;
	}

	public void setBattery(double battery) {
		this.battery = battery;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public double getPercentageBattery() {

		double res = (this.battery / this.max_battery) * 100;
		return res;
	}

	public double getMax_battery() {
		return max_battery;
	}

	public void setMax_battery(double max_battery) {
		this.max_battery = max_battery;
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
