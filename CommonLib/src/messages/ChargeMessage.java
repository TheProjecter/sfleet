package messages;

import java.io.Serializable;

public class ChargeMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 486951629633033075L;

	private double charge;
	
	public ChargeMessage(){
		this.setCharge(10);
	}

	public void setCharge(double charge) {
		this.charge = charge;
	}

	public double getCharge() {
		return charge;
	}
	
}
