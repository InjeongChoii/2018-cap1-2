package org.androidtown.modifiedui;

import android.app.Application;
import android.os.StrictMode;


/**
 * 앱 전역에서 사용할 수 있는 클래스
 */
public class MyApp extends Application {
    private String userID;

    @Override
    public void onCreate() {
        super.onCreate();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String id) {
        this.userID = id;
    }
}
