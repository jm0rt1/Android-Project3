package com.example.project3;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.project3.Model.Conversations;
import com.example.project3.Model.Message;
import com.example.project3.Model.Model;
import com.example.project3.Model.User;
import com.example.project3.Server.ServerInterface;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;

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
                ArrayList<Message> receivedMessages = ServerInterface.Messages.getReceivedMessages(Model.getInstance().getUser().id);

                // TODO Build conversations and save into model
                Conversations conversations = new Conversations();
                ArrayList<Integer> otherUserIds = new ArrayList();
                ArrayList<User> otherUsers = new ArrayList();

                for (int i=0; i<sentMessages.size();i++){
                    if (!otherUserIds.contains(sentMessages.get(i).getRecipientId())){
                        ServerInterface.Users.getUserById(sentMessages.get(i).getRecipientId());
                        otherUsers.add(ServerInterface.Users.getUserById(sentMessages.get(i).getRecipientId()));
                        otherUserIds.add(sentMessages.get(i).getRecipientId());
                    }
                }
                for (int i=0; i<receivedMessages.size();i++){

                    if (!otherUserIds.contains(receivedMessages.get(i).getSenderId())){
                        ServerInterface.Users.getUserById(receivedMessages.get(i).getSenderId());
                        otherUsers.add(ServerInterface.Users.getUserById(receivedMessages.get(i).getRecipientId()));
                        otherUserIds.add(receivedMessages.get(i).getSenderId());

                    }

                }

                int otherUserId =0;
                for (int i=0; i<otherUsers.size(); i++){
//                    ArrayList<Message> convSent = ServerInterface.Messages.getSentMessages(Model.getInstance().getUser().id);
//                    ArrayList<Message> convReceived = ServerInterface.Messages.getRecievedMessages(Model.getInstance().getUser().id);

                    ArrayList<Message> messages = new ArrayList<>();

                    for (int j=0; j<receivedMessages.size();j++){
                        otherUserId = receivedMessages.get(i).getSenderId();
                        if (otherUsers.get(i).id == otherUserId){
                            otherUserId = receivedMessages.get(j).getSenderId();
                            messages.add(receivedMessages.get(j) );
                        }
                    }

                    for (int j=0; j<sentMessages.size();j++){
                        otherUserId = sentMessages.get(i).getRecipientId();
                        if (otherUsers.get(i).id == otherUserId){
                            otherUserId = sentMessages.get(j).getRecipientId();
                            messages.add(sentMessages.get(j));
                        }
                    }
                    if (messages.size()>0){
                        if (otherUserId != 0) {
                            User other = ServerInterface.Users.getUserById(otherUserId);
                            messages.sort(Comparator.comparing(Message::getParentMessageId));
                            Conversations.Conversation conv = new Conversations.Conversation(messages,other);
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
            Integer[] ids = Model.getInstance().getConversations().getUserIds();

            ChatsList adapter = new ChatsList(ChatsActivity.this, Model.getInstance().getConversations().getChatNames());


            ListView listView = (ListView) findViewById(R.id.my_list_view);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long l) {


                    Intent intent = new Intent(parentRef.get().getApplicationContext(), ConversationActivity.class);
                    intent.putExtra("other_user_id", ids[position]);
                    startActivity(intent);

                }
            });
        }
    }




}