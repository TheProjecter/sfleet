package messages;

import java.io.Serializable;

public class Station implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7555309895538946237L;
	
	private int lat;
	
	private int lon;
	
	public Station(int lat, int lon){
		this.setLat(lat);
		this.setLon(lon);
	}

	public void setLat(int lat) {
		this.lat = lat;
	}

	public int getLat() {
		return lat;
	}

	public void setLon(int lon) {
		this.lon = lon;
	}

	public int getLon() {
		return lon;
	}
}
