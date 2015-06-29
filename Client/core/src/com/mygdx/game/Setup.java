package com.mygdx.game;

public class Setup {
    Globals globals;

    Setup(Globals globals) {
        this.globals = globals;
    }

    public void connect() {
        String response = globals.tools.pushNetRequest(new String []{"request"}, new String []{ "JOIN"});
        if (response == null) {
            globals.error = "Connection Error";
            globals.gameState = Globals.GameState.ERROR;
        }
        globals.playerId = globals.network.processJson(response, "player");
        globals.game = globals.network.processJson(response, "game");
        globals.gameState = Globals.GameState.MENU;
    }
}
