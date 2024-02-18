package com.mostafiz.loginregisterwithmysql;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {
    TextView logintv,texterror;
    Button register;
    ProgressBar progressBar;
    String name,email,password,address,phone;
    TextInputEditText registerfullname,registeremail,registerpassword,registeraddress,registerphone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        logintv=findViewById(R.id.logintv);
        texterror=findViewById(R.id.texterror);
        register=findViewById(R.id.registerbutton);
        progressBar=findViewById(R.id.progressbar);
        registerfullname=findViewById(R.id.registerfullname);
        registeremail=findViewById(R.id.registeremail);
        registerpassword=findViewById(R.id.registerpassword);
        registeraddress=findViewById(R.id.registeraddress);
        registerphone=findViewById(R.id.registerphone);

        logintv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this,Login.class));
                finish();
            }
        });



        texterror.setVisibility(View.GONE);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=registerfullname.getText().toString();
                email=registeremail.getText().toString();
                password=registerpassword.getText().toString();
                address=registeraddress.getText().toString();
                phone=registerphone.getText().toString();
                progressBar.setVisibility(View.VISIBLE);

                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    if (password.length()>=6){
                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        String url ="https://mostafizemon.000webhostapp.com/Shoe%20Store/register.php";

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        progressBar.setVisibility(View.GONE);
                                        if (response.equals("Success")){
                                            startActivity(new Intent(Registration.this,Login.class));
                                            finish();
                                        }
                                        else{
                                            texterror.setText(response);
                                            texterror.setVisibility(View.VISIBLE);
                                        }

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressBar.setVisibility(View.GONE);
                            }
                        }){
                            protected Map<String, String> getParams(){
                                Map<String, String> paramV = new HashMap<>();
                                paramV.put("name",name);
                                paramV.put("email",email);
                                paramV.put("password",password);
                                paramV.put("address",address);
                                paramV.put("phone",phone);
                                return paramV;
                            }
                        };
                        queue.add(stringRequest);

                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        registerpassword.setError("Password must be 6 length");
                        registerpassword.requestFocus();
                    }

                }
                else {
                    progressBar.setVisibility(View.GONE);
                    registeremail.setError("Valid Email Required");
                    registeremail.requestFocus();
                }

            }
        });


    }
}