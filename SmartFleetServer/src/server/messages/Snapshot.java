package server.messages;

import java.io.Serializable;
import java.util.Map;

public class Snapshot implements Serializable {

	private Map<Integer, StationMessage> stations;
	
	private Map<Integer, CarMessage> cars;
	
	public Snapshot(Map<Integer, StationMessage> stations, Map<Integer, CarMessage> cars){
		this.stations = stations;
		this.cars = cars;
	}

	public Map<Integer, StationMessage> getStations() {
		return stations;
	}

	public void setStations(Map<Integer, StationMessage> stations) {
		this.stations = stations;
	}

	public Map<Integer, CarMessage> getCars() {
		return cars;
	}

	public void setCars(Map<Integer, CarMessage> cars) {
		this.cars = cars;
	}
}
