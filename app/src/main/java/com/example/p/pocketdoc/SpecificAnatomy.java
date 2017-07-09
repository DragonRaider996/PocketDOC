package com.example.p.pocketdoc;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SpecificAnatomy extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    RecyclerSpecificAnatomyAdapter adapter;
    String title = "";
    int haid = 0;
    MaterialDialog dialog;
    TextView textView1,textView2,textView3,textView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_anatomy);

        haid = getIntent().getIntExtra("haid", 0);
        title = getIntent().getStringExtra("hatopic");

        toolbar = (Toolbar) findViewById(R.id.specificAnatomyToolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView1 = (TextView) findViewById(R.id.specificAnatomyInformation1);
        textView2 = (TextView) findViewById(R.id.specificAnatomyInformation2);
        textView3 = (TextView) findViewById(R.id.specificAnatomyInformation3);
        textView4 = (TextView) findViewById(R.id.specificAnatomyInformation4);

        recyclerView = (RecyclerView) findViewById(R.id.specificAnatomyRecycler);
        adapter = new RecyclerSpecificAnatomyAdapter(this,title);

        function();

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this).title("Loading").content("Please wait, it might take some time !!").progress(true, 0).progressIndeterminateStyle(false);
        dialog = builder.build();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }

    private void function() {
        String url = Url.anatomy + "/" + haid;

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Login", MODE_PRIVATE);

        String token = sharedPreferences.getString("token", null);

        final String refreshToken = "Bearer " + token;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray array = response.getJSONArray("paragraph");
                    String temp;
                    for(int i=0;i<array.length();i++)
                    {
                        temp = array.getString(i);
                        if(i==0)
                        {
                            textView1.setText(temp);
                        }
                        else if(i==1)
                        {
                            textView2.setText(temp);
                        }
                        else if(i==2)
                        {
                            textView3.setText(temp);
                        }
                        else
                        {
                            textView4.setText(temp);
                        }
                    }

                    JSONArray images = response.getJSONArray("images");
                    String image;
                    for(int i=0;i<images.length();i++)
                    {
                        image = images.getString(i);
                        adapter.setData(image);
                    }
                    dialog.dismiss();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError || error instanceof NoConnectionError) {
                    Toast.makeText(SpecificAnatomy.this, "Please Check your Internet Connection!!!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    String statuscode = String.valueOf(error.networkResponse.statusCode);
                    if (statuscode.equals("401")) {
                        Toast.makeText(SpecificAnatomy.this, "Seems like your session is expired. Please Login Again!!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(SpecificAnatomy.this, "Seems like some error has occured!!", Toast.LENGTH_SHORT).show();
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
