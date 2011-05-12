package SmartFleet.station;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import messages.Station;
import messages.StationList;

import structs.ServerStation;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class UpdateCentralServerService extends Service{

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
				
				long time = 0;
				
				if(!this.sfs.getMyStation().getWaitingTime().isEmpty()){
					for(long l : this.sfs.getMyStation().getWaitingTime())
						time += l;
					time /= this.sfs.getMyStation().getWaitingTime().size();
				}
				
				ServerStation ss = new ServerStation(this.sfs.getId(),
													this.sfs.getMyStation().getMylocation().getLatitudeE6(),
													this.sfs.getMyStation().getMylocation().getLongitudeE6(),
													this.sfs.getMyport(),
													this.sfs.getMyip(),
													time,
													this.sfs.getCarsDocked(),
													this.sfs.getMyStation().getFlightQueue());
				
				try {
					Socket socket = new Socket(this.sfs.getServerip(), this.sfs.getServerport());
					ObjectOutput oo = new ObjectOutputStream(socket.getOutputStream());
					oo.writeObject(ss);
					ObjectInput oi = new ObjectInputStream(socket.getInputStream());
					StationList sl = (StationList)oi.readObject();
					socket.close();
					
					if(sl.getStations() != null){
						for (Station st : sl.getStations())
							this.sfs.getMyStation().getStations().add(st);
					}
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
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
