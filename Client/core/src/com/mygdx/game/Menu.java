package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Menu {
    private Globals globals;
    private Page page;
    enum Page {
        Home, Play, Stats
    }
    enum Quadran {
        TL, TR, BL, BR, NULL
    }
    Menu (Globals globals) {
        this.globals = globals;
        this.page = Page.Home;
    }
    private Quadran getInput() {
        if (Gdx.input.justTouched()) {
            if (Gdx.input.getY() < globals.height/2) {
                if (Gdx.input.getX() > globals.width/2) {
                    System.out.println("Quadran.TR");
                    return Quadran.TR;
                } else {
                    System.out.println("Quadran.TL");
                    return Quadran.TL;
                }
            }
            else  {
                if (Gdx.input.getX() > globals.width/2) {
                    System.out.println("Quadran.BR");
                    return Quadran.BR;
                } else {
                    System.out.println("Quadran.BL");
                    return Quadran.BL;
                }
            }
        }
        return Quadran.NULL;
    }
    public void draw() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        globals.batch.begin();
        switch (page) {
            case Home:
                MyGdxGame.text.draw("Play", globals.width/2-20, globals.height-200);
                MyGdxGame.text.draw("Stats", globals.width/2-20, 200);
                break;
            case Play:
                break;
            case Stats:
                MyGdxGame.text.draw("Stats not yet Avaliable", globals.width/2-100, globals.height-200);
                MyGdxGame.text.draw("Wins: ", globals.width/2-20, 200);
                MyGdxGame.text.draw("Losses: ", globals.width/2-20, 175);
                break;
        }
        globals.batch.end();
    }
    public void update() {
        Quadran input = getInput();
        if (input == Quadran.NULL) return;
        switch (page) {
            case Home:
                if (input == Quadran.TL || input == Quadran.TR) {
                    page = Page.Play;
                } else {
                    page = Page.Stats;
                }
                break;
            case Play:
                globals.gameState = Globals.GameState.STARTED;
                page = Page.Home;
                break;
            case Stats:
                page = Page.Home;
                break;
        }
        return;
    }
}
