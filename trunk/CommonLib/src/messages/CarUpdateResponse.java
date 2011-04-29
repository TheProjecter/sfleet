package messages;

import java.io.Serializable;
import java.util.ArrayList;

import structs.RWCar;
import structs.RWStation;

public class CarUpdateResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3899103853284357433L;
	
	private ArrayList<RWStation> stations;
	
	private ArrayList<RWCar> carsAt200;
	
	private ArrayList<RWCar> carsAt300;

	public CarUpdateResponse(ArrayList<RWStation> stations, ArrayList<RWCar> carsAt200, ArrayList<RWCar> carsAt300){
		this.stations = stations;
		this.carsAt200 = carsAt200;
		this.carsAt300 = carsAt300;
	}

	public ArrayList<RWStation> getStations() {
		return stations;
	}

	public void setStations(ArrayList<RWStation> stations) {
		this.stations = stations;
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
}
