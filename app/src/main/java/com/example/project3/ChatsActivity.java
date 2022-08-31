package com.example.project3;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.project3.Model.Conversations;
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

        Toolbar myToolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        User user  = Model.getInstance().getUser();
        int i = user.id;
        new RefreshChats(this,findViewById(R.id.my_list_view)).execute();
    }


    final class RefreshChats extends AsyncTask<Void, Integer, Conversations> {
        private final WeakReference<Activity> parentRef;
        private final WeakReference<ListView> listViewRef;


        public RefreshChats(final Activity parent, ListView listView){
            parentRef = new WeakReference<Activity>(parent);
            listViewRef = new WeakReference<ListView>(listView);
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Conversations doInBackground(Void... voids) {
            try {
                ArrayList<Message> sentMessages = ServerInterface.Messages.getSentMessages(Model.getInstance().getUser().id);
                ArrayList<Message> receivedMessages = ServerInterface.Messages.getRecievedMessages(Model.getInstance().getUser().id);
                // TODO Build conversations and save into model
                ArrayList<Integer> conversationIds = new ArrayList();
                Conversations conversations = new Conversations();
                for (int i=0; i<sentMessages.size();i++){
                    int messageConversationId = sentMessages.get(i).getConversationId();
                    if (!conversationIds.contains(messageConversationId)){
                        conversationIds.add(messageConversationId);
                    }
                }
                for (int i=0; i<receivedMessages.size();i++){
                    int messageConversationId = sentMessages.get(i).getConversationId();
                    if (!conversationIds.contains(messageConversationId)){
                        conversationIds.add(messageConversationId);
                    }
                }

                int otherUserId =0;
                for (int i=0; i<conversationIds.size(); i++){
                    ArrayList<Message> convSent = ServerInterface.Messages.getSentMessages(Model.getInstance().getUser().id);
                    ArrayList<Message> convReceived = ServerInterface.Messages.getRecievedMessages(Model.getInstance().getUser().id);
                    for (int j=0; j<receivedMessages.size();j++){
                        int messageConversationId = receivedMessages.get(i).getConversationId();
                        if (conversationIds.get(i) == messageConversationId){
                            otherUserId = receivedMessages.get(i).getSenderId();
                            convReceived.add(receivedMessages.get(i) );
                        }
                    }

                    for (int j=0; j<sentMessages.size();j++){
                        int messageConversationId = sentMessages.get(i).getConversationId();
                        if (conversationIds.get(i) == messageConversationId){
                            otherUserId = sentMessages.get(i).getRecipientId();
                            convSent.add(sentMessages.get(i));
                        }
                    }
                    if (convSent.size()>0 || convReceived.size()>0){
                        if (otherUserId != 0) {
                            User other = ServerInterface.Users.getUserById(otherUserId);
                            Conversations.Conversation conv = new Conversations.Conversation(sentMessages,receivedMessages,other,conversationIds.get(i));
                            conversations.addConversation(conv);
                        }
                    }

                }


                return conversations;


            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }
        @Override
        protected void onPostExecute(Conversations result) {

            Model.getInstance().setConversations(result);

            Model.getInstance().getConversations();
            listViewRef.get();

            ;
            ChatsList adapter = new ChatsList(ChatsActivity.this, Model.getInstance().getConversations().getChatNames());


            ListView listView = (ListView) findViewById(R.id.my_list_view);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long l) {


                    Intent intent = new Intent(parentRef.get().getApplicationContext(), ConversationActivity.class);
                    startActivity(intent);

                }
            });
        }
    }
}