package com.example.p.pocketdoc;

import android.content.SharedPreferences;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
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

public class SpecificFirstAid extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    RecyclerView recyclerView;
    RecyclerSpecificFirstAidAdapter adapter;
    MaterialDialog dialog;
    int faid = 0;
    String detail;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_first_aid);

        faid = getIntent().getIntExtra("faid",0);
        detail = getIntent().getStringExtra("detail");

        imageView = (ImageView) findViewById(R.id.imageFirstAid);

        setImage();

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapseSpecificFirstAid);
        collapsingToolbarLayout.setTitle(detail);
        toolbar = (Toolbar) findViewById(R.id.toolbarSpecificFirstAid);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.specificFirstAidRecycler);
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new RecyclerSpecificFirstAidAdapter(this);
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

    private void setImage() {

        if(detail.equals("No Breathing"))
        {
            imageView.setImageResource(R.drawable.nobreathing);
        }
        else if(detail.equals("Bleeding"))
        {
            imageView.setImageResource(R.drawable.bleeding);
        }
        else if(detail.equals("Shock"))
        {
            imageView.setImageResource(R.drawable.shock);
        }
        else if(detail.equals("Heart Attack"))
        {
            imageView.setImageResource(R.drawable.heartattack);
        }
        else if(detail.equals("Choking"))
        {
            imageView.setImageResource(R.drawable.choking);
        }
        else if(detail.equals("Eye Injuries"))
        {
            imageView.setImageResource(R.drawable.eyeinjuries);
        }
        else if(detail.equals("Burns"))
        {
            imageView.setImageResource(R.drawable.burns);
        }
        else if(detail.equals("Exposure To Hazardous Materials"))
        {
            imageView.setImageResource(R.drawable.exposuretohazardousmaterials);
        }
        else if(detail.equals("Broken Bones"))
        {
            imageView.setImageResource(R.drawable.brokenbones);
        }
        else if(detail.equals("Heat Exhaustion"))
        {
            imageView.setImageResource(R.drawable.heatexhaustion);
        }
        else if(detail.equals("Heat Stroke"))
        {
            imageView.setImageResource(R.drawable.heatstroke);
        }
        else
        {
            imageView.setImageResource(R.drawable.fainting);
        }
    }

    public void function() {
        String url = Url.firstAid + "/"+faid;

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Login", MODE_PRIVATE);

        String token = sharedPreferences.getString("token", null);

        final String refreshToken = "Bearer " + token;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject topic = response.getJSONObject("topic");
                    String faname = topic.getString("faquestion");
                    collapsingToolbarLayout.setTitle(faname);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray ans = response.getJSONArray("ans");
                    JSONObject object = new JSONObject();
                    String answer = "";
                    int aid = 0;
                    for (int i = 0; i < ans.length(); i++) {
                        try {
                            object = ans.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            answer = object.getString("answer");
                            aid = object.getInt("aid");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        FirstAidData temp = new FirstAidData(aid,answer);
                        adapter.setData(temp);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError || error instanceof NoConnectionError) {
                    Toast.makeText(SpecificFirstAid.this, "Please Check your Internet Connection!!!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    String statuscode = String.valueOf(error.networkResponse.statusCode);
                    if (statuscode.equals("401")) {
                        Toast.makeText(SpecificFirstAid.this, "Seems like your session is expired. Please Login Again!!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(SpecificFirstAid.this, "Seems like some error has occured!!", Toast.LENGTH_SHORT).show();
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

}
