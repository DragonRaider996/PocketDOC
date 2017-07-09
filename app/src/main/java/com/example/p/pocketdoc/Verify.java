package com.example.p.pocketdoc;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Verify extends AppCompatActivity {

    TextInputLayout verify;
    String code,uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        uid = getIntent().getStringExtra("uid");

        verify = (TextInputLayout) findViewById(R.id.verification);
        verify.setErrorEnabled(true);
    }

    public void verify(View v)
    {
        code = verify.getEditText().getText().toString().trim();

        if(code.isEmpty())
        {
            verify.setError("Please Enter the value");
        }
        else
        {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(this).title("Verifying").content("Please wait, it might take some time !!").progress(true, 0).progressIndeterminateStyle(false);
            final MaterialDialog dialog = builder.build();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            verify.setError(null);
            verify.setErrorEnabled(false);

            String url = Url.verify;

            int uidint = Integer.parseInt(uid);
            int codeint = Integer.parseInt(code);

            Integer uidfinal = uidint;
            Integer codefinal = codeint;

            Map<String,Integer> data = new HashMap<String, Integer>();
            data.put("Uid",uidfinal);
            data.put("Code",codefinal);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(Verify.this, "Verification was Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    dialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    //String body="";
                    if(error instanceof NetworkError || error instanceof NoConnectionError)
                    {
                        Toast.makeText(Verify.this, "Please Check your Internet Connection!!!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else {
                        //String statuscode = String.valueOf(error.networkResponse.statusCode);
                        Toast.makeText(Verify.this, "Invalid Code!!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            SingletonRequest.getInstance(this.getApplicationContext()).addtoRequestQueue(request);

        }

    }
}
