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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class qrscannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
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

                        if (message.equals("You've already appointed a cycle.")){
                            startActivity(new Intent(qrscannerActivity.this, qrscanner2Activity.class));
                            Toast.makeText(qrscannerActivity.this, message + " Scan again to pay the bill.", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(qrscannerActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    finish();
                } else {
                    try {
                        String message = json.getString("message");
                        Toast.makeText(qrscannerActivity.this, message, Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(qrscannerActivity.this, stopwatchActivity.class));
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        };

        cb.header("Authorization", "Bearer " + token);

        aQuery.ajax(murl + "cycle/" + result.getText() + "/appoint", JSONObject.class, cb);
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
