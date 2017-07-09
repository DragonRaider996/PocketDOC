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

public class ForumDetail extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    RecyclerDetailForumAdapter adapter;
    TextView question,description;
    MaterialDialog dialog;
    FloatingActionButton button;
    int fid =0;
    String quest,descrip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_detail);

        fid = getIntent().getIntExtra("fid",0);

        toolbar = (Toolbar) findViewById(R.id.forumDetailToolbar);
        toolbar.setTitle("Forum");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.forumDetailRecycler);
        recyclerView.setNestedScrollingEnabled(false);
        question = (TextView) findViewById(R.id.questionForumDetail);
        description = (TextView) findViewById(R.id.descriptionForumDetail);
        button = (FloatingActionButton) findViewById(R.id.floatingForumDetail);
        
        adapter = new RecyclerDetailForumAdapter(this);
        
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

    private void function() {

        String url = Url.forumAll+ fid ;

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Login", MODE_PRIVATE);

        String token = sharedPreferences.getString("token", null);

        final String refreshToken = "Bearer " + token;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    quest = response.getString("question");
                    descrip= response.getString("description");

                    question.setText(quest);
                    description.setText(descrip);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray array = response.getJSONArray("comments");
                    String name;
                    String comment;
                    JSONObject object = new JSONObject();
                    for(int i=0;i<array.length();i++)
                    {
                        object = array.getJSONObject(i);
                        name = object.getString("name");
                        comment = object.getString("comment");

                        Comments comments = new Comments(comment,name);
                        adapter.setData(comments);
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
                    Toast.makeText(ForumDetail.this, "Please Check your Internet Connection!!!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    String statuscode = String.valueOf(error.networkResponse.statusCode);
                    if (statuscode.equals("401")) {
                        Toast.makeText(ForumDetail.this, "Seems like your session is expired. Please Login Again!!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(ForumDetail.this, "Seems like some error has occured!!", Toast.LENGTH_SHORT).show();
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


    public void comment(View v)
    {
        Intent intent = new Intent(this,CommentPost.class);
        intent.putExtra("fid",fid);
        intent.putExtra("question",quest);
        intent.putExtra("description",descrip);
        startActivity(intent);
    }

}
