package SmartFleet;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import messages.CarGossipMsg;
import messages.CarRegisterMessage;
import messages.Station;
import messages.StationList;
import messages.StationRegisterMessage;
import structs.Flight;
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
					serverCar.setInformationTime(Calendar.getInstance().getTimeInMillis());
					double distance = 0;
					int lat = car.getLat();
					int lon = car.getLog();
					for(Flight f : car.getRoute().getRoute()){
						distance +=	this.distanceBetween(f.getLat(), f.getLon(), lat, lon);
						lat = f.getLat();
						lon = f.getLon();
					}
					int timeToUpdate = (int)distance/10;
					serverCar.setTimeToUpdate((timeToUpdate * 1000) + 10000);
					if(this.state.getMissingcars().containsKey(serverCar.getId()))
							this.state.getMissingcars().remove(serverCar.getId());
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
		long time = Calendar.getInstance().getTimeInMillis();
		ServerCar car = new ServerCar(packet.getId(), packet.getLat(), packet.getLon(), 0, 3600 * 10, null, time, 0);
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Station st = new Station(packet.getLat(), packet.getLon());
		for (ServerStation ss : this.state.getStations().values()) {
			try {
				Socket s = new Socket(ss.getIp(), ss.getPort());
				ObjectOutputStream o = new ObjectOutputStream(s.getOutputStream());
				o.writeObject(st);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		ServerStation serverStation = new ServerStation(packet.getId(), packet.getLat(), packet.getLon(), packet.getPort(), packet.getIp(), 0, packet.getCarsDocked(), null);
		this.state.getStations().put(serverStation.getId(), serverStation);
		
	}
	
	public double distanceBetween(int lat1, int lon1, int lat2, int lon2){
		
		lat1 /= 0.000009;
		lon1 /= 0.000011;
		
		lat2 /= 0.000009;
		lon2 /= 0.000011;
		
		double dist = Math.sqrt(Math.pow((lat1 - lat2), 2) + Math.pow((lon1 - lon2), 2));
		
		return dist;
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