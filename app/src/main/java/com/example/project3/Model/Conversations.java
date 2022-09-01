package com.example.project3.Model;

import java.util.ArrayList;

public class Conversations {
    public static class Conversation{
        public ArrayList<Message> mMessages;
        public User mOtherUser;


        public Conversation(ArrayList<Message> messages,  User otherUser){
            mMessages = messages;
            mOtherUser = otherUser;

        }


    }
    ArrayList<Conversation> mConversations;

    public Conversations(){
        mConversations = new ArrayList<>();
    }

    public void addConversation(Conversation convToAdd){
        mConversations.add(convToAdd);
    }

    public Conversation getConversationByOtherUser(User user) throws Exception {
        int userId = user.id;
        for(int i = 0; i<mConversations.size(); i++) {
            if (mConversations.get(i).mOtherUser.id == userId){
                return mConversations.get(i);
            }
        }
        throw new Exception("could not find user");
    }

    public Conversation getConversationByOtherUserId(int userId) throws Exception {
        for(int i = 0; i<mConversations.size(); i++) {
            if (mConversations.get(i).mOtherUser.id == userId){
                return mConversations.get(i);
            }
        }
        throw new Exception("could not find user id");

    }


    public String[] getChatNames(){
        String[] chatNames = new String[mConversations.size()];
        for (int i=0; i<mConversations.size(); i++){
            chatNames[i]=(mConversations.get(i).mOtherUser.name);
        }
        return chatNames;
    }

    public String[] getMostRecentMessages(){
        String[] messages = new String[mConversations.size()];
        for (int i=0; i<mConversations.size(); i++){
            messages[i]=(mConversations.get(i).mMessages.get(mConversations.get(i).mMessages.size()-1).getMessageBody());
        }
        return messages;
    }

    public Integer[] getUserIds(){
        Integer[] ids = new Integer[mConversations.size()];
        for (int i=0; i<mConversations.size(); i++){
            ids[i]=(mConversations.get(i).mOtherUser.id);
        }
        return ids;
    }

    public User[] getOtherUsers(){
        User[] users = new User[mConversations.size()];
        for (int i=0; i<mConversations.size(); i++){
            users[i]=(mConversations.get(i).mOtherUser);
        }
        return users;
    }

    public User getOtherUser(int id) throws Exception {
        for (int i=0; i<mConversations.size(); i++){
            if (id == mConversations.get(i).mOtherUser.id){
                return mConversations.get(i).mOtherUser;
            }
        }
        throw new Exception("could not find user");

    }


}
