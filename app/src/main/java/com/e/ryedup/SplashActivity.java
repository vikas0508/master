package com.e.ryedup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;

import Utils.Constants;

public class SplashActivity extends AppCompatActivity {
  Intent intent;
    SharedPreferences preference;
    String login_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        preference = getSharedPreferences(Constants.Shared_Pref, Context.MODE_PRIVATE);
        login_id=preference.getString("loginid","");

      new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
              if (login_id.equalsIgnoreCase("")){
                  intent=new Intent(getApplicationContext(),LoginActivity.class);
                  startActivity(intent);
                  finish();
              }else {
                  intent=new Intent(getApplicationContext(),HomeActivity.class);
                  startActivity(intent);
                  finish();
              }

        /*      intent=new Intent(getApplicationContext(),LoginActivity.class);
              startActivity(intent);
              finish();*/


          }
      },1200);

    }



}
