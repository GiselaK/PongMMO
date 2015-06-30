package com.mygdx.game;

public class Tools {
    private Globals globals;

    Tools(Globals globals) {
        this.globals = globals;
    }
    public String genPostData(String key1, String value1, String key2, String value2) {
        return key1+"="+value1+"&"+key2+"="+value2;
    }

    public String pushNetRequest(String[] keys, String[] value) {
        String result =  globals.network.sendRequest(keys, value);
        if (result == null) {
            globals.error = "Connection Failed";
            globals.gameState = Globals.GameState.ERROR;
            return null;
        } else {
            return result;
        }
    }
}