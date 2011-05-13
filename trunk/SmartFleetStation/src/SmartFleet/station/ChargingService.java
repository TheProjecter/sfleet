package SmartFleet.station;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ChargingService extends Service{

	private static SmartFleetStation MAIN_ACTIVITY;
	
	private Timer timer = new Timer();
	
	public static void setMainActivity(SmartFleetStation activity) {
		MAIN_ACTIVITY = activity;
	}
	
	@Override
	public void onCreate(){
		
	}
	
	@Override
	public void onStart(Intent intent, int startid){
		this.timer.scheduleAtFixedRate(new TimerTask() {
			
			SmartFleetStation sfs = MAIN_ACTIVITY;
			
			@Override
			public void run(){
				this.sfs.executeAndUpdateCharge();
			}
			
		}, 0, 500);
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
	

