package com.example.remmberpassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText usernameEdit;
    private EditText passwordEdit;
    private Button loginButton;
    private CheckBox isRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        usernameEdit = (EditText) findViewById(R.id.username);
        passwordEdit = (EditText) findViewById(R.id.password);
        isRemember = (CheckBox) findViewById(R.id.isRemember);
        loginButton = (Button) findViewById(R.id.login);

        boolean Remember = pref.getBoolean("remember_password",false);
        if(Remember){
            String account = pref.getString("account","");
            String passwordValue = pref.getString("password","");
            usernameEdit.setText(account);
            passwordEdit.setText(passwordValue);
            isRemember.setChecked(true);
        }
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                if (username.equals("admin") && password.equals("123456")) {
                    editor = pref.edit();
                    if (isRemember.isChecked()) {
                        editor.putBoolean("remember_password", true);
                        editor.putString("account", username);
                        editor.putString("password", password);
                    } else {
                        editor.clear();
                    }
                    editor.apply();
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}