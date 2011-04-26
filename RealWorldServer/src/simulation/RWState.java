package simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import structs.RWCar;
import structs.RWStation;


public class RWState {

	int serialid;
	
	private HashMap<Integer, RWCar> carCommList;
	private HashMap<Integer, RWCar> carSubscribers;
	private HashMap<Integer, RWStation> stationCommList;
	
	public RWState(){
		this.serialid = 0;
		this.setCarCommList(new HashMap<Integer, RWCar>());
		this.setCarSubscribers(new HashMap<Integer, RWCar>());
		this.setStationCommList(new HashMap<Integer, RWStation>());
	}
	
	public int getSerial(){
		int res = this.serialid;
		this.serialid++;
		return res;
	}
	
	public RWStation getCarStation(int id){
		
		RWCar thiscar = this.getCarCommList().get(id);
		
		for(Entry<Integer, RWStation> e : this.getStationCommList().entrySet()){
			if(e.getValue().getLat() == thiscar.getLat() &&
					e.getValue().getLog() == thiscar.getLog()){
				return e.getValue();
			}
		}
		
		return null;
	}

	public void setCarCommList(HashMap<Integer, RWCar> carCommList) {
		this.carCommList = carCommList;
	}

	public HashMap<Integer, RWCar> getCarCommList() {
		return carCommList;
	}

	public void setStationCommList(HashMap<Integer, RWStation> stationCommList) {
		this.stationCommList = stationCommList;
	}

	public HashMap<Integer, RWStation> getStationCommList() {
		return stationCommList;
	}

	public void setCarSubscribers(HashMap<Integer, RWCar> carSubscribers) {
		this.carSubscribers = carSubscribers;
	}

	public HashMap<Integer, RWCar> getCarSubscribers() {
		return carSubscribers;
	}
	
	
	
}
