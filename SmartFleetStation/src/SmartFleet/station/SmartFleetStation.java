package SmartFleet.station;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import messages.ChargeMessage;
import messages.LoginResponse;
import messages.RouteSending;
import messages.Station;
import messages.StationLogin;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import structs.Flight;
import structs.RWCar;
import structs.Route;
import structs.ServerCar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;

public class SmartFleetStation extends Activity {
	
	private boolean	booking	= false;
	private HashMap<Integer, RWCar>	carsDocked;
	private HashMap<Integer, RWCar>	carsToCharge;
	
	private LinkedList<RWCar>			clist			= new LinkedList<RWCar>();

	private Flight						currentflight;
	
	private int							id				= 0;
	
	private String						myip			= "169.254.247.246";
	private int							myport			= 5001;
	
	private FlyingStation				myStation;
	private String						realworldip		= "169.254.8.254";

	private int							realworldport	= 6798;
	private LinkedList<RouteSending>	rslist			= new LinkedList<RouteSending>();
	
	private String						serverip		= "169.254.8.254";
	
	private int							serverport		= 6799;
	final Runnable						mCallPassengers	= new Runnable() {
															
															public void run() {
																
																callPassengers();
															}
														};
	
	// Need handler for callbacks to the UI thread
	final Handler						mHandler		= new Handler();
	// Create runnable for posting
	final Runnable						mUpdateResults	= new Runnable() {
															
															public void run() {

																updateUI();
															}
														};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_CANCELED)
			return;
		
		GeoPoint gp = new GeoPoint(data.getIntExtra("lat", 0), data.getIntExtra("log", 0));
		this.currentflight.setLat(gp.getLatitudeE6());
		this.currentflight.setLon(gp.getLongitudeE6());
		
		String locationname = "UNDEFINED";
		
		double lat = gp.getLatitudeE6() / 1E6;
		double log = gp.getLongitudeE6() / 1E6;
		
		HttpURLConnection connection = null;
		URL serverAddress = null;
		
		try {
			// build the URL using the latitude & longitude you want to lookup
			// NOTE: I chose XML return format here but you can choose something else
			serverAddress = new URL("http://maps.google.com/maps/geo?q=" + Double.toString(lat) + "," + Double.toString(log) + "&output=xml&oe=utf8&sensor=true&key=" + R.string.ApiMapKey);
			// set up out communications stuff
			connection = null;
			
			Log.d("CUCU", Double.toString(lat));
			Log.d("CUCU", Double.toString(log));
			// Set up the initial connection
			connection = (HttpURLConnection) serverAddress.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setReadTimeout(10000);
			
			connection.connect();
			
			try {
				InputStreamReader isr = new InputStreamReader(connection.getInputStream());
				InputSource source = new InputSource(isr);
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser parser = factory.newSAXParser();
				XMLReader xr = parser.getXMLReader();
				GoogleReverseGeocodeXmlHandler handler = new GoogleReverseGeocodeXmlHandler();
				
				xr.setContentHandler(handler);
				xr.parse(source);
				
				locationname = handler.getStreetName();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		this.currentflight.setDestination(locationname);
	}
	
	public void bookCancel(View V) {

		this.currentflight = null;
		this.mHandler.post(this.mUpdateResults);
		this.setContentView(R.layout.main);
		this.booking = false;
	}
	
	public void bookCommit(View V) {

		EditText name = (EditText) this.findViewById(R.id.pname);
		EditText num = (EditText) this.findViewById(R.id.pnumber);
		
		String pname = name.getText().toString();
		int numb;
		try {
			numb = Integer.parseInt(num.getText().toString());
		} catch (NumberFormatException e) {
			numb = -1;
		}
		
		if (pname.equals("") || pname == null || numb < 1 || numb > 4 || this.currentflight.getLat() == 0 || this.currentflight.getLon() == 0) {
			String error = "There's something wrong with your values...";
			Toast.makeText(this.getApplicationContext(), error, Toast.LENGTH_SHORT).show();
		}
		else {
			this.currentflight.setPartyname(pname);
			this.currentflight.setNpassengers(numb);
			this.currentflight.setTimeof(Calendar.getInstance().getTimeInMillis());
			
			this.myStation.getFlightQueue().add(this.currentflight);
			this.currentflight = null;
			this.mHandler.post(this.mUpdateResults);
			this.setContentView(R.layout.main);
		}
		
		this.booking = false;
	}
	
	public void callPassengers() {

		// if(this.clist.isEmpty() || this.rslist.isEmpty())
		// return;
		
		final RWCar car = this.clist.removeFirst();
		final RouteSending rs = this.rslist.removeFirst();
		
		String partiesToCall = "Calling Parties to Car " + car.getId() + ":\n";
		
		for (Flight f : rs.getRoute().getRoute()) {
			if (f.getPartyname() != "") {
				partiesToCall += f.getPartyname();
				partiesToCall += "\n";
			}
		}
		
		// CHAMA PASSAGEIROS PARA O CARRO
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(partiesToCall);
		builder.setCancelable(false);
		OnClickListener listen = new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int id) {

				RWCar cars = car;
				RouteSending rss = rs;
				
				Socket s;
				try {
					Log.d("Smartfleet", "Sending route");
					s = new Socket(cars.getIp(), cars.getPort());
					ObjectOutput oo = new ObjectOutputStream(s.getOutputStream());
					oo.writeObject(rss);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dialog.cancel();
			}
		};
		
		builder.setPositiveButton("Ok", listen);
		
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public void dispatchRoute() {

		if (this.booking)
			return;
		
		if (this.carsDocked.isEmpty())
			return;
		
		RWCar car = null;
		for (RWCar c : this.carsDocked.values()) {
			if (car == null || car.getBattery() < c.getBattery())
				car = c;
		}
		
		if(!this.getMyStation().getMissingcars().isEmpty()){
			this.findMissingVehicle(car);
			return;
		}
		
		if(this.myStation.getFlightQueue().isEmpty())
			return;
		
		Route r = new Route();
		Flight f = this.myStation.getFlightQueue().get(0);
		r.getRoute().add(f);
		this.myStation.getWaitingTime().add(Calendar.getInstance().getTimeInMillis() - f.getTimeof());
		
		// ACHA A ESTA‚åO MAIS PROXIMA DO ULTIMO DESTINO
		Station dest = null;
		double dist = 0;
		
		for (Station s : this.myStation.getStations()) {
			double newdist = this.findDistance(s.getLat(), s.getLon(), r.getRoute().getLast().getLat(), r.getRoute().getLast().getLon());
			if (dest == null || dist > newdist) {
				dest = s;
				dist = newdist;
			}
		}
		
		Flight destiny = new Flight();
		destiny.setLat(dest.getLat());
		destiny.setLon(dest.getLon());
		r.getRoute().add(destiny);
		
		// Verifica se o voo Ž possivel sen‹o cancela o flight
		if (this.isRoutePossible(r) && this.getBatteryForRoute(r) <= car.getBattery()) {
			this.carsDocked.remove(car.getId());
			this.myStation.getFlightQueue().remove(0);
		}
		else if (!this.isRoutePossible(r)) {
			this.myStation.getFlightQueue().remove(0);
			this.mHandler.post(this.mUpdateResults);
			return;
		}
		else
			return;
		
		// TENTA POR MAIS QUALUQER COISA NA VIAGEM
		
		for (Flight ftry : this.myStation.getFlightQueue()) {
			
			Route newr = r.clone();
			newr.getRoute().removeLast();
			newr.getRoute().add(ftry);
			
			Station last = null;
			double distince = 0;
			
			for (Station s : this.myStation.getStations()) {
				double newdist = this.findDistance(s.getLat(), s.getLon(), newr.getRoute().getLast().getLat(), newr.getRoute().getLast().getLon());
				if (last == null || distince > newdist) {
					last = s;
					distince = newdist;
				}
			}
			
			Flight destinys = new Flight();
			destinys.setLat(last.getLat());
			destinys.setLon(last.getLon());
			newr.getRoute().add(destinys);
			
			if (this.isRoutePossible(newr) && this.getBatteryForRoute(newr) <= car.getBattery()) {
				r = newr;
			}
		}
		
		for (Flight ftoRem : r.getRoute()) {
			if (this.myStation.getFlightQueue().contains(ftoRem))
				this.myStation.getFlightQueue().remove(ftoRem);
		}
		
		// POE NA LISTA PARA SEREM CHAMADOS
		this.rslist.add(new RouteSending(r));
		this.clist.add(car);
		
		this.mHandler.post(this.mUpdateResults);
		this.mHandler.post(this.mCallPassengers);
	}
	
	public void findMissingVehicle(RWCar car){
		if (car != null){
			ServerCar sc = this.getMyStation().getMissingcars().get(0);
			Route r = new Route();
			Route aux = sc.getRoute().clone();
			aux.getRoute().removeLast();
			for(;!aux.getRoute().isEmpty();){
				r.getRoute().add(aux.getRoute().getLast());
				aux.getRoute().removeLast();
				r.getRoute().getLast().setNpassengers(0);
				r.getRoute().getLast().setPartyname("");
			}
			Flight f = new Flight();
			f.setLat(sc.getLat());
			f.setLon(sc.getLon());
			r.getRoute().add(f);
	
			Station dest = null;
			double dist = 0;

			for (Station s : this.myStation.getStations()) {
				double newdist = this.findDistance(s.getLat(), s.getLon(), r.getRoute().getLast().getLat(), r.getRoute().getLast().getLon());
				if (dest == null || dist > newdist) {
					dest = s;
					dist = newdist;
				}
			}

			Flight destiny = new Flight();
			destiny.setLat(dest.getLat());
			destiny.setLon(dest.getLon());
			r.getRoute().add(destiny);

			// Verifica se o voo Ž possivel sen‹o cancela o flight
			if (this.isRoutePossible(r) && this.getBatteryForRoute(r) <= car.getBattery()) {
				this.carsDocked.remove(car.getId());
				this.getMyStation().getMissingcars().remove(0);
			}
			else if (!this.isRoutePossible(r)) {
				this.getMyStation().getMissingcars().remove(0);
				return;
			}
			else
				return;
			
			RouteSending rss = new RouteSending(r);
			
			Socket s;
			try {
				Log.d("Smartfleet", "Sending route");
				s = new Socket(car.getIp(), car.getPort());
				ObjectOutput oo = new ObjectOutputStream(s.getOutputStream());
				oo.writeObject(rss);
				// s.close();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void executeAndUpdateCharge() {

		List<Integer> l = new ArrayList<Integer>();
		for (RWCar c : this.getCarsToCharge().values()) {
			if (c.getPercentageBattery() == 100 || !this.getCarsDocked().containsKey(c.getId()))
				l.add(c.getId());
		}
		for (int i : l) {
			this.getCarsToCharge().remove(i);
		}
		
		HashMap<Integer, Double> carbatcharge = new HashMap<Integer, Double>();
		for (RWCar c : this.getCarsDocked().values()) {
			if (c.getPercentageBattery() < 100 && !this.getCarsToCharge().containsKey(c.getId()))
				carbatcharge.put(c.getId(), c.getPercentageBattery());
		}
		
		int min1id = -1;
		double min1per = 200;
		int min2id = -1;
		
		for (Entry<Integer, Double> e : carbatcharge.entrySet()) {
			if (e.getValue() < min1per) {
				min2id = min1id;
				min1id = e.getKey();
				min1per = e.getValue();
			}
		}
		
		if (this.getCarsToCharge().size() < 1) {
			if (min1id != -1)
				this.getCarsToCharge().put(min1id, this.getCarsDocked().get(min1id));
			if (min2id != -1)
				this.getCarsToCharge().put(min2id, this.getCarsDocked().get(min2id));
		}
		else if (this.getCarsToCharge().size() < 2) {
			if (min1id != -1)
				this.getCarsToCharge().put(min1id, this.getCarsDocked().get(min1id));
		}
		
		for (RWCar c : this.getCarsToCharge().values()) {
			Log.d("smartfleet", "Trying to charge car " + c.getId());
			ChargeMessage cm = new ChargeMessage();
			c.setBattery(c.getBattery() + cm.getCharge());
			try {
				Socket s = new Socket(c.getIp(), c.getPort());
				ObjectOutput oo = new ObjectOutputStream(s.getOutputStream());
				oo.writeObject(cm);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public double findDistance(double lat1, double lon1, double lat2, double lon2) {

		lat1 /= (0.000009 * 1E6);
		lon1 /= (0.000011 * 1E6);
		
		lat2 /= (0.000009 * 1E6);
		lon2 /= (0.000011 * 1E6);
		
		double dist = Math.sqrt(Math.pow(lat1 - lat2, 2) + Math.pow(lon1 - lon2, 2));
		return dist;
	}
	
	public double getBatteryForRoute(Route r) {

		double batnec = 0;
		Flight prev = null;
		
		for (Flight f : r.getRoute()) {
			
			if (prev == null) {
				batnec += this.findDistance(this.getMylocation().getLatitudeE6(), this.getMylocation().getLongitudeE6(), f.getLat(), f.getLon());
				prev = f;
			}
			else {
				batnec += this.findDistance(prev.getLat(), prev.getLon(), f.getLat(), f.getLon());
				prev = f;
			}
			
			batnec += 200;
		}
		
		return batnec;
	}
	
	public HashMap<Integer, RWCar> getCarsDocked() {

		return this.carsDocked;
	}
	
	public HashMap<Integer, RWCar> getCarsToCharge() {

		return this.carsToCharge;
	}
	
	public Flight getCurrentflight() {

		return this.currentflight;
	}
	
	public int getId() {

		return this.id;
	}
	
	public Handler getmHandler() {

		return this.mHandler;
	}
	
	public Runnable getmUpdateResults() {

		return this.mUpdateResults;
	}
	
	public String getMyip() {

		return this.myip;
	}
	
	public GeoPoint getMylocation() {

		return this.myStation.getMylocation();
	}
	
	public int getMyport() {

		return this.myport;
	}
	
	public FlyingStation getMyStation() {

		return this.myStation;
	}
	
	public String getRealworldip() {

		return this.realworldip;
	}
	
	public int getRealworldport() {

		return this.realworldport;
	}
	
	public String getServerip() {

		return this.serverip;
	}
	
	public int getServerport() {

		return this.serverport;
	}
	
	public boolean isBooking() {

		return this.booking;
	}
	
	public boolean isRoutePossible(Route r) {

		int npeople = 0;
		double distance = 0;
		Flight prev = null;
		
		for (Flight f : r.getRoute()) {
			npeople += f.getNpassengers();
			if (prev == null) {
				distance += this.findDistance(this.getMylocation().getLatitudeE6(), this.getMylocation().getLongitudeE6(), f.getLat(), f.getLon());
				prev = f;
			}
			else {
				distance += this.findDistance(prev.getLat(), prev.getLon(), f.getLat(), f.getLon());
				prev = f;
			}
		}
		
		if (npeople <= 4 && distance <= 36000)
			return true;
		else
			return false;
	}
	
	public void loadBookFlight(View V) {

		this.setContentView(R.layout.bookflight);
		this.currentflight = new Flight();
		this.booking = true;
	}
	
	public void loadSelectDestination(View V) {

		Intent myIntent = new Intent(this, SmartFleetDest.class);
		startActivityForResult(myIntent, 0);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		GeoPoint me = new GeoPoint(38736830, -9138181);
		
		// GeoPoint IST = new GeoPoint(38736830, -9138181);
		// GeoPoint TP = new GeoPoint(38736320, -9301917);
		GeoPoint AirPort = new GeoPoint(38765775, -9133007);
		
		this.myStation = new FlyingStation(me);
		this.setCarsDocked(new HashMap<Integer, RWCar>());
		this.setCarsToCharge(new HashMap<Integer, RWCar>());
		
		Station mes = new Station(me.getLatitudeE6(), me.getLongitudeE6());
		this.myStation.getStations().add(mes);
		
		this.setContentView(R.layout.main);
		this.mHandler.post(this.mUpdateResults);
		
		StationDispatchService.setMainActivity(this, this.myport);
		final Intent dispatcher = new Intent(this, StationDispatchService.class);
		startService(dispatcher);
		
		RouteDispatchService.setMainActivity(this);
		final Intent rdispatcher = new Intent(this, RouteDispatchService.class);
		startService(rdispatcher);
		
		ChargingService.setMainActivity(this);
		final Intent chargings = new Intent(this, ChargingService.class);
		startService(chargings);
		
		UpdateCentralServerService.setMainActivity(this);
		final Intent ucss = new Intent(this, UpdateCentralServerService.class);
		startService(ucss);
		
		this.registerOnRealWorld();
		
	}
	

	public void registerOnRealWorld() {

		try {
			Socket s = new Socket(this.realworldip, this.realworldport);
			StationLogin l = new StationLogin();
			
			l.setLat(this.myStation.getMylocation().getLatitudeE6());
			l.setLog(this.myStation.getMylocation().getLongitudeE6());
			l.setPort(this.myport);
			
			ObjectOutput oo = new ObjectOutputStream(s.getOutputStream());
			oo.writeObject(l);
			ObjectInput oi = new ObjectInputStream(s.getInputStream());
			LoginResponse cr = null;
			try {
				cr = (LoginResponse) oi.readObject();
				s.close();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.setId(cr.getId());
			
			Log.d("smartfleet", "Successfully logged at Real World Server.");
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void setBooking(boolean booking) {

		this.booking = booking;
	}
	
	public void setCarsDocked(HashMap<Integer, RWCar> carsDocked) {

		this.carsDocked = carsDocked;
	}
	
	public void setCarsToCharge(HashMap<Integer, RWCar> carsToCharge) {

		this.carsToCharge = carsToCharge;
	}
	
	public void setCurrentflight(Flight currentflight) {

		this.currentflight = currentflight;
	}
	
	public void setId(int id) {

		this.id = id;
	}
	
	public void setMyip(String myip) {

		this.myip = myip;
	}
	
	public void setMylocation(GeoPoint mylocation) {

		this.myStation.setMylocation(mylocation);
	}
	
	public void setMyport(int myport) {

		this.myport = myport;
	}
	
	public void setMyStation(FlyingStation myStation) {

		this.myStation = myStation;
	}
	
	public void setRealworldip(String realworldip) {

		this.realworldip = realworldip;
	}
	
	public void setRealworldport(int realworldport) {

		this.realworldport = realworldport;
	}
	
	public void setServerip(String serverip) {

		this.serverip = serverip;
	}
	
	public void setServerport(int serverport) {

		this.serverport = serverport;
	}
	
	public void updateUI() {

		if (this.booking)
			return;
		
		ListView lv = (ListView) this.findViewById(R.id.WaitingList);
		ArrayList<String> a = new ArrayList<String>();
		for (Flight f : this.myStation.getFlightQueue()) {
			a.add(f.getPartyname() + "-" + f.getNpassengers() + "-" + f.getDestination());
		}
		lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, a));
		
	}
	
}