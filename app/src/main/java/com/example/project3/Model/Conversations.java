package com.example.project3.Model;

import java.util.ArrayList;

public class Conversations {
    public class Conversation{
        ArrayList<Message> mSentMessages;
        ArrayList<Message> mReceivedMessages;
        User mOtherUser;

        public Conversation(ArrayList<Message> sentMessages, ArrayList<Message> receivedMessages, User otherUser){
            mSentMessages = sentMessages;
            mReceivedMessages = receivedMessages;
            mOtherUser = otherUser;
        }
    }
    ArrayList<Conversation> mConversations;

    public void addConversation(Conversation convToAdd){
        mConversations.add(convToAdd);
    }

}
