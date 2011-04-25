package SmartFleet.station;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class SelectionOverlay extends Overlay{

	GeoPoint mylocation;
	
	public SelectionOverlay(){
		super();
	}
	
	@Override
	public boolean onTap(GeoPoint p, MapView mapview){
		this.mylocation = p;
		return true;
	}
	
	public GeoPoint getDestination(){
		return this.mylocation;
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {  
	   if(this.mylocation == null)
		   return;
		Projection projection = mapView.getProjection();  
	    Paint paint = new Paint();  
	    Point point = new Point();  
	    projection.toPixels(this.mylocation, point);  
	    paint.setColor(255);  
	    paint.setStrokeWidth(5);  
	    paint.setAlpha(100);
	    canvas.drawCircle(point.x, point.y, 7, paint);
	    super.draw(canvas, mapView, shadow);  
	}  
	
}
