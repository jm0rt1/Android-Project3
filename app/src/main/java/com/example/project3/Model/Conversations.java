package com.example.project3.Model;

import java.util.ArrayList;

public class Conversations {
    public class Conversation{
        ArrayList<Message> mMessages;
        public Conversation(ArrayList<Message> messages){
            mMessages = messages;
        }
    }
    ArrayList<Conversation> mConversations;

}
