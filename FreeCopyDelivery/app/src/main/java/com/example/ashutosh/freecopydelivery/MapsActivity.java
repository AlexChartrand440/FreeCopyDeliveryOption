package com.example.ashutosh.freecopydelivery;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.*;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.*;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,OnMarkerClickListener {

    private GoogleMap mMap;

    private static final LatLng store101 = new LatLng(28.7041, 77.1025);
    private static final LatLng store102 = new LatLng(19.0760, 72.8777);
    private static final LatLng store103 = new LatLng(13.0827, 80.2707);

    private static final LatLng college201 = new LatLng(23.2131, 72.6870);
    private static final LatLng college202 = new LatLng(28.5450, 77.1926);
    private static final LatLng college203 = new LatLng(19.1334, 72.9133);
    private static final LatLng college204 = new LatLng(12.9952, 80.2380);

    private Marker mStore101;
    private Marker mStore102;
    private Marker mStore103;

    private Marker mCollege201;
    private Marker mCollege202;
    private Marker mCollege203;
    private Marker mCollege204;

    private String sourcePlace="";
    private String destinationPlace="";

    private int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
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
        //googleMap.addMarker(new MarkerOptions().position(new LatLng(33.602,-111.888)).title("My Title"));



        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraUpdate centre=CameraUpdateFactory.newLatLng(new LatLng(20.5937,78.9629));

        CameraUpdate zoom=CameraUpdateFactory.zoomTo(5);
        googleMap.moveCamera(centre);
        googleMap.animateCamera(zoom);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                double latitude=latLng.latitude;
                double longitude=latLng.longitude;

                Geocoder geocoder;
                List<Address> addresses;
                geocoder=new Geocoder(MapsActivity.this,Locale.getDefault());

                try
                {
                    addresses=geocoder.getFromLocation(latitude,longitude,1);
                    String address=addresses.get(0).getAddressLine(0);
                    String city=addresses.get(0).getLocality();
                    String state=addresses.get(0).getAdminArea();
                    String country=addresses.get(0).getCountryName();
                    String postalCode=addresses.get(0).getPostalCode();

                    String message="Address Is :"+address+" "+city+" "+state+" "+country+" "+postalCode;
                    Toast.makeText(MapsActivity.this,message,Toast.LENGTH_LONG).show();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        mStore101 = googleMap.addMarker(new MarkerOptions()
                .position(store101)
                .title("Store 1"));
        mStore101.setTag(0);

        mStore102 = googleMap.addMarker(new MarkerOptions()
                .position(store102)
                .title("Store 2"));
        mStore102.setTag(0);

        mStore103 = googleMap.addMarker(new MarkerOptions()
                .position(store103)
                .title("Store 3"));
        mStore103.setTag(0);


        mCollege201 = googleMap.addMarker(new MarkerOptions()
                .position(college201)
                .title("IIT Gandhinagar"));
        mCollege201.setTag(0);

        mCollege202 = googleMap.addMarker(new MarkerOptions()
                .position(college202)
                .title("IIT Delhi"));
        mCollege202.setTag(0);

        mCollege203 = googleMap.addMarker(new MarkerOptions()
                .position(college203)
                .title("IIT Bombay"));
        mCollege203.setTag(0);

        mCollege204 = googleMap.addMarker(new MarkerOptions()
                .position(college204)
                .title("IIT Madras"));
        mCollege204.setTag(0);

        Toast.makeText(MapsActivity.this,"Click on the Store and College markers respectively",Toast.LENGTH_LONG).show();
        googleMap.setOnMarkerClickListener(this);



        /*mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    @Override
    public boolean onMarkerClick(Marker marker)
    {
        //Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.

            if(i==0)
            {
                sourcePlace=marker.getTitle().toString();
                Toast.makeText(MapsActivity.this,"Store is Clicked",Toast.LENGTH_SHORT).show();
                i++;
            }
            else if(i==1)
            {
                i=0;
                destinationPlace=marker.getTitle().toString();
                Toast.makeText(MapsActivity.this,"College is Clicked",Toast.LENGTH_SHORT).show();
            }

        if(!sourcePlace.isEmpty() && !destinationPlace.isEmpty())
        {
            Toast.makeText(MapsActivity.this,"Change Intent is Clicked",Toast.LENGTH_SHORT).show();
            Intent changePage=new Intent(MapsActivity.this,DeliveryOptions.class);
            changePage.putExtra("src",sourcePlace);
            changePage.putExtra("destn",destinationPlace);
            startActivity(changePage);
        }

        return false;
    }
}
