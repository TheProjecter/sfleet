package SmartFleet.monitor;

public class MyStation {

	private int id;

	private int lat;
	
	private int lon;

	private int nWaitPassengers;
	
	private double AverageWaitTime;

	public MyStation(int id, int lat, int lon, int nWaitP, double avgwt){
		
		this.id = id;
		this.lat = lat;
		this.lon = lon;
		this.setnWaitPassengers(nWaitP);
		this.setAverageWaitTime(avgwt);
		
	}
		
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public void setnWaitPassengers(int nWaitPassengers) {
		this.nWaitPassengers = nWaitPassengers;
	}

	public int getnWaitPassengers() {
		return nWaitPassengers;
	}

	public void setAverageWaitTime(double averageWaitTime) {
		AverageWaitTime = averageWaitTime;
	}

	public double getAverageWaitTime() {
		return AverageWaitTime;
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
	
}
