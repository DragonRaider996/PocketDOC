package com.example.p.pocketdoc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForumPost extends AppCompatActivity {

    TextInputLayout question;
    TextInputLayout description;
    String quest,descript;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_post);

        toolbar = (Toolbar) findViewById(R.id.forumPostToolbar);
        toolbar.setTitle("Post your Question");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        question = (TextInputLayout) findViewById(R.id.questionForumPost);
        description = (TextInputLayout) findViewById(R.id.descriptionForumPost);

        question.setErrorEnabled(true);
        description.setErrorEnabled(true);

    }

    public void postData(View v)
    {
        int flag = 0;
        quest = question.getEditText().getText().toString().trim();
        descript = description.getEditText().getText().toString().trim();

        if(quest.isEmpty())
        {
            question.setError("Please enter a valid Question");
        }else{
            if(quest.length()>200)
            {
                question.setError("Only 1400 characters allowed");
            }else {
                question.setErrorEnabled(false);
                question.setError(null);
                flag = flag + 1;
            }
        }
        if(descript.isEmpty())
        {
            description.setError("Please Enter your Description for the Question");
        }else {
            if (descript.length() > 1400) {
                description.setError("Only 1400 characters allowed");
            } else {
                description.setErrorEnabled(false);
                description.setError(null);
                flag = flag + 1;
            }
        }
        if(flag == 2)
        {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(this).title("Posting the question").content("Please wait, it might take some time !!").progress(true, 0).progressIndeterminateStyle(false);
            final MaterialDialog dialog = builder.build();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Login", MODE_PRIVATE);

            String token = sharedPreferences.getString("token", null);
            final String refreshToken = "Bearer " + token;
            String uid = sharedPreferences.getString("uid",null);

            Map<String, String> requ = new HashMap<String, String>();
            requ.put("Question", quest);
            requ.put("Description", descript);
            requ.put("Uid",uid);

            String url = Url.forumAll;

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(requ), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String message = response.getString("message");
                        Toast.makeText(ForumPost.this, "Your Question was Successfully Posted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),ForumMain.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if(error instanceof NetworkError || error instanceof NoConnectionError)
                    {
                        Toast.makeText(ForumPost.this, "Please Check your Internet Connection!!!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else {
                        String statuscode = String.valueOf(error.networkResponse.statusCode);
                        if(statuscode.equals("417"))
                        {
                            Toast.makeText(ForumPost.this, "Incorrect Data", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        }
                        else
                        {
                            Toast.makeText(ForumPost.this, "Please Log in Again", Toast.LENGTH_SHORT).show();
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


}
