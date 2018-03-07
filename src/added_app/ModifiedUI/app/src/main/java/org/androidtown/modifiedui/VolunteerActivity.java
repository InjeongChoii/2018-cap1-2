package org.androidtown.modifiedui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class VolunteerActivity extends AppCompatActivity {
    private static final String TAG = "VolunteerActivity";
    SupportMapFragment mapFragment;
    GoogleMap map;
    String locationX;
    String locationY;
    double mLat;
    double mLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d(TAG, "GoogleMap is ready.");
                map = googleMap;
            }
        });

        try{
            MapsInitializer.initialize(this);
        }catch (Exception e){
            e.printStackTrace();
        }

        startLocationService();
        requestMyLocation();


        Button obRegiButton = (Button) findViewById(R.id.obRegiButton);
        obRegiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VolunteerActivity.this,LocationRegisterActivity.class);


                intent.putExtra("locationX",locationX);
                intent.putExtra("locationY",locationY);
                VolunteerActivity.this.startActivity(intent);
            }
        });


    }

    private void requestMyLocation(){
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try{
            long minTime = 2000;
            float minDistance =0;
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    minTime, minDistance, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            showCurrentLocation(location);
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
                    });
            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastLocation != null){
                showCurrentLocation(lastLocation);
            }
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    minTime, minDistance, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            showCurrentLocation(location);
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
                    });
        }catch (SecurityException e){
            e.printStackTrace();
        }

    }

    private void showCurrentLocation(Location location){  //마커 표시
        LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint,18));
        //showMyLocationMarker(location);
    }

    private void startLocationService(){
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        GPSListener gpsListener = new GPSListener();
        long minTime = 2000;
        float minDistance = 0;

        try{
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime, minDistance, gpsListener);

            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTime, minDistance, gpsListener);

            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastLocation !=null){
                Double latitude = lastLocation.getLatitude();
                Double longitude = lastLocation.getLongitude();

                locationX = String.valueOf(latitude);
                locationY = String.valueOf(longitude);
                mLat = Double.parseDouble(locationX);
                mLng = Double.parseDouble(locationY);


            }
        }catch (SecurityException ex){
            ex.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), "위치확인이 시작되었습니다. 로그를 확인하세요.", Toast.LENGTH_SHORT).show();
    }

    public void onAddMarker(){
        LatLng position = new LatLng(mLat,mLng);

        //나의 위치 마커
        MarkerOptions mymarker = new MarkerOptions()
                .position(position);   //마커위치

        // 반경
        CircleOptions circle1KM = new CircleOptions().center(position) //원점
                .radius(5)      //반지름 단위 : m
                .strokeWidth(0f)  //선너비 0f : 선없음
                .fillColor(Color.parseColor("#880000ff")); //배경색

        //마커추가
        this.map.addMarker(mymarker);

        //원추가
        this.map.addCircle(circle1KM);

    }

protected class GPSListener implements LocationListener { //위치 리스너

    @Override
    public void onLocationChanged(Location location) {
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();

        String msg = "Latitude : "+latitude+ "\nLongitude : "+longitude;
        Log.i("GPSListener",msg);

        locationX = String.valueOf(latitude);
        locationY = String.valueOf(longitude);
        mLat = Double.parseDouble(locationX);
        mLng = Double.parseDouble(locationY);

        onAddMarker();

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
}

