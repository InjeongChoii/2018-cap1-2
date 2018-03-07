package org.androidtown.modifiedui;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by EJW on 2017-10-04.
 */

public class RegisterRequest extends StringRequest {
    final static private String URL= "http://10.30.30.192/ProbonoDBConn/Odi/Register_Odi.php";

    private Map<String, String> parameters;

    public RegisterRequest(String userID, String userName, String dORv, String carer, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("userName", userName);
        parameters.put("dORv", dORv);
        parameters.put("carer", carer);
    }
    @Override
    public Map<String, String> getParams(){
        return parameters;
    }

}
