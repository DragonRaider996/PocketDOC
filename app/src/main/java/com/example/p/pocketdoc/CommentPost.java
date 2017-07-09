package com.example.p.pocketdoc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
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

public class CommentPost extends AppCompatActivity {

    TextView question,description;
    TextInputLayout comments;
    int fid;
    String quest,descript,role,token,refreshToken,uid,comment;
    SharedPreferences sharedPreferences;
    MaterialDialog dialog;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_post);

        sharedPreferences = getApplicationContext().getSharedPreferences("Login", MODE_PRIVATE);

        toolbar = (Toolbar) findViewById(R.id.commentPostToolbar);
        toolbar.setTitle("Post Comment");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String token = sharedPreferences.getString("token", null);
        refreshToken = "Bearer " + token;
        uid = sharedPreferences.getString("uid",null);
        role = sharedPreferences.getString("role",null);

        fid = getIntent().getIntExtra("fid",0);
        quest = getIntent().getStringExtra("question");
        descript = getIntent().getStringExtra("description");
        question = (TextView) findViewById(R.id.questionCommentPost);
        question.setText(quest);
        description = (TextView) findViewById(R.id.descriptionCommentPost);
        description.setText(descript);

        comments = (TextInputLayout) findViewById(R.id.commentsPost);
        comments.setErrorEnabled(true);
    }


    public void postComment(View view)
    {
        int flag = 0;
        comment = comments.getEditText().getText().toString().trim();

        if(comment.isEmpty())
        {
            comments.setError("Please enter a valid Comment");
        }
        else
        {
            if(comment.length()>1400)
            {
                comments.setError("Only 1400 characters allowed");
            }
            else
            {
                comments.setErrorEnabled(false);
                comments.setError(null);
                flag = 1;
            }
        }

        if(flag == 1) {
            if (role.equals("User")) {
                MaterialDialog.Builder builder = new MaterialDialog.Builder(this).title("Error").content("Sorry you can't comment on the question as you are not a doctor").positiveText("Ok").onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });
                dialog = builder.build();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            } else {
                String url = Url.forumAll+fid+"/comments";

                MaterialDialog.Builder builder = new MaterialDialog.Builder(this).title("Posting the question").content("Please wait, it might take some time !!").progress(true, 0).progressIndeterminateStyle(false);
                final MaterialDialog dialog = builder.build();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                String fis = String.valueOf(fid);

                Map<String, String> requ = new HashMap<String, String>();
                requ.put("Fid", fis);
                requ.put("Comments", comment);
                requ.put("Uid",uid);

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(requ), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            Toast.makeText(CommentPost.this, "Your Question was Successfully Posted", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),ForumDetail.class);
                            intent.putExtra("fid",fid);
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
                            Toast.makeText(CommentPost.this, "Please Check your Internet Connection!!!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }else {
                            String statuscode = String.valueOf(error.networkResponse.statusCode);
                            if(statuscode.equals("417"))
                            {
                                Toast.makeText(CommentPost.this, "Incorrect Data", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();

                            }
                            else
                            {
                                Toast.makeText(CommentPost.this, "Please Log in Again", Toast.LENGTH_SHORT).show();
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
