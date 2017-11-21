package org.androidtown.modifiedui;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapCallActivity extends AppCompatActivity {

    private static final String TAG2 = "MapCallActivity";
    SupportMapFragment mapFragment;
    GoogleMap map;
    TextView locationText;
    MarkerOptions myLocationMarker;

    private static final String TAG = "";
    private long btnPressTime = 0;
    private Toast toast;
    private String tel = "tel:";

    //boolean isGrantStorage = grantExternalStoragePermission();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_call);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String carer = intent.getStringExtra("carer");
        tel = tel + getPhoneNumberText(carer);

        startLocationService();
        requestMyLocation();



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

        //음성합성 : 보호자에게 전화를 걸려면 화면 하단을 두번 터치해주세요.
        Button callButton = (Button) findViewById(R.id.callButton);
        callButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                //if (isGrantStorage) {
                    if (System.currentTimeMillis() > btnPressTime + 1000) {
                        btnPressTime = System.currentTimeMillis();
                        return;
                    }
                    if (System.currentTimeMillis() <= btnPressTime + 1000) {
                        Intent intent2 = new Intent(Intent.ACTION_CALL, Uri.parse(tel));
                        startActivity(intent2);
                    }
                //}
            }
        });
    }

    public String getPhoneNumberText(String number) {
        String phoneText = "";

        number = number.replace("-", "");

        int length = number.length();

        if (number.length() >= 10) {
            phoneText = number.substring(0, 3) + "-"
                    + number.substring(3, length-4) + "-"
                    + number.substring(length-4, length);
        }

        return phoneText;
    }

    private void requestMyLocation(){
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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

    private void startLocationService(){
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        MapCallActivity.GPSListener gpsListener = new GPSListener();
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

//            Toast toast = Toast.makeText(getApplicationContext(),"Latitude : " + latitude + " , longitude : " + longitude , Toast.LENGTH_LONG);
//            toast.show();
            String msg = "Latitude : "+latitude+ "\nLongitude : "+longitude;
            Log.i("GPSListener",msg);

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