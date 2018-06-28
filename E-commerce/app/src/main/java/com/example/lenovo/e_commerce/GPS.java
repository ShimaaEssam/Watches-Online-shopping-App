package com.example.lenovo.e_commerce;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class GPS extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    EditText AddressText;
    Location LocListener;
    LocationManager LocManager;
    Button Current_Location;
    String name;
    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        db = new DB(this);
        name = getIntent().getExtras().getString("name");
        AddressText = (EditText) findViewById(R.id.mapname);
        Current_Location = (Button) findViewById(R.id.mapbtn);
        //   LocListener=new Location(String.valueOf(getApplicationContext()));
        LocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            LocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 0, this);
        } catch (SecurityException SE) {
            Toast.makeText(getApplicationContext(), "You are not allowed to access current location ", Toast.LENGTH_SHORT).show();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(30.04441960, 31.235711600), 8));
        Current_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                Geocoder G = new Geocoder(getApplicationContext());
                List<Address> AddressList;
                android.location.Location Loc = null;
                try {
                    Loc = LocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                } catch (SecurityException S) {
                    Toast.makeText(getApplicationContext(), "You are not allowed to access current location ", Toast.LENGTH_SHORT).show();
                }
                if (Loc != null) {
                    LatLng myposition = new LatLng(Loc.getLatitude(), Loc.getLongitude());
                    try {
                        AddressList = G.getFromLocation(myposition.latitude, myposition.longitude, 1);
                        if (!AddressList.isEmpty()) {
                            String address = " ";
                            for (int i = 0; i <= AddressList.get(0).getMaxAddressLineIndex(); i++)
                                address = AddressList.get(0).getAddressLine(i) + ",";
                            mMap.addMarker(new MarkerOptions().position(myposition).title("My Position").snippet(address)).setDraggable(true);
                            AddressText.setText(address);
                            String addre = AddressText.getText().toString();
                            db.update_Address(name, addre);

                        }
                    } catch (IOException O) {
                        mMap.addMarker(new MarkerOptions().position(myposition).title("My Position"));
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myposition, 15));
                } else
                    Toast.makeText(getApplicationContext(), "Please wait until your position is determined", Toast.LENGTH_SHORT).show();
            }
        });
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Geocoder G = new Geocoder(getApplicationContext());
                List<Address> AddressList;
                try {
                    AddressList = G.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
                    if (!AddressList.isEmpty()) {
                        String address = " ";
                        for (int i = 0; i <= AddressList.get(0).getMaxAddressLineIndex(); i++)
                            address = AddressList.get(0).getAddressLine(i) + ",";
                        AddressText.setText(address);
                    } else {
                        Toast.makeText(getApplicationContext(), "No address for this location", Toast.LENGTH_SHORT).show();
                        AddressText.getText().clear();
                    }
                } catch (IOException O) {
                    Toast.makeText(getApplicationContext(), "can't get address,check your network", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v("location",location.getLatitude()+"");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
