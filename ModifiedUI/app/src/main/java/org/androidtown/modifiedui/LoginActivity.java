package org.androidtown.modifiedui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText idText = (EditText) findViewById(R.id.idText);
        final Button loginButton = (Button) findViewById(R.id.loginButton);
//        Button dbutton = (Button)findViewById(R.id.disabledButton);
//        Button vbutton = (Button)findViewById(R.id.volunteerButton);

        String userID = null;
        TelephonyManager mgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        try{
            userID = mgr.getLine1Number();
            userID = userID.replace("+82","0");
            if(userID != null) {
                idText.setText(userID);
            }else{
                idText.setText("failed to get phone number");
            }
        }catch (Exception e){}

        ((MyApp)getApplicationContext()).setUserID(userID);

//        Toast toast = Toast.makeText(getApplicationContext(),((MyApp) getApplicationContext()).getUserID(), Toast.LENGTH_LONG);
//        toast.show();

        //디비 연동 로그인
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userID = idText.getText().toString();
                //final String dORv = "";
                /*if(!loginCkbx.isChecked()){ //비장애인인 경우
                    dORv = "v";
                } else if(loginCkbx.isChecked()){
                    dORv = "d";
                }
                final String finalDORv = dORv;*/

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                String userID = jsonResponse.getString("userID");
                                String dORv = jsonResponse.getString("dORv");
                                String carer = jsonResponse.getString("carer");

//                                Toast toast = Toast.makeText(getApplicationContext(), userID +" dORV : "+ dORv + carer, Toast.LENGTH_LONG);
//                                toast.show();

                                if(dORv.equals("d")){
                                    Intent intent = new Intent(LoginActivity.this, MapCallActivity.class);
                                    intent.putExtra("userID", userID);
                                    intent.putExtra("carer", carer);
                                    LoginActivity.this.startActivity(intent);
                                }else if(dORv.equals("v")){
                                    Intent intent = new Intent(LoginActivity.this, VolunteerActivity.class);
                                    intent.putExtra("userID", userID);
                                    LoginActivity.this.startActivity(intent);
                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage("꾜ㅠ")
                                            .setNegativeButton("다시 시도", null)
                                            .create()
                                            .show();
                                }

                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("로그인에 실패하였습니다.")
                                        .setNegativeButton("다시 시도", null)
                                        .create()
                                        .show();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });


        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

    }
}
