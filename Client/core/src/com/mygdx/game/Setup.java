package com.mygdx.game;

public class Setup {
    public static void connect(Globals globals) {
        String response = globals.tools.pushNetRequest(new String []{"request"}, new String []{ "JOIN"});
//        String result = globals.tools.pushNetRequest(new String[]{"request"}, new String []{"READY"});
        if (response == null) {
            globals.error = "Response Null:"+response;
            globals.gameState = Globals.GameState.ERROR;
        }
        else{
            globals.playerId = globals.network.processJson(response, "player");
            System.out.println("globals.playerId: "+globals.playerId);
            globals.game = globals.network.processJson(response, "game");
            globals.serverTimeStamp = globals.network.processJson(response, "timestamp");
            globals.gameState = Globals.GameState.WAITING;
            globals.ready = globals.network.processJson(response, "ready");
        }
    }
}
