package simulation;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import simulation.msg.CarStationAdvertise;
import simulation.structs.RWCar;
import simulation.structs.RWStation;


public class RWState {

	int serialid;
	
	private HashMap<Integer, RWCar> carCommList;
	private ArrayList<Integer> carSubscribers;
	private HashMap<Integer, RWStation> stationCommList;
	
	public RWState(){
		this.serialid = 0;
		this.setCarCommList(new HashMap<Integer, RWCar>());
		this.setCarSubscribers(new ArrayList<Integer>());
		this.setStationCommList(new HashMap<Integer, RWStation>());
	}
	
	public int getSerial(){
		int res = this.serialid;
		this.serialid++;
		return res;
	}
	
	public void updateCarsOnStations(){
		
		for(Entry<Integer, RWCar> c : this.getCarCommList().entrySet()){
			for(Entry<Integer, RWStation> e : this.getStationCommList().entrySet()){
				if(e.getValue().getLat() == c.getValue().getLat() &&
						e.getValue().getLog() == c.getValue().getLog()){

					CarStationAdvertise csa = new CarStationAdvertise(e.getValue());

					try {
						
						System.out.println("-->Trying to send an advertisement for station " + e.getKey() +
								" to car " + c.getKey() + " on:");
						System.out.println("--->IP: " + c.getValue().getIp());
						System.out.println("--->port: " + c.getValue().getPort());
						
						Socket socket = new Socket(c.getValue().getIp(), c.getValue().getPort());
						ObjectOutput oo = new ObjectOutputStream(socket.getOutputStream());
						oo.writeObject(csa);
						socket.close();

						System.out.println("-->Sent an advertisement for station " + e.getKey() +
											" to car " + c.getKey());

					} catch (UnknownHostException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			}
		}
		
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

	public void setCarSubscribers(ArrayList<Integer> carSubscribers) {
		this.carSubscribers = carSubscribers;
	}

	public ArrayList<Integer> getCarSubscribers() {
		return carSubscribers;
	}
	
	
	
}
