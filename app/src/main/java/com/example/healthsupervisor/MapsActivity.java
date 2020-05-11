package com.example.healthsupervisor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.fasterxml.jackson.core.ObjectCodec;
import com.github.ybq.android.spinkit.style.Pulse;
import com.google.android.gms.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.mbms.StreamingServiceInfo;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;

import static com.github.ybq.android.spinkit.animation.AnimationUtils.start;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{
    private static boolean userPressedBackAgain ;
    private GoogleMap mMap;
    private MeowBottomNavigation meo;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentLocationMarker;
    public static final int REQUEST_LOCATION = 99;
    int PROXIMITY_RADIUS = 2000;
    double latitude,longitude;
    private ProgressBar p;
    private MyCountDown timer;
    View v1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkLoationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        meo=(MeowBottomNavigation)findViewById(R.id.bottom_nav);
        meo.add(new MeowBottomNavigation.Model(1,R.drawable.headlines));
        meo.add(new MeowBottomNavigation.Model(2,R.drawable.diagnosis));
        meo.add(new MeowBottomNavigation.Model(3,R.drawable.reminder));
        meo.add(new MeowBottomNavigation.Model(4,R.drawable.nearest));
        meo.show(4,false);

        meo.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                // YOUR CODES
                switch (model.getId()){
                    case 1:
                        startActivity(new Intent(MapsActivity.this,HomeActivity.class));
                        Animatoo.animateSlideRight(MapsActivity.this);
                        break;
                    case 2:
                        startActivity(new Intent(MapsActivity.this,DiagnosisActivity.class));
                        Animatoo.animateSlideRight(MapsActivity.this);
                        break;
                    case 3:
                        startActivity(new Intent(MapsActivity.this,ReminderActivity.class));
                        Animatoo.animateSlideRight(MapsActivity.this);
                        break;
                    case 4:
                        break;
                }

                return null;
            }
        });
        SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = wmbPreference.getBoolean("FIRSTRUN", true);
        if (isFirstRun)
        {
            // Code to run once
            v1=findViewById(R.id.check);

            new GuideView.Builder(MapsActivity.this)
                    .setTitle("Click to see the nearest hospitals")
                    .setTargetView(v1)
                    .build()
                    .show();
            SharedPreferences.Editor editor = wmbPreference.edit();
            editor.putBoolean("FIRSTRUN", false);
            //editor.commit();
            editor.apply();
        }


        p = (ProgressBar)findViewById(R.id.progr);
        Pulse pulse = new Pulse();
        p.setIndeterminateDrawable(pulse);
        p.setVisibility(View.VISIBLE);
        timer = new MyCountDown(3000, 1000);
    }
    private class MyCountDown extends CountDownTimer
    {
        long duration, interval;
        public MyCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            // TODO Auto-generated constructor stub
            start();
        }

        @Override
        public void onFinish() {
            p.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onTick(long duration) {
            // could set text for a timer here
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REQUEST_LOCATION:
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission is granted
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                        if(client == null){
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else{

                }
        }
        return;
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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }


    }

    public void onClick(View v){
        if (v.getId()==R.id.check){
            String hospital = "hospital";
            String url = getUrl(latitude, longitude, hospital);
            Object dataTransfer[] = new Object[2];
            dataTransfer[0] = mMap;
            dataTransfer[1] = url;
            GetNearbyPlacesData getNearbyPlacesData =  new GetNearbyPlacesData();
            getNearbyPlacesData.execute(dataTransfer);
        }
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace){
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"YOUR_API_KEY");

        return googlePlaceUrl.toString();
    }
    protected synchronized void buildGoogleApiClient(){
        client = new GoogleApiClient.Builder(this).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).
                addApi(LocationServices.API).build();

        client.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        latitude=location.getLatitude();
        longitude = location.getLongitude();

        if(currentLocationMarker != null){
            currentLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("current location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        currentLocationMarker = mMap.addMarker(markerOptions);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(15).build();
        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomBy(15));

        if (client != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(client,locationRequest,this);


    }


    public boolean checkLoationPermission(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
            }
            else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
            }
            return false;
        }
        else
            return false;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onBackPressed () {

        if (! userPressedBackAgain ) {
            Toast. makeText ( this, "Back again to exit" , Toast. LENGTH_SHORT ) .show () ;

            userPressedBackAgain = true;
        } else {
            Intent intent = new Intent (Intent. ACTION_MAIN ) ;
            intent.addCategory (Intent. CATEGORY_HOME ) ;
            intent.setFlags (Intent. FLAG_ACTIVITY_NEW_TASK ) ;
            startActivity (intent) ;
        }

        new CountDownTimer( 3000 , 1000 ) {
            @Override
            public void onTick ( long millisUntilFinished) {

            }

            @Override
            public void onFinish () {
                userPressedBackAgain = false;
            }
        } .start () ;
    }
}
