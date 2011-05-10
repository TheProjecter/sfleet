package messages;

import java.io.Serializable;

import structs.Route;

public class CarRegisterMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2514072869982996844L;

	private int id;
	
	private int lat;
	
	private int lon;
	
	private Route route;

	public CarRegisterMessage(int id, int lat, int lon, Route route){
		
		this.id = id;
		this.lat = lat;
		this.lon = lon;
		this.route = route;
		
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

	public void setRoute(Route route) {
		this.route = route;
	}

	public Route getRoute() {
		return route;
	}
}
