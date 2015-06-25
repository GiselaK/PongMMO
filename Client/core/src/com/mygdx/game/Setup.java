package com.mygdx.game;

public class Setup {
    Globals globals;

    Setup(Globals globals) {
        this.globals = globals;
    }

    public String connect() {
        String response = globals.tools.pushNetRequest("type", "JOIN", "sleeep?", "nope", "LOLZ", "yes", "a", "b");
        if (response == null) return "Connection failure.";
        System.out.println(globals.network.processJson(response, "result"));
        if (globals.network.processJson(response, "result").equals("failure"))  return "JOIN Request Failed.";
        System.out.println(globals.network.processJson(response, "player"));
        globals.playerId=globals.network.processJson(response, "player");
        return "success";
    }
}
