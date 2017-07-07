package com.tg.narcis.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tg.narcis.finalproject.MainActivity;
import com.tg.narcis.finalproject.R;
import com.tg.narcis.finalproject.SignUpActivity;
import com.tg.narcis.finalproject.User;
import com.tg.narcis.finalproject.database.DataBaseHelper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    Button log_in, sign_up;
    EditText  user_edit, password_edit;
    SharedPreferences sp;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        log_in = (Button) findViewById(R.id.log_in_button);
        sign_up = (Button) findViewById(R.id.sign_up_button);
        user_edit = (EditText) findViewById(R.id.user_edit);
        password_edit = (EditText) findViewById(R.id.password_edit);

        log_in.setOnClickListener(this);
        sign_up.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        username = user_edit.getText().toString();
        String pass = password_edit.getText().toString();
        switch (view.getId()) {
            case R.id.log_in_button:
                User user = DataBaseHelper.getInstance(this).queryUser(username);
                if(user != null){ //can log in
                    sp = getSharedPreferences("FinalProject", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("isLoged", true);
                    editor.apply();
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                    user_edit.setText("");
                    password_edit.setText("");
                    Toast.makeText(this, "User does not exist, pleas sign up", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.sign_up_button:
                Intent i = new Intent(this, SignUpActivity.class);
                startActivity(i);
                break;
        }
    }
}
