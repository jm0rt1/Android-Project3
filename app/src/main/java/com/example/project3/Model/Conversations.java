package com.example.project3.Model;

import java.util.ArrayList;

public class Conversations {
    public static class Conversation{
        ArrayList<Message> mSentMessages;
        ArrayList<Message> mReceivedMessages;
        User mOtherUser;
        int mConversationId;

        public Conversation(ArrayList<Message> sentMessages, ArrayList<Message> receivedMessages, User otherUser, int conversationId){
            mSentMessages = sentMessages;
            mReceivedMessages = receivedMessages;
            mOtherUser = otherUser;
            mConversationId = conversationId;
        }
    }
    ArrayList<Conversation> mConversations;

    public Conversations(){
        mConversations = new ArrayList<>();
    }

    public void addConversation(Conversation convToAdd){
        mConversations.add(convToAdd);
    }
    public String[] getChatNames(){
        String[] chatNames = new String[mConversations.size()];
        for (int i=0; i<mConversations.size(); i++){
            chatNames[i]=(mConversations.get(i).mOtherUser.name);
        }
        return chatNames;
    }

}
