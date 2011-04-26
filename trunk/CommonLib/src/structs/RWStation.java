package structs;

import java.io.Serializable;

public class RWStation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4027848765924524232L;

	private int id;
	
	private String ip;
	
	private int port;
	
	private int lat;
	
	private int log;
	
	public RWStation(){
		
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

	public int getLat() {
		return lat;
	}

	public void setLat(int lat) {
		this.lat = lat;
	}

	public int getLog() {
		return log;
	}

	public void setLog(int log) {
		this.log = log;
	}
	
	
	
}
