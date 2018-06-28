package com.example.lenovo.e_commerce;

import android.content.Context;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by LENOVO on 12/15/2017.
 */
public class Location implements LocationListener {
     Context activitycontext;
    public  Location(Context cont)
    {
        activitycontext=cont;
    }

    @Override
    public void onProviderDisabled(String provider)
    {
        Toast.makeText(activitycontext,"GPS Diabled ",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onProviderEnabled(String provider)
    {
        Toast.makeText(activitycontext,"GPS Enabled ",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onLocationChanged(android.location.Location location)
    {
        Toast.makeText(activitycontext,location.getAltitude()+","+location.getLongitude(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }
}
