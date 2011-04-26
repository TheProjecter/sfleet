package messages;

import java.io.Serializable;

import structs.RWCar;

public class CarAdvertisement implements Serializable{

	private RWCar car;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7418060445842725343L;

	public CarAdvertisement(){
		
	}
	
	public void setCar(RWCar car) {
		this.car = car;
	}

	public RWCar getCar() {
		return car;
	}

	

}
