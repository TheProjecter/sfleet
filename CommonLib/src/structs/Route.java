package structs;

import java.io.Serializable;
import java.util.LinkedList;

public class Route implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1927071448614432216L;

	private LinkedList<Flight> flights;
	
	public Route(){
		this.flights = new LinkedList<Flight>();
	}

	public void setRoute(LinkedList<Flight> route) {
		this.flights = route;
	}

	public LinkedList<Flight> getRoute() {
		return flights;
	}
	
	@Override
	public Route clone(){
		Route newroute = new Route();
		for(Flight f : this.getRoute()){
			newroute.getRoute().add(f);
		}
		return newroute;
	}
	
}
