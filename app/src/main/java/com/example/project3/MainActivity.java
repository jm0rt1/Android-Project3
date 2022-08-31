package com.example.project3;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.project3.Model.Model;
import com.example.project3.Model.User;
import com.example.project3.Server.ServerInterface;

import org.json.JSONException;

import java.lang.ref.WeakReference;

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
        new VerifyLogin(inputtedName,inputtedPassword, this).execute();


    }

    final class VerifyLogin extends AsyncTask<Void, Integer, Boolean> {
        private final WeakReference<Activity> parentRef;

        private String mName;
        private String mPassword;


        public VerifyLogin(String name, String pwd,final Activity parent){
            parentRef = new WeakReference<Activity>(parent);
            mName= name;
            mPassword = pwd;
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                User user = ServerInterface.Users.getUserByName(mName);
                if (user == null){
                    return false;
                }

                if (user.password.equals(mPassword) ){  // TODO Yes, this would probably be done server-side
                    Model.getInstance().setUser(user);
                    return true;
                }
                return false;


            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }
        @Override
        protected void onPostExecute(Boolean result) {
            if (result){
                Intent intent = new Intent(parentRef.get().getApplicationContext(), ChatsActivity.class);
                startActivity(intent);


            }else{
                return;
            }
        }
    }

}

