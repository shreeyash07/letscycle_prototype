package com.trans.mac.registerdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

public class BalanceActivity extends AppCompatActivity {

    AQuery aQuery;
    String murl ;
    TextView bal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        murl = getString(R.string.url);

        bal = findViewById(R.id.display_balance2);


        aQuery = new AQuery(this);

        SharedPreferences sharedPreferences = getSharedPreferences("LetsCycle", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        Log.d("testig", "onCreate: " + token);
        AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>(){
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status){
                Log.d("testig", "callback: " + json.toString());
                try {
                    bal.setText("Balance: Rs. " + json.getString("credit"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        cb.header("Authorization", "Bearer " + token);

        aQuery.ajax(murl + "user/balance", JSONObject.class, cb);
    }
}
