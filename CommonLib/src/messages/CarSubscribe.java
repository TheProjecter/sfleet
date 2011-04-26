package messages;

import java.io.Serializable;

public class CarSubscribe implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6029085998849789958L;
	
	private int id;
	
	public CarSubscribe(int id){
		this.setId(id);
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
}
