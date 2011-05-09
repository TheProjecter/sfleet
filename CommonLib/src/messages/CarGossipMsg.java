package messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import structs.RWCar;

public class CarGossipMsg implements Serializable {
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 6387424978710547046L;
	List<RWCar>					gossip;
	
	public CarGossipMsg() {

		this.gossip = new ArrayList<RWCar>();
		
	}
	
	public CarGossipMsg(Collection<RWCar> carList) {

		this.gossip = new ArrayList<RWCar>(carList);
	}
	
	public List<RWCar> getGossip() {

		return this.gossip;
	}
	
	public void setGossip(List<RWCar> gossip) {

		this.gossip = gossip;
	}
	
}
