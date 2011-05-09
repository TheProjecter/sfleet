package SmartFleet;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import messages.CarGossipMsg;
import messages.CarRegisterMessage;
import messages.Station;
import messages.StationList;
import messages.StationRegisterMessage;
import structs.RWCar;
import structs.ServerCar;
import structs.ServerStation;

public class ServerWorker implements Runnable {
	
	private Socket		socket;
	private ServerState	state;
	
	public ServerWorker(Socket socket, ServerState state) {

		this.socket = socket;
		this.state = state;
	}
	
	private void doCarGossipMsg(CarGossipMsg packet) {

		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (RWCar car : packet.getGossip()) {
			ServerCar serverCar = this.state.getCars().get(car.getId());
			if (serverCar != null) {
				if (serverCar.getClock() < car.getClock()) {
					serverCar.setClock(car.getClock());
					serverCar.setBattery(car.getBattery());
					serverCar.setLat(car.getLat());
					serverCar.setLon(car.getLog());
					serverCar.setRoute(car.getRoute());
				}
			}
		}
	}
	
	private void doCarRegisterMessage(CarRegisterMessage packet) {

		try {
			this.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("-->Logged Car " + packet.getId() + " to the server.");
		ServerCar car = new ServerCar(packet.getId(), packet.getLat(), packet.getLon(), 0, 3600 * 10, null);
		this.state.getCars().put(car.getId(), car);
	}
	
	private void doServerStation(ServerStation ss) {

		try {
			this.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.state.getStations().put(ss.getId(), ss);
	}
	
	private void doStationRegisterMessage(StationRegisterMessage packet) {

		System.out.println("-->Logged Station " + packet.getId() + " to the server.");
		System.out.println("--->IP: " + packet.getIp());
		System.out.println("--->port: " + packet.getPort());
		ArrayList<Station> stations = new ArrayList<Station>();
		for (ServerStation ss : this.state.getStations().values()) {
			Station st = new Station(ss.getLat(), ss.getLon());
			stations.add(st);
		}
		StationList stationList = new StationList(stations);
		try {
			ObjectOutputStream o = new ObjectOutputStream(this.socket.getOutputStream());
			o.writeObject(stationList);
			this.socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ServerStation serverStation = new ServerStation(packet.getId(), packet.getLat(), packet.getLon(), packet.getPort(), packet.getIp(), 0, packet.getCarsDocked(), null);
		this.state.getStations().put(serverStation.getId(), serverStation);
		
	}
	
	@Override
	public void run() {

		ObjectInput io = null;
		Object packet = null;
		
		try {
			io = new ObjectInputStream(this.socket.getInputStream());
			packet = io.readObject();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (packet instanceof CarRegisterMessage)
			this.doCarRegisterMessage((CarRegisterMessage) packet);
		if (packet instanceof StationRegisterMessage)
			this.doStationRegisterMessage((StationRegisterMessage) packet);
		if (packet instanceof ServerStation)
			this.doServerStation((ServerStation) packet);
		if (packet instanceof CarGossipMsg)
			this.doCarGossipMsg((CarGossipMsg) packet);
	}
	
}
