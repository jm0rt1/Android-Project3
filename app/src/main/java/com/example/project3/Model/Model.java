package com.example.project3.Model;

import java.util.ArrayList;

public class Model {
    private static Model instance;
    private boolean isInitialized = false;

    public void setUser(User user) {
        this.user = user;
    }

    private User user;

    public void setConversations(Conversations conversations) {
        this.conversations = conversations;
    }

    private Conversations conversations;

    public static synchronized Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }
    public void loadModels(User user) {
        if (!isInitialized) {

            isInitialized = true;
        }
    }
    public User getUser() {
        return user;
    }


    public Conversations getConversations() {
        return conversations;
    }
}
