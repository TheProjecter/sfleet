package SmartFleet.Car;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;

public class FlyingCar extends TimerTask {

	private double battery;

	private Handler h;

	private double height;

	private MapController mapcontrol;

	private double max_battery;

	private GeoPoint myDestination;

	private Runnable r;

	private RouteOverlay routeoverlay;

	private Timer timer;

	private double velocity;

	GeoPoint myLocation;
	
	//private Map<Integer, Info> stations;
	
	//private Map<Integer, Info> cars;
	
	//private Flight flight;

	public FlyingCar(MapController m, RouteOverlay r, Handler hand, Runnable ru) {

		super();

		this.velocity = 10; // meters per second
		this.height = 0;

		this.max_battery = 10 * 3600;
		this.battery = this.max_battery;

		this.mapcontrol = m;
		this.routeoverlay = r;

		this.h = hand;
		this.r = ru;

		this.timer = new Timer("Realtimer");
		this.timer.scheduleAtFixedRate(this, 1000, 1000);
		
		//this.stations = new HashMap<Integer, Info>();
		//this.cars = new HashMap<Integer, Info>();
		
		//this.flight = null;
		//this.port = 0;
	}

	public double getBattery() {

		return this.battery;
	}

	public Handler getH() {

		return this.h;
	}

	public double getHeight() {

		return this.height;
	}

	public MapController getMapcontrol() {

		return this.mapcontrol;
	}

	public double getMax_battery() {

		return this.max_battery;
	}

	public GeoPoint getMyDestination() {

		return this.myDestination;
	}

	public GeoPoint getMyLocation() {

		return this.myLocation;
	}

	public double getPercentageBattery() {

		double res = (this.battery / this.max_battery) * 100;
		return res;
	}

	public Runnable getR() {

		return this.r;
	}

	public RouteOverlay getRouteoverlay() {

		return this.routeoverlay;
	}

	public Timer getTimer() {

		return this.timer;
	}

	public double getVelocity() {

		return this.velocity;
	}

	@Override
	public void run() {

		this.updatePosition();
		this.h.post(this.r);
	}

	public void setBattery(double battery) {

		this.battery = battery;
	}

	public void setDestination(GeoPoint p) {

		this.myDestination = p;
	}

	public void setH(Handler h) {

		this.h = h;
	}

	public void setHeight(double h) {

		if (this.height < h) {
			this.battery -= (h - this.height) * 2;
		}
		this.height = h;
	}

	public void setLocation(GeoPoint p) {

		this.myLocation = p;
	}

	public void setMapcontrol(MapController mapcontrol) {

		this.mapcontrol = mapcontrol;
	}

	public void setMax_battery(double max_battery) {

		this.max_battery = max_battery;
	}

	public void setMyDestination(GeoPoint myDestination) {

		this.myDestination = myDestination;
	}

	public void setMyLocation(GeoPoint myLocation) {

		this.myLocation = myLocation;
	}

	public void setR(Runnable r) {

		this.r = r;
	}

	public void setRouteoverlay(RouteOverlay routeoverlay) {

		this.routeoverlay = routeoverlay;
	}

	public void setTimer(Timer timer) {

		this.timer = timer;
	}

	public void setVelocity(double velocity) {

		this.velocity = velocity;
	}

	public void updatePosition() {

		if (this.myDestination != null) {

			if (this.myLocation.equals(this.myDestination)) {
				this.myDestination = null;
				this.setHeight(0);
				return;
			}

			if (this.height == 0)
				this.setHeight(200);

			double disty = this.myDestination.getLatitudeE6()
					- this.myLocation.getLatitudeE6();
			double distx = this.myDestination.getLongitudeE6()
					- this.myLocation.getLongitudeE6();

			double angle = Math.atan(Math.abs(distx) / Math.abs(disty));

			double yv = this.velocity * Math.cos(angle);
			double xv = this.velocity * Math.sin(angle);

			if (this.myLocation.getLatitudeE6() > this.myDestination
					.getLatitudeE6())
				yv = -yv;
			if (this.myLocation.getLongitudeE6() > this.myDestination
					.getLongitudeE6())
				xv = -xv;

			// latitude 1¼ = 109000m
			// longitude 1¼ = 86000m
			// lat 1m = 0.000009¼
			// lon 1m = 0.000011¼

			xv *= 0.000011;
			yv *= 0.000009;

			// For speed up the cars uncomment this...
			// xv *= 100;
			// yv *= 100;

			int logd = (int) (this.myLocation.getLongitudeE6() + (xv * 1E6));
			int latd = (int) (this.myLocation.getLatitudeE6() + (yv * 1E6));

			if (this.myLocation.getLatitudeE6() < this.myDestination
					.getLatitudeE6()) {
				if (latd > this.myDestination.getLatitudeE6())
					latd = this.myDestination.getLatitudeE6();
			} else if (this.myLocation.getLatitudeE6() > this.myDestination
					.getLatitudeE6())
				if (latd < this.myDestination.getLatitudeE6())
					latd = this.myDestination.getLatitudeE6();
			if (this.myLocation.getLongitudeE6() < this.myDestination
					.getLongitudeE6()) {
				if (logd > this.myDestination.getLongitudeE6())
					logd = this.myDestination.getLongitudeE6();
			} else if (this.myLocation.getLongitudeE6() > this.myDestination
					.getLongitudeE6())
				if (logd < this.myDestination.getLongitudeE6())
					logd = this.myDestination.getLongitudeE6();

			this.battery -= 10;

			this.myLocation = new GeoPoint(latd, logd);

			this.mapcontrol.setCenter(this.myLocation);
			this.mapcontrol.animateTo(this.myLocation);

			this.routeoverlay.setOrigin(this.myLocation);
			this.routeoverlay.setDest(this.myDestination);
			
		}
	}

	
}
