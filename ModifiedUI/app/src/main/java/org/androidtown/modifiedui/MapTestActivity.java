package org.androidtown.modifiedui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapTestActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap googleMap;

    final static double mLat = 37.6103619;
    final static double mLng = 126.9955614;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_test);

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        //지도타입 - 일반
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //기본위치 설정
        LatLng position = new LatLng(mLat,mLng);
        //화면중앙의 위치와 카메라 줌 비율
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position,20));
        onAddMarker();
    }

    //마커, 원 추가
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
        this.googleMap.addMarker(mymarker);

        //원추가
        this.googleMap.addCircle(circle1KM);

    }
}
