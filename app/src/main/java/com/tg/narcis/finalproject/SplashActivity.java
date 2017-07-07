package com.tg.narcis.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sp = getSharedPreferences("FinalProject", Context.MODE_PRIVATE);
        boolean logedIn = sp.getBoolean("isLoged", false);
        Intent i;
        if(logedIn){

            i = new Intent(this, MainActivity.class);
        }
        else{
            i = new Intent(this, LoginActivity.class);
        }
        startActivity(i);
        finish();
    }
}
