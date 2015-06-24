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
        rectangle = new Rectangle();
        ballBounds = new Rectangle(ballPosition.x, ballPosition.y, ball.getWidth(), ball.getHeight());

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
        ballPosition.set(globals.width / 2 - (ball.getWidth() ), globals.height / 2 - (ball.getHeight() / 2));
        game.pTwo.playerPosition.y = ((globals.height - globals.gameHeight) / 2) - game.paddleHeight;
        game.pTwo.playerPosition.y = ((globals.height - globals.gameHeight) / 2) - game.paddleHeight;

    }
    public void updateGame() {
        moveBall();
    }
    public void setBallPos() {
        ballPosition.y = ((height - globals.gameHeight)/2) + (globals.gameHeight  / 2);
        ballPosition.x = ((width - globals.getWidth)/2) + (globals.getWidth  / 2);
    }
    public void setBallVel(){
        ballVelocity.x *= -1;
        ballVelocity.y *= 1;
    }
    public void moveBall() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        if ((ballPosition.y  > game.pOne.playerPosition.y && ballPosition.y < game.pOne.playerPosition.y + game.paddleHeight && ballPosition.x <= ((globals.width - globals.getWidth) / 2) && ballPosition.x >= ((globals.width - globals.getWidth) / 2)  - game.paddleWidth && ballVelocity.x < 0 && ballVelocity.y > 0)){
            setBallVel();
        }
        if (ballPosition.y  > game.pOne.playerPosition.y && ballPosition.y < game.pOne.playerPosition.y + game.paddleHeight && ballPosition.x <= ((globals.width - globals.getWidth) / 2) && ballPosition.x >= ((globals.width - globals.getWidth) / 2)  - game.paddleWidth && ballVelocity.x < 0 && ballVelocity.y < 0){
            setBallVel();
        }

        if ((ballPosition.y + ballSize > game.pTwo.playerPosition.y) && ( ballPosition.y < game.pTwo.playerPosition.y + game.paddleHeight ) &&  ballPosition.x + ballSize >= ((globals.width - globals.getWidth) / 2) + globals.getWidth && ballPosition.x  + ballSize<= ((globals.width - globals.getWidth) / 2) + globals.getWidth + game.paddleWidth && ballVelocity.x > 0 && ballVelocity.y < 0 ) {
            setBallVel();
        }
        if ((ballPosition.y + ballSize > game.pTwo.playerPosition.y) && ( ballPosition.y < game.pTwo.playerPosition.y + game.paddleHeight ) &&  ballPosition.x + ballSize >= ((globals.width - globals.getWidth) / 2) + globals.getWidth && ballPosition.x  + ballSize<= ((globals.width - globals.getWidth) / 2) + globals.getWidth + game.paddleWidth && ballVelocity.x > 0 && ballVelocity.y > 0 ) {
            setBallVel();
        }

        if (ballPosition.y <= ((height - globals.gameHeight) /2) && ballVelocity.y < 0 ) {
            ballVelocity.y *= -1;

        } else if (ballPosition.y + ballSize >= (height - globals.gameHeight) /2 + globals.gameHeight && ballVelocity.y > 0 ) {
            ballVelocity.y *= -1;

        } else if (ballPosition.x <= ((width - globals.getWidth)/2) - (4*ballSize)  && ballVelocity.x < 0) {
            setBallPos();
            game.addScore("pTwo");
            game.restartGame("x");
//            ballVelocity.x = speed;
//            ++;
        }else if (ballPosition.x + ballSize >= (((width - globals.getWidth)/2) + globals.getWidth+ 4*ballSize) && ballVelocity.x > 0 ) {
            setBallPos();
            game.addScore("pOne");
            game.restartGame("y");
//            game.pTwo.score++;

//            ballVelocity.y = speed;

        }
        ballPosition.y += ballVelocity.y*deltaTime;
        ballPosition.x += ballVelocity.x*deltaTime;
        ballBounds.set(ballPosition.x, ballPosition.y, ball.getWidth(), ball.getHeight());
    }

    public void setPosition(float x, float y){
        float width = rectanle.getWidth();
        float height = rectanle.getHeight();
        rectangle.set(x, y, width, height);
    }

}
