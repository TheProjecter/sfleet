package simulation;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
		
	}
	
	public void doCarUpdate(CarUpdate cu){
		RWCar c = this.state.getCarSubscribers().get(cu.getId());
		c.setLat(cu.getLat());
		c.setLog(cu.getLog());
		System.out.println("-->Updated Car " + c.getId());
		System.out.println("--->Latitude " + c.getLat());
		System.out.println("--->Longitude " + c.getLog());
		
		try{
			//TODO RESPOSTA AO UPDATE
			CarUpdateResponse cur = new CarUpdateResponse();
			
			ObjectOutput oo = new ObjectOutputStream(this.socket.getOutputStream());
			oo.writeObject(cur);	
			this.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void doCarUnsubscribe(CarUnsubscribe cs){
		
		CarStationAdvertise csa = new CarStationAdvertise();
		csa.setStation(this.state.getCarStation(cs.getId()));
		
		try {
			ObjectOutput oo = new ObjectOutputStream(this.socket.getOutputStream());
			oo.writeObject(csa);
			this.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		RWCar car = this.state.getCarCommList().get(cs.getId());
		
		if(this.state.getCarSubscribers().containsKey(car.getId()))
			this.state.getCarSubscribers().remove(car.getId());
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

}