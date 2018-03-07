package org.androidtown.modifiedui;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by EJW on 2017-10-09.
 */

public class LoginRequest extends StringRequest{

    final static private String URL= "http://10.30.30.192/ProbonoDBConn/Odi/Login_Odi.php";

    private Map<String, String> parameters;

    public LoginRequest(String userID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);

    }
    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
