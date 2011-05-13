package messages;

import java.io.Serializable;
import java.util.Map;

import structs.ServerCar;
import structs.ServerStation;

public class Snapshot implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8125549455440328840L;

	private Map<Integer, ServerStation> stations;
	
	private Map<Integer, ServerCar> cars;
	
	private Map<Integer, ServerCar> missings;
	
	private int total_people = 0;
	
	private double total_battery = 0;
	
	private double total_km = 0;
	
	private int nflights = 0;
	
	private double total_time = 0;
	
	private double straight_sum = 0;
	
	public Snapshot(Map<Integer, ServerStation> stations, Map<Integer, ServerCar> cars, Map<Integer, ServerCar> miss){
		this.stations = stations;
		this.cars = cars;
		this.missings = miss;
	}

	public Map<Integer, ServerStation> getStations() {
		return stations;
	}

	public void setStations(Map<Integer, ServerStation> stations) {
		this.stations = stations;
	}

	public Map<Integer, ServerCar> getCars() {
		return cars;
	}

	public void setCars(Map<Integer, ServerCar> cars) {
		this.cars = cars;
	}

	public void setMissings(Map<Integer, ServerCar> missings) {
		this.missings = missings;
	}

	public Map<Integer, ServerCar> getMissings() {
		return missings;
	}

	public void setTotal_people(int total_people) {
		this.total_people = total_people;
	}

	public int getTotal_people() {
		return total_people;
	}

	public double getTotal_battery() {
		return total_battery;
	}

	public void setTotal_battery(double totalBattery) {
		total_battery = totalBattery;
	}

	public double getTotal_km() {
		return total_km;
	}

	public void setTotal_km(double totalKm) {
		total_km = totalKm;
	}

	public int getNflights() {
		return nflights;
	}

	public void setNflights(int nflights) {
		this.nflights = nflights;
	}

	public double getTotal_time() {
		return total_time;
	}

	public void setTotal_time(double totalTime) {
		total_time = totalTime;
	}

	public void setStraight_sum(double straight_sum) {
		this.straight_sum = straight_sum;
	}

	public double getStraight_sum() {
		return straight_sum;
	}
	
	
}
