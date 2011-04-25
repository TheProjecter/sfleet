package simulation.structs;

public class RWCar {

	private int id;
	
	private String ip;
	
	private int port;
	
	private double height;
	
	private int lat;
	
	private int log;
	
	public RWCar(){
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getHeight() {
		return height;
	}

	public void setLat(int lat) {
		this.lat = lat;
	}

	public int getLat() {
		return lat;
	}

	public void setLog(int log) {
		this.log = log;
	}

	public int getLog() {
		return log;
	}
	
	
	
}
