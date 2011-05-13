package SmartFleet.Car;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CarUpdateService extends Service{

	
	private static SmartFleetCar MAIN_ACTIVITY;

	private Timer timer = new Timer();
	
	public static void setMainActivity(SmartFleetCar activity) {
		MAIN_ACTIVITY = activity;
	}

	@Override
	public void onStart(Intent intent, int startid){
		
		this.timer.scheduleAtFixedRate(new TimerTask() {

			SmartFleetCar sfc = MAIN_ACTIVITY;
			
			private double height = 0;
			private boolean subbed = false;
			
			@Override
			public void run(){
				
				this.height = this.sfc.getMyCar().getHeight();
				
				if (this.height == 0 && !this.sfc.isAtStation()){
					if(!this.subbed){
						this.sfc.subscribeRealWorld();
						this.subbed = true;
					}					
				}
				else if(this.height > 0){
					if(!this.subbed){
						this.sfc.subscribeRealWorld();
						this.subbed = true;
					}
					
					this.sfc.updateRealWorld();
					
				}else if (this.height == 0 && this.subbed == true){
					this.sfc.unsubscribeRealWorld();
					this.subbed = false;
				}				
			}
			
		}, 0, 1000);
	}
	
	@Override
	public void onDestroy(){
		this.timer.cancel();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
