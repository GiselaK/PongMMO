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
        while (true){
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
                String result = globals.tools.pushNetRequest(new String[]{"request", "player", "y", "direction", "by", "bx", "velocityX", "velocityY", "game", "oneScore", "twoScore"}, new String[]{"UPDATE", "1", (game.pOne.playerPosition.y + calcPos() * 200) + "", game.pOne.direction, game.ball.ballPosition.y + "", game.ball.ballPosition.x + "", game.ball.ballVelocity.x + "", game.ball.ballVelocity.y + "",globals.game,game.pOne.score+"",game.pTwo.score+""});
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
                    if(game.pOne.score==4||game.pTwo.score==4){
                        if(Integer.parseInt(globals.network.processJson(result, "oneScore"))==0&&game.pOne.score>0||Integer.parseInt(globals.network.processJson(result, "twoScore"))==0&&game.pTwo.score>0){
                            if(game.pOne.score>=game.pTwo.score){
                                globals.winner="1";
                                globals.gameState= Globals.GameState.GAMEOVER;
                            }
                            else {
                                globals.winner="2";
                                globals.gameState= Globals.GameState.GAMEOVER;
                            }
                        }
                    }
                    if(globals.network.processJson(result,"oneScore")!=null &&Integer.parseInt(globals.network.processJson(result,"oneScore"))!=game.pOne.score){
//                        System.out.println("Player 1:"+game.pOne.score);
                        game.pOne.score=Integer.parseInt(globals.network.processJson(result, "oneScore"));
                    }
                    if(globals.network.processJson(result,"twoScore")!=null &&Integer.parseInt(globals.network.processJson(result,"twoScore"))!=game.pTwo.score){
//                        System.out.println("Player 2:"+game.pTwo.score);
                        game.pTwo.score = Integer.parseInt(globals.network.processJson(result,"twoScore"));
                    }
                }
            }
        }
    }}
}

