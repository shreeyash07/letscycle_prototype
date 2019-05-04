package com.trans.mac.registerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class KYCform extends AppCompatActivity {


    Button done;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kycform);

        done = findViewById(R.id.done);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KYCform.this, MainActivity.class));
                Toast.makeText(KYCform.this, "KYC form Registered", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
