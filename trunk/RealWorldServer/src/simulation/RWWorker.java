package simulation;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import messages.CarLogin;
import messages.CarStationAdvertise;
import messages.CarSubscribe;
import messages.CarUnsubscribe;
import messages.CarUpdate;
import messages.CarUpdateResponse;
import messages.LoginResponse;
import messages.StationLogin;
import structs.RWCar;
import structs.RWStation;


public class RWWorker implements Runnable{

	private Socket socket;
	private RWState state;
	
	public RWWorker(Socket socket, RWState state){
		this.socket = socket;
		this.state = state;
	}
	
	public void doCarLogin(CarLogin cr){
		
		String ip = socket.getInetAddress().toString();		
		ip = (String) ip.subSequence(1, ip.length());
		int port = socket.getPort();
		
		RWCar rwcar = new RWCar();
		rwcar.setId(this.state.getSerial());
		rwcar.setIp(ip);
		rwcar.setPort(cr.getPort());
		rwcar.setLat(cr.getLat());
		rwcar.setLog(cr.getLog());
		rwcar.setHeight(cr.getHeight());	 
		
		this.state.getCarCommList().put(rwcar.getId(), rwcar);
		
		LoginResponse clr = new LoginResponse();
		clr.setId(rwcar.getId());
		clr.setStation(this.state.getCarStation(rwcar.getId()));
		
		System.out.println("-->Logged Car " + rwcar.getId() + " to the server.");
		System.out.println("--->IP: " + rwcar.getIp());
		System.out.println("--->port: " + rwcar.getPort());
		
		try {
			ObjectOutput oo = new ObjectOutputStream(socket.getOutputStream());
			oo.writeObject(clr);
			this.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void doStationLogin(StationLogin sl){
		
		String ip = socket.getInetAddress().toString();		
		ip = (String) ip.subSequence(1, ip.length());
		int port = socket.getPort();
		
		RWStation station = new RWStation();
		station.setId(this.state.getSerial());
		station.setIp(ip);
		station.setPort(sl.getPort());
		station.setLat(sl.getLat());
		station.setLog(sl.getLog());
		
		this.state.getStationCommList().put(station.getId(), station);
		
		LoginResponse clr = new LoginResponse();
		clr.setId(station.getId());
		
		System.out.println("-->Logged Station " + station.getId() + " to the server.");
		System.out.println("--->IP: " + station.getIp());
		System.out.println("--->port: " + station.getPort());
		
		try {
			ObjectOutput oo = new ObjectOutputStream(socket.getOutputStream());
			oo.writeObject(clr);
			this.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void doCarSubscribe(CarSubscribe cs){
		try {
			this.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		RWCar car = this.state.getCarCommList().get(cs.getId());
		
		if(!this.state.getCarSubscribers().containsKey(car.getId()))
			this.state.getCarSubscribers().put(car.getId(), car);
		
		System.out.println("-->Subscribed Car " + car.getId() + " to receive updates.");
		System.out.println("--->IP: " + car.getIp());
		System.out.println("--->port: " + car.getPort());
		
	}
	
	public void doCarUpdate(CarUpdate cu){
		RWCar c = this.state.getCarSubscribers().get(cu.getId());
		c.setLat(cu.getLat());
		c.setLog(cu.getLog());
		
		try{
			//TODO RESPOSTA AO UPDATE
			ArrayList<RWCar> carsAt200 = new ArrayList<RWCar>();
			ArrayList<RWCar> carsAt300 = new ArrayList<RWCar>();
			boolean nearStation = false;
			for(RWCar car : this.state.getCarSubscribers().values()){
				double distance = this.distanceBetween(car.getLat(), car.getLog(), c.getLat(), c.getLog());
				if(distance <= 200){
					RWCar carInfo = new RWCar(car.getIp(), car.getPort());
					carsAt200.add(carInfo);
				}
				else if(distance <= 300){
					RWCar carInfo = new RWCar(car.getIp(), car.getPort());
					carsAt300.add(carInfo);
				}
			}
			for(RWCar car : this.state.getCarCrash().values()){
				double distance = this.distanceBetween(car.getLat(), car.getLog(), c.getLat(), c.getLog());
				if(distance <= 200){
					RWCar carInfo = new RWCar(car.getIp(), car.getPort());
					carsAt200.add(carInfo);
				}
				else if(distance <= 300){
					RWCar carInfo = new RWCar(car.getIp(), car.getPort());
					carsAt300.add(carInfo);
				}
			}
			for(RWStation station : this.state.getStationCommList().values()){
				if(this.distanceBetween(station.getLat(), station.getLog(), c.getLat(), c.getLog()) <= 300){
					nearStation = true;
					break;
				}
			}
			
			CarUpdateResponse cur = new CarUpdateResponse(carsAt200, carsAt300, nearStation);
			ObjectOutput oo = new ObjectOutputStream(this.socket.getOutputStream());
			oo.writeObject(cur);	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void doCarUnsubscribe(CarUnsubscribe cs){
		
		CarStationAdvertise csa = new CarStationAdvertise();
		csa.setStation(this.state.getCarStation(cs.getId()));
		if(csa.getStation() == null)
			this.state.getCarCrash().put(cs.getId(), this.state.getCarCommList().get(cs.getId()));
		
		try {
			ObjectOutput oo = new ObjectOutputStream(this.socket.getOutputStream());
			oo.writeObject(csa);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		RWCar car = this.state.getCarCommList().get(cs.getId());
		
		if(this.state.getCarSubscribers().containsKey(car.getId()))
			this.state.getCarSubscribers().remove(car.getId());
	
		System.out.println("-->Unsubscribed Car " + car.getId() + " to stop receiving updates.");
		System.out.println("--->IP: " + car.getIp());
		System.out.println("--->port: " + car.getPort());
		
	}
	
	@Override
	public void run() {
		
		ObjectInput io = null;
		Object packet = null;
		
		try {
			
			io = new ObjectInputStream(socket.getInputStream());
			packet = io.readObject(); 
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(packet instanceof CarLogin){
			this.doCarLogin((CarLogin)packet);
		}
		else if(packet instanceof StationLogin){
			this.doStationLogin((StationLogin)packet);
		}
		else if(packet instanceof CarSubscribe){
			this.doCarSubscribe((CarSubscribe)packet);
		}
		else if(packet instanceof CarUpdate){
			this.doCarUpdate((CarUpdate)packet);
		}
		else if(packet instanceof CarUnsubscribe){
			this.doCarUnsubscribe((CarUnsubscribe)packet);
		}
			
	}
	
	public double distanceBetween(double lat1, double lon1, double lat2, double lon2){
		
		lat1 /= (0.000009 * 1E6);
		lon1 /= (0.000011 * 1E6);
		
		lat2 /= (0.000009 * 1E6);
		lon2 /= (0.000011 * 1E6);
		
		double dist = Math.sqrt(Math.pow((lat1 - lat2), 2) + Math.pow((lon1 - lon2), 2));
		
		return dist;
	}

}
