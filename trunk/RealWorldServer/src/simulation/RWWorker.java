package simulation;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;

import simulation.msg.CarLogin;
import simulation.msg.CarUpdate;
import simulation.msg.LoginResponse;
import simulation.msg.StationLogin;
import simulation.structs.RWCar;
import simulation.structs.RWStation;


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
		//a pressao para testar
		rwcar.setPort(cr.getPort());
		rwcar.setLat(cr.getLat());
		rwcar.setLog(cr.getLog());
		rwcar.setHeight(cr.getHeight());
		
		 
		
		this.state.getCarCommList().put(rwcar.getId(), rwcar);
		
		LoginResponse clr = new LoginResponse(rwcar.getId(), rwcar.getPort());
		
		System.out.println("-->Logged Car " + rwcar.getId() + " to the server.");
		System.out.println("--->IP: " + rwcar.getIp());
		System.out.println("--->port: " + rwcar.getPort());
		
		try {
			ObjectOutput oo = new ObjectOutputStream(socket.getOutputStream());
			oo.writeObject(clr);
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
		
		LoginResponse clr = new LoginResponse(station.getId(), station.getPort());
		
		System.out.println("-->Logged Station " + station.getId() + " to the server.");
		System.out.println("--->IP: " + station.getIp());
		System.out.println("--->port: " + station.getPort());
		
		try {
			ObjectOutput oo = new ObjectOutputStream(socket.getOutputStream());
			oo.writeObject(clr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.state.updateCarsOnStations();
		
	}
	
	public void doCarUpdate(CarUpdate cu){
		//RWCar c = this.state.getCarCommList().get(cu.getId());
		//c.setHeight(cu.getHeight());
		//c.setLat(cu.getLat());
		//c.setLog(cu.getLog());
		//System.out.println("-->Updated Car " + cu.getId());
		//System.out.println("--->Latitude " + cu.getLat());
		//System.out.println("--->Longitude " + cu.getLog());
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
		else if(packet instanceof CarUpdate){
			this.doCarUpdate((CarUpdate)packet);
		}
		
		
		
	}

}
