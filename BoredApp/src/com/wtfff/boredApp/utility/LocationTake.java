package com.wtfff.boredApp.utility;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;

public class LocationTake {
	
	private android.location.Location location_now=null;
	
	public android.location.Location updateStat(Context mContext, LocationListener mLocationListener){
		LocationManager mgr=(LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);;
    	if(mgr.isProviderEnabled(LocationManager.GPS_PROVIDER))
    	{
    		mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, mLocationListener);
			if(mgr!=null){
				location_now = mgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			}
		}
    	if(location_now==null && mgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
    		mgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, mLocationListener);
    		if (mgr!= null){
    	    	short i=0;
    	    	while(location_now  == null){
    	    		location_now = mgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    	    		if(i++==1000){
    	    			location_now = mgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    	    			break;
    	    		}
    	    	}
    		}
	    }
    	if(location_now==null && mgr.isProviderEnabled(LocationManager.PASSIVE_PROVIDER))
    	{
    		mgr.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 2000, 0, mLocationListener);
			if(mgr!=null){
				location_now = mgr.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
			}
		}
    	return location_now;
    }

}
