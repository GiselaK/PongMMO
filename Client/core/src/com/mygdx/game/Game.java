package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.concurrent.*;

public class Game {
    private Globals globals;
    public Player pOne;
    public Player pTwo;
    private ShapeRenderer shapie;
    public Ball ball;
    public float paddleHeight;
    public float paddleWidth;
    public int winningScore;

    Game(Globals globals) {
        this.globals = globals;
        pOne = new Player(globals);
        pTwo = new Player(globals);
        shapie = new ShapeRenderer();
        paddleHeight = globals.gameHeight/5;
        paddleWidth = globals.gameHeight/10;
        ball = new Ball(globals, this);
        winningScore = 20;

        resizeGame();
        resetGame();
        (new Thread(new ScrewThreads(globals, this))).start();
    }
    public void updatePlayersPos (Player myPaddle, Player yourPlayer){
        myPaddle.checkMove();
        if(yourPlayer.direction.equals("right") && !(yourPlayer.playerPosition.y < 0)){
            yourPlayer.move(yourPlayer.direction);
        }
        else if (yourPlayer.direction.equals("left") && !(yourPlayer.playerPosition.y > globals.gameHeight - paddleHeight)){
            yourPlayer.move(yourPlayer.direction);
        }
    }
    public void update() {
        ball.updateGame();
        if(globals.playerId!=null) {
            if (globals.playerId.equals("1")) {
                updatePlayersPos(pOne, pTwo);
            } else {
                updatePlayersPos(pTwo, pOne);
            }
        }
    }
    public void addScore(String player){
        if(player.equals("pOne")){
            pOne.score++;
        }
        else if(player.equals("pTwo")){
            pTwo.score++;
        }
        if(pOne.score == winningScore){
            globals.winner = "1";
            globals.gameState = Globals.GameState.GAMEOVER;
        }
        else if(pTwo.score == winningScore){
            globals.winner = "2";
            globals.gameState = Globals.GameState.GAMEOVER;
        }

    }

    public void draw() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapie.begin(ShapeRenderer.ShapeType.Filled);
        shapie.setColor(0, 0, 0, 1);
        shapie.rect(((globals.width - globals.getWidth) / 2), (globals.height - globals.gameHeight) / 2, globals.getWidth, globals.gameHeight);
        shapie.setColor(1, 1, 1, 1);
        shapie.rect(ball.ballPosition.x, ball.ballPosition.y, ball.ballSize, ball.ballSize);
        shapie.rect((((globals.width - globals.getWidth) / 2)) - paddleWidth, ((globals.height - globals.gameHeight) / 2) + pOne.playerPosition.y, paddleWidth, paddleHeight);
        shapie.rect((((globals.width + globals.getWidth) / 2)), ((globals.height - globals.gameHeight) / 2)+ pTwo.playerPosition.y, paddleWidth, paddleHeight);
        shapie.end();
        globals.batch.begin();
        MyGdxGame.text.draw("Player One Score: "+ pOne.score, 200, 200);
        MyGdxGame.text.draw("Player Two Score: " + pTwo.score, 200, 175);
        globals.batch.end();
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