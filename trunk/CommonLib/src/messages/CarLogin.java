package messages;

import java.io.Serializable;

public class CarLogin implements Serializable{

	private int lat;
	
	private int log;
	
	private double height;
	
	private int port;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8141285709980451977L;

	public CarLogin(){
		
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

	public void setHeight(double height) {
		this.height = height;
	}

	public double getHeight() {
		return height;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}
	
}
