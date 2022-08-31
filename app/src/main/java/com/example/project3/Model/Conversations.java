package com.example.project3.Model;

import java.util.ArrayList;

public class Conversations {
    public static class Conversation{
        public ArrayList<Message> mMessages;
        User mOtherUser;
        int mConversationId;

        public Conversation(ArrayList<Message> messages,  User otherUser, int conversationId){
            mMessages = messages;
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
    public Conversation getConversationById(int id){
        for(int i = 0; i<mConversations.size(); i++) {
            if (mConversations.get(i).mConversationId == id){
                return mConversations.get(i);
            }
        }
        return null;
    }
    public String[] getChatNames(){
        String[] chatNames = new String[mConversations.size()];
        for (int i=0; i<mConversations.size(); i++){
            chatNames[i]=(mConversations.get(i).mOtherUser.name);
        }
        return chatNames;
    }

}
