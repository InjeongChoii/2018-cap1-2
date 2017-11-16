package org.androidtown.modifiedui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;

import org.json.JSONObject;

import java.util.ArrayList;

import static org.androidtown.modifiedui.R.id.idText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, SpeechRecognizeListener {

    private SpeechRecognizerClient client;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SpeechRecognizerManager.getInstance().initializeLibrary(this);

        final EditText idText = (EditText) findViewById(R.id.idText);
        final Button loginButton = (Button) findViewById(R.id.loginButton);

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

        Toast toast = Toast.makeText(getApplicationContext(),((MyApp) getApplicationContext()).getUserID(), Toast.LENGTH_LONG);
        toast.show();

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });
    }


    public void startUsingSpeechSDK() {

        // library를 초기화 합니다.
        // API를 사용할 시점이 되었을 때 initializeLibrary(Context)를 호출한다.
        // 사용을 마치면 finalizeLibrary()를 호출해야 한다.
        SpeechRecognizerManager.getInstance().initializeLibrary(this);

        findViewById(R.id.loginButton).setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // API를 더이상 사용하지 않을 때 finalizeLibrary()를 호출한다.
        SpeechRecognizerManager.getInstance().finalizeLibrary();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        String serviceType = SpeechRecognizerClient.SERVICE_TYPE_WEB;

        // 음성합성 sample activity 열기
        if (id == R.id.loginButton) {
//            final String userID = idText.getText().toString();

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
    }

    @Override
    public void onReady() {
        //TODO implement interface DaumSpeechRecognizeListener method
    }

    @Override
    public void onBeginningOfSpeech() {
        //TODO implement interface DaumSpeechRecognizeListener method
    }

    @Override
    public void onEndOfSpeech() {
        //TODO implement interface DaumSpeechRecognizeListener method
    }

    @Override
    public void onError(int errorCode, String errorMsg) {
        //TODO implement interface DaumSpeechRecognizeListener method
        Log.e("SpeechSampleActivity", "onError");

        client = null;
    }

    @Override
    public void onPartialResult(String text) {
        //TODO implement interface DaumSpeechRecognizeListener method
    }

    @Override
    public void onResults(Bundle results) {
        final StringBuilder builder = new StringBuilder();
        Log.i("SpeechSampleActivity", "onResults");

        ArrayList<String> texts = results.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
        ArrayList<Integer> confs = results.getIntegerArrayList(SpeechRecognizerClient.KEY_CONFIDENCE_VALUES);

        for (int i = 0; i < texts.size(); i++) {
            builder.append(texts.get(i));
            builder.append(" (");
            builder.append(confs.get(i).intValue());
            builder.append(")\n");
        }

        final Activity activity = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // finishing일때는 처리하지 않는다.
                if (activity.isFinishing()) return;

                AlertDialog.Builder dialog = new AlertDialog.Builder(activity).
                        setMessage(builder.toString()).
                        setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog.show();

            }
        });

        client = null;
    }

    @Override
    public void onAudioLevel(float v) {
        //TODO implement interface DaumSpeechRecognizeListener method
    }

    @Override
    public void onFinished() {
        Log.i("SpeechSampleActivity", "onFinished");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 300:
                if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    startUsingSpeechSDK();
                } else {
                    finish();
                }
                break;
            default:
                break;
        }
    }


}
