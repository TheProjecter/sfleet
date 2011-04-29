package SmartFleet.Car;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import structs.RWCar;
import structs.RWStation;
import structs.Route;
import android.os.Handler;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;

public class FlyingCar extends TimerTask {

	private double battery;

	private Handler h;

	private double height;

	private MapController mapcontrol;

	private double max_battery;

	private Runnable r;

	private RouteOverlay routeoverlay;

	private Timer timer;

	private double velocity;

	GeoPoint myLocation;
	
	private ArrayList<RWStation> stations;
	
	private ArrayList<RWCar> carsAt200;
	
	private ArrayList<RWCar> carsAt300;
	
	private HashMap<Integer, RWCar> carsSeen;
	
	//private Flight flight;
	
	private Route route;
	
	private int clock;

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
		
		this.stations = new ArrayList<RWStation>();
		
		this.carsAt200 = new ArrayList<RWCar>();
		
		this.carsAt300 = new ArrayList<RWCar>();
		
		this.carsSeen = new HashMap<Integer, RWCar>();
		
		
		this.route = new Route();
		this.setCarsSeen(new HashMap<Integer, RWCar>());
		
		this.setClock(0);
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
	
	public void addBattery(double battery) {

		this.battery += battery;
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


	public void setMyLocation(GeoPoint myLocation) {
		this.myLocation = myLocation;
		this.routeoverlay.setMylocation(myLocation);
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
		
		if(this.getBattery() == 0){
			this.setHeight(0);
			return;
		}
		
		if (!this.route.getRoute().isEmpty()) {

			if (this.myLocation.equals(new GeoPoint(this.route.getRoute().get(0).getLat(),
													this.route.getRoute().get(0).getLon()))) {
				this.route.getRoute().removeFirst();
				this.routeoverlay.setRoute(this.route);
				this.setHeight(0);
				//TODO PARA PARA SAIR PASSAGEIROS
				return;
			}

			if (this.height == 0)
				this.setHeight(200);

			double disty = this.route.getRoute().get(0).getLat()
					- this.myLocation.getLatitudeE6();
			double distx = this.route.getRoute().get(0).getLon()
					- this.myLocation.getLongitudeE6();

			double angle = Math.atan(Math.abs(distx) / Math.abs(disty));

			double yv = this.velocity * Math.cos(angle);
			double xv = this.velocity * Math.sin(angle);

			if (this.myLocation.getLatitudeE6() > this.route.getRoute().get(0).getLat())
				yv = -yv;
			if (this.myLocation.getLongitudeE6() > this.route.getRoute().get(0).getLon())
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

			if (this.myLocation.getLatitudeE6() < this.route.getRoute().get(0).getLat()) {
				if (latd > this.route.getRoute().get(0).getLat())
					latd = this.route.getRoute().get(0).getLat();
			} else if (this.myLocation.getLatitudeE6() >this.route.getRoute().get(0).getLat())
				if (latd < this.route.getRoute().get(0).getLat())
					latd = this.route.getRoute().get(0).getLat();
			if (this.myLocation.getLongitudeE6() < this.route.getRoute().get(0).getLon()) {
				if (logd > this.route.getRoute().get(0).getLon())
					logd = this.route.getRoute().get(0).getLon();
			} else if (this.myLocation.getLongitudeE6() > this.route.getRoute().get(0).getLon())
				if (logd < this.route.getRoute().get(0).getLon())
					logd = this.route.getRoute().get(0).getLon();

			this.battery -= 10;
			if(this.battery < 0)
				this.battery = 0;

			this.myLocation = new GeoPoint(latd, logd);

			this.mapcontrol.setCenter(this.myLocation);
			this.mapcontrol.animateTo(this.myLocation);

			this.routeoverlay.setMylocation(this.myLocation);
			this.routeoverlay.setRoute(this.route);
			
		}
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public ArrayList<RWStation> getStations() {
		return stations;
	}

	public void setStations(ArrayList<RWStation> stations) {
		this.stations = stations;
	}

	public ArrayList<RWCar> getCarsAt200() {
		return carsAt200;
	}

	public void setCarsAt200(ArrayList<RWCar> carsAt200) {
		this.carsAt200 = carsAt200;
	}

	public ArrayList<RWCar> getCarsAt300() {
		return carsAt300;
	}

	public void setCarsAt300(ArrayList<RWCar> carsAt300) {
		this.carsAt300 = carsAt300;
	}

	public void setCarsSeen(HashMap<Integer, RWCar> carsSeen) {
		this.carsSeen = carsSeen;
	}

	public HashMap<Integer, RWCar> getCarsSeen() {
		return carsSeen;
	}

	public void setClock(int clock) {
		this.clock = clock;
	}

	public int getClock() {
		return clock;
	}
	
	public void incrementClock() {
		this.clock++;
	}
	
}
