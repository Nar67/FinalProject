package com.tg.narcis.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tg.narcis.finalproject.database.DataBaseHelper;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sp;
    Button signUp;
    EditText password, r_password, user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signUp = (Button) findViewById(R.id.sign_up_butt);
        user = (EditText) findViewById(R.id.user_edit_su);
        password = (EditText) findViewById(R.id.password_edit_su);
        r_password = (EditText) findViewById(R.id.password_edit2_su);

        signUp.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_up_butt:
                if(user.getText().toString() == null) {
                    Toast.makeText(this, "Write username and password please", Toast.LENGTH_SHORT).show();
                    break;
                }
                String pass = password.getText().toString();
                Log.v("pass", pass);
                Log.v("pass", r_password.getText().toString());
                if(pass.equals(r_password.getText().toString())) {
                    String username = user.getText().toString();
                    Log.v("user", "user: " + username);
                    User usern = DataBaseHelper.getInstance(this).queryUser(username);
                    if(usern == null){
                        DataBaseHelper.getInstance(this).createUser(username, pass);
                        sp = getSharedPreferences("FinalProject", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("isLoged", true);
                        editor.apply();
                        Toast.makeText(this, "User registered", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                    else {
                        password.setText("");
                        r_password.setText("");
                        Toast.makeText(this, "Error singning up, try again please", Toast.LENGTH_SHORT).show();

                    }
                }
                else {
                    user.setText("");
                    password.setText("");
                    r_password.setText("");
                    Toast.makeText(this, "Passwords doesn't match", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
