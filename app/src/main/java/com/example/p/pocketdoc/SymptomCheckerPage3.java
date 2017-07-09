package com.example.p.pocketdoc;

import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SymptomCheckerPage3 extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    RecyclerViewPage3Adapter adapter;
    MaterialDialog dialog;
    ArrayList<Integer> values = new ArrayList<Integer>();
    int sid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_checker_page3);

        values = getIntent().getIntegerArrayListExtra("list");
        sid = getIntent().getIntExtra("sid",0);

        toolbar = (Toolbar) findViewById(R.id.symptomPage3Toolbar);
        toolbar.setTitle("Symptom Checker");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.symptomPage3Recycler);
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new RecyclerViewPage3Adapter(this);
        function();
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this).title("Loading").content("Please wait, it might take some time !!").progress(true, 0).progressIndeterminateStyle(false);
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
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    public void function() {
        String url = Url.symptomPage2+sid+"/disease";

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Login", MODE_PRIVATE);

        String token = sharedPreferences.getString("token", null);

        final String refreshToken = "Bearer " + token;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url, new JSONArray(values), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject object = new JSONObject();
                int did = 0;
                String dname = "";
                Diseases temp;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        object = response.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        did = object.getInt("did");
                        dname = object.getString("dname");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    temp = new Diseases(did, dname);
                    adapter.setData(temp);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError || error instanceof NoConnectionError) {
                    Toast.makeText(SymptomCheckerPage3.this, "Please Check your Internet Connection!!!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    String statuscode = String.valueOf(error.networkResponse.statusCode);
                    if (statuscode.equals("401")) {
                        Toast.makeText(SymptomCheckerPage3.this, "Seems like your session is expired. Please Login Again!!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(SymptomCheckerPage3.this, "Seems like some error has occured!!", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }
}



