package org.androidtown.modifiedui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.security.Permission;

/**
 * Created by EJW on 2017-10-04.
 * 시작 대기화면ㅋㅋㅋㅋㅋㅋ
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            Thread.sleep(3000); //3초동안 대기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(this, PermissionActivity.class));
        finish();
    }
}
