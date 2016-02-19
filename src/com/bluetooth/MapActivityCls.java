package com.bluetooth;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MapActivityCls extends MapActivity {
	public static boolean flag = false;
	Intent incomingIntent;
    MapView mymap;
    MapController mapController;
    MyLocationOverlay myOverlay;
	LocationManager locManager;
	LocationListener myLocListener;
	DataHelper dh;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        
        incomingIntent = getIntent();
        
        mymap = (MapView) findViewById(R.id.myMapView);
        mapController = mymap.getController();
        mapController.setZoom(8);
        mymap.setBuiltInZoomControls(true);
        
        myOverlay = new MyLocationOverlay(this, mymap);
        mymap.getOverlays().add(myOverlay);
        mymap.postInvalidate();
        
        locManager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        myLocListener = new MyListener();
        dh= new DataHelper(getApplicationContext());
   // last known location
        
        final Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        updateWithNewLocation(location);
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocListener);
        myOverlay.enableMyLocation();
    
    }

	private void updateWithNewLocation(final Location location) {
		if(location!=null){
			final double lat = location.getLatitude();
			final double longi = location.getLongitude();
			
			if(flag == false){
				flag = true;
				String address =  incomingIntent.getStringExtra("btAddress"); 
				Toast.makeText(getApplicationContext(), address, 
						200).show();
				
				// Insert Lat and longi inside database but   it shows force close
				
				dh.insert(lat, longi);
				Object locationInfo  []= dh.fetchLocationInfo();
			       double log = (Double)locationInfo[0];
			        double latt = (Double)locationInfo[1];
			        Toast.makeText(getApplicationContext(), "StoredLocation: .... " + log + "," + latt, 
							200).show();
				//dh.display();
				
			}
			final GeoPoint geoPoint = new GeoPoint((int)(lat*1E6), (int)(longi*1E6));
			//Toast.makeText(getApplicationContext(), "Latitude:-"+lat+"\nLongitude:- "+longi, 500).show();
			
			mymap.getController().animateTo(geoPoint);
			mymap.getController().setCenter(geoPoint);
		}
		
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	
	
	
	
	
	// my location listener
	class MyListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			updateWithNewLocation(location);
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
	}	@Override
	protected void onStop() {
		super.onStop();
		locManager.removeUpdates(myLocListener);
	}
	public void proceed(View v){
		Intent intent = new Intent(getApplicationContext(),Locatepath.class);
		startActivity(intent);
		/*final double src_lat=73.25341;
		final double src_lon=71.695043;
		final double des_lat1=83.690043;
		final double des_lon1=83.190053;
		String uri = "http://maps.google.com/maps?saddr=" + src_lat+","+src_lon+"&daddr="+des_lat1+","+des_lon1;
		 Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
		 intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
		 startActivity(intent);*/
	}
}
