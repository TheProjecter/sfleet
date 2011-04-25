package simulation;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Timer;
import java.util.TimerTask;



public class RWServer {
	
	public static void main(String[] args){
		
		ServerSocket server;
		RWWorker worker;
		RWState state;
		
		
		
		try {
			server = new ServerSocket(6798);
			state = new RWState();
			
			System.out.println("->Real World Server: I am running...");
			
			
			while(true){
				worker = new RWWorker(server.accept(), state);
				Thread t = new Thread(worker);
				t.start();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
