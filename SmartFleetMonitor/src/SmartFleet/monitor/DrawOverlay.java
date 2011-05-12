package SmartFleet.monitor;

import structs.Flight;
import structs.ServerCar;
import structs.ServerStation;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class DrawOverlay extends Overlay{

		private SmartFleetMonitor sfm; 

		public DrawOverlay(SmartFleetMonitor sfm) {  
			this.sfm = sfm; 
		}  

		@Override
		public boolean onTap(GeoPoint p, MapView mapview){
			
			if(this.sfm.isShowstations()){

				for(ServerStation s : this.sfm.getStationlist().values())
				{
					if(Math.abs((s.getLat() - p.getLatitudeE6())) <= 5000 &&
							Math.abs((s.getLon() - p.getLongitudeE6())) <= 5000){
						this.sfm.callStationInfo(s);
						return true;
					}
				}
			}

			if(this.sfm.isShowcars()){

				for(ServerCar c : this.sfm.getCarlist().values())
				{
					if(Math.abs((c.getLat() - p.getLatitudeE6())) <= 5000 &&
							Math.abs((c.getLon() - p.getLongitudeE6())) <= 5000){
						this.sfm.callCarInfo(c);
					}
				}
			}
			
			return true;
		}
		
		@Override  
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {  
		    Projection projection = mapView.getProjection();  
		    Paint paint = new Paint();  
		    Point point = new Point(); 
		    Point point2 = new Point();  
	
		    paint.setColor(android.graphics.Color.BLUE);  
		    paint.setStrokeWidth(5);  
		    paint.setAlpha(200);
		    
		    if(this.sfm.isShowstations() == true){
		    	for(ServerStation s : this.sfm.getStationlist().values())
		    	{
		    		projection.toPixels(new GeoPoint(s.getLat(), s.getLon()), point);  
		    		canvas.drawCircle(point.x, point.y, 7, paint);
		    	}
		    }

		    paint.setColor(android.graphics.Color.RED);

		    if(this.sfm.isShowcars()){
		    	for(ServerCar c : this.sfm.getCarlist().values())
		    	{	
		    		projection.toPixels(new GeoPoint(c.getLat(), c.getLon()), point);
		    		canvas.drawCircle(point.x, point.y, 5, paint);
		    		
		    		for(Flight f : c.getRoute().getRoute()){
		    			projection.toPixels(new GeoPoint(f.getLat(),
		    											 f.getLon()),
		    											 point2);
		    			canvas.drawLine(point.x, point.y, point2.x, point2.y, paint);
		    			point = point2;
		    		}
		    	}
		    }
		    

		    super.draw(canvas, mapView, shadow);  
		}  
		
}
	

