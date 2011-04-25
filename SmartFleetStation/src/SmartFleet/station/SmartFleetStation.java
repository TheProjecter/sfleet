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

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import simulation.msg.LoginResponse;
import simulation.msg.StationLogin;
import android.app.Activity;
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
	
	// Need handler for callbacks to the UI thread 
    final Handler mHandler = new Handler(); 
    // Create runnable for posting 
    final Runnable mUpdateResults = new Runnable() { 
        public void run() { 
            updateUI(); 
        } 
    }; 
    
    private FlyingStation myStation;
    
	private Flight currentflight;
	
	private int id = 0;
	
	private String realworldip = "194.210.225.53";
	private int realworldport = 6798;
	
	private int myport = 5001;
	private String myip = "194.210.225.53";
	
	private boolean booking = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        GeoPoint IST = new GeoPoint(38736830, -9138181);
        GeoPoint TP = new GeoPoint(38736320, -9301917);
        GeoPoint AirPort = new GeoPoint(38765775, -9133007);
        
        this.myStation = new FlyingStation(IST);
        
        this.myStation.getDestinations().put("IST", IST);
        this.myStation.getDestinations().put("Tagus", TP);
        this.myStation.getDestinations().put("Airport", AirPort);
        
        this.setContentView(R.layout.main);
        this.mHandler.post(mUpdateResults);
        
      //  LoginService.setMainActivity(this);
      //  final Intent smartFleetStationService = new Intent(this, LoginService.class);
		//startService(smartFleetStationService);
        
	//	AddCarService.setMainActivity(this);
     //   final Intent addCarService = new Intent(this, AddCarService.class);
	//	startService(addCarService);
		
        StationDispatchService.setMainActivity(this, this.myport);
        final Intent dispatcher = new Intent(this, StationDispatchService.class);
        startService(dispatcher);
        
        this.registerOnRealWorld();
		
    }
    
    public void loadBookFlight(View V){
    	this.setContentView(R.layout.bookflight);
    	this.currentflight = new Flight();
    	this.booking = true;   	
    }
    
    public void loadSelectDestination(View V){
    	 Intent myIntent = new Intent(this, SmartFleetDest.class);
         startActivityForResult(myIntent, 0);
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
    	if(resultCode == RESULT_CANCELED)
    		return;
    	
    	GeoPoint gp = new GeoPoint(data.getIntExtra("lat", 0), data.getIntExtra("log", 0));
    	this.currentflight.setDestcoords(gp);
    	
    	String locationname = "UNDEFINED";
    	
    	double lat = gp.getLatitudeE6()/1E6;
		double log = gp.getLongitudeE6()/1E6;
    	
		 
        HttpURLConnection connection = null;
        URL serverAddress = null;

        try 
        {    	
            // build the URL using the latitude & longitude you want to lookup
            // NOTE: I chose XML return format here but you can choose something else
            serverAddress = new URL("http://maps.google.com/maps/geo?q=" + Double.toString(lat) + "," + Double.toString(log) +
                        "&output=xml&oe=utf8&sensor=true&key=" + R.string.ApiMapKey);
            //set up out communications stuff
            connection = null;
            
            Log.d("CUCU", Double.toString(lat));
            Log.d("CUCU", Double.toString(log));
            //Set up the initial connection
            connection = (HttpURLConnection)serverAddress.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setReadTimeout(10000);
                          
            connection.connect();
            
            try
            {
                InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                InputSource source = new InputSource(isr);
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                XMLReader xr = parser.getXMLReader();
                GoogleReverseGeocodeXmlHandler handler = new GoogleReverseGeocodeXmlHandler();
                
                xr.setContentHandler(handler);
                xr.parse(source);
                
                locationname = handler.getStreetName();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

		
    	this.currentflight.setDestination(locationname);
    }
    
    public void bookCancel(View V){
    	this.currentflight = null;
    	this.mHandler.post(mUpdateResults);
    	this.setContentView(R.layout.main);
    	this.booking = false;
    }
    
    public void bookCommit(View V){
    	EditText name = (EditText)this.findViewById(R.id.pname);
    	EditText num = (EditText)this.findViewById(R.id.pnumber);
    	
       	String pname = name.getText().toString();
       	int numb;
       	try{
       		numb = Integer.parseInt(num.getText().toString());
       	}catch(NumberFormatException e){
       		numb = -1;
       	}
       	
       	if(pname.equals("") || pname == null || numb < 1 || numb > 4 || this.currentflight.getDestcoords() == null ){
       		String error = "There's something wrong with your values...";
       		Toast.makeText(this.getApplicationContext(), error, Toast.LENGTH_SHORT).show();
       	}else{
       		this.currentflight.setPartyname(pname);
       		this.currentflight.setNpassengers(numb);

       		this.myStation.getFlightQueue().add(this.currentflight);
       		this.currentflight = null;
       		this.mHandler.post(mUpdateResults);
       		this.setContentView(R.layout.main);
       	}
       	
       	this.booking = false;
    }
    
    public void updateUI(){
    	ListView lv = (ListView)this.findViewById(R.id.WaitingList);
    	ArrayList<String> a = new ArrayList<String>();
    	for(Flight f : this.myStation.getFlightQueue()){
    		a.add(f.getPartyname() + "-" + f.getNpassengers() + "-" + f.getDestination());
    	}
    	lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, a));
    	
    }

public void registerOnRealWorld(){
		
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
				cr = (LoginResponse)oi.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.setId(cr.getId());
			this.setMyport(cr.getPort());
		
			s.close();
		
			Log.d("smartfleet", "Successfully logged at Real World Server.");
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
    
	public void setMylocation(GeoPoint mylocation) {
		this.myStation.setMylocation(mylocation);
	}

	public GeoPoint getMylocation() {
		return this.myStation.getMylocation();
	}

	public boolean isBooking() {
		return booking;
	}

	public void setBooking(boolean booking) {
		this.booking = booking;
	}

	public FlyingStation getMyStation() {
		return myStation;
	}

	public void setMyStation(FlyingStation myStation) {
		this.myStation = myStation;
	}

	public Flight getCurrentflight() {
		return currentflight;
	}

	public void setCurrentflight(Flight currentflight) {
		this.currentflight = currentflight;
	}

	public Handler getmHandler() {
		return mHandler;
	}

	public Runnable getmUpdateResults() {
		return mUpdateResults;
	}

	public void setMyport(int myport) {
		this.myport = myport;
	}

	public int getMyport() {
		return myport;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setMyip(String myip) {
		this.myip = myip;
	}

	public String getMyip() {
		return myip;
	}
    
}