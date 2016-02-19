package com.bluetooth;



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



public class Locatepath extends Activity {
	public LocationManager locMgr;
	public LocationListener listener;
	public static Location location;
	DataHelper dh;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.locate_path);
       
       dh= new DataHelper(getApplicationContext());
             locMgr = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        listener = new MyListener();        
        location = locMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,listener);

        //updateWithNewLocation(location);
        
    }
    /*
	private void updateWithNewLocation(Location location) {
		if(location!=null){
			 double lat = location.getLatitude();
			 double longi = location.getLongitude();
			String stringToToast ="Lat: "+lat+"\nLon: "+longi;
			Toast.makeText(getApplicationContext(), stringToToast, 100).show();
		}
	}
	*/
	public void getLocation(View v){
		//final double des_lon1 =18.61800;
	//	final double des_lat1 =73.71800;
		
		Object locationInfo  []= dh.fetchLocationInfo();
	       double des_lon1 = (Double)locationInfo[0];
	        double des_lat1 = (Double)locationInfo[1];
		//updateWithNewLocation(location);
		if(location!=null){
			//String stringToToast ="Lat: "+location.getLatitude()+"\nLon: "+location.getLongitude();
			//Toast.makeText(getApplicationContext(), stringToToast, 100).show();
			
			String uri = "http://maps.google.com/maps?saddr=" + location.getLatitude()+","+location.getLongitude()+"&daddr="+des_lat1+","+des_lon1;
			 Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
			 intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
			 startActivity(intent);
			
		}
		else
			Toast.makeText(getApplicationContext(), "Location not available", 500).show();
		
		
	}
    
    class MyListener implements LocationListener{
		@Override
		public void onLocationChanged(final Location l) {
			//updateWithNewLocation(l);
			Locatepath.location = l;
			Toast.makeText(getApplicationContext(), "changed", 500).show();
		}
		@Override
		public void onProviderDisabled(String provider) {
			Intent gpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(gpsIntent);
		}
		@Override
		public void onProviderEnabled(String provider) {}
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}
	}	

    @Override
    protected void onStop() {
    	super.onStop();
    	locMgr.removeUpdates(listener);
    }
}
