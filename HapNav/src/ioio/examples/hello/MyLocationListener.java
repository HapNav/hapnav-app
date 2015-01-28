package ioio.examples.hello;

import ioio.examples.hello.Guider;
import ioio.examples.hello.Main;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class MyLocationListener extends Activity implements LocationListener{
	

	@Override
	public void onLocationChanged(Location location) {
		 int latitude = (int) (location.getLatitude());
		    int longitude = (int) (location.getLongitude());
		    Log.i("Geo_Location", "Latitude: " + latitude + ", Longitude: " + longitude);
		    int routeLati = Integer.parseInt(Main.latiIs);
		    int routeLong = Integer.parseInt(Main.longIs);
		    String Maneuver = Main.turnIs; 
		
		 if( latitude - routeLati<= 0.5 && longitude - routeLong <= 0.5)
		 {
			 Guider.sendSignal(Maneuver) ;
		 }
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}


}