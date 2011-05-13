package SmartFleet.Car;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import messages.CarGossipMsg;

import structs.RWCar;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class CarCommunicationService extends Service {

private static SmartFleetCar MAIN_ACTIVITY;
	
	private Timer timer = new Timer();
		
	public static void setMainActivity(SmartFleetCar activity) {
		MAIN_ACTIVITY = activity;
	}
	
	@Override
	public void onCreate(){
		
	}
	
	@Override
	public void onStart(Intent intent, int startid){
		this.timer.schedule(new TimerTask() {
			
			SmartFleetCar sfc = MAIN_ACTIVITY;
			
			@Override
			public void run(){
				Socket s = null;
				if(!this.sfc.getMyCar().getCarsAt300().isEmpty()){
					for(RWCar car : this.sfc.getMyCar().getCarsAt300()){				
						try {	
							if(s != null)
								s.close();
							
							s = new Socket(car.getIp(), car.getPort());
							
							Log.d("smartfleet", "Comunicating with a car at 300.");
							
							this.sfc.getMyCar().incrementClock();
							
							RWCar rwcar = new RWCar(this.sfc.getId(),
													this.sfc.getMyCar().getBattery(),
													this.sfc.getMyCar().getHeight(), 
													this.sfc.getMyCar().getClock(),
													300,
													this.sfc.getMyCar().getVelocity(),
													this.sfc.getMyCar().getRoute());
							
							rwcar.setLat(this.sfc.getMyCar().getMyLocation().getLatitudeE6());
							rwcar.setLog(this.sfc.getMyCar().getMyLocation().getLongitudeE6());
							
							ObjectOutput oo = new ObjectOutputStream(s.getOutputStream());
							oo.writeObject(rwcar);
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				if(!this.sfc.getMyCar().getCarsAt200().isEmpty()){
					for(RWCar car : this.sfc.getMyCar().getCarsAt200()){				
						try {
							if(s != null)
								s.close();
							
							s = new Socket(car.getIp(), car.getPort());
							
							Log.d("smartfleet", "Comunicating with car at 200.");
							
							this.sfc.getMyCar().incrementClock();
							
							RWCar rwcar = new RWCar(this.sfc.getId(),
													this.sfc.getMyCar().getBattery(),
													this.sfc.getMyCar().getHeight(), 
													this.sfc.getMyCar().getClock(),
													200,
													this.sfc.getMyCar().getVelocity(),
													this.sfc.getMyCar().getRoute());
							
							rwcar.setLat(this.sfc.getMyCar().getMyLocation().getLatitudeE6());
							rwcar.setLog(this.sfc.getMyCar().getMyLocation().getLongitudeE6());
							
							ObjectOutput oo = new ObjectOutputStream(s.getOutputStream());
							oo.writeObject(rwcar);
		
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				if(this.sfc.getMyCar().isNearStation()){
					try {
						
						if(s != null)
							s.close();
						
						s = new Socket(this.sfc.getServerip(), this.sfc.getServerport());

						Log.d("smartfleet", "Comunicating with the server.");

						this.sfc.getMyCar().incrementClock();
						
						long currenttime = Calendar.getInstance().getTimeInMillis();
						
						for(RWCar car : this.sfc.getMyCar().getCarsSeen().values())
							car.setInformationtime(currenttime - car.getInformationtime());

						RWCar rwcar = new RWCar(this.sfc.getId(),
								this.sfc.getMyCar().getBattery(),
								this.sfc.getMyCar().getMyLocation().getLatitudeE6(),
								this.sfc.getMyCar().getMyLocation().getLongitudeE6(),
								this.sfc.getMyCar().getClock(),
								this.sfc.getMyCar().getRoute(),
								this.sfc.getMyCar().getVelocity(),
								0);

						this.sfc.getMyCar().getCarsSeen().put(rwcar.getId(), rwcar);

						CarGossipMsg cgm = new CarGossipMsg(this.sfc.getMyCar().getCarsSeen().values());
						
						this.sfc.getMyCar().getCarsSeen().clear();
						
						ObjectOutput oo = new ObjectOutputStream(s.getOutputStream());
						oo.writeObject(cgm);


					} catch (IOException e) {
						Log.d("SmartFleetCar", "Server is down...");
					}
				}
			}			
		}, 0, 1000);
	}
	
	@Override
	public void onDestroy(){
		this.timer.cancel();
	}
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
