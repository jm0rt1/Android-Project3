package com.example.project3;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;

import com.example.project3.Model.Message;
import com.example.project3.Model.Model;
import com.example.project3.Model.User;
import com.example.project3.Server.ServerInterface;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ChatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        User user  = Model.getInstance().getUser();
        int i = user.id;
        new RefreshChats(this,findViewById(R.id.my_list_view)).execute();
    }


    final class RefreshChats extends AsyncTask<Void, Integer, Boolean> {
        private final WeakReference<Activity> parentRef;
        private final WeakReference<ListView> listViewRef;


        public RefreshChats(final Activity parent, ListView listView){
            parentRef = new WeakReference<Activity>(parent);
            listViewRef = new WeakReference<ListView>(listView);
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                ArrayList<Message> sentMessages = ServerInterface.Messages.getSentMessages(Model.getInstance().getUser().id);
                ArrayList<Message> recievedMessages = ServerInterface.Messages.getRecievedMessages(Model.getInstance().getUser().id);
                // TODO Build conversations and save into model

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