package com.mostafiz.loginregisterwithmysql;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    TextView register,loginerror;
    ProgressBar loginprogressbar;
    TextInputEditText textInputEditTextemail,textInputEditTextpassword;
    String email,password,profilename,profileaddress,profileemail;
    Button login;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        register=findViewById(R.id.registertv);
        textInputEditTextemail=findViewById(R.id.loginemail);
        textInputEditTextpassword=findViewById(R.id.loginpassword);
        loginprogressbar=findViewById(R.id.loginprogressbar);
        loginerror=findViewById(R.id.loginerror);
        login=findViewById(R.id.loginbutton);
        sharedPreferences=getSharedPreferences("myinformation",MODE_PRIVATE);
        if (sharedPreferences.getString("loged","false").equals("true")){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }


        loginerror.setVisibility(View.GONE);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginprogressbar.setVisibility(View.VISIBLE);
                    email=textInputEditTextemail.getText().toString();
                    password=textInputEditTextpassword.getText().toString();
                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        if (password.length()>=6){
                            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                            String url ="https://mostafizemon.000webhostapp.com/Shoe%20Store/login.php";

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            loginprogressbar.setVisibility(View.GONE);

                                            try {
                                                JSONObject jsonObject=new JSONObject(response);
                                                String status=jsonObject.getString("status");
                                                String message=jsonObject.getString("message");
                                                if (status.equals("success")){
                                                    profilename=jsonObject.getString("name");
                                                    profileaddress=jsonObject.getString("address");
                                                    profileemail=jsonObject.getString("email");

                                                    SharedPreferences.Editor editor=sharedPreferences.edit();
                                                    editor.putString("loged","true");
                                                    editor.putString("name",profilename);
                                                    editor.putString("email",profileemail);
                                                    editor.putString("address",profileaddress);
                                                    editor.apply();

                                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                                    finish();
                                                }
                                                else {
                                                    loginerror.setText(message);
                                                    loginerror.setVisibility(View.VISIBLE);
                                                }


                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }


                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    loginprogressbar.setVisibility(View.GONE);
                                }
                            }){
                                protected Map<String, String> getParams(){
                                    Map<String, String> paramV = new HashMap<>();
                                    paramV.put("email",email);
                                    paramV.put("password",password);
                                    return paramV;
                                }
                            };
                            queue.add(stringRequest);

                        }
                        else {
                            loginprogressbar.setVisibility(View.GONE);
                            textInputEditTextpassword.setError("Password must be 6 length");
                            textInputEditTextpassword.requestFocus();
                        }

                    }
                    else {
                        loginprogressbar.setVisibility(View.GONE);
                        textInputEditTextemail.setError("Valid Email Required");
                        textInputEditTextemail.requestFocus();
                    }

                }
            });








        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Registration.class));
            }
        });











    }
}