package SmartFleet.Car;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class CarDispatchService extends Service{

	private static SmartFleetCar MAIN_ACTIVITY;
	
	private Timer timer = new Timer();
	
	private static int PORT;
	
	public static void setMainActivity(SmartFleetCar activity, int port) {
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
					CarWorker c;
					
					if(server.isClosed())
						Log.d("smartfleet", "Running Dispatcher at port shit");
					else
						Log.d("smartfleet", "Running Dispatcher at port ok");
						
					while(true){
						Socket s = server.accept();
						
						Log.d("smartfleet", s.getLocalSocketAddress().toString());
						
						c = new CarWorker(s, MAIN_ACTIVITY);
						Thread t = new Thread(c);
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
