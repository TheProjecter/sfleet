package SmartFleet.Car;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.Socket;

import messages.ChargeMessage;
import messages.RouteSending;

public class CarWorker implements Runnable{

	private Socket socket;
	
	private SmartFleetCar sfc;
	
	public CarWorker(Socket socket, SmartFleetCar sfc){
		this.socket = socket;
		this.sfc = sfc;
	}
	
	public void doSetRoute(RouteSending rs){
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.sfc.getMyCar().setRoute(rs.getRoute());
	}
	
	public void doCharge(ChargeMessage m){
		try {
			this.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.sfc.getMyCar().addBattery(m.getCharge());
		this.sfc.mHandler.post(this.sfc.mUpdateResults);
		
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
		
		if(packet instanceof RouteSending){
			this.doSetRoute((RouteSending)packet);
		}
		else if(packet instanceof ChargeMessage){
			this.doCharge((ChargeMessage)packet);
		}
		
	
		
	}

}
