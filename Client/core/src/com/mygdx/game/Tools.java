package com.mygdx.game;

public class Tools {
    private Globals globals;

    Tools(Globals globals) {
        this.globals = globals;
    }
    public String genPostData(String key1, String value1, String key2, String value2) {
        return key1+"="+value1+"&"+key2+"="+value2;
    }

    public String pushNetRequest(String a, String b, String c, String d, String e, String f, String h, String i) {
        String result =  globals.network.sendRequest(a, b, c, d, e, f, h, i);
        if (result == null) {
            globals.error = "Connection Failed";
            globals.gameState = Globals.GameState.ERROR;
            return null;
        } else {
            return result;
        }
    }
}
