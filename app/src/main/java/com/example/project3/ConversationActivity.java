package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.project3.Model.Conversations;
import com.example.project3.Model.Model;

public class ConversationActivity extends AppCompatActivity {
    private RecyclerView mMessageRecycler;
    private ConversationAdapter mMessageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        mMessageRecycler = (RecyclerView) findViewById(R.id.conversation_recycler_view);

        Conversations.Conversation conv = Model.getInstance().getConversations().getConversationById(1);

        mMessageAdapter = new ConversationAdapter(this, conv);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);
    }
}