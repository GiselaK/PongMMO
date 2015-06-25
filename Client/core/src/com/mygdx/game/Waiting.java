package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Waiting {
    private Globals globals;
    public boolean ready;
    private boolean sentReady;
    private int counter = 0;

    Waiting (Globals globals) {
        this.globals = globals;
        this.ready = false;
        this.sentReady = false;
    }

    public void update() {
        if (Gdx.input.justTouched()) {
            if (!this.ready) {
                String response = globals.tools.pushNetRequest("type", "READY", "player", globals.playerId, "Broken", "Not yes", "a", "b");

                if (globals.network.processJson(response, "result").equals("failure")) {
                    globals.gameState = Globals.GameState.ERROR;
                    globals.error = "Send ready request failed";
                } else {
                    if (globals.network.processJson(response, "ready").equals("TRUE")) {
                        this.ready = true;
                    } else {
                        this.sentReady = true;
                    }
                }
            }
        }
        if (sentReady) {
                String response = globals.tools.pushNetRequest("type", "CHECK", "checkType", "READY", "This", "That", "a", "b");
                if (globals.network.processJson(response, "result").equals("failure")) {
                    globals.gameState = Globals.GameState.ERROR;
                    globals.error = "Check ready request failed";
                } else {
                    if (globals.network.processJson(response, "ready").equals("TRUE")) {
                        this.ready = true;
                    }
                }
        }
    }

    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        globals.batch.begin();
        if (sentReady) {
            MyGdxGame.text.draw("You are ready", 200, 200);
            MyGdxGame.text.draw("Waiting on other player", 200, 175);
        } else {
            MyGdxGame.text.draw("Tap to Set Ready", 200, 200);
            MyGdxGame.text.draw("You have joined as player " + globals.playerId, 200, 175);
        }
        globals.batch.end();
    }
}
