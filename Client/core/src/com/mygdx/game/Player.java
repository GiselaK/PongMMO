package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class Player {
    SpriteBatch batch;
    Texture paddle;
    public Vector2 playerPosition;
    int height;
    int width;
    int lastTouch;
    static boolean left = true;
    public int score;
    public Rectangle playerBounds;


    Player () {
        batch = new SpriteBatch();
        paddle = new Texture("badlogic.jpg");
        playerPosition = new Vector2();
        height = Gdx.graphics.getHeight();
        width = Gdx.graphics.getWidth();
        playerPosition.y = (height/2)-(100/2);
        lastTouch = height/2;
        playerBounds = new Rectangle(playerPosition.x, playerPosition.y, paddle.getWidth(), paddle.getHeight());
        if (left) {
            left = false;
        } else {
            playerPosition.x=width-50;
        }

    }

    public void moveLeft(){
        playerPosition.y+=2;
        playerBounds.set(playerPosition.x, playerPosition.y, paddle.getWidth(), paddle.getHeight());
    }

    public void moveRight(){
        playerPosition.y-=2;
        playerBounds.set(playerPosition.x, playerPosition.y, paddle.getWidth(), paddle.getHeight());
    }

}