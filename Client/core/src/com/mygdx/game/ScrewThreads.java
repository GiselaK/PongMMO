package com.mygdx.game;

public class ScrewThreads implements Runnable {
    private long lastUpdateBall;
    private long lastUpdatePlayer;
    private Globals globals;
    private Game game;
    ScrewThreads (Globals globals, Game game){
        this.globals = globals;
        this.game = game;
    }
    public void run() {
        //TODO: Temporary Fix
        globals.gameState = Globals.GameState.STARTED;

        while(true && globals.gameState.equals(Globals.GameState.STARTED)) {
            if(globals.playerId!=null) {
                if (globals.playerId.equals("1")) {
                    String result = globals.tools.pushNetRequest(new String[]{"request", "player", "y", "direction", "by", "bx", "velocityX", "velocityY"}, new String[]{"UPDATE", "1", game.pOne.playerPosition.y + "", game.pOne.direction, game.ball.ballPosition.y + "", game.ball.ballPosition.x + "", game.ball.ballVelocity.x + "", game.ball.ballVelocity.y + ""});
                    if (!(globals.network.processJson(result, "timeStamp") == null) && (Long.parseLong(globals.network.processJson(result, "timeStamp")) > lastUpdatePlayer)) {
                        game.pTwo.playerPosition.y = Float.parseFloat(globals.network.processJson(result, "y"));
                        game.pTwo.direction = globals.network.processJson(result, "direction");
                        lastUpdatePlayer = Long.parseLong(globals.network.processJson(result, "timeStamp"));
                    }

                }
                else {
                    String result = globals.tools.pushNetRequest(new String[]{"request", "player", "y", "direction"}, new String[]{"UPDATE", "2", game.pTwo.playerPosition.y + "", game.pTwo.direction});
                    if (!(globals.network.processJson(result, "timeStamp") == null)) {
                        if (Long.parseLong(globals.network.processJson(result, "timeStamp")) > lastUpdatePlayer) {
                            game.pOne.playerPosition.y = Float.parseFloat(globals.network.processJson(result, "y"));
                            game.pOne.direction = globals.network.processJson(result, "direction");
                            lastUpdatePlayer = Long.parseLong(globals.network.processJson(result, "timeStamp"));
                        }
                        if (Long.parseLong(globals.network.processJson(result, "timeStamp")) > lastUpdateBall + 5) {
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
}
