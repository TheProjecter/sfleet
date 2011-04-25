package SmartFleet.Car;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

import simulation.msg.CarAdvertisement;
import simulation.msg.CarStationAdvertise;

public class CarWorker implements Runnable{

	private Socket socket;
	
	private SmartFleetCar sfc;
	
	public CarWorker(Socket socket, SmartFleetCar sfc){
		this.socket = socket;
		this.sfc = sfc;
	}
	
	public void doCarStationAdvertise(CarStationAdvertise csa){
		
		CarAdvertisement ca = new CarAdvertisement();
		ca.setIp(this.sfc.getMyip());
		ca.setPort(this.sfc.getMyport());
		
		try {
			
			Socket s = new Socket(csa.getSation().getIp(), csa.getSation().getPort());
			//this.socket.close();
			//Socket s = new Socket("10.0.2.2", 5001);
			if(s.isBound())
				Log.d("cne","ta bound");
			if(s.isConnected())
				Log.d("cne", "ta logado");
			if(!s.isOutputShutdown())
				Log.d("cne", "ta output logado");
			ObjectOutput oo = new ObjectOutputStream(s.getOutputStream());
			oo.writeObject(ca);
			oo.close();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
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
