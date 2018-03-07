package org.androidtown.management;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by EJW on 2017-09-30.
 */

public class DeleteRequest extends StringRequest{

    final static private String URL = "http://172.30.1.4/ProbonoDBConn/Delete.php";
    private Map<String, String> parameters;

    public DeleteRequest(String userID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID",userID);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
