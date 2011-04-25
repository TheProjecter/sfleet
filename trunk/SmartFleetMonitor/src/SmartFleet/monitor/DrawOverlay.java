package SmartFleet.monitor;

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

				for(MyStation s : this.sfm.getStationlist())
				{
					if(Math.abs((s.getMylocation().getLatitudeE6() - p.getLatitudeE6())) <= 5000 &&
							Math.abs((s.getMylocation().getLongitudeE6() - p.getLongitudeE6())) <= 5000){
						this.sfm.callStationInfo(s);
					}
				}
			}

			if(this.sfm.isShowcars()){

				for(MyCar c : this.sfm.getCarlist())
				{
					if(Math.abs((c.getMyLocation().getLatitudeE6() - p.getLatitudeE6())) <= 5000 &&
							Math.abs((c.getMyLocation().getLongitudeE6() - p.getLongitudeE6())) <= 5000){
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
		    	for(MyStation s : this.sfm.getStationlist())
		    	{
		    		projection.toPixels(s.getMylocation(), point);  
		    		canvas.drawCircle(point.x, point.y, 7, paint);
		    	}
		    }

		    paint.setColor(android.graphics.Color.RED);

		    if(this.sfm.isShowcars()){
		    	for(MyCar c : this.sfm.getCarlist())
		    	{	
		    		projection.toPixels(c.getMyLocation(), point);
		    		projection.toPixels(c.getMyDestination(), point2);
		    		canvas.drawCircle(point.x, point.y, 5, paint);
		    		canvas.drawCircle(point2.x, point2.y, (float) 2.5, paint);
		    		canvas.drawLine(point.x, point.y, point2.x, point2.y, paint);
		    	}
		    }
		    

		    super.draw(canvas, mapView, shadow);  
		}  
		
}
	

