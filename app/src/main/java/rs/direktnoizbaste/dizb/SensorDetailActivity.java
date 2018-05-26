package rs.direktnoizbaste.dizb;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import rs.direktnoizbaste.dizb.app.AppConfig;
import rs.direktnoizbaste.dizb.app.AppController;
import rs.direktnoizbaste.dizb.app.SessionManager;


public class SensorDetailActivity extends AppCompatActivity {

    private SessionManager session;
    String SensorMAC = null;
    Integer KulturaId = null;
    String userID = null;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ArrayList arraylist;

    ListView listView;
    ViewAdapterSensorDetail adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_detail);
        arraylist = new ArrayList<>();

        listView = (ListView) findViewById(R.id.idListViewKategorije);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            SensorMAC = extras.getString("SensorMAC"); // retrieve the data using keyName
            KulturaId = extras.getInt("KulturaId");
        }
        Toast.makeText(this, SensorMAC + " " + KulturaId, Toast.LENGTH_SHORT).show();


        session = new SessionManager(getApplicationContext());
        userID = session.getUID();
        Log.d("testmiki", userID);
        Log.d("testmiki", "aaa");
        getResults(SensorMAC, KulturaId, userID);


    }

    private void getResults(final String sensorMAC, final Integer kulturaId, final String uid) {
        String tag_string_req = "req_login";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GETINFOPARAMETER_POST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {

                        jsonArray = jsonObject.getJSONArray("podaciSenzor");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject c = jsonArray.getJSONObject(i);
                            String ImeKulture = c.getString("ImeKulture");
                            Integer IdListaSenzora = c.getInt("IdListaSenzora");
                            Integer IdKulture = c.getInt("IdKulture");
                            Integer IdSenzorTip = c.getInt("IdSenzorTip");
                            String senzorTipIme = c.getString("senzorTipIme");

                            Integer OdPodaciIdeal = c.getInt("OdPodaciIdeal");
                            Integer DoPodaciIdeal = c.getInt("DoPodaciIdeal");
                            Integer OdZutoIdeal = c.getInt("OdZutoIdeal");
                            Integer DoZutoIdeal = c.getInt("DoZutoIdeal");


                            HashMap<String, String> grupaPodataka = new HashMap<>();
                            grupaPodataka.put("ImeKulture", ImeKulture);
                            grupaPodataka.put("IdListaSenzora", String.valueOf(IdListaSenzora));
                            grupaPodataka.put("IdSenzorTip", String.valueOf(IdSenzorTip));
                            grupaPodataka.put("senzorTipIme", senzorTipIme);
                            grupaPodataka.put("OdPodaciIdeal", String.valueOf(OdPodaciIdeal));
                            grupaPodataka.put("DoPodaciIdeal", String.valueOf(DoPodaciIdeal));
                            grupaPodataka.put("OdZutoIdeal", String.valueOf(OdZutoIdeal));
                            grupaPodataka.put("DoZutoIdeal", String.valueOf(DoZutoIdeal));

                            jsonArray = c.getJSONArray("podacizaSenzor");
                            for (int y = 0; y < jsonArray.length(); y++) {



                                JSONObject m = jsonArray.getJSONObject(y);
                                //String vremeSenzor = m.getString("vremeSenzor");

                                String vremeSenzor = m.getString("vremeSenzor");
                                DateFormat df = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");
                                try {
                                    Date birthDate = df.parse(vremeSenzor);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                                Integer idSenzorIncr = m.getInt("idSenzorIncr");
                                float vrednostSenzor = Float.valueOf(m.getString("vrednostSenzor"));
                                String OpisNotifikacije = m.getString("OpisNotifikacije");

                                grupaPodataka.put("vremeSenzor",vremeSenzor);
                                grupaPodataka.put("idSenzorIncr", String.valueOf(idSenzorIncr));
                                grupaPodataka.put("vrednostSenzor", String.valueOf(vrednostSenzor));
                                grupaPodataka.put("OpisNotifikacije", OpisNotifikacije);

                                arraylist.add(grupaPodataka);

                            }



                        }

                        // Get And Sent all data to ListView
                        callListView(arraylist);
                        Log.d("testmiki", String.valueOf(arraylist));

                    } else {
                        Log.d("testmiki", "Usao u ERROR");
                        // login error
                        String errorMsg = jsonObject.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                // progressDialog.hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "getsensordetailactivity");
                params.put("tag", "getdensorbasicinfo");
                params.put("sensormac", sensorMAC);
                params.put("id", uid);
                params.put("kulturaid", String.valueOf(kulturaId));

                Log.d("testmiki", String.valueOf(params));
                return params;
            }

        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));

        // Adding request to  queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void callListView(ArrayList arraylist) {
        Log.d("testmiki", String.valueOf(arraylist));

        if (arraylist.size() > 0) {
            adapter = new ViewAdapterSensorDetail(SensorDetailActivity.this, arraylist);
            listView.setAdapter(adapter);

        } else {
            Toast.makeText(getApplicationContext(), "No date from sensor.", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
