package org.androidtown.management;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by EJW on 2017-09-30.
 */

public class RegisterRequest extends StringRequest{
    final static private String URL= "http://172.30.1.18/ProbonoDBConn/Register.php";
    private Map<String, String> parameters;


    public RegisterRequest(String userID, String userPassword, String userName, int userAge, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("userPassword", userPassword);
        parameters.put("userName", userName);
        parameters.put("userAge", userAge+"");
    }
    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
