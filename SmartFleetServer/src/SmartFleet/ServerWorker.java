package SmartFleet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;

import messages.CarGossipMsg;
import messages.MonitorUpdate;
import messages.Snapshot;
import messages.Station;
import messages.StationList;
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
					serverCar.setInformationTime(Calendar.getInstance().getTimeInMillis() - car.getInformationtime());
					serverCar.setVelocity(car.getVelocity());
					double distance = 0;
					double lat = car.getLat();
					double lon = car.getLog();
					int stops = 0;
					for (Flight f : car.getRoute().getRoute()){
							distance += this.distanceBetween(f.getLat(), f.getLon(), lat, lon);
							lat = f.getLat();
							lon = f.getLon();
							stops++;
					}
					long timeToUpdate = (long)(distance/car.getVelocity());
					timeToUpdate *= 1000;
					timeToUpdate += (stops*1000);
					timeToUpdate += serverCar.getInformationTime();
					serverCar.setTimeToUpdate(timeToUpdate);
					if (this.state.getMissingcars().containsKey(serverCar.getId())){
						this.state.getMissingcars().remove(serverCar.getId());
						this.state.getCarsfound().put(serverCar.getId(), serverCar);
					}
				}
			}
		
			else{
				System.out.println("-->Logged Car " + car.getId() + " to the server.");
				long time = Calendar.getInstance().getTimeInMillis();
				ServerCar sc = new ServerCar(car.getId(), car.getLat(), car.getLog(), car.getClock(), car.getBattery(), car.getRoute(), time, 0, car.getVelocity());
				this.state.getCars().put(sc.getId(), sc);
			}
		}
		
		save();
		
	}

	
	private void doMonitorUpdate() {

		Snapshot snapshot = new Snapshot(this.state.getStations(), this.state.getCars());
		ObjectOutputStream o;
		try {
			o = new ObjectOutputStream(this.socket.getOutputStream());
			o.writeObject(snapshot);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void doServerStation(ServerStation ss) {
		if(!this.state.getStations().containsKey(ss.getId())){
			System.out.println("-->Logged Station " + ss.getId() + " to the server.");
			System.out.println("--->IP: " + ss.getIp());
			System.out.println("--->port: " + ss.getPort());
			ArrayList<Station> stations = new ArrayList<Station>();
			for (ServerStation ss1 : this.state.getStations().values()) {
				Station st = new Station(ss1.getLat(), ss1.getLon());
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
			Station st = new Station(ss.getLat(), ss.getLon());
			for (ServerStation ss1 : this.state.getStations().values()) {
				try {
					Socket s = new Socket(ss1.getIp(), ss1.getPort());
					ObjectOutputStream o = new ObjectOutputStream(s.getOutputStream());
					o.writeObject(st);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		else{
			StationList stationList = new StationList(null);
			try {
				ObjectOutputStream o = new ObjectOutputStream(this.socket.getOutputStream());
				o.writeObject(stationList);
				this.socket.close();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		this.state.getStations().put(ss.getId(), ss);
		
		save();		
	}
	
	public double distanceBetween(double lat1, double lon1, double lat2, double lon2) {

		lat1 /= (0.000009 * 1E6);
		lon1 /= (0.000011 * 1E6);
		
		lat2 /= (0.000009 * 1E6);
		lon2 /= (0.000011 * 1E6);
		
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
		
		if (packet instanceof ServerStation)
			this.doServerStation((ServerStation) packet);
		if (packet instanceof CarGossipMsg)
			this.doCarGossipMsg((CarGossipMsg) packet);
		if (packet instanceof MonitorUpdate)
			this.doMonitorUpdate();
	}
	
	public void save() {

		try {
			FileOutputStream fout = new FileOutputStream("backup.cmov");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(this.state);
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
