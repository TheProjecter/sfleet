package SmartFleet;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import structs.ServerCar;

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
					if(!sc.getRoute().getRoute().isEmpty() && state.getMissingcars().containsKey(sc.getId())){
						long time = Calendar.getInstance().getTimeInMillis();
						if(time > sc.getTimeToUpdate()){
							state.getMissingcars().put(sc.getId(), sc);
							System.out.println("Car: " + sc.getId() + " is missing.");
						}
					}
				}
			}
			
		}, 1000, 1000);

	}

}
