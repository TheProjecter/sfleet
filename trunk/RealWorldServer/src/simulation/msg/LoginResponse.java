package simulation.msg;

import java.io.Serializable;

public class LoginResponse implements Serializable{

	private int id;
	
	private int port;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8522312154486048156L;

	public LoginResponse(int id, int port){
		this.setId(id);
		this.setPort(port);
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}
	
}
