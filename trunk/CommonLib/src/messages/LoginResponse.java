package messages;

import java.io.Serializable;

import structs.RWStation;

public class LoginResponse implements Serializable{

	private int id;
	
	private RWStation station;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8522312154486048156L;

	public LoginResponse(){
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}


	public void setStation(RWStation station) {
		this.station = station;
	}

	public RWStation getStation() {
		return station;
	}
	
}
