package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
//    public Rectangle playerBounds;
    public String direction;
    public String connection;
    private Globals globals;




    Player (Globals globals) {
        batch = new SpriteBatch();
        paddle = new Texture("badlogic.jpg");
        playerPosition = new Vector2();
        height = globals.gameHeight/5;
        width = globals.gameHeight/10;
        playerPosition.y = (height / 2) - (100 / 2);
        lastTouch = height / 2;
        direction = "nowhere";
//        playerBounds = new Rectangle(playerPosition.x, playerPosition.y, paddle.getWidth(), paddle.getHeight());
        if (left) {
            left = false;
        } else {
            playerPosition.x = width - 50;
        }
        this.globals = globals;
    }
    public void checkMove(){
        lastTouch=Gdx.input.getY();
        boolean touchedLeft = lastTouch > globals.height/2;
        boolean touchedRight = lastTouch < globals.height/2;
        if (touchedLeft) {
            direction="left";
        }
        if (touchedRight) {
            direction="right";
        }
        move(direction);
    }
    public void move(String direction){
        float deltaTime = Gdx.graphics.getDeltaTime();
        boolean paddleOffScreenLeft = playerPosition.y > globals.gameHeight - globals.gameHeight/5;
        boolean paddleOffScreenRight = playerPosition.y < 0;
        if (direction.equals("left") && !paddleOffScreenLeft) {
            playerPosition.y += 200*deltaTime;
//            playerBounds.set(playerPosition.x, playerPosition.y, paddle.getWidth(), paddle.getHeight());
        }
        if (direction.equals("right") && !paddleOffScreenRight) {
            playerPosition.y -= 200*deltaTime;
//            playerBounds.set(playerPosition.x, playerPosition.y, paddle.getWidth(), paddle.getHeight());
        }
    }

}