package com.trans.mac.registerdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class qrscanner2Activity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView ScannerView;

    AQuery aQuery;

    String murl ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScannerView = new ZXingScannerView(this);

        aQuery = new AQuery(this);

        setContentView(ScannerView);

        murl = getString(R.string.url);
    }

    @Override
    public void handleResult(Result result) {


        SharedPreferences sharedPreferences = getSharedPreferences("LetsCycle", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                Log.d("testig", Integer.toString(status.getCode()));

                if (status.getCode() > 300){
                    try {
                        json = new JSONObject(status.getError());

                        String message = json.getString("message");
                        Toast.makeText(qrscanner2Activity.this, message, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        String message = json.getString("message");
                        double amount = json.getDouble("amount");
                        int cid = json.getInt("cycle_id");


                        Toast.makeText(qrscanner2Activity.this, message, Toast.LENGTH_SHORT).show();
                        Intent i =new Intent(qrscanner2Activity.this, PaymentActivity.class);
                        i.putExtra("amount", amount);
                        i.putExtra("cycle_id", cid);
                        startActivity(i);
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        };

        cb.header("Authorization", "Bearer " + token);

        aQuery.ajax(murl + "cycle/" + result.getText() + "/bill", JSONObject.class, cb);
    }

    @Override
    protected void onPause() {
        super.onPause();


        ScannerView.stopCamera();

    }

    @Override
    protected void onResume() {
        super.onResume();
        ScannerView.setResultHandler(this);
        ScannerView.startCamera();

    }
}