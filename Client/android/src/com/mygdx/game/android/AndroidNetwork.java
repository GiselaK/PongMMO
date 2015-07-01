package com.mygdx.game.android;

import android.content.SharedPreferences;

import com.mygdx.game.Network;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class AndroidNetwork implements Network {

    @Override
    public String processJson(String data, String key) {

        try {
            JSONObject obj = new JSONObject(data);
            return obj.getString(key);

        } catch (JSONException e) {
            return null;
        }
    }
    @Override
    public String sendRequest(String[] keys, String[] values) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://21b7fcfb.ngrok.io");

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);

        for (int i=0; i<keys.length; i++) {
            nameValuePair.add(new BasicNameValuePair(keys[i], values[i]));
        }

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException error) {
            return null;
        }
        try {
            HttpResponse response = httpClient.execute(httpPost);
            return EntityUtils.toString(response.getEntity());
        }
        catch (ClientProtocolException error) {
            return null;
        } catch (IOException error) {
            return null;
        }
    }

}