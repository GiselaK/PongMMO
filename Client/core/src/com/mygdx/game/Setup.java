package com.mygdx.game;

public class Setup {
    Globals globals;

    Setup(Globals globals) {
        this.globals = globals;
    }

    public String connect() {
        String response = globals.tools.pushNetRequest(new String []{"request"}, new String []{ "JOIN"});
        if (response == null) return "Connection failure.";
        globals.playerId=globals.network.processJson(response, "player");
        return "success";
    }
}
