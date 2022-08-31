package com.example.project3.Model;

public class Message {

    private String mMessageBody;
    private int mSenderId;
    private int mParentMessageId;
    private int mRecipientId;
    public Message(int id, String messageBody,int senderId,int parentMessageId,int RecipientId){
        mId = id;
        mMessageBody = messageBody;
        mSenderId = senderId;
        mParentMessageId = parentMessageId;
        mRecipientId = RecipientId;

    }
    public int getId() {
        return mId;
    }

    private int mId;

    public String getMessageBody() {
        return mMessageBody;
    }

    public int getSenderId() {
        return mSenderId;
    }

    public int getParentMessageId() {
        return mParentMessageId;
    }

    public int getRecipientId() {
        return mRecipientId;
    }



}
