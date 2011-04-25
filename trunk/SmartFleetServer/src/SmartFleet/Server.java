package SmartFleet;

import java.io.*;
import java.net.*;

class Server {
	// TODO: Simple server 
	// Modoficar para preencher lacunas
	//mudar ip no cliente que ainda Ž o carro quando carrega
	// o tagus como destino
	public static void main(String argv[]) throws Exception {
		String clientSentence;
		String capitalizedSentence;
		ServerSocket welcomeSocket = new ServerSocket(6769);
		System.out.println("Runing1..." + welcomeSocket.getInetAddress());

		while (true) {
			
			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			clientSentence = inFromClient.readLine();
			
			System.out.println("Received: " + clientSentence);
			capitalizedSentence = clientSentence.toUpperCase() + '\n';
			//outToClient.writeBytes(capitalizedSentence);
		}
	}
}