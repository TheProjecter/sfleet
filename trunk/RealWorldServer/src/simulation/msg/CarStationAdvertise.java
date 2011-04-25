package simulation.msg;

import java.io.Serializable;

import simulation.structs.RWStation;

public class CarStationAdvertise implements Serializable{

	private RWStation station;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3313660416436344697L;

	public CarStationAdvertise(RWStation s){
		this.setSation(s);
	}

	public void setSation(RWStation s) {
		this.station = s;
	}

	public RWStation setSation() {
		return station;
	}
	
	
}
