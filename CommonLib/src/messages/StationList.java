package messages;

import java.io.Serializable;
import java.util.ArrayList;

public class StationList implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 588916674561124420L;
	
	private ArrayList<Station> stations;
	
	public StationList(ArrayList<Station> stations){
		this.stations = stations;
	}

	public void setStations(ArrayList<Station> stations) {
		this.stations = stations;
	}

	public ArrayList<Station> getStations() {
		return stations;
	}
}
