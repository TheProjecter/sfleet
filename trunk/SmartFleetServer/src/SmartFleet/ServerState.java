package SmartFleet;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import server.messages.CarMessage;
import server.messages.StationMessage;


public class ServerState {
	
	private Map<Integer, StationMessage> stations;
	
	private Map<Integer, CarMessage> cars;
	
	public ServerState(){
		this.stations = new HashMap<Integer, StationMessage>();
		this.cars = new HashMap<Integer, CarMessage>();
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
