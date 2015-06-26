package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.concurrent.*;

public class Game {
    private Globals globals;
    public Player pOne;
    public Player pTwo;
    private int counter = 0;
    private ShapeRenderer shapie;
    private Ball ball;
    public float paddleHeight;
    public float paddleWidth;

    Game(Globals globals) {
        this.globals = globals;
        pOne = new Player();
        pTwo = new Player();
        shapie = new ShapeRenderer();
        paddleHeight = globals.gameHeight/5;
        paddleWidth = globals.gameHeight/10;
        ball = new Ball(globals, this);
        resizeGame();
        resetGame();
        (new Thread(new HelloThread())).start();
        //(new Thread(new NopeThread())).start();
    }

    public void update() {
        playerTouch();
        ball.updateGame();
        if (globals.playerId.equals("1")) {
            if(pTwo.direction.equals("right")&&!(pTwo.playerPosition.y < 0)){
                pTwo.moveRight();
            }
            else if (pTwo.direction.equals("left")&&!(pTwo.playerPosition.y > globals.gameHeight - paddleHeight)){
                pTwo.moveLeft();
            }
        } else {
            if(pOne.direction.equals("right")&&!(pOne.playerPosition.y < 0)){
                pOne.moveRight();
            }
            else if (pOne.direction.equals("left")&&!(pOne.playerPosition.y > globals.gameHeight - paddleHeight)){
                pOne.moveLeft();
            }
        }

    }
    public void addScore(String player){
        if(player=="pOne"){
            pOne.score++;
        }
        else if(player=="pTwo"){
            pTwo.score++;
        }
        if(pOne.score==47){
            globals.winner = "ONE";
            globals.gameState = Globals.GameState.GAMEOVER;
//            gameOver("Player One");
        }
        else if(pTwo.score==47){
            globals.winner = "TWO";
            globals.gameState = Globals.GameState.GAMEOVER;
//            gameOver("Player Two");
        }

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
        if(globals.playerId.equals("1")){
            move(pOne);
        }
        if(globals.playerId.equals("2")){
            move(pTwo);
        }
    }
    public void move (Player player){
        player.lastTouch=Gdx.input.getY();
        boolean touchedRight = player.lastTouch > globals.height/2;
        boolean touchedLeft = player.lastTouch < globals.height/2;
        boolean paddleOffScreenRight = player.playerPosition.y < 0;
        boolean paddleOffScreenLeft = player.playerPosition.y > globals.gameHeight - paddleHeight;

        if ((touchedRight && !  paddleOffScreenRight)){
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

    private class HelloThread implements Runnable {
        private long lastUpdateBall;
        private long lastUpdatePlayer;
        public void run() {
            while(true) {
            //System.out.println(globals.playerId);
            if(globals.playerId!=null){
                System.out.println("Paddle Update");
                if (globals.playerId.equals("1")){

                    String result = globals.tools.pushNetRequest(new String[]{"request", "player", "y", "direction", "by", "bx", "velocityX", "velocityY"}, new String[]{"UPDATE", "1", pOne.playerPosition.y + "", pOne.direction, ball.ballPosition.y+"", ball.ballPosition.x+"", ball.ballVelocity.x+"", ball.ballVelocity.y+""});
                    if (!(globals.network.processJson(result, "timeStamp") == null)) {
                        System.out.println("Player 1 Paddle Update");
                        if (Long.parseLong(globals.network.processJson(result, "timeStamp")) > lastUpdatePlayer) {
                            pTwo.playerPosition.y = Float.parseFloat(globals.network.processJson(result, "y"));
                            pTwo.direction = globals.network.processJson(result, "direction");
                            lastUpdatePlayer = Long.parseLong(globals.network.processJson(result, "timeStamp"));
                        }
                    }

                } else {

                    String result = globals.tools.pushNetRequest(new String[]{"request", "player", "y", "direction"}, new String[]{"UPDATE", "2", pTwo.playerPosition.y + "", pTwo.direction});
                    if (!(globals.network.processJson(result, "timeStamp") == null)) {
                        System.out.println("Player 2 Paddle Update");
                        if (Long.parseLong(globals.network.processJson(result, "timeStamp")) > lastUpdatePlayer) {
                            pOne.playerPosition.y = Float.parseFloat(globals.network.processJson(result, "y"));
                            pOne.direction = globals.network.processJson(result, "direction");
                            lastUpdatePlayer = Long.parseLong(globals.network.processJson(result, "timeStamp"));
                        }
                        if (Long.parseLong(globals.network.processJson(result, "timeStamp")) > lastUpdateBall+5) {
                            ball.ballPosition.x = Float.parseFloat(globals.network.processJson(result, "bx"));
                            ball.ballPosition.y = Float.parseFloat(globals.network.processJson(result, "by"));
                            ball.ballVelocity.x = Float.parseFloat(globals.network.processJson(result, "vx"));
                            ball.ballVelocity.y = Float.parseFloat(globals.network.processJson(result, "vy"));
                            lastUpdateBall = Long.parseLong(globals.network.processJson(result, "timeStamp"));
                        }
                    }

                }

            }
            //System.out.println("gr8 d8 m8, i r8 8/8, no h8");
        }}
    }

    private class NopeThread implements Runnable {
        private long lastUpdate;
        public void run() {
            while(true) {
                //System.out.println(globals.playerId);
                if(globals.playerId!=null){
                    if (globals.playerId.equals("1")){
                        globals.tools.pushNetRequest(new String[]{"request", "meth", "by", "bx", "velocityX", "velocityY" }, new String []{"BALL", "SET", ball.ballPosition.y+"", ball.ballPosition.x+"", ball.ballVelocity.x+"", ball.ballVelocity.y+""});

                    } else {
                        String result = globals.tools.pushNetRequest(new String[]{"request", "meth", "y", "x", "velocityX", "velocityY" }, new String []{"BALL", "GET", ball.ballPosition.y+"", ball.ballPosition.x+"", ball.ballVelocity.x+"", ball.ballVelocity.y+""});
                        System.out.println(result);
                        if (!(globals.network.processJson(result, "setTime") == null)) {
                            System.out.println(globals.network.processJson(result, "setTime"));
                            System.out.println(lastUpdate);
                            if (Long.parseLong(globals.network.processJson(result, "setTime")) > lastUpdate+5) {
                                System.out.println(result);
                                ball.ballPosition.x = Float.parseFloat(globals.network.processJson(result, "x"));
                                ball.ballPosition.y = Float.parseFloat(globals.network.processJson(result, "y"));
                                ball.ballVelocity.x = Float.parseFloat(globals.network.processJson(result, "vx"));
                                ball.ballVelocity.y = Float.parseFloat(globals.network.processJson(result, "vy"));
                                lastUpdate = Long.parseLong(globals.network.processJson(result, "setTime"));
                            }
                        }
                    }

                }
            }}
    }
}
