package com.example.p.pocketdoc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

public class SymptomCheckerPage2 extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    RecyclerViewPage2Adapter adapter;
    MaterialDialog dialog;
    FloatingActionButton floatingActionButton;
    int sid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_checker_page2);

        sid = getIntent().getIntExtra("sid",0);

        toolbar = (Toolbar) findViewById(R.id.symptomPage2Toolbar);
        toolbar.setTitle("Symptom Checker");
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floating);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.symptomPage2Recycler);
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new RecyclerViewPage2Adapter(this);
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
        String url = Url.symptomPage2+sid;

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Login", MODE_PRIVATE);

        String token = sharedPreferences.getString("token", null);

        final String refreshToken = "Bearer " + token;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject object = new JSONObject();
                int sid = 0;
                String sname = "";
                Symptoms temp;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        object = response.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        sid = object.getInt("sid");
                        sname = object.getString("sname");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    temp = new Symptoms(sid, sname);
                    adapter.setData(temp);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError || error instanceof NoConnectionError) {
                    Toast.makeText(SymptomCheckerPage2.this, "Please Check your Internet Connection!!!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    String statuscode = String.valueOf(error.networkResponse.statusCode);
                    if (statuscode.equals("401")) {
                        Toast.makeText(SymptomCheckerPage2.this, "Seems like your session is expired. Please Login Again!!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(SymptomCheckerPage2.this, "Seems like some error has occured!!", Toast.LENGTH_SHORT).show();
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


    public void finalAnswer(View v)
    {
        ArrayList<Integer> ans = new ArrayList<Integer>();
        ans = adapter.getAnswer();
        if(ans.isEmpty())
        {
            Toast.makeText(this, "Please select at least one from the following!!", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(this, SymptomCheckerPage3.class);
            intent.putExtra("list", ans);
            intent.putExtra("sid", sid);
            startActivity(intent);
            this.finish();
        }
    }

}
