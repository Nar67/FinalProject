package com.tg.narcis.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tg.narcis.finalproject.database.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sp = getSharedPreferences("FinalProject", Context.MODE_PRIVATE);
        boolean logedIn = sp.getBoolean("isLoged", false);
        String currentUsername = sp.getString("username", "-1");
        String adress = sp.getString("address", "-1");
        boolean firstTime = sp.getBoolean("isFirstTime", true);
        if(firstTime) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("isFirstTime", false);
            editor.apply();
            List<User> users = insertStuffedUsers();
            insertUsersInDataBase(users);
        }

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

    private List<User> insertStuffedUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User("Link", "0", "10"));
        users.add(new User("Ganondorf", "0", "11"));
        users.add(new User("Green Toon Link", "0", "12"));
        users.add(new User("Red Toon Link", "0", "13"));
        users.add(new User("Fierce Deity Link", "0", "3"));
        users.add(new User("Skull kid", "0", "14"));
        users.add(new User("Some Deku", "0", "15"));
        users.add(new User("Some Goron", "0", "16"));
        users.add(new User("Some Zora", "0", "17"));
        users.add(new User("Navy", "0", "18"));
        users.add(new User("Midna", "0", "19"));
        users.add(new User("Hitler Jesucristo", "0", "19"));
        users.add(new User("TechnoViking", "0", "19"));
        users.add(new User("Stalin", "0", "19"));
        users.add(new User("Lennin", "0", "19"));
        return users;
    }

    private void insertUsersInDataBase(List<User> users) {
        for(User user : users) {
            DataBaseHelper.getInstance(this).createUser(user.getUsername(), user.getPassword(), user.getScore());
        }

    }
}
