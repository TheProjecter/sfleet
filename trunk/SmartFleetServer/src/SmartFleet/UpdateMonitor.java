package SmartFleet;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import messages.Snapshot;



public class UpdateMonitor implements Runnable {

	private ServerState state;
	private String ip;
	private int port;
	private Timer timer = new Timer();
	
	public UpdateMonitor(ServerState state, String ip, int port){
		this.state = state;
		this.ip = ip;
		this.port = port;
	}
	
	public void run() {
		this.timer.schedule(new TimerTask() {
			
			@Override
			public void run(){
				try {
					Snapshot snapshot = new Snapshot(state.getStations(), state.getCars());
					Socket s = new Socket(ip, port);
					ObjectOutputStream o = new ObjectOutputStream(s.getOutputStream());
					o.writeObject(snapshot);					
				}catch (IOException e) {
					e.printStackTrace();	
				}
			}
			
		}, 1000, 1000);

	}
}
