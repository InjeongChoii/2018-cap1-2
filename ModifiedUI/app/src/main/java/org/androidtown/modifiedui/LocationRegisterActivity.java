package org.androidtown.modifiedui;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LocationRegisterActivity extends AppCompatActivity {
    //Management\RegisterActivity 참고

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_register);

        final EditText latitude = (EditText)findViewById(R.id.latitude);
        final EditText longitude = (EditText)findViewById(R.id.longitude);
        final EditText typeText = (EditText)findViewById(R.id.type);
        //String userID = "";

        Button locationButton = (Button) findViewById(R.id.locationButton);

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String locationX = latitude.getText().toString();
                String locationY = longitude.getText().toString();
                String type = typeText.getText().toString();
                String userID = "010-9999-9998";

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("successL");
                            if(success){
                                AlertDialog.Builder builder = new AlertDialog.Builder(LocationRegisterActivity.this);
                                builder.setMessage("위치 등록에 성공했습니다.")
                                        .setPositiveButton("확인",null)
                                        .create()
                                        .show();
                                finish();
                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(LocationRegisterActivity.this);
                                builder.setMessage("위치 등록에 실패하였습니다.")
                                        .setNegativeButton("다시 시도", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                LocationRequest locationRequest = new LocationRequest(locationX, locationY, type, userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LocationRegisterActivity.this);
                queue.add(locationRequest);

            }
        });
    }
}
