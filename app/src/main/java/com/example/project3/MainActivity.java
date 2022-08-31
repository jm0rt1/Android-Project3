package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.project3.Model.Model;

public class MainActivity extends AppCompatActivity {
    EditText mNameEditText;
    EditText mPasswordEditText;
    Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Model.getInstance();

        initUIResources();
    }

    private void initUIResources() {
        mNameEditText = findViewById(R.id.name_edit_text);
        mPasswordEditText = findViewById(R.id.password_edit_text);
        mLoginButton = findViewById(R.id.login_button);
    }

    public void login(View v){
        String inputtedName = mNameEditText.getText().toString();
        String inputtedPassword = mPasswordEditText.getText().toString();


    }
}