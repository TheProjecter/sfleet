package SmartFleet.monitor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MonitorListener extends Service {

private static SmartFleetMonitor MAIN_ACTIVITY;
	
	private Timer timer = new Timer();
	
	private static int PORT;
	
	public static void setMainActivity(SmartFleetMonitor activity, int port) {
		MAIN_ACTIVITY = activity;
		PORT = port;
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
					ServerSocket server = new ServerSocket(PORT);
					
					Log.d("smartfleetmonitor", "Running Dispatcher at port " + PORT + ".");
					MonitorWorker mw;
					
					
					while(true){
						Socket socket = server.accept();						
						mw = new MonitorWorker(socket, MAIN_ACTIVITY);
						Thread t = new Thread(mw);
						t.start();
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}, 0);
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
