package com.example.p.pocketdoc;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Register extends AppCompatActivity {


    TextInputLayout emailInput, usernameInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailInput = (TextInputLayout) findViewById(R.id.emailTextInput);
        usernameInput = (TextInputLayout) findViewById(R.id.nameTextInput);
        passwordInput = (TextInputLayout) findViewById(R.id.passwordTextInput);

        emailInput.setErrorEnabled(true);
        usernameInput.setErrorEnabled(true);
        passwordInput.setErrorEnabled(true);

    }

    public void registerUser(View v) {
        String email = emailInput.getEditText().getText().toString().trim();
        String password = passwordInput.getEditText().getText().toString().trim();
        final String username = usernameInput.getEditText().getText().toString().trim();

        int count = 0;

        if (email.isEmpty() || email.contains(" ")) {
            emailInput.setError("Please Enter a Valid Email");
        } else {
            if (email.length() > 254) {
                emailInput.setError("Please Enter a Valid Email");
            } else {
                if( !isValidEmail(email))
                {
                    emailInput.setError("Please Enter a Valid Email");
                }
                else {
                    emailInput.setErrorEnabled(false);
                    emailInput.setError(null);
                    count = count + 1;
                }
            }
        }
        if (password.isEmpty() || password.contains(" ")) {
            passwordInput.setError("At Least 6 characters and no Space Allowed");
        } else {
            if (password.length() > 15) {
                passwordInput.setError("Only 15 characters allowed");
            } else {
                if (password.length() < 6) {
                    passwordInput.setError("At Least 6 characters");
                } else {
                    passwordInput.setErrorEnabled(false);
                    passwordInput.setError(null);
                    count = count+1;
                }
            }
        }
        if (username.isEmpty() || username.contains(" ")) {
            usernameInput.setError("At Least 5 characters and no Space Allowed");
        } else {
            if (username.length() > 25) {
                usernameInput.setError("Only 25 characters allowed");
            } else {
                if (username.length() < 3) {
                    usernameInput.setError("At Least 3 characters");
                } else {
                    usernameInput.setErrorEnabled(false);
                    usernameInput.setError(null);
                    count = count+1;
                }
            }
        }

        if(count==3)
        {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(this).title("Registering").content("Please wait, it might take some time !!").progress(true, 0).progressIndeterminateStyle(false);
            final MaterialDialog dialog = builder.build();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            Map<String,String> requ = new HashMap<String, String>();
            requ.put("UserName",username);
            requ.put("Email",email);
            requ.put("Password",password);


            String url = Url.register;

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(requ), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    String uid = "";
                    try {
                        uid = response.getString("uid");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(),Verify.class);
                    intent.putExtra("uid",uid);
                    Toast.makeText(Register.this, "Check your mail for the Verification Code ", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(error instanceof NetworkError || error instanceof NoConnectionError)
                    {
                        Toast.makeText(Register.this, "Please Check your Internet Connection!!!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else {
                        String statuscode = String.valueOf(error.networkResponse.statusCode);
                        if(statuscode.equals("400"))
                        {
                            Toast.makeText(Register.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        else if(statuscode.equals("422"))
                        {
                            emailInput.setError("Email Already Exists");
                            dialog.dismiss();
                        }
                        else
                        {
                            usernameInput.setError("User Name Already Exists");
                            dialog.dismiss();
                        }
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

    public boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.isEmpty()) {
            return false;
        } else {
            if (email.matches(emailPattern)) {
                return true;
            } else {
                return false;
            }
        }
    }
}
