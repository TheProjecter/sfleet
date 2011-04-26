package messages;

import java.io.Serializable;

public class CarUpdate implements Serializable{

	private int id;
	
	private int lat;
	
	private int log;
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -1205390432576044131L;

	public CarUpdate(){
		
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

	public int getLog() {
		return log;
	}

	public void setLog(int log) {
		this.log = log;
	}
		
	
}
