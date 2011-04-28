package SmartFleet;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.Socket;

import server.messages.CarMessage;
import server.messages.StationMessage;

public class ServerWorker implements Runnable{

	private Socket socket;
	private ServerState state;
	
	public ServerWorker(Socket socket, ServerState state){
		this.socket = socket;
		this.state = state;
	}
	
	@Override
	public void run() {
		
		
		ObjectInput io = null;
		Object packet = null;
		
		try {			
			io = new ObjectInputStream(socket.getInputStream());
			packet = io.readObject();
			socket.close();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(packet instanceof CarMessage)
			this.doCarMessage((CarMessage)packet);
		if(packet instanceof StationMessage)
			this.doStationMessage((StationMessage)packet);
	}

	private void doStationMessage(StationMessage packet) {
		StationMessage st = this.state.getStations().get(packet.getId());
		if(st != null){
			st.setWaitingtime(packet.getWaitingtime());
			st.setPassengers(packet.getPassengers());
		}
		else{
			st = new StationMessage(packet.getId(), packet.getLat(), packet.getLon(), packet.getPassengers(), packet.getWaitingtime());
			this.state.getStations().put(st.getId(), st);
		}		
	}

	private void doCarMessage(CarMessage packet) {
		CarMessage car = this.state.getCars().get(packet.getId());
		if(car != null){
			car.setBattery(packet.getBattery());
			car.setLat(packet.getLat());
			car.setLon(packet.getLon());
		}
		else{
			car = new CarMessage(packet.getId(), packet.getLat(), packet.getLon(), packet.getBattery());
			this.state.getCars().put(car.getId(), car);
		}	
	}
}
