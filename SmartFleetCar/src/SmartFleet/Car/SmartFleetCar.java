package SmartFleet.Car;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;

import messages.CarAdvertisement;
import messages.CarLogin;
import messages.CarRegisterMessage;
import messages.CarStationAdvertise;
import messages.CarSubscribe;
import messages.CarUnsubscribe;
import messages.CarUpdate;
import messages.CarUpdateResponse;
import messages.LoginResponse;
import structs.Flight;
import structs.RWCar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class SmartFleetCar extends MapActivity {

	private MapView mapView;
	private MapController mc;

	private TextView batterytext;
	private TextView heightext;
	private TextView timestoptext;
	private TextView partynamestext;

	private FlyingCar myCar;
	
	private int id = 0;

	private String realworldip = "194.210.229.17";
	private int realworldport = 6798;
	
	private int myport = 5000;
	private String myip = "194.210.229.17";

	private String serverip = "194.210.228.108";
	private int serverport = 6799;

	
	// Need handler for callbacks to the UI thread
	final Handler mHandler = new Handler();
	// Create runnable for posting
	final Runnable mUpdateResults = new Runnable() {
		public void run() {
			updateUI();
		}
	};

	final Runnable mPassengersLeave = new Runnable() {
		public void run() {
			notifyPassengertoLeave();
		}
	};
	
	/** Called when the activity is first created. */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);

		this.mapView = (MapView) findViewById(R.id.mapview);
		this.mc = mapView.getController();

		this.batterytext = (TextView) findViewById(R.id.battery);
		this.heightext = (TextView) findViewById(R.id.height);
		this.timestoptext = (TextView) findViewById(R.id.expectedtime);
		this.partynamestext = (TextView) findViewById(R.id.partynames);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		GeoPoint IST = new GeoPoint(38736830, -9138181);
		//GeoPoint TP = new GeoPoint(38736320, -9301917);
		//GeoPoint AirPort = new GeoPoint(38765775, -9133007);

		// mapView.setBuiltInZoomControls(true);
		this.mapView.setSatellite(false);
		this.mc.setZoom(16);
		
		RouteOverlay ro = new RouteOverlay(IST, 255);
		this.myCar = new FlyingCar(this.mc, ro, this.mHandler,
				this.mUpdateResults, this.mPassengersLeave);
		this.myCar.setLocation(IST);
		
		this.mapView.getOverlays().add(ro);
		this.mc.setCenter(IST);
		this.mc.animateTo(IST);
			
		CarDispatchService.setMainActivity(this, this.myport);
	    final Intent CarDispatchService = new Intent(this, CarDispatchService.class);
		startService(CarDispatchService);
		
		CarUpdateService.setMainActivity(this);
	    final Intent CarUpdateService = new Intent(this, CarUpdateService.class);
		startService(CarUpdateService);
		
		//CarCommunicationService.setMainActivity(this);
	    //final Intent carCommunicationService = new Intent(this, CarCommunicationService.class);
		//startService(carCommunicationService);

		this.registerOnRealWorld();
		//this.registerOnCentralServer();
		
	}

	@Override
	protected boolean isRouteDisplayed() {

		return false;
	}

	public void updateUI() {
		// TODO: RUIQ este cast (int) e necessario?
		this.batterytext.setText((int) this.myCar.getPercentageBattery() + "%");
		this.heightext.setText((int) this.myCar.getHeight() + "m");
		
		if(!this.myCar.getRoute().getRoute().isEmpty()){
			double distance = this.distanceBetween(this.myCar.getMyLocation().getLatitudeE6(),
					this.myCar.getMyLocation().getLongitudeE6(), 
					this.myCar.getRoute().getRoute().getFirst().getLat(),
					this.myCar.getRoute().getRoute().getFirst().getLon());

			double times = distance / this.myCar.getVelocity();

			int time_h = (int)(times/3600);
			int time_m = (int)((times%3600)/60);
			int time_s = (int)(times%60.0);

			String time = "   " + time_h + "h:" + time_m + "m:" + time_s + "s";
			this.timestoptext.setText("Time to next stop:" + time);
		}
		else
			this.timestoptext.setText("Time to next stop:");
		
		String partynames = "Parties:\n";
		for(Flight f : this.myCar.getRoute().getRoute()){
			if(!f.getPartyname().equals("")){
				partynames += f.getPartyname() + "\n";
			}
		}
		this.partynamestext.setText(partynames);
	}
	
	public void notifyPassengertoLeave(){
		
		Flight f = this.myCar.getRoute().getRoute().removeFirst();
		
		if(!f.getPartyname().equals("")){
			String s = "The party " + f.getPartyname() + " has arrived to the destination.";
			Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
		}
		
	}
	
	public MapView getMapView() {
		return mapView;
	}

	public void setMapView(MapView mapView) {
		this.mapView = mapView;
	}

	public MapController getMc() {
		return mc;
	}

	public void setMc(MapController mc) {
		this.mc = mc;
	}

	public TextView getBatterytext() {
		return batterytext;
	}

	public void setBatterytext(TextView batterytext) {
		this.batterytext = batterytext;
	}

	public TextView getHeightext() {
		return heightext;
	}

	public void setHeightext(TextView heightext) {
		this.heightext = heightext;
	}

	public FlyingCar getMyCar() {
		return myCar;
	}

	public void setMyCar(FlyingCar myCar) {
		this.myCar = myCar;
	}

	public Handler getmHandler() {
		return mHandler;
	}

	public Runnable getmUpdateResults() {
		return mUpdateResults;
	}
	
	public void registerOnRealWorld(){
		
		try {
			Socket s = new Socket(this.realworldip, this.realworldport);
			CarLogin l = new CarLogin();
			
			l.setLat(this.myCar.getMyLocation().getLatitudeE6());
			l.setLog(this.myCar.getMyLocation().getLongitudeE6());
			l.setHeight(this.myCar.getHeight());
			l.setPort(this.myport);
			
			ObjectOutput oo = new ObjectOutputStream(s.getOutputStream());
			oo.writeObject(l);
			ObjectInput oi = new ObjectInputStream(s.getInputStream());
			LoginResponse cr = null;
			try {
				cr = (LoginResponse)oi.readObject();
				s.close();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.id = cr.getId();
			
			if(cr.getStation() != null){
				Socket s2 = new Socket(cr.getStation().getIp(), cr.getStation().getPort());
				CarAdvertisement ca = new CarAdvertisement();
				RWCar car = new RWCar();
				car.setId(this.id);
				car.setIp(this.myip);
				car.setPort(this.myport);
				car.setBattery(this.myCar.getBattery());
				ca.setCar(car);

				ObjectOutput oo2 = new ObjectOutputStream(s2.getOutputStream());
				oo2.writeObject(ca);

				Log.d("smartfleet", "Successfully logged at Real World Server. my port:" + s.getLocalPort());
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public void registerOnCentralServer(){
		
		try {
			Socket s = new Socket(this.serverip, this.serverport);
			CarRegisterMessage csm = new CarRegisterMessage(this.id, 
														this.myCar.getMyLocation().getLatitudeE6(),
														this.myCar.getMyLocation().getLongitudeE6(),
														this.myCar.getRoute());
			
			
			ObjectOutput oo = new ObjectOutputStream(s.getOutputStream());
			oo.writeObject(csm);
			s.close();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	public void updateRealWorld(){
		
		try {
			Socket s = new Socket(this.realworldip, this.realworldport);
			CarUpdate l = new CarUpdate();
			
			l.setId(this.id);
			l.setLat(this.myCar.getMyLocation().getLatitudeE6());
			l.setLog(this.myCar.getMyLocation().getLongitudeE6());
			
			ObjectOutput oo = new ObjectOutputStream(s.getOutputStream());
			oo.writeObject(l);
			
			ObjectInput oi = new ObjectInputStream(s.getInputStream());
			CarUpdateResponse cur = (CarUpdateResponse) oi.readObject();
			s.close();
			
			this.getMyCar().setNearStation(cur.isNearStation());
			this.getMyCar().setCarsAt200(cur.getCarsAt200());
			this.getMyCar().setCarsAt300(cur.getCarsAt300());
						
			//TODO processar o input do update
			
			Log.d("CarUpdate", "Successfully updated.");
			
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
	
	public void subscribeRealWorld(){
		try {
			Socket s = new Socket(this.realworldip, this.realworldport);
			CarSubscribe l = new CarSubscribe(this.id);
			
			ObjectOutput oo = new ObjectOutputStream(s.getOutputStream());
			oo.writeObject(l);			
			s.close();
		
			Log.d("CarSubscribe", "Successfully subbed.");
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void unsubscribeRealWorld(){
		try {
			Socket s = new Socket(this.realworldip, this.realworldport);
			CarUnsubscribe l = new CarUnsubscribe(this.id);
			
			ObjectOutput oo = new ObjectOutputStream(s.getOutputStream());
			oo.writeObject(l);	
			ObjectInput oi = new ObjectInputStream(s.getInputStream());
			CarStationAdvertise csa = (CarStationAdvertise) oi.readObject();
			s.close();
			
			if(csa.getStation() != null){
				Socket s2 = new Socket(csa.getStation().getIp(), csa.getStation().getPort());
				CarAdvertisement ca = new CarAdvertisement();
				RWCar car = new RWCar();
				car.setId(this.id);
				car.setIp(this.myip);
				car.setPort(this.myport);
				car.setBattery(this.myCar.getBattery());
				ca.setCar(car);

				ObjectOutput oo2 = new ObjectOutputStream(s2.getOutputStream());
				oo2.writeObject(ca);
			}
			
			Log.d("CarUnSbuscribe", "Successfully unsubbed.");
			
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
	
	public void setMyport(int myport) {
		this.myport = myport;
	}

	public int getMyport() {
		return myport;
	}

	public void setMyip(String myip) {
		this.myip = myip;
	}

	public String getMyip() {
		return myip;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRealworldip() {
		return realworldip;
	}

	public void setRealworldip(String realworldip) {
		this.realworldip = realworldip;
	}

	public int getRealworldport() {
		return realworldport;
	}

	public void setRealworldport(int realworldport) {
		this.realworldport = realworldport;
	}

	public String getServerip() {
		return serverip;
	}

	public void setServerip(String serverip) {
		this.serverip = serverip;
	}

	public int getServerport() {
		return serverport;
	}

	public void setServerport(int serverport) {
		this.serverport = serverport;
	}

	public double distanceBetween(double lat1, double lon1, double lat2, double lon2){
		
		lat1 /= (0.000009 * 1E6);
		lon1 /= (0.000011 * 1E6);
		
		lat2 /= (0.000009 * 1E6);
		lon2 /= (0.000011 * 1E6);
		
		double dist = Math.sqrt(Math.pow((lat1 - lat2), 2) + Math.pow((lon1 - lon2), 2));
		
		return dist;
	}
	
}
