package com.example.project3.Model;

public class Model {
    private static Model instance;
    private boolean isInitialized = false;
    private User user;
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
