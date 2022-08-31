package com.example.project3.Model;

import java.util.ArrayList;

public class Conversations {
    public class Conversation{
        ArrayList<Message> mSentMessages;
        ArrayList<Message> mReceivedMessages;

        public Conversation(ArrayList<Message> sentMessages, ArrayList<Message> receivedMessages){
            mSentMessages = sentMessages;
            mReceivedMessages = receivedMessages;
        }
    }
    ArrayList<Conversation> mConversations;

    public void addConversation(Conversation convToAdd){
        mConversations.add(convToAdd);
    }

}
