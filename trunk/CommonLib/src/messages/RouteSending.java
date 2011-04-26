package messages;

import java.io.Serializable;

import structs.Route;

public class RouteSending implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2381219040055247156L;

	private Route route;
	
	public RouteSending(Route r){
		this.setRoute(r);
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public Route getRoute() {
		return route;
	}
	
	
	
}
