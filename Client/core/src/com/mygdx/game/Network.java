package com.mygdx.game;

/**
 * Created by elf on 6/16/15.
 */
public interface Network {
    public String sendRequest(String a, String b, String c, String d, String e, String f);
    public String processJson(String data, String key);
}
