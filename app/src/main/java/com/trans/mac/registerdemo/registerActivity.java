package com.trans.mac.registerdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class registerActivity extends AppCompatActivity {

    EditText username, surname, newpassw, cpassw, dob, email, location, phno;
    RadioGroup gender;
    Button done, cancle;
    AQuery aQuery;

    String murl ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

       aQuery =  new AQuery(this);

        murl = getString(R.string.url);

       setupUIViews();

       done.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (valid()){

                   String name = username.getText().toString().trim();

                   String password = newpassw.getText().toString().trim();


                   String emailV = email.getText().toString().trim();

                   String phone_number = phno.getText().toString().trim();

                   String address = location.getText().toString().trim();


                   Map<String, Object> params = new HashMap<String, Object>();
                   params.put("email", emailV);
                   params.put("password", password);
                   params.put("name", name);
                   params.put("phone_number", phone_number);
                   params.put("address", address);

                   aQuery.ajax(murl + "register", params, JSONObject.class, new AjaxCallback<JSONObject>() {

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
                                           Toast.makeText(registerActivity.this, json.getString(key), Toast.LENGTH_SHORT).show();
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
                                   Toast.makeText(registerActivity.this, message, Toast.LENGTH_SHORT).show();

                                   SharedPreferences sharedPreferences = getSharedPreferences("LetsCycle", Context.MODE_PRIVATE);
                                   SharedPreferences.Editor editor = sharedPreferences.edit();
                                   editor.putString("token", token);
                                   editor.commit();

//                                    sharedPreferences.getString("token", "");

                                   startActivity(new Intent(registerActivity.this, NavigationActivity.class));
                                   finish();

                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }
                           }

                       }
                   });


               }


           }
       });

       cancle.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(registerActivity.this, MainActivity.class));
               finish();
           }
       });


    }




private void setupUIViews(){
    username = findViewById(R.id.username);
    surname = findViewById(R.id.surname);
    newpassw = findViewById(R.id.newpassw);
    cpassw = findViewById(R.id.cpassw);
    dob = findViewById(R.id.dob);
    email = findViewById(R.id.email);
    location = findViewById(R.id.location);
    phno = findViewById(R.id.phno);
    gender = findViewById(R.id.gender);
    done = findViewById(R.id.done);
    cancle = findViewById(R.id.cancle);


}

private Boolean valid (){
    Boolean result = false;

    String name = username.getText().toString().trim();
    String lastname = surname.getText().toString().trim();
    String newpassword = newpassw.getText().toString().trim();
    String confirmpassword = cpassw.getText().toString().trim();
    String dateofbirth = dob.getText().toString().trim();
    String emailid = email.getText().toString().trim();
    String locationvalue = location.getText().toString().trim();
    String phoneValue = phno.getText().toString().trim();
    RadioButton CheckedBtn = findViewById(gender.getCheckedRadioButtonId());
    String genderValue = CheckedBtn.getText().toString();

    if (name.isEmpty() && lastname.isEmpty() && newpassword.isEmpty() && confirmpassword.isEmpty() &&  dateofbirth.isEmpty() && emailid.isEmpty()&& locationvalue.isEmpty() && phoneValue.isEmpty()) {
        Toast.makeText(registerActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();

    }  else{

            return true;

        }
        return result;
    }





    public static boolean isFieldEmpty(EditText view) {
        String value = view.getText().toString();

        if (value.trim().length() > 0) {
            return true;
        } else {
            view.setError("enter value");
            return false;
        }
    }

    public static boolean isValidEmail(EditText view) {
        String value = view.getText().toString();
        if (isFieldEmpty(view)) {

            if (Patterns.EMAIL_ADDRESS.matcher(value).matches()) {

                return true;
            } else {
                view.setError("Invalid Email Address");
            }

        }
        return false;


    }
}
