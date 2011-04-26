package SmartFleet.Car;

import structs.Flight;
import structs.Route;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class RouteOverlay extends Overlay {  
	
	private Route route;
	private int color;  
	private GeoPoint mylocation;

	public RouteOverlay(GeoPoint l, int color) {   
		this.color = color;  
		this.route = null;
		this.mylocation = l;
	}  
	
	@Override  
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {  
	    
		if(this.mylocation == null)
			return;
		
		Projection projection = mapView.getProjection();  
	    Paint paint = new Paint();  
	    Point point = new Point();  
	    projection.toPixels(this.mylocation, point);  
	    paint.setColor(color);  
	    paint.setStrokeWidth(5);  
	    paint.setAlpha(200);
	    canvas.drawCircle(point.x, point.y, 7, paint);
	    
	    if(this.route == null || this.route.getRoute().isEmpty())
	    	return;

	    Point prev = point;
	    
	    for(Flight f : this.route.getRoute()){
	    	Point point2 = new Point();
	    	projection.toPixels(new GeoPoint(f.getLat(), f.getLon()), point2);
	    	canvas.drawCircle(point2.x, point2.y, 7, paint);
	    	canvas.drawLine(prev.x, prev.y, point2.x, point2.y, paint);
	    	prev = point2;
	    }
	    super.draw(canvas, mapView, shadow);  
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public Route getRoute() {
		return route;
	}

	public void setMylocation(GeoPoint mylocation) {
		this.mylocation = mylocation;
	}

	public GeoPoint getMylocation() {
		return mylocation;
	}  
	
}