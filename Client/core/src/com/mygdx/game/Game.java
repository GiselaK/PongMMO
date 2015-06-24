package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.concurrent.TimeUnit;

public class Game {
    private Globals globals;
    public Player pOne;
    public Player pTwo;
    private int counter = 0;
    private ShapeRenderer shapie;
    private Ball ball;
    public float paddleWidth;
    public float paddleHeight;

    Game(Globals globals) {
        this.globals = globals;
        pOne = new Player();
        pTwo = new Player();
        shapie = new ShapeRenderer();
        ball = new Ball(globals, this);
        paddleHeight = globals.gameHeight/5;
        paddleWidth = globals.gameHeight/10;

        resizeGame();
        resetGame();
    }

    public void update() {
        playerTouch();
        ball.updateGame();
        counter++;
        if (counter == 30) {
            if (globals.playerId.equals("ONE")){
                globals.tools.pushNetRequest("checkPlayer", "ONE", "type", "MOVE", "y", pOne.playerPosition.y + "");
            } else {
                globals.tools.pushNetRequest("checkPlayer", "TWO", "type", "MOVE", "y", pTwo.playerPosition.y + "");
            }
        } else if (counter == 60) {
            if (globals.playerId.equals("ONE")) {
                String result = globals.tools.pushNetRequest("type", "CHECK", "checkType", "PADDLE", "checkPlayer", "TWO");
                pTwo.playerPosition.y = Float.parseFloat(globals.network.processJson(result, "paddleY"));
            } else {
                String result = globals.tools.pushNetRequest("type", "CHECK", "checkType", "PADDLE", "checkPlayer", "ONE");
                pOne.playerPosition.y = Float.parseFloat(globals.network.processJson(result, "paddleY"));
            }
            counter = 0;
        }
        /*if (ball.ballBounds.overlaps(pOne.playerBounds)){
            //long time = 39L;
            ball.ballVelocity.x *= -1;
            ball.ballVelocity.y *= -1;
            //TimeUnit.SECONDS.sleep(39L);
            //Thread.sleep(1000);

        }
        if (ball.ballBounds.overlaps(pTwo.playerBounds)){
            ball.ballVelocity.x *= -1;
            ball.ballVelocity.y *= -1;
        }*/
    }

    public void draw() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapie.begin(ShapeRenderer.ShapeType.Filled);
        //Draw Board
        shapie.setColor(0, 0, 0, 1);
        shapie.rect(((globals.width - globals.getWidth) / 2), (globals.height - globals.gameHeight) / 2, globals.getWidth, globals.gameHeight);
        //Draw Ball
        shapie.setColor(1, 1, 1, 1);
        shapie.rect(ball.ballPosition.x, ball.ballPosition.y, ball.ballSize, ball.ballSize);
        //Draw Paddles
        shapie.rect((((globals.width - globals.getWidth) / 2)) - paddleWidth, ((globals.height - globals.gameHeight) / 2) + pOne.playerPosition.y, paddleWidth, paddleHeight);
        shapie.rect((((globals.width + globals.getWidth) / 2)), ((globals.height - globals.gameHeight) / 2)+ pTwo.playerPosition.y, paddleWidth, paddleHeight);
        shapie.end();

        globals.batch.begin();
        MyGdxGame.text.draw("Player One Score: "+pOne.score, 200, 200);
        MyGdxGame.text.draw("Player Two Score: " + pTwo.score, 200, 175);
        globals.batch.end();
    }

    public void playerTouch(){
        if(globals.playerId.equals("ONE")){
            move(pOne);
        }
        if(globals.playerId.equals("TWO")){
            move(pTwo);
        }
    }
    public void move (Player player){
        player.lastTouch=Gdx.input.getY();
        boolean touchedRight = player.lastTouch > globals.height/2;
        boolean touchedLeft = player.lastTouch < globals.height/2;
        boolean paddleOffScreenRight = player.playerPosition.y < 0;
        boolean paddleOffScreenLeft = player.playerPosition.y > globals.gameHeight - paddleHeight;



        if ((touchedRight && !paddleOffScreenRight)){
            player.moveRight();
        }
        else if (touchedLeft && !paddleOffScreenLeft) {
            player.moveLeft();
        }
    }

    private void resizeGame() {
        globals.width = Gdx.graphics.getWidth();
        globals.height = Gdx.graphics.getHeight();
        ball.resizeGame();
    }
    private void resetGame(){
        ball.rectangle.set(globals.width/2, globals.height/2, 300, 300);
        ball.resetGame();
        ball.setPosition(ball.rectanle.getWidth(), ball.rectanle.getHeight());
    }
}
