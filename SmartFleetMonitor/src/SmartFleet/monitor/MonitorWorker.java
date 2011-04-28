package SmartFleet.monitor;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.Socket;

import android.util.Log;

import server.messages.*;

public class MonitorWorker implements Runnable {
	
	private Socket socket;
	
	private SmartFleetMonitor sfm;
	
	public MonitorWorker(Socket socket, SmartFleetMonitor sfm){
		this.socket = socket;
		this.sfm = sfm;
	}

	public void run() {
		
		Object packet = null;
		
		try {		
			ObjectInputStream io = new ObjectInputStream(socket.getInputStream());
			packet = io.readObject();
			socket.close();
		} catch (EOFException e){
			Log.d("Merda", e.toString());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(packet instanceof CarMessage){
			this.doCarMessage((CarMessage)packet);
		}
		else if(packet instanceof StationMessage)
			this.doStationMessage((StationMessage)packet);
			
	}

	private void doStationMessage(StationMessage packet) {
		// TODO Auto-generated method stub
		Log.d("smartfleetmonitor", "Recieved message from station " + packet.getId());
		MyStation st = this.sfm.getStationlist().get(packet.getId());
		if(st != null){
			st.setAverageWaitTime(packet.getWaitingtime());
			st.setnWaitPassengers(packet.getPassengers());
		}
		else{
			st = new MyStation(packet.getId(), packet.getLat(), packet.getLon(), packet.getPassengers(), packet.getWaitingtime());
			this.sfm.getStationlist().put(st.getId(), st);
		}
		this.sfm.mHandler.post(this.sfm.mUpdateResults);
	}

	private void doCarMessage(CarMessage packet) {
		// TODO Auto-generated method stub
		Log.d("smartfleetmonitor", "Recieved message from car " + packet.getId());
		MyCar car = this.sfm.getCarlist().get(packet.getId());
		if(car != null){
			car.setBattery(packet.getBattery());
			car.setLat(packet.getLat());
			car.setLon(packet.getLon());
		}
		else{
			car = new MyCar(packet.getId(), packet.getBattery(), packet.getLat(), packet.getLon());
			this.sfm.getCarlist().put(car.getId(), car);
		}
		
		this.sfm.mHandler.post(this.sfm.mUpdateResults);		
	}

}
