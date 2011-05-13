package messages;

import java.io.Serializable;
import java.util.ArrayList;

import structs.RWCar;

public class CarUpdateResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3899103853284357433L;

	boolean nearStation;
	
	private ArrayList<RWCar> carsAt200;
	
	private ArrayList<RWCar> carsAt300;

	public CarUpdateResponse(ArrayList<RWCar> carsAt200, ArrayList<RWCar> carsAt300, boolean nearStation){
		this.nearStation = nearStation;
		this.carsAt200 = carsAt200;
		this.carsAt300 = carsAt300;
	}

	public ArrayList<RWCar> getCarsAt200() {
		return carsAt200;
	}

	public void setCarsAt200(ArrayList<RWCar> carsAt200) {
		this.carsAt200 = carsAt200;
	}

	public ArrayList<RWCar> getCarsAt300() {
		return carsAt300;
	}

	public void setCarsAt300(ArrayList<RWCar> carsAt300) {
		this.carsAt300 = carsAt300;
	}

	public boolean isNearStation() {
		return nearStation;
	}

	public void setNearStation(boolean nearStation) {
		this.nearStation = nearStation;
	}
	
}
