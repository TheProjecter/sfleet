package SmartFleet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
				FileInputStream fin = new FileInputStream(file);
				ObjectInputStream in = new ObjectInputStream(fin);
				state = (ServerState)in.readObject();
				fin.close();
				
				//state = new ServerState();
				// descomentar e retiorar a linha anterior para n‹o ter persistencia...
				
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
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
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
