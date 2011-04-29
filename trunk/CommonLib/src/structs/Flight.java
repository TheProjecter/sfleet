package structs;

import java.io.Serializable;

public class Flight implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8785288674218776171L;
	private String partyname;
	private int npassengers;
	private String destination;
	private int lat;
	private int lon;
	private long timeof;
	
	public Flight(){
		this.partyname = "";
		this.npassengers = 1;
		this.destination = null;
		this.lat = 0;
		this.lon = 0;
	}
	
	public Flight(String partyname, int npassengers, String destination, int lat, int lon){
		this.partyname = partyname;
		this.npassengers = npassengers;
		this.destination = destination;
		this.lat = lat;
		this.lon = lon;
	}
	
	public void setPartyname(String partyname) {
		this.partyname = partyname;
	}
	public String getPartyname() {
		return partyname;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getDestination() {
		return destination;
	}
	public void setNpassengers(int npassengers) {
		this.npassengers = npassengers;
	}
	public int getNpassengers() {
		return npassengers;
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

	public void setTimeof(long timeof) {
		this.timeof = timeof;
	}

	public long getTimeof() {
		return timeof;
	}
	
}
