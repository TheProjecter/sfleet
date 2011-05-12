package SmartFleet.Car;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Calendar;

import messages.ChargeMessage;
import messages.RouteSending;
import structs.RWCar;

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
	
	public void doRWCar(RWCar car){
		try {
			this.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(car.getDistance() <= 200 && !heightOK(car.getHeight(), this.sfc.getMyCar().getHeight())){
			if(this.sfc.getMyCar().getBattery() > car.getBattery()){
				double h = this.sfc.getMyCar().getHeight();
				this.sfc.getMyCar().setHeight(h + 100);
			}
			else if(this.sfc.getMyCar().getBattery() == car.getBattery() && this.sfc.getId() > car.getId()){
				double h = this.sfc.getMyCar().getHeight();
				this.sfc.getMyCar().setHeight(h + 100);
			}
		}
		car.setInformationtime(Calendar.getInstance().getTimeInMillis());
		this.sfc.getMyCar().getCarsSeen().put(car.getId(), car);		
	}
	
	private boolean heightOK(double height, double height2) {
		// TODO Auto-generated method stub
		double difference = Math.abs(height - height2);
		
		if(difference >= 100)
			return true;
		else
			return false;
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
		else if(packet instanceof RWCar){
			this.doRWCar((RWCar)packet);
		}
		
	
		
	}

}
