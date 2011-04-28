package messages;

import java.io.Serializable;
import java.util.HashMap;

import structs.RWCar;

public class StationRegisterMessage implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1830779935648468317L;
	
	private int id;
	
	private int lat;
	
	private int lon;
	
	private int port;
	
	private String ip;
	
	private HashMap<Integer, RWCar> carsDocked;

	public StationRegisterMessage(int id, int lat, int lon, int port, String ip, HashMap<Integer, RWCar> carsDocked){
		
		this.id = id;
		this.lat = lat;
		this.lon = lon;
		this.port = port;
		this.ip = ip;
		this.carsDocked = carsDocked;
		
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

	public int getLon() {
		return lon;
	}

	public void setLon(int lon) {
		this.lon = lon;
	}

	public void setCarsDocked(HashMap<Integer, RWCar> carsDocked) {
		this.carsDocked = carsDocked;
	}

	public HashMap<Integer, RWCar> getCarsDocked() {
		return carsDocked;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}
	
}
