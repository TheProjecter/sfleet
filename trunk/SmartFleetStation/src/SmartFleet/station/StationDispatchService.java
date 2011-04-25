package SmartFleet.station;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class StationDispatchService extends Service {
	
	private static SmartFleetStation MAIN_ACTIVITY;
	
	private Timer timer = new Timer();
	
	private static int PORT;
	
	public static void setMainActivity(SmartFleetStation activity, int port) {
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
					
				//server.bind(new InetSocketAddress("10.0.2.15",  PORT));
					
					Log.d("smartfleet", "Running Dispatcher at port " + PORT + ".");
					StationWorker s;
					
					if(server.isClosed())
						Log.d("smartfleet", "Running Dispatcher at port shit");
					else
						Log.d("smartfleet", "Running Dispatcher at port ok");
						
					while(true){
						Socket sa = server.accept();
						
						Log.d("smartfleet", sa.getLocalSocketAddress().toString());
						
						s = new StationWorker(sa, MAIN_ACTIVITY);
						Thread t = new Thread(s);
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
