package rs.direktnoizbaste.dizb.web_requests;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import rs.direktnoizbaste.dizb.R;
import rs.direktnoizbaste.dizb.app.AppConfig;
import rs.direktnoizbaste.dizb.app.AppController;
import rs.direktnoizbaste.dizb.callback_interfaces.WebRequestCallbackInterface;
import rs.direktnoizbaste.dizb.dialogs.ProgressDialogCustom;

/**
 * Created by 1 on 1/29/2016.
 */
public class PullSensorListRequest {
    WebRequestCallbackInterface webRequestCallbackInterface;
    ListView listView;
    private Context context;
    private ProgressDialogCustom progressDialog;
    private JSONObject[] jsonObjects;

    public PullSensorListRequest(Activity context) {
        this.context = context;
        progressDialog = new ProgressDialogCustom(context);
        progressDialog.setCancelable(false);
        listView = (ListView) context.findViewById(R.id.listView);
        webRequestCallbackInterface = null;
    }

    public void setCallbackListener(WebRequestCallbackInterface listener) {
        this.webRequestCallbackInterface = listener;
    }

    /**
     * function to pull sensor list form web server
     */
    public void pullSensorList(final String uid) {
        // Tag used to cancel the request
        String tag_string_req = "req_pull_sensors";
        progressDialog.showDialog(context.getString(R.string.progress_update_sensor_list));

        String url = String.format(AppConfig.URL_SENSOR_LIST_GET, uid);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progressDialog.hideDialog();
                if (response != null) {
                    Log.i("pullSensorResp:", "Nije null");
                    Log.i("pullSensorResp:", response);
                } else {
                    Log.d("pullSensorResp", "NULL RESPONSE");
                }

                try {

                    JSONObject jObj = new JSONObject(response);

                    boolean success = jObj.getBoolean("success");


                    if (success) {
                        //create Array of JSON objects
                        JSONArray jArr = jObj.getJSONArray("senzor");

                        jsonObjects = new JSONObject[jArr.length()];
                        for (int i = 0; i < jArr.length(); i++) {
                            jsonObjects[i] = jArr.getJSONObject(i);
                        }

                        webRequestCallbackInterface.webRequestSuccess(true, jsonObjects);


                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        webRequestCallbackInterface.webRequestSuccess(false, jsonObjects);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                webRequestCallbackInterface.webRequestError(error.getMessage());
                progressDialog.hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Post params
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "povuciSensorUid");
                params.put("id", uid);
                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to  queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}