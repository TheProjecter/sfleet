package SmartFleet.monitor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import messages.MonitorUpdate;
import messages.Snapshot;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdateMonitor extends Service {

private static SmartFleetMonitor MAIN_ACTIVITY;
	
	private Timer timer = new Timer();
	
	private static int PORT;
	private static String IP;
	
	public static void setMainActivity(SmartFleetMonitor activity, int port, String ip) {
		MAIN_ACTIVITY = activity;
		PORT = port;
		IP = ip;
	}
	
	@Override
	public void onCreate(){
		
	}
	
	@Override
	public void onStart(Intent intent, int startid){
		this.timer.schedule(new TimerTask() {
			
			@Override
			public void run(){
				try {				
					Socket s = new Socket(IP, PORT);
					ObjectOutputStream o = new ObjectOutputStream(s.getOutputStream());
					o.writeObject(new MonitorUpdate());
					ObjectInputStream io = new ObjectInputStream(s.getInputStream());
					Snapshot snap = (Snapshot)io.readObject();
					s.close();
					Log.d("smartfleetmonitor", "Recieved a snapshot");
					MAIN_ACTIVITY.setStationlist(snap.getStations());
					MAIN_ACTIVITY.setCarlist(snap.getCars());
					MAIN_ACTIVITY.setMissinglist(snap.getMissings());
					MAIN_ACTIVITY.setNflights(snap.getNflights());
					MAIN_ACTIVITY.setTotal_people(snap.getTotal_people());
					MAIN_ACTIVITY.setTotal_battery(snap.getTotal_battery());
					MAIN_ACTIVITY.setTotal_km(snap.getTotal_km());
					MAIN_ACTIVITY.setTotal_time(snap.getTotal_time());
					MAIN_ACTIVITY.setStraightsum(snap.getStraight_sum());
					MAIN_ACTIVITY.mHandler.post(MAIN_ACTIVITY.mUpdateResults);
				} catch (IOException e) {
					Log.d("SmartFleetMonitor", "Server is down...");
				}catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
