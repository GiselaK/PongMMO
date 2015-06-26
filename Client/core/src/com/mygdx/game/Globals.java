package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Globals {
    public static SpriteBatch batch;
    public int width;
    public int height;
    public enum GameState {
        WAITING, STARTED, GAMEOVER, ERROR
    }
    public GameState gameState;
    public String playerId;
    public Tools tools;
    public Network network;
    public String error;
    public int gameHeight;
    public int getWidth;
    public String winner;

    Globals() {
        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();
        this.batch = new SpriteBatch();
        this.tools = new Tools(this);
        this.gameHeight = 500;
        this.getWidth = 600;
        this.winner = "Winner";
    }
}
