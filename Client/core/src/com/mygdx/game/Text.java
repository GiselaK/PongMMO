package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class  Text {
    private BitmapFont font;
    private Globals globals;

    Text(Globals globals) {
        this.globals = globals;
        font = new BitmapFont(Gdx.files.internal("font.fnt"), Gdx.files.internal("font.png"), false);
    }
    public void draw(String text, int x, int y){
        font.setColor(Color.WHITE);
        font.draw(globals.batch, text, x, y);
    }
}
