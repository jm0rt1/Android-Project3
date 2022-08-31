package com.example.project3;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.project3.Model.Conversations;
import com.example.project3.Model.Message;
import com.example.project3.Model.Model;
import com.example.project3.Model.User;
import com.example.project3.Server.ServerInterface;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Comparator;

public class ConversationActivity extends AppCompatActivity {
    private RecyclerView mMessageRecycler;
    private ConversationAdapter mMessageAdapter;

    private EditText mMessageEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        mMessageRecycler = (RecyclerView) findViewById(R.id.conversation_recycler_view);
        mMessageEditText = (EditText)  findViewById(R.id.message_edit_text);

        RefreshRecycler();
    }

    private void RefreshRecycler() {
        Conversations.Conversation conv = Model.getInstance().getConversations().getConversationById(1);

        mMessageAdapter = new ConversationAdapter(this, conv);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);
        mMessageRecycler.scrollToPosition(conv.mMessages.size()-1);
    }

    public void send(View v){
        String message = mMessageEditText.getText().toString();
        Conversations.Conversation conv = Model.getInstance().getConversations().getConversationById(1);
//        conv.mMessages.add(new Message(3,message,Model.getInstance().getUser().id,conv.mMessages.get(conv.mMessages.size()-1).getId(),conv.mOtherUser.id,conv.getConversationId()));
        new SendMessage(new Message(3,message,Model.getInstance().getUser().id,conv.mMessages.get(conv.mMessages.size()-1).getId(),conv.mOtherUser.id,conv.getConversationId()),this,mMessageRecycler).execute();
    }

    final class RefreshChats extends AsyncTask<Void, Integer, Conversations> {
        private final WeakReference<Activity> parentRef;


        public RefreshChats(final Activity parent){
            parentRef = new WeakReference<Activity>(parent);
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Conversations doInBackground(Void... voids) {
            try {
                ArrayList<Message> sentMessages = ServerInterface.Messages.getSentMessages(Model.getInstance().getUser().id);
                ArrayList<Message> receivedMessages = ServerInterface.Messages.getReceivedMessages(Model.getInstance().getUser().id);

//                messages.addAll(sentMessages);
//                messages.addAll(receivedMessages);



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
                    int messageConversationId = receivedMessages.get(i).getConversationId();
                    if (!conversationIds.contains(messageConversationId)){
                        conversationIds.add(messageConversationId);
                    }
                }

                int otherUserId =0;
                for (int i=0; i<conversationIds.size(); i++){
//                    ArrayList<Message> convSent = ServerInterface.Messages.getSentMessages(Model.getInstance().getUser().id);
//                    ArrayList<Message> convReceived = ServerInterface.Messages.getRecievedMessages(Model.getInstance().getUser().id);

                    ArrayList<Message> messages = new ArrayList<>();

                    for (int j=0; j<receivedMessages.size();j++){
                        int messageConversationId = receivedMessages.get(i).getConversationId();
                        if (conversationIds.get(i) == messageConversationId){
                            otherUserId = receivedMessages.get(j).getSenderId();
                            messages.add(receivedMessages.get(j) );
                        }
                    }

                    for (int j=0; j<sentMessages.size();j++){
                        int messageConversationId = sentMessages.get(i).getConversationId();
                        if (conversationIds.get(i) == messageConversationId){
                            otherUserId = sentMessages.get(j).getRecipientId();
                            messages.add(sentMessages.get(j));
                        }
                    }
                    if (messages.size()>0){
                        if (otherUserId != 0) {
                            User other = ServerInterface.Users.getUserById(otherUserId);
                            messages.sort(Comparator.comparing(Message::getParentMessageId));
                            Conversations.Conversation conv = new Conversations.Conversation(messages,other,conversationIds.get(i));
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
            RefreshRecycler();


            }
        }


    final class SendMessage extends AsyncTask<Void, Integer, Boolean> {
        private final WeakReference<Activity> parentRef;
        private final WeakReference<RecyclerView> recyclerViewRef;
        private final Message mMessage;


        public SendMessage(Message message, final Activity parent, RecyclerView recyclerView){
            parentRef = new WeakReference<Activity>(parent);
            recyclerViewRef = new WeakReference<RecyclerView>(recyclerView);
            mMessage=message;
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Boolean doInBackground(Void... voids) {

            return ServerInterface.Messages.sendMessage(mMessage, parentRef.get().getApplicationContext());

        }
        @Override
        protected void onPostExecute(Boolean result) {

            new RefreshChats(this.parentRef.get()).execute();


        }
    }





}