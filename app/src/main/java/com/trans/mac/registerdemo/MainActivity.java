package com.trans.mac.registerdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



public class MainActivity extends AppCompatActivity {

    EditText username, password;
    Button login;
    CheckBox remember;
    TextView forgot, register;

    AQuery aQuery;
    String murl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        murl = getString(R.string.url);


        SharedPreferences sharedPreferences = getSharedPreferences("LetsCycle", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        if (!token.equals("")){
            Intent i = new Intent(this, NavigationActivity.class);
            startActivity(i);
            finish();
        }

        aQuery = new AQuery(this);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        remember = findViewById(R.id.remember);
        forgot = findViewById(R.id.forgot);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, registerActivity.class);
                startActivity(intent);


            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> params = new HashMap<String, Object>();

                String emailV = username.getText().toString().trim();
                String passwordV = password.getText().toString().trim();


                params.put("email", emailV);
                params.put("password", passwordV);

                aQuery.ajax(murl + "login", params, JSONObject.class, new AjaxCallback<JSONObject>() {

                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        Log.d("testig", Integer.toString(status.getCode()));

                        if (status.getCode() > 300){
                            try {
                                json = new JSONObject(status.getError());

                                Iterator<String> keys = json.keys();

                                while (keys.hasNext()) {
                                    String key = keys.next();
                                    try {
                                        Toast.makeText(MainActivity.this, json.getString(key), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                String token = json.getString("token");
                                String message = json.getString("message");
                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                                SharedPreferences sharedPreferences = getSharedPreferences("LetsCycle", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("token", token);
                                editor.commit();

                                startActivity(new Intent(MainActivity.this, NavigationActivity.class));
                                finish();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
            }
        });

    }




}

