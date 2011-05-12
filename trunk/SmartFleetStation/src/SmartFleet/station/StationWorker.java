package SmartFleet.station;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.Socket;

import messages.CarAdvertisement;
import messages.Station;
import structs.RWCar;
import structs.ServerCar;
import android.util.Log;

public class StationWorker implements Runnable{

		private Socket socket;
		
		private SmartFleetStation sfs;
		
		public StationWorker(Socket socket, SmartFleetStation sfs){
			this.socket = socket;
			this.sfs = sfs;
		}
		
		public void doCarAdvertisement(CarAdvertisement ca){
			try {
				this.socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			RWCar rwcar = ca.getCar();
			
			this.sfs.getCarsDocked().put(rwcar.getId(), rwcar);
			Log.d("smartfleet", "Received car "+ rwcar.getId());
			
		}
		
		public void doStationAdd(Station s){
			this.sfs.getMyStation().getStations().add(s);
		}
		
		public void doMissingCar(ServerCar car){
			this.sfs.getMyStation().getMissingcars().add(car);
		}
		
		public void run() {
			
			
			ObjectInput io = null;
			Object packet = null;
			
			try {
				
				io = new ObjectInputStream(socket.getInputStream());
				packet = io.readObject(); 
				
			//} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
			//	e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(packet instanceof CarAdvertisement){
				doCarAdvertisement((CarAdvertisement)packet);
			}
			else if(packet instanceof Station){
				doStationAdd((Station)packet);
			}
			else if(packet instanceof ServerCar){
				doMissingCar((ServerCar)packet);
			}
		
			
		}

	}