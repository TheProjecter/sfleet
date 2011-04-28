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
	
	public Snapshot(Map<Integer, ServerStation> stations, Map<Integer, ServerCar> cars){
		this.stations = stations;
		this.cars = cars;
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
}
