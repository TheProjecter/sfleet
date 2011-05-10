package SmartFleet;

import java.util.HashMap;
import java.util.Map;

import structs.ServerCar;
import structs.ServerStation;

public class ServerState {
	
	private Map<Integer, ServerStation> stations;
	
	private Map<Integer, ServerCar> cars;
	
	private Map<Integer, ServerCar> missingcars;
	
	public ServerState(){
		this.stations = new HashMap<Integer, ServerStation>();
		this.cars = new HashMap<Integer, ServerCar>();
		this.missingcars = new HashMap<Integer, ServerCar>();
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

	public void setMissingcars(Map<Integer, ServerCar> missingcars) {
		this.missingcars = missingcars;
	}

	public Map<Integer, ServerCar> getMissingcars() {
		return missingcars;
	}
}