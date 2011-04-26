package structs;

import java.io.Serializable;
import java.util.LinkedList;

public class Route implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1927071448614432216L;

	private LinkedList<Flight> route;
	
	public Route(){
		this.route = new LinkedList<Flight>();
	}

	public void setRoute(LinkedList<Flight> route) {
		this.route = route;
	}

	public LinkedList<Flight> getRoute() {
		return route;
	}
	
}
