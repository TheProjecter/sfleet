package simulation.msg;

import java.io.Serializable;

public class CarAdvertisement implements Serializable{


	private String ip;
	
	private int port;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1700548201752062688L;
	
	public CarAdvertisement(){
		
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}
	
	

}
