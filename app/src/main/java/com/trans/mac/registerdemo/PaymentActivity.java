package com.trans.mac.registerdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity {

    AQuery aQuery;
    String murl;
    String token;
    int cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        murl = getString(R.string.url);

        aQuery = new AQuery(this);

        SharedPreferences sharedPreferences = getSharedPreferences("LetsCycle", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        cid = getIntent().getExtras().getInt("cycle_id");
        double amount = getIntent().getExtras().getDouble("amount");

        TextView amt = findViewById(R.id.amonut);
        TextView cycleid = findViewById(R.id.cycleid);


        amt.setText("Your fare amount is Rs. " + Double.toString(amount));
        cycleid.setText("Cycle id: " + Integer.toString(cid));

        Button btn = findViewById(R.id.pay);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {

                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        Log.d("testig", Integer.toString(status.getCode()));

                        if (status.getCode() > 300){
                            try {
                                json = new JSONObject(status.getError());

                                String message = json.getString("message");


                                Toast.makeText(PaymentActivity.this, message, Toast.LENGTH_SHORT).show();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            finish();
                        } else {
                            try {
                                String message = json.getString("message");
                                Toast.makeText(PaymentActivity.this, message, Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(PaymentActivity.this, NavigationActivity.class));
                                finish();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                };

                cb.header("Authorization", "Bearer " + token);

                aQuery.ajax(murl + "cycle/" + cid + "/pay", JSONObject.class, cb);
            }
        });

    }
}
