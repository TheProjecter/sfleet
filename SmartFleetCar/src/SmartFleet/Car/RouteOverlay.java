package SmartFleet.Car;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class RouteOverlay extends Overlay {  
	private GeoPoint gp1;  
	private GeoPoint gp2;  
	private int color;  

	public RouteOverlay(GeoPoint gp1, GeoPoint gp2, int color) {  
		this.gp1 = gp1;  
		this.gp2 = gp2;  
		this.color = color;  
	}  

	public void setOrigin(GeoPoint p){
		this.gp1 = p;
	}
	
	public void setDest(GeoPoint p){
		this.gp2 = p;
	}
	
	@Override  
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {  
	    Projection projection = mapView.getProjection();  
	    Paint paint = new Paint();  
	    Point point = new Point();  
	    projection.toPixels(gp1, point);  
	    paint.setColor(color);  
	    Point point2 = new Point();  
	    projection.toPixels(gp2, point2);  
	    paint.setStrokeWidth(5);  
	    paint.setAlpha(200);
	    canvas.drawCircle(point.x, point.y, 7, paint);
	    canvas.drawCircle(point2.x, point2.y, 7, paint);
	    canvas.drawLine(point.x, point.y, point2.x, point2.y, paint);  
	    super.draw(canvas, mapView, shadow);  
	}  
	
}