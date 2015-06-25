package com.mygdx.game;

/**
 * Created by elf on 6/16/15.
 */
public interface Network {
    public String sendRequest(String[] keys, String[] values);
    public String processJson(String data, String key);
}
