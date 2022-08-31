package com.example.project3.Model;

public class Model {
    private static Model instance;
    private boolean isInitialized = false;
    private User user;

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
}
