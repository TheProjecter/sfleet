package messages;

import java.io.Serializable;

import structs.RWStation;

public class CarStationAdvertise implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7297886811684350699L;

	private RWStation station;
	
	public CarStationAdvertise(){
		
	}

	public void setStation(RWStation station) {
		this.station = station;
	}

	public RWStation getStation() {
		return station;
	}
	
}
