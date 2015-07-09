package com.mygdx.game;

import com.badlogic.gdx.Gdx;

import java.sql.Timestamp;

public class ScrewThreads implements Runnable {
    private long lastUpdateBall;
    private long lastUpdatePlayer;
    private Globals globals;
    private Game game;

    ScrewThreads (Globals globals, Game game){
        this.globals = globals;
        this.game = game;
    }

    private Long calcPos() {
        java.util.Date date = new java.util.Date();
        if (globals.serverTimeStamp == null){
            return (long) 0;
        }
        else {
            return Long.parseLong(globals.serverTimeStamp) - date.getTime();
        }

    }

    public void run() {
        System.out.println("run");
        while (globals.gameState.equals(Globals.GameState.WAITING)){
            System.out.println("Waiting");
            String response = globals.tools.pushNetRequest(new String []{"request"}, new String []{ "CHECK"});
            if((globals.network.processJson(response,"ready")!=null)){
                globals.ready = globals.network.processJson(response, "ready");
                if(globals.ready.equals("TRUE")) {
                    globals.gameState = Globals.GameState.STARTED;
                }
                else{
                    System.out.println("Check:"+globals.ready);
                }
            }
            else {
                System.out.println("Null: " +globals.network.processJson(response,"ready"));
            }
        }
        while (globals.gameState.equals(Globals.GameState.STARTED)) {
            System.out.println("Started");
            float deltaTime = Gdx.graphics.getDeltaTime();
            if (globals.playerId.equals("1")) {
                String result = globals.tools.pushNetRequest(new String[]{"request", "player", "y", "direction", "by", "bx", "velocityX", "velocityY", "game"}, new String[]{"UPDATE", "1", (game.pOne.playerPosition.y + calcPos() * 200) + "", game.pOne.direction, game.ball.ballPosition.y + "", game.ball.ballPosition.x + "", game.ball.ballVelocity.x + "", game.ball.ballVelocity.y + "",globals.game});
                if (!(globals.network.processJson(result, "timeStamp") == null) && (Long.parseLong(globals.network.processJson(result, "timeStamp")) > lastUpdatePlayer)) {
                    System.out.println("p1 if"+Gdx.graphics.getDeltaTime());
                    game.pTwo.playerPosition.y = Float.parseFloat(globals.network.processJson(result, "y"));
                    game.pTwo.direction = globals.network.processJson(result, "direction");
                    lastUpdatePlayer = Long.parseLong(globals.network.processJson(result, "timeStamp"));
                }


            } else {
                System.out.println("p2 start "+System.currentTimeMillis());
                String result = globals.tools.pushNetRequest(new String[]{"request", "player", "y", "direction", "game"}, new String[]{"UPDATE", "2", (game.pTwo.playerPosition.y + calcPos() * 200) + "", game.pTwo.direction, globals.game});
                if (!(globals.network.processJson(result, "timeStamp") == null)) {
                    if (Long.parseLong(globals.network.processJson(result, "timeStamp")) > lastUpdatePlayer) {
                        System.out.println("p2 if "+System.currentTimeMillis());
                        game.pOne.playerPosition.y = Float.parseFloat(globals.network.processJson(result, "y"));
                        game.pOne.direction = globals.network.processJson(result, "direction");
                        lastUpdatePlayer = Long.parseLong(globals.network.processJson(result, "timeStamp"));
                    }
                    if (Long.parseLong(globals.network.processJson(result, "timeStamp")) > lastUpdateBall + 5) {
                        System.out.println("p2 if 2 "+System.currentTimeMillis());
                        game.ball.ballPosition.x = Float.parseFloat(globals.network.processJson(result, "bx"));
                        game.ball.ballPosition.y = Float.parseFloat(globals.network.processJson(result, "by"));
                        game.ball.ballVelocity.x = Float.parseFloat(globals.network.processJson(result, "vx"));
                        game.ball.ballVelocity.y = Float.parseFloat(globals.network.processJson(result, "vy"));
                        lastUpdateBall = Long.parseLong(globals.network.processJson(result, "timeStamp"));
                    }
                }
            }
        }
    }
}

