package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
    private float tatas;
    public Sound bounce = Gdx.audio.newSound(Gdx.files.internal("paddlehit.wav"));
    public Music music = Gdx.audio.newMusic(Gdx.files.internal("song.mp3"));

    Ball (Globals globals, Game game) {

        batch = new SpriteBatch();
        ball = new Texture(Gdx.files.internal("ball.png"));
        sprite = new Sprite(ball);
        sprite.setPosition(10, 10);
        ballPosition = new Vector2();
        ballVelocity = new Vector2();
        speed = globals.gameHeight * 4/10;
        ballVelocity.y = speed;
        ballVelocity.x = speed;
        rectanle = new Texture(Gdx.files.internal("rectangle.jpeg"));
        rectangle = new Rectangle();
        ballBounds = new Rectangle(ballPosition.x, ballPosition.y, ball.getWidth(), ball.getHeight());
        tatas = globals.gameHeight / 20;

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
        music.play();
        music.isLooping();
        music.setVolume(1);
    }
    public void setBallPos() {
        ballPosition.y = ((height - globals.gameHeight)/2) + (globals.gameHeight  / 2);
        ballPosition.x = ((width - globals.getWidth)/2) + (globals.getWidth  / 2);
    }
    public void setBallVel(){
        ballVelocity.x *= -1;
        ballVelocity.y *= 1;
    }
    public boolean pOneYEqualsBallY(){
        if(ballPosition.y  > game.pOne.playerPosition.y - tatas && ballPosition.y < game.pOne.playerPosition.y + game.paddleHeight + tatas){
            return true;
        }
        return false;
    }
    public boolean pTwoYEqualsBallY(){
        if((ballPosition.y + ballSize > game.pTwo.playerPosition.y + tatas) && ( ballPosition.y < game.pTwo.playerPosition.y + game.paddleHeight - tatas)){
            return true;
        }
        return false;
    }
    public boolean pOneXEqualsBallX(){
        if(ballPosition.x <= ((globals.width - globals.getWidth) / 2) && ballPosition.x >= ((globals.width - globals.getWidth) / 2)  - game.paddleWidth - tatas){
            return true;
        }
        return false;
    }
    public boolean pTwoXEqualsBallX(){
        if(ballPosition.x + ballSize >= ((globals.width - globals.getWidth) / 2) + globals.getWidth && ballPosition.x  + ballSize<= ((globals.width - globals.getWidth) / 2) + globals.getWidth + game.paddleWidth + tatas){
            return true;
        }
        return false;
    }
    public void changeColor(){
        game.r = game.rand.nextFloat();
        game.g = game.rand.nextFloat();
        game.b = game.rand.nextFloat();
    }
    public void moveBall() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        if ((pOneYEqualsBallY() && pOneXEqualsBallX() && ballVelocity.x < 0 && ballVelocity.y > 0)){
            changeColor();
            setBallVel();
            bounce.play();
        }
        if (pOneYEqualsBallY() && pOneXEqualsBallX() && ballVelocity.x < 0 && ballVelocity.y < 0){
            changeColor();
            setBallVel();
            bounce.play();
        }

        if (pTwoYEqualsBallY() && pTwoXEqualsBallX() && ballVelocity.x > 0 && ballVelocity.y < 0 ) {
            changeColor();
            setBallVel();
        }
        if (pTwoYEqualsBallY() &&  pTwoXEqualsBallX() && ballVelocity.x > 0 && ballVelocity.y > 0 ) {
            changeColor();
            setBallVel();
            bounce.play();
        }

        if (ballPosition.y <= ((height - globals.gameHeight) /2) && ballVelocity.y < 0 ) {
            ballVelocity.y *= -1;

        } else if (ballPosition.y + ballSize >= (height - globals.gameHeight) /2 + globals.gameHeight && ballVelocity.y > 0 ) {
            ballVelocity.y *= -1;

        } else if (ballPosition.x <= ((width - globals.getWidth)/2) - (4*ballSize)  && ballVelocity.x < 0) {
            setBallPos();
            if(globals.playerId.equals("1")){
                game.addScore("pTwo");
            }
//            else if(globals.playerId.equals("2")&&game.pTwo.score==4&&game.stupidity==true){
//                game.addScore("pTwo");
//            }
//            else if(globals.playerId.equals("2")&&game.pTwo.score==4){
//                game.stupidity=true;
//            }
            ballVelocity.x*=-1;
        }else if (ballPosition.x + ballSize >= (((width - globals.getWidth)/2) + globals.getWidth+ 4*ballSize) && ballVelocity.x > 0 ) {
            setBallPos();
            if(globals.playerId.equals("1")){
                game.addScore("pOne");

            }
//            else if(globals.playerId.equals("2")&&game.pOne.score==4 &&game.stupidity==true){
//                game.addScore("pOne");
//            }
//            else if (globals.playerId.equals("2")&&game.pTwo.score==4){
//                game.stupidity=true;
//            }
            ballVelocity.x*=-1;
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
