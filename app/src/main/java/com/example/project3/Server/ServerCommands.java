package com.example.project3.Server;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

public class ServerCommands {
    public static class Urls{
        public static String MESSAGES_ADD = "http://10.0.2.2/jm/api/messages/add";
        public static String MESSAGES = "http://10.0.2.2/jm/api/messages";
        public static String MESSAGES_DELETE = "http://10.0.2.2/jm/api/posts/delete";

    }

    public static String downloadJSONUsingHTTPGetRequest(String urlString)
    {
        String jsonString = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            if(httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStream stream = httpConnection.getInputStream();
                jsonString = getStringFromStream(stream);
            }
            httpConnection.disconnect();
            Log.d("JSONObject", "Got object!");
        }
        catch (UnknownHostException e1)
        {
            e1.printStackTrace();
            Log.d("JSONObject", "UnknownHost");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.d("JSONObject", "Other exception");
        }
        return jsonString;
    }

    private static String getStringFromStream(InputStream stream)
    {
        String line;
        String jsonString = null;
        if(stream != null)
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder out = new StringBuilder();
            try
            {
                while ((line = reader.readLine())!= null)
                {
                    out.append(line);
                }
                reader.close();
                jsonString = out.toString();
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
        }
        return jsonString;
    }

    public static Boolean sendHttpPostRequest(String urlString, JSONObject json)
    {
        boolean result = false;
        HttpURLConnection httpConnection = null;
        try
        {
            URL url = new URL(urlString);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.addRequestProperty("content-type","application/json");
            httpConnection.setChunkedStreamingMode(0);
            OutputStreamWriter out = new OutputStreamWriter(httpConnection.getOutputStream());
            out.write(json.toString());
            out.close();
            if(httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStream stream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String line;
                while((line = reader.readLine()) != null)
                {
                    Log.d("PostRequest", line);
                }
                reader.close();
                result = true;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            result = false;
        }
        if(httpConnection != null)
        {
            httpConnection.disconnect();
        }
        return result;
    }

    public static Boolean sendHttpPutRequest(String urlString, JSONObject json)
    {
        boolean result = false;
        HttpURLConnection httpConnection = null;
        try
        {
            URL url = new URL(urlString);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("PUT");
            httpConnection.addRequestProperty("content-type","application/json");
            httpConnection.setChunkedStreamingMode(0);
            OutputStreamWriter out = new OutputStreamWriter(httpConnection.getOutputStream());
            out.write(json.toString());
            out.close();
            if(httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStream stream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String line;
                while((line = reader.readLine()) != null)
                {
                    Log.d("PostRequest", line);
                }
                reader.close();
                result = true;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            result = false;
        }
        if(httpConnection != null)
        {
            httpConnection.disconnect();
        }
        return result;
    }

    public static Boolean sendHttpDeleteRequest (String urlString)
    {
        String jsonString = null;
        try
        {
            URL url = new URL(urlString);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("DELETE");
            if(httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStream stream = httpConnection.getInputStream();
                jsonString = getStringFromStream(stream); //might want to read the response
            }
            httpConnection.disconnect();
            return true;
        }
        catch (UnknownHostException e1)
        {
            e1.printStackTrace();
            return false;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
    }
}