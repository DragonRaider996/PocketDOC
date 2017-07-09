package com.example.p.pocketdoc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class Login extends AppCompatActivity {

    TextInputLayout email;
    TextInputLayout password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (TextInputLayout) findViewById(R.id.email);
        password = (TextInputLayout) findViewById(R.id.password);
        email.setErrorEnabled(true);
        password.setEnabled(true);
    }

    public void login(View v) {

        int flag = 0;

        String emailInput = email.getEditText().getText().toString().trim();
        String passwordInput = password.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            email.setError("Please Enter a Valid Email");
        } else {
            if(emailInput.length()>254)
            {
                email.setError("Only 254 characters allowed");
            }else {
                email.setErrorEnabled(false);
                email.setError(null);
                flag = flag + 1;
            }
        }

        if (passwordInput.isEmpty() || passwordInput.length() < 6) {
            password.setError("At Least 6 characters");
        } else {
            if (passwordInput.length() > 15) {
                password.setError("Only 15 characters allowed");
            } else {
                password.setErrorEnabled(false);
                password.setError(null);
                flag = flag+1;
            }
        }


        if(flag == 2) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(this).title("Authorizing").content("Please wait, it might take some time !!").progress(true, 0).progressIndeterminateStyle(false);
            final MaterialDialog dialog = builder.build();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            Map<String, String> requ = new HashMap<String, String>();
            requ.put("Email", emailInput);
            requ.put("Password", passwordInput);

            String url = Url.login;

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(requ), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String uid = response.getString("uid");
                        String token = response.getString("refreshToken");
                        String role = response.getString("role");

                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Login",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("uid",uid);
                        editor.putString("token",token);
                        editor.putString("role",role);
                        editor.commit();

                        Intent intent = new Intent(getApplicationContext(),Home.class);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if(error instanceof NetworkError || error instanceof NoConnectionError)
                    {
                        Toast.makeText(Login.this, "Please Check your Internet Connection!!!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else {
                        String statuscode = String.valueOf(error.networkResponse.statusCode);
                        if(statuscode.equals("422"))
                        {
                            Toast.makeText(Login.this, "Email Verification is remaining", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        }
                        else
                        {
                            Toast.makeText(Login.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }

                }
            }) {
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


    public void register(View v) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}
