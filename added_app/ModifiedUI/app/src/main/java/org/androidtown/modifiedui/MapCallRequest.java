package org.androidtown.modifiedui;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by EJW on 2017-11-16.
 */

public class MapCallRequest extends StringRequest {
    final static private String URL= "http://10.30.30.192/ProbonoDBConn/Odi/MapCall_Odi.php";
    private Map<String, String> parameters;


    public MapCallRequest(String locationX, String locationY, String type, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("locationX", locationX);
        parameters.put("locationY", locationY);
        parameters.put("type", type);
    }
    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
