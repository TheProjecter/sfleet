package messages;

import java.io.Serializable;

public class CarUnsubscribe implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8383550281674392264L;

	private int id;
	
	public CarUnsubscribe(int id){
		this.setId(id);
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}	
	
}
