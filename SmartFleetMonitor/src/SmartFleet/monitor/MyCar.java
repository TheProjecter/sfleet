package SmartFleet.monitor;

import com.google.android.maps.GeoPoint;

public class MyCar {
	
	private double battery;

	private double height;

	private int id;

	private double max_battery;

	private GeoPoint myDestination;

	private GeoPoint myLocation;

	public MyCar(int id, double battery, double height, GeoPoint dest, GeoPoint location){
		this.battery = battery;
		this.height = height;
		this.id = id;
		this.max_battery = 10 * 3600;
		this.myDestination = dest;
		this.myLocation = location;
	}
	
	public double getBattery() {
		return battery;
	}

	public void setBattery(double battery) {
		this.battery = battery;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public GeoPoint getMyDestination() {
		return myDestination;
	}

	public void setMyDestination(GeoPoint myDestination) {
		this.myDestination = myDestination;
	}

	public GeoPoint getMyLocation() {
		return myLocation;
	}

	public void setMyLocation(GeoPoint myLocation) {
		this.myLocation = myLocation;
	}
	
	public double getPercentageBattery() {

		double res = (this.battery / this.max_battery) * 100;
		return res;
	}
	
}
