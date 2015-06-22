package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.BitmapFont;


public class Ball {
    public Texture ball;
    public Vector2 ballPosition;
    public Vector2 ballVelocity;
    public int speed;
    public Sprite sprite;
    public float ballSize;
    SpriteBatch batch;
    int width;
    int height;
    public Rectangle rectangle;
    public Rectangle ballBounds;
    public Texture rectanle;
    private Globals globals;
    private Game game;

    Ball (Globals globals, Game game) {
        batch = new SpriteBatch();
        ball = new Texture(Gdx.files.internal("ball.png"));
        sprite = new Sprite(ball);
        sprite.setPosition(10, 10);
        ballPosition = new Vector2();
        ballVelocity = new Vector2();
        speed = globals.gameHeight * 3/10;
        ballVelocity.y = speed;
        ballVelocity.x = speed;
        rectanle = new Texture(Gdx.files.internal("rectangle.jpeg"));
        ballBounds = new Rectangle();
        rectangle = new Rectangle();

        this.game = game;
        this.globals = globals;

        resize();
        resetGame();

    }
    public void resizeGame() {
        width = globals.width;
        height = globals.height;
        ballSize = globals.gameHeight /10;
        Gdx.graphics.getDensity();
        System.out.println(Gdx.graphics.getDensity());
    }

    public void resize() {
        resizeGame();
    }

    public void resetGame(){
        ballPosition.set(width / 2 - (ball.getWidth() / 2), globals.gameHeight / 2 - (ball.getHeight() / 2));

    }
    public void updateGame() {
        moveBall();
    }
    public void moveBall() {
        if (Math.abs(ballPosition.y-game.pTwo.playerPosition.y)<100 && ballPosition.x > ((globals.width - globals.getWidth) / 2)+globals.getWidth){
            ballVelocity.x *= -1;
            ballVelocity.y *= -1;
        }
        if (Math.abs(ballPosition.y-game.pOne.playerPosition.y)<100 && ballPosition.x < ((globals.width - globals.getWidth) / 2)){
            ballVelocity.x *= -1;
            ballVelocity.y *= -1;
        }
        float deltaTime = Gdx.graphics.getDeltaTime();
        if (ballPosition.y <= ((height - globals.gameHeight) /2) && ballVelocity.y < 0 ) {
            ballVelocity.y *= -1;

        } else if (ballPosition.y + ballSize >= (height - globals.gameHeight) /2 + globals.gameHeight && ballVelocity.y > 0 ) {
            ballVelocity.y *= -1;

        } else if (ballPosition.x <= ((width - globals.getWidth)/2) - (4*ballSize)  && ballVelocity.x < 0) {
            ballPosition.y = ((height - globals.gameHeight)/2) + (globals.gameHeight  / 2);
            ballPosition.x = ((width - globals.getWidth)/2) + (globals.getWidth  / 2);
            ballVelocity.x = speed;
            game.pOne.score++;
        }else if (ballPosition.x + ballSize >= (((width - globals.getWidth)/2) + globals.getWidth+ 4*ballSize) && ballVelocity.x > 0 ) {
            game.pTwo.score++;
            ballPosition.y = ((height - globals.gameHeight)/2) + (globals.gameHeight  / 2);
            ballPosition.x = ((width - globals.getWidth)/2) + (globals.getWidth  / 2);
            ballVelocity.y = speed;

        }
        ballPosition.y += ballVelocity.y*deltaTime;
        ballPosition.x += ballVelocity.x*deltaTime;
    }

    public void setPosition(float x, float y){
        float width = rectanle.getWidth();
        float height = rectanle.getHeight();
        rectangle.set(x, y, width, height);
    }

}
