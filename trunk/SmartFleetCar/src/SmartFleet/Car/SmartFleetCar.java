package SmartFleet.Car;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import messages.CarAdvertisement;
import messages.CarLogin;
import messages.CarRegisterMessage;
import messages.CarStationAdvertise;
import messages.CarSubscribe;
import messages.CarUnsubscribe;
import messages.CarUpdate;
import messages.CarUpdateResponse;
import messages.LoginResponse;
import structs.RWCar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class SmartFleetCar extends MapActivity {

	private MapView mapView;
	private MapController mc;

	private TextView batterytext;
	private TextView heightext;

	private FlyingCar myCar;
	
	private int id = 0;

	private String realworldip = "192.168.0.11";
	private int realworldport = 6798;
	
	private int myport = 5000;
	private String myip = "192.168.0.11";
	
	private String serverip = "194.210.228.38";
	private int serverport = 6799;
	

	// Need handler for callbacks to the UI thread
	final Handler mHandler = new Handler();
	// Create runnable for posting
	final Runnable mUpdateResults = new Runnable() {
		public void run() {
			updateUI();
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
				this.mUpdateResults);
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

		this.registerOnRealWorld();
		//this.registerOnCentralServer();
		
	}

	// TODO: RUIQ IDEIAS??
	@Override
	protected boolean isRouteDisplayed() {

		return false;
	}


	public void updateUI() {
		// TODO: RUIQ este cast (int) e necessario?
		this.batterytext.setText((int) this.myCar.getPercentageBattery() + "%");
		this.heightext.setText((int) this.myCar.getHeight() + "m");
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
														this.myCar.getBattery());
			
			
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
}
