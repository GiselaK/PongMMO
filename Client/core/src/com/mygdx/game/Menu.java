package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import java.util.Random;

public class Menu {
    private Globals globals;
    private Page page;
    private Text2 text2;
    Random rand = new Random();
    enum Page {
        Home, Play, Credits
    }
    enum Quadran {
        TL, TR, BL, BR, NULL
    }
    Menu (Globals globals) {
        this.globals = globals;
        this.page = Page.Home;
        this.text2 = new Text2(globals);
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
                text2.font.getData().setScale(3, 3);
                text2.draw("PONGIE", globals.width / 2 - 130, globals.height - 100, rand.nextInt(), rand.nextInt(), rand.nextInt() );
                //MyGdxGame.text.font.setColor(1,1,1,1);


                MyGdxGame.text.font.getData().setScale(1, 1);
                MyGdxGame.text.draw("Play", globals.width/2-20, globals.height - 200);
                MyGdxGame.text.draw("Credits", globals.width/2-40, 170);

                MyGdxGame.text.draw("INSTRUCTIONS:", globals.width/2-400, 100);
                MyGdxGame.text.draw("Tap the top half of the screen to move up", globals.width/2-400, 50);
                MyGdxGame.text.draw("Tap the bottom half of the screen to move down", globals.width/2-400, 25);
                break;
            case Play:
                globals.gameState = Globals.GameState.WAITING;
                break;
            case Credits:
                MyGdxGame.text.draw("Bryce Mao", globals.width/2-100, globals.height-200);
                MyGdxGame.text.draw("Charlotte Deming", globals.width/2-160, globals.height-250);
                MyGdxGame.text.draw("Elias Fox", globals.width/2-100, globals.height-300);
                MyGdxGame.text.draw("Gisela Kottmeir", globals.width/2-145, globals.height-350);
                MyGdxGame.text.draw("-TAP TO GO BACK-", globals.width/2-145, globals.height-500);

                break;
        }
        globals.batch.end();
    }
    public void update() {
        Quadran input = getInput();
        if (input == Quadran.NULL){ return;}
        switch (page) {
            case Home:
                if (input == Quadran.TL || input == Quadran.TR) {
                    page = Page.Play;
                } else {
                    page = Page.Credits;
                }
                break;
            case Play:
                globals.gameState = Globals.GameState.WAITING;
                page = Page.Home;
                break;
            case Credits:
                page = Page.Home;
                break;
        }
        return;
    }
}
