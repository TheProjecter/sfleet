package simulation.msg;

import java.io.Serializable;

public class StationLogin implements Serializable {

	private int lat;
	
	private int log;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4295893679054345209L;

	public StationLogin(){
		
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
