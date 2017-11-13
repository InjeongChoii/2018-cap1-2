package org.androidtown.modifiedui;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class VolunteerActivity extends AppCompatActivity {
    private static final String TAG = "VolunteerActivity";
    SupportMapFragment mapFragment;
    GoogleMap map;
    TextView locationText;
    MarkerOptions myLocationMarker;

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

        requestMyLocation();
       // locationText = (TextView) findViewById(R.id.locationText);

        /*위치확인 버튼 : 삭제함
        Button locationButton = (Button) findViewById(R.id.locationButton);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startLocationService();   ->현재위치 좌표 받아오기
                requestMyLocation();
            }
        });*/

        Button obRegiButton = (Button) findViewById(R.id.obRegiButton);
        obRegiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obRegiIntent = new Intent(VolunteerActivity.this,LocationRegisterActivity.class);
                VolunteerActivity.this.startActivity(obRegiIntent);
            }
        });


    }

    public void onResume(){
        super.onResume();
        if(map != null){
            map.setMyLocationEnabled(true);     //액티비티 중지시 내 위치 표시 활성화?
        }
    }
    public void onPause(){
        super.onPause();
        if(map !=null){
            map.setMyLocationEnabled(false);    //액티비티 중지시 내 위치 표시 비활성화
        }
    }

    private void requestMyLocation(){
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //boolean isGPSEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //boolean isnetworkEnabled = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        try{
            long minTime = 10000;
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
    /*private void showMyLocationMarker(Location location){
        if(myLocationMarker == null){
            myLocationMarker = new MarkerOptions();
            myLocationMarker.position(
                    new LatLng(location.getLatitude(), location.getLongitude()));
            myLocationMarker.title("내 위치 \n");
            myLocationMarker.snippet("GPS로 확인한 위치");
            myLocationMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
            map.addMarker(myLocationMarker);
        }else{
            myLocationMarker.position(new LatLng(location.getLatitude(),location.getLongitude()));
        }
    }*/

    private void startLocationService(){
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        GPSListener gpsListener = new GPSListener();
        long minTime = 10000;
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

                locationText.setText("내위치 : "+latitude + ", "+longitude);
                /*Toast.makeText(getApplicationContext(), "Last Known Location : "+ "Latitude"+ latitude
                + "\nLongitude: "+longitude, Toast.LENGTH_LONG).show();*/
            }
        }catch (SecurityException ex){
            ex.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), "위치확인이 시작되었습니다. 로그를 확인하세요.", Toast.LENGTH_SHORT).show();
    }


private class GPSListener implements LocationListener { //위치 리스너

    @Override
    public void onLocationChanged(Location location) {
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();

        String msg = "Latitude : "+latitude+ "\nLongitude : "+longitude;
        Log.i("GPSListener",msg);

        locationText.setText("내 위치 : " + latitude + ", "+longitude);
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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

