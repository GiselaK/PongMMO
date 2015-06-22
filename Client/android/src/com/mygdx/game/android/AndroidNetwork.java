package com.mygdx.game.android;

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
    public String sendRequest(String a, String b, String c, String d, String e, String f) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://pong-mmo.herokuapp.com/");



        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair(a, b));
        nameValuePair.add(new BasicNameValuePair(c, d));
        nameValuePair.add(new BasicNameValuePair(e, f));

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