package com.example.p.pocketdoc;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewSpecificDisease extends AppCompatActivity {

    TextView textView;
    Toolbar toolbar;
    RecyclerView recyclerView1,recyclerView2,recyclerView3;
    RecyclerViewSpecificDiseaseAdapter adapter1,adapter2,adapter3;
    MaterialDialog dialog;
    int count = 0;
    int did;
    String dname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_specific_disease);

        textView = (TextView) findViewById(R.id.specificDiseaseInformation);
        toolbar = (Toolbar) findViewById(R.id.specificDiseaseToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView1 = (RecyclerView) findViewById(R.id.specificDiseaseRecycler1);
        recyclerView2 = (RecyclerView) findViewById(R.id.specificDiseaseRecycler2);
        recyclerView3 = (RecyclerView) findViewById(R.id.specificDiseaseRecycler3);

        recyclerView1.setNestedScrollingEnabled(false);
        recyclerView3.setNestedScrollingEnabled(false);
        recyclerView2.setNestedScrollingEnabled(false);

        adapter1 = new RecyclerViewSpecificDiseaseAdapter(this);
        adapter2 = new RecyclerViewSpecificDiseaseAdapter(this);
        adapter3 = new RecyclerViewSpecificDiseaseAdapter(this);

        did = getIntent().getIntExtra("did",0);
        dname = getIntent().getStringExtra("dname");
        toolbar.setTitle(dname);
        count = 0;

        function();

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this).title("Loading").content("Please wait, it might take some time !!").progress(true, 0).progressIndeterminateStyle(false);
        dialog = builder.build();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        adapter1.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                count = count+1;
                if(count == 3)
                {
                    dialog.dismiss();
                }
                adapter1.unregisterAdapterDataObserver(this);
            }
        });
        recyclerView1.setAdapter(adapter1);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));

        adapter2.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                count = count+1;
                if(count == 3)
                {
                    dialog.dismiss();
                }
                adapter2.unregisterAdapterDataObserver(this);
            }
        });
        recyclerView2.setAdapter(adapter2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        adapter3.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                count = count+1;
                if(count == 3)
                {
                    dialog.dismiss();
                }
                adapter3.unregisterAdapterDataObserver(this);
            }
        });
        recyclerView3.setAdapter(adapter3);
        recyclerView3.setLayoutManager(new LinearLayoutManager(this));


    }

    public void function() {
        String url = Url.specificDisease+did;

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Login", MODE_PRIVATE);

        String token = sharedPreferences.getString("token", null);

        final String refreshToken = "Bearer " + token;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject disease = response.getJSONObject("disease");
                    String dname = disease.getString("dname");
                    toolbar.setTitle(dname);
                    String dinfo = disease.getString("dinfo");
                    textView.setText(dinfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray symptoms = response.getJSONArray("symptoms");
                    JSONObject object = new JSONObject();
                    String sname = "";
                    for (int i = 0; i < symptoms.length(); i++) {
                        try {
                            object = symptoms.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            sname = object.getString("sname");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter1.setData(sname);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray preventions = response.getJSONArray("prevention");
                    JSONObject object = new JSONObject();
                    String pname = "";
                    for (int i = 0; i < preventions.length(); i++) {
                        try {
                            object = preventions.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            pname = object.getString("pname");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter2.setData(pname);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray remedies = response.getJSONArray("remedies");
                    JSONObject object = new JSONObject();
                    String rname="";
                    for (int i = 0; i < remedies.length(); i++) {
                        try {
                            object = remedies.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            rname = object.getString("rname");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter3.setData(rname);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError || error instanceof NoConnectionError) {
                    Toast.makeText(ViewSpecificDisease.this, "Please Check your Internet Connection!!!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    String statuscode = String.valueOf(error.networkResponse.statusCode);
                    if (statuscode.equals("401")) {
                        Toast.makeText(ViewSpecificDisease.this, "Seems like your session is expired. Please Login Again!!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(ViewSpecificDisease.this, "Seems like some error has occured!!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            }
        }){
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home)
        {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
