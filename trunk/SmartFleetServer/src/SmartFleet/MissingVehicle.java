package SmartFleet;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import structs.ServerCar;
import structs.ServerStation;

public class MissingVehicle implements Runnable {
	
	private ServerState state;
	private Timer timer = new Timer();
	
	public MissingVehicle(ServerState state){
		this.state = state;
	}

	@Override
	public void run() {
		this.timer.schedule(new TimerTask() {
			
			@Override
			public void run(){
				for(ServerCar sc : state.getCars().values()){
					if(!sc.getRoute().getRoute().isEmpty() && !state.getMissingcars().containsKey(sc.getId())){
						long time = Calendar.getInstance().getTimeInMillis();						
						if(time > sc.getTimeToUpdate()){
							state.getMissingcars().put(sc.getId(), sc);
							System.out.println("Car: " + sc.getId() + " is missing.");
							int lat = sc.getRoute().getRoute().getLast().getLat();
							int lon = sc.getRoute().getRoute().getLast().getLon();
							for(ServerStation ss : state.getStations().values()){
								if(ss.getLat() == lat && ss.getLon() == lon){
									try {
										Socket	s = new Socket(ss.getIp(), ss.getPort());
										ObjectOutputStream oi = new ObjectOutputStream(s.getOutputStream());
										oi.writeObject(sc);
									} catch (UnknownHostException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						}
					}
				}
			}
			
		}, 1000, 1000);

	}

}
