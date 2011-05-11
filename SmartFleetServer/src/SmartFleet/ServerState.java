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
	
	private Map<Integer, ServerStation>	stations;
	
	public ServerState() {

		this.stations = new HashMap<Integer, ServerStation>();
		this.cars = new HashMap<Integer, ServerCar>();
		this.missingcars = new HashMap<Integer, ServerCar>();
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
}
