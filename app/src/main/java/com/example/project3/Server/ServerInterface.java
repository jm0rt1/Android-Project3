package com.example.project3.Server;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

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

        }
        private static class Keys {
            public static String TITLE ="title";
            public static String CONTENT = "content";
            public static String OWNER = "owner_id";
            public static String ID = "id";
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private static void  guard(){
            //Call first in every method

            if ((Objects.equals(JsonCache, "") || timeSinceLastUpdate == 0)||(timeSinceLastUpdate+(1000)*60 > Instant.now().toEpochMilli())){
                JsonCache = ServerCommands.downloadJSONUsingHTTPGetRequest(ServerCommands.Urls.MESSAGES);
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private static ArrayList<String> getTitles() throws JSONException {
            guard();

            JSONArray jsonArray = new JSONArray(JsonCache);
            ArrayList<String> names = new ArrayList<String>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                names.add(obj.getString(Keys.TITLE));
            }
            return names;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private static ArrayList<String[]> getPosts() throws JSONException {

            guard();

            JSONArray jsonArray = new JSONArray(JsonCache);
            ArrayList<String[]> names = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                String[] pair = new String[3];
                JSONObject obj = jsonArray.getJSONObject(i);
                pair[0] = obj.getString(Keys.TITLE);
                pair[1] = obj.getString(Keys.CONTENT);
                pair[2] = obj.getString(Keys.ID);

                names.add(pair);

            }
            return names;
        }

        private static boolean sendPost(int owner_id,String title, String content,  Context c){




            if(title.equals(""))
            {
                Toast.makeText(c, "title field cannot be empty", Toast.LENGTH_SHORT).show();
            }
            else if(content.equals(""))
            {
                Toast.makeText(c, "content field cannot be empty", Toast.LENGTH_SHORT).show();
            }
            else if(!(owner_id>=1))
            {
                Toast.makeText(c, "owner id field not valid ="+String.valueOf(owner_id), Toast.LENGTH_SHORT).show();
            }
            else
            {
                JSONObject newPost = new JSONObject();
                try {
                    newPost.put(Keys.TITLE, title);
                    newPost.put(Keys.CONTENT, content);
                    newPost.put(Keys.OWNER, owner_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return ServerCommands.sendHttpPostRequest(ServerCommands.Urls.MESSAGES_ADD, newPost);

            }
            return false;
        }

        private static boolean deletePost(String id){
            boolean result = ServerCommands.sendHttpDeleteRequest(ServerCommands.Urls.MESSAGES_DELETE +"?id="+ id);
            return result;
        }

        final static class Download extends AsyncTask<Void, Integer, ArrayList<String[]>> {

            private final WeakReference<Activity> parentRef;
            private final WeakReference<RecyclerView> recyclerViewRef;

            public Download(final Activity parent, RecyclerView recyclerView) {
                parentRef = new WeakReference<Activity>(parent);
                recyclerViewRef= new WeakReference<RecyclerView>(recyclerView);
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected ArrayList<String[]> doInBackground(Void... voids) {
                try {
                    ArrayList<String[]> posts = Messages.getPosts();
                    return posts;

                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(ArrayList<String[]> data) {


//                final PostRecyclerAdapter adapter = new PostRecyclerAdapter(parentRef.get().getApplicationContext(), data);
//
//                recyclerViewRef.get().setHasFixedSize(true);
//                recyclerViewRef.get().setAdapter(adapter);
//                recyclerViewRef.get().setItemAnimator(new DefaultItemAnimator());
            }
        }


        final static class Upload extends AsyncTask<Void, Integer, Boolean> {

            private final WeakReference<Activity> parentRef;
            private final int mOwner_id;
            private final String mTitle;
            private final String mContent;

            public Upload(final Activity parent,int owner_id,String title, String content) {
                parentRef = new WeakReference<Activity>(parent);
                mOwner_id= owner_id;
                mTitle= title;
                mContent=content;
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                     return Messages.sendPost(mOwner_id, mTitle, mContent, parentRef.get().getApplicationContext());

                } catch (Exception e) {
                    Log.e(TAG,e.toString());
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean result) {

            }
        }


        final static class Delete extends AsyncTask<Void, Integer, Boolean> {

            private final String mId;

            public Delete(String id) {
                mId = id;
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    return Messages.deletePost(mId);

                } catch (Exception e) {
                    Log.e(TAG,e.toString());
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean result) {

            }
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
            user.id = obj.getString(Users.Keys.ID);

            return user;
        }

    }
}

