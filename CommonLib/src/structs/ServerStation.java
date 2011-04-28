package structs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerStation implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -543350088638097145L;
	private int id;
	private int lat;
	private int lon;
	private int port;
	private String ip;
	private HashMap<Integer, RWCar> carsDocked;
	private ArrayList<Flight> flightqueue;

	public ServerStation(int id, int lat, int lon, int port, String ip, HashMap<Integer, RWCar> carsDocked, ArrayList<Flight> flightqueue){
		this.id = id;
		this.lat = lat;
		this.setLon(lon);
		this.ip = ip;
		this.port = port;
		this.carsDocked = carsDocked;
		this.flightqueue = flightqueue;
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLat() {
		return lat;
	}

	public void setLat(int lat) {
		this.lat = lat;
	}

	public void setLon(int lon) {
		this.lon = lon;
	}

	public int getLon() {
		return lon;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public HashMap<Integer, RWCar> getCarsDocked() {
		return carsDocked;
	}

	public void setCarsDocked(HashMap<Integer, RWCar> carsDocked) {
		this.carsDocked = carsDocked;
	}

	public ArrayList<Flight> getFlightqueue() {
		return flightqueue;
	}

	public void setFlightqueue(ArrayList<Flight> flightqueue) {
		this.flightqueue = flightqueue;
	}
	
	
}
