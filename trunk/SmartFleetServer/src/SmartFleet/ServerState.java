package SmartFleet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import structs.ServerCar;
import structs.ServerStation;

public class ServerState implements Serializable {
	
	/**
	 * 
	 */
	private static final long			serialVersionUID	= -3356659562904866677L;
	
	private Map<Integer, ServerCar>		cars;
	
	private Map<Integer, ServerCar>		missingcars;
	
	private Map<Integer, ServerCar>		carsfound;
	
	private Map<Integer, ServerStation>	stations;
	
	private int total_people = 0;
	
	private double total_battery = 0;
	
	private double total_km = 0;
	
	private int nflights = 0;
	
	private double total_time = 0;
	
	public ServerState() {

		this.stations = new HashMap<Integer, ServerStation>();
		this.cars = new HashMap<Integer, ServerCar>();
		this.missingcars = new HashMap<Integer, ServerCar>();
		this.carsfound = new HashMap<Integer, ServerCar>();

	}
	
	public Map<Integer, ServerCar> getCars() {

		return this.cars;
	}
	
	public Map<Integer, ServerCar> getMissingcars() {

		return this.missingcars;
	}
	
	public Map<Integer, ServerStation> getStations() {

		return this.stations;
	}
	
	public void setCars(Map<Integer, ServerCar> cars) {

		this.cars = cars;
	}
	
	public void setMissingcars(Map<Integer, ServerCar> missingcars) {

		this.missingcars = missingcars;
	}
	
	public void setStations(Map<Integer, ServerStation> stations) {

		this.stations = stations;
	}

	public void setCarsfound(Map<Integer, ServerCar> carsfound) {
		this.carsfound = carsfound;
	}

	public Map<Integer, ServerCar> getCarsfound() {
		return carsfound;
	}

	public void setNpeople(int npeople) {
		this.total_people = npeople;
	}

	public int getNpeople() {
		return total_people;
	}

	public void setTotal_battery(double total_battery) {
		this.total_battery = total_battery;
	}

	public double getTotal_battery() {
		return total_battery;
	}

	public void setTotal_km(double total_km) {
		this.total_km = total_km;
	}

	public double getTotal_km() {
		return total_km;
	}

	public void setNflights(int nflights) {
		this.nflights = nflights;
	}

	public int getNflights() {
		return nflights;
	}

	public void setTotal_time(double total_time) {
		this.total_time = total_time;
	}

	public double getTotal_time() {
		return total_time;
	}
}
