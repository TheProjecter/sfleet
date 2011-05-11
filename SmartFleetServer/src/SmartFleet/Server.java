package SmartFleet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	public static void main(String[] args) {

		ServerSocket server;
		ServerWorker worker;
		ServerState state = null;
		
		File file = new File("backup.cmov");
		try {
			if (file.exists()) { // existe ficheiro com estado
				// FileInputStream fin = new FileInputStream(file);
				// ObjectInputStream in = new ObjectInputStream(fin);
				// state = (ServerState) in.readObject();
				// fin.close();
				state = new ServerState();
				// TODO: descomentar e retiorar a linha anterior
				
			}
			else {
				state = new ServerState();
				FileOutputStream fout = new FileOutputStream("backup.cmov");
				ObjectOutputStream oos = new ObjectOutputStream(fout);
				oos.writeObject(state);
				oos.close();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// POPULATE MONITOR FOR TESTING
		/* StationMessage IST = new StationMessage(0, 38736830, -9138181, 3, 2); StationMessage TP = new StationMessage(1, 38736320, -9301917, 10, 10); StationMessage AIRPORT = new StationMessage(2, 38765775, -9133007, 40, 30);
		 * 
		 * CarMessage car1 = new CarMessage(4, 38736320, -9301917, 26000); CarMessage car2 = new CarMessage(8, 38765775, -9133007, 4000);
		 * 
		 * state.getNewcars().put(car1.getId(), car1); state.getNewcars().put(car2.getId(), car2);
		 * 
		 * state.getNewstations().put(IST.getId(), IST); state.getNewstations().put(TP.getId(), TP); state.getNewstations().put(AIRPORT.getId(), AIRPORT);
		 * 
		 * car1.setBattery(20000); car1.setLat(38736830); car1.setLon(-9138181); car2.setBattery(3000); car2.setLat(38736320); car2.setLon(-9301917);
		 * 
		 * state.getUpdatecars().put(car1.getId(), car1); state.getUpdatecars().put(car2.getId(), car2);
		 * 
		 * IST.setPassengers(15); IST.setWaitingtime(15); TP.setPassengers(15); TP.setWaitingtime(15); AIRPORT.setPassengers(15); AIRPORT.setWaitingtime(15); state.getUpdatestations().put(IST.getId(), IST); state.getUpdatestations().put(AIRPORT.getId(), AIRPORT); state.getUpdatestations().put(TP.getId(), TP); */

		try {
			server = new ServerSocket(6799);
			
			System.out.println("->Central Server: I am running...");
			
			MissingVehicle missing = new MissingVehicle(state);
			Thread m = new Thread(missing);
			m.start();
			
			while (true) {
				Socket s = server.accept();
				worker = new ServerWorker(s, state);
				Thread t = new Thread(worker);
				t.start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
