package SmartFleet.Car;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.Socket;

import simulation.msg.CarStationAdvertise;
import android.util.Log;

public class CarWorker implements Runnable{

	private Socket socket;
	
	private SmartFleetCar sfc;
	
	public CarWorker(Socket socket, SmartFleetCar sfc){
		this.socket = socket;
		this.sfc = sfc;
	}
	
	public void doCarStationAdvertise(CarStationAdvertise csa){
		Log.d("Nice", "Mega");
	}
	
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
		
		if(packet instanceof CarStationAdvertise){
			this.doCarStationAdvertise((CarStationAdvertise)packet);
		}
	
		
	}

}
