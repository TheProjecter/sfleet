package SmartFleet.Car;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import simulation.msg.CarLogin;
import simulation.msg.CarUpdate;
import simulation.msg.LoginResponse;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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
	
	private String realworldip = "194.210.225.53";
	private int realworldport = 6798;
	
	private int myport = 0;
	

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
		RouteOverlay ro = new RouteOverlay(IST, IST, 255);
		this.mapView.getOverlays().add(ro);

		this.myCar = new FlyingCar(this.mc, ro, this.mHandler,
				this.mUpdateResults);

		this.myCar.setLocation(IST);
		this.mc.setCenter(IST);
		this.mc.animateTo(IST);
		
		this.registerOnRealWorld();
		
		CarDispatchService.setMainActivity(this, this.myport);
	    final Intent CarDispatchService = new Intent(this, CarDispatchService.class);
		startService(CarDispatchService);
			
	}

	// TODO: RUIQ IDEIAS??
	@Override
	protected boolean isRouteDisplayed() {

		return false;
	}

	public void setDestinationTagus(View V) {
		this.myCar.setDestination(new GeoPoint(38736320, -9301917));
	}

	public void setDestinationIST(View V) {
		this.myCar.setDestination(new GeoPoint(38736830, -9138181));
	}

	public void setDestinationAirPort(View V) {
		this.myCar.setDestination(new GeoPoint(38765775, -9133007));
	}

	public void updateUI() {
		// TODO: RUIQ este cast (int) e necessario?
		this.batterytext.setText((int) this.myCar.getPercentageBattery() + "%");
		this.heightext.setText((int) this.myCar.getHeight() + "m");
		if(this.myCar.getHeight() > 0 && this.myport != 0)
			this.updateRealWorld();
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
			
			ObjectOutput oo = new ObjectOutputStream(s.getOutputStream());
			oo.writeObject(l);
			ObjectInput oi = new ObjectInputStream(s.getInputStream());
			LoginResponse cr = null;
			try {
				cr = (LoginResponse)oi.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.id = cr.getId();			
			this.myport = cr.getPort();
			
			Log.d("smartfleet", "Successfully logged at Real World Server. my port:" + s.getLocalPort());
			
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
			l.setHeight(this.myCar.getHeight());
			
			ObjectOutput oo = new ObjectOutputStream(s.getOutputStream());
			oo.writeObject(l);
		
			s.close();
		
			Log.d("CarUpdate", "Successfully updated.");
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
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
	
}
