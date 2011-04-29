package SmartFleet.monitor;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.Socket;

import structs.ServerCar;
import structs.ServerStation;

import messages.CarMessage;
import messages.Snapshot;
import messages.StationMessage;


import android.util.Log;

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
		
		if(packet instanceof Snapshot)
			this.doSnapshot((Snapshot)packet);
			
	}

	private void doSnapshot(Snapshot packet) {
		// TODO Auto-generated method stub
		Log.d("smartfleetmonitor", "Recieved a snapshot");
		this.sfm.setStationlist(packet.getStations());
		this.sfm.setCarlist(packet.getCars());
		this.sfm.mHandler.post(this.sfm.mUpdateResults);
	}

}
