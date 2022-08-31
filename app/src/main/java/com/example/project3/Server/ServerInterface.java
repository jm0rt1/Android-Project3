package com.example.project3.Server;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project3.Model.Message;
import com.example.project3.Model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;

public class ServerInterface {
    public static final String TAG = "ServerInterface";
    public static class Messages {
        private static String JsonCache = ""; // TODO: need to cache this in a better way for a bigger database
        private static long timeSinceLastUpdate = 0;
        public static class Urls{
            public static String MESSAGES_ADD = "http://10.0.2.2/jm/api/messages/add";
            public static String MESSAGES = "http://10.0.2.2/jm/api/messages";
            public static String MESSAGES_DELETE = "http://10.0.2.2/jm/api/posts/delete";
            public static String MESSAGES_BY_SENDER = "http://10.0.2.2/jm/api/messages/by_sender/";
            public static String MESSAGES_BY_RECIPIENT = "http://10.0.2.2/jm/api/messages/by_recipient/";


        }
        private static class Keys {
            public static String ID = "id";
            public static String MESSAGE_BODY ="message_body";
            public static String SENDER_ID = "sender_id";
            public static String PARENT_MESSAGE_ID = "parent_message_id";
            public static String RECIPIENT_ID = "recipient_id";
            public static String CONVERSATION_ID = "conversation_id";

        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private static void  guard(){
            //Call first in every method

            if ((Objects.equals(JsonCache, "") || timeSinceLastUpdate == 0)||(timeSinceLastUpdate+(1000)*60 > Instant.now().toEpochMilli())){
                JsonCache = ServerCommands.downloadJSONUsingHTTPGetRequest(ServerCommands.Urls.MESSAGES);
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public static ArrayList<Message> getSentMessages(int senderId) throws JSONException {

            JsonCache = ServerCommands.downloadJSONUsingHTTPGetRequest(Urls.MESSAGES_BY_SENDER+String.valueOf(senderId));
            ArrayList<Message> messages = jsonCacheToArrayList();

            return messages;
        }


        @RequiresApi(api = Build.VERSION_CODES.O)
        public static ArrayList<Message> getReceivedMessages(int recipientId) throws JSONException {

            JsonCache = ServerCommands.downloadJSONUsingHTTPGetRequest(Urls.MESSAGES_BY_RECIPIENT+String.valueOf(recipientId));
            ArrayList<Message> messages = jsonCacheToArrayList();

            return messages;
        }

        @NonNull
        private static ArrayList<Message> jsonCacheToArrayList() throws JSONException {
            JSONArray jsonArray = new JSONArray(JsonCache);
            ArrayList<Message> messages = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                int parent;
                if (obj.getString(Keys.PARENT_MESSAGE_ID).equals("null")){
                    parent = 0;
                } else{
                    parent = Integer.parseInt(obj.getString(Keys.PARENT_MESSAGE_ID));
                }
                Message message = new Message(Integer.parseInt(obj.getString(Keys.ID)),
                        obj.getString(Keys.MESSAGE_BODY),
                        Integer.parseInt(obj.getString(Keys.SENDER_ID)),
                        parent,
                        Integer.parseInt(obj.getString(Keys.RECIPIENT_ID)),
                        Integer.parseInt(obj.getString(Keys.CONVERSATION_ID)));


                messages.add(message);

            }
            return messages;
        }

        public static boolean sendMessage(Message message, Context c){



                JSONObject newPost = new JSONObject();
                try {
                    newPost.put(Keys.MESSAGE_BODY, message.getMessageBody());
                    newPost.put(Keys.SENDER_ID, message.getSenderId());
                    newPost.put(Keys.PARENT_MESSAGE_ID, message.getParentMessageId());
                    newPost.put(Keys.RECIPIENT_ID, message.getRecipientId());
                    newPost.put(Keys.CONVERSATION_ID, message.getConversationId());
                    newPost.put(Keys.ID, message.getId());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return ServerCommands.sendHttpPostRequest(ServerCommands.Urls.MESSAGES_ADD, newPost);


        }

        private static boolean deletePost(String id){
            boolean result = ServerCommands.sendHttpDeleteRequest(ServerCommands.Urls.MESSAGES_DELETE +"?id="+ id);
            return result;
        }


    }

    public static class Users {

        private static String JsonCache = ""; // TODO: need to cache this in a better way for a bigger database
        private static long timeSinceLastUpdate = 0;
        private static class Keys {
            public static String NAME ="name";
            public static String PASSWORD = "password";
            public static String ID = "id";
        }
        private static class Urls{
            public static String USERS = "http://10.0.2.2/jm/api/users";
            public static String USERS_BY_ID = "http://10.0.2.2/jm/api/users/by_id/";
            public static String USERS_BY_NAME = "http://10.0.2.2/jm/api/users/by_name/";

        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        private static void  guard(){
            //Call first in every method

            if ((Objects.equals(JsonCache, "") || timeSinceLastUpdate == 0)||(timeSinceLastUpdate+(1000)*60 > Instant.now().toEpochMilli())){
                JsonCache = ServerCommands.downloadJSONUsingHTTPGetRequest(Urls.USERS);
            }
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        public static User getUserByName(String name) throws JSONException {
            JsonCache = ServerCommands.downloadJSONUsingHTTPGetRequest(Urls.USERS_BY_NAME+name);
            if (JsonCache == null){
                Log.e(TAG, "Unsuccessful login due to network error");
                return null;
            }
            JSONArray jsonArray = new JSONArray(JsonCache);


            //Names should be unique
            if (jsonArray.length() == 0 || jsonArray.length() > 1){
                return null;
            }


            User user = new User();
            JSONObject obj = jsonArray.getJSONObject(0);

            user.name = obj.getString(Users.Keys.NAME);
            user.password = obj.getString(Keys.PASSWORD);
            user.id = Integer.parseInt(obj.getString(Users.Keys.ID));

            return user;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public static User getUserById(int id) throws JSONException {
            JsonCache = ServerCommands.downloadJSONUsingHTTPGetRequest(Urls.USERS_BY_ID+id);
            if (JsonCache == null){
                Log.e(TAG, "Unsuccessful login due to network error");
                return null;
            }
            JSONArray jsonArray = new JSONArray(JsonCache);


            //Names should be unique
            if (jsonArray.length() == 0 || jsonArray.length() > 1){
                return null;
            }


            User user = new User();
            JSONObject obj = jsonArray.getJSONObject(0);

            user.name = obj.getString(Users.Keys.NAME);
            user.password = obj.getString(Keys.PASSWORD);
            user.id = Integer.parseInt(obj.getString(Users.Keys.ID));

            return user;
        }

    }
}

