package com.example.p.pocketdoc;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class NearbyHospital extends AppCompatActivity {

    MaterialBetterSpinner spinner;
    Toolbar toolbar;
    RecyclerView recyclerView;
    RecyclerNearbyHospitalAdapter adapter;
    MaterialDialog dialog;
    Context context = this;
    String area[] = {"Akota","Old Padra Road","Manjalpur","Karelibaug","Dandia Bazar","Alkapuri","Waghodia","Fatehgunj","Sama"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_hospital);

        spinner = (MaterialBetterSpinner) findViewById(R.id.spinnerHospital);
        toolbar = (Toolbar) findViewById(R.id.toolbarHospital);
        toolbar.setTitle("Nearby Hospital");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,area);
        spinner.setAdapter(adapterSpinner);

        recyclerView = (RecyclerView) findViewById(R.id.nearbyHospitalRecycler);
        adapter = new RecyclerNearbyHospitalAdapter(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        spinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                adapter.clearData();
                try {
                    function(spinner.getText().toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                MaterialDialog.Builder builder = new MaterialDialog.Builder(context).title("Loading").content("Please wait, it might take some time !!").progress(true, 0).progressIndeterminateStyle(false);
                dialog = builder.build();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onItemRangeChanged(int positionStart, int itemCount) {
                        super.onItemRangeChanged(positionStart, itemCount);
                        dialog.dismiss();
                        adapter.unregisterAdapterDataObserver(this);
                    }
                });
            }
        });

    }

    public void function(String area) throws UnsupportedEncodingException {

        String url = Url.hospital + Uri.encode(area,"UTF-8");


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Login", MODE_PRIVATE);

        String token = sharedPreferences.getString("token", null);

        final String refreshToken = "Bearer " + token;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject object = new JSONObject();
                int hid = 0;
                String hname = "",haddress="",harea="",hcontact="",hlat="",hlong="",himage="";

                for (int i = 0; i < response.length(); i++) {
                    try {
                        object = response.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        hid = object.getInt("hid");
                        hname = object.getString("hname");
                        haddress = object.getString("haddress");
                        harea = object.getString("harea");
                        hcontact = object.getString("hcontact");
                        hlat = object.getString("hlat");
                        hlong = object.getString("hlong");
                        himage = object.getString("himage");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    HospitalData data = new HospitalData(haddress,harea,hcontact,hid,himage,hlat,hlong,hname);

                    adapter.setData(data);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError || error instanceof NoConnectionError) {
                    Toast.makeText(NearbyHospital.this, "Please Check your Internet Connection!!!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    String statuscode = String.valueOf(error.networkResponse.statusCode);
                    if (statuscode.equals("401")) {
                        Toast.makeText(NearbyHospital.this, "Seems like your session is expired. Please Login Again!!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(NearbyHospital.this, "Seems like some error has occured!!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", refreshToken);
                return headers;
            }


        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        SingletonRequest.getInstance(this.getApplicationContext()).addtoRequestQueue(request);

    }
}
