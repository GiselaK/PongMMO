package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.sun.org.apache.xpath.internal.operations.Bool;

public class
		MyGdxGame extends ApplicationAdapter {
	private Network network;
	public static Text text;
	private Globals globals;
	private Game game;
    private Menu menu;
	public Boolean setup;
	public Boolean firstTime;
//	private OrthographicCamera camera;
	public void setNetwork(Network network) {
		this.network = network;
	}

	@Override
	public void create () {
		globals = new Globals();
		game = new Game(globals);
		globals.network = network;
		text = new Text(globals);
        menu = new Menu(globals);
		firstTime = true;
		game.runThread();
		//camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

	}

	@Override
	public void render () {
		switch (globals.gameState) {
			case MENU:
				firstTime=true;
				setup=false;
				menu.update();
				menu.draw();
				break;
			case STARTED:
				game.update();
				game.draw();
				break;
			case WAITING:
				if(setup==false){
					Setup.connect(globals);
					setup=true;
				}

				Gdx.gl.glClearColor(0, 0, 0, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				globals.batch.begin();
				text.draw("Please wait for another player", globals.width / 2 - 250, globals.height / 2);
				globals.batch.end();
//				if(firstTime==true){

//					firstTime=false;
//				}
				break;
			case GAMEOVER:
				game.stupidity=false;
				firstTime=true;
				setup=false;
				Gdx.gl.glClearColor(0, 0, 0, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				globals.batch.begin();
				if(globals.playerId.equals(globals.winner)){
                   text.draw("You Won. Tap to Play Again", globals.width / 2 - 200, globals.height / 2);
				}
				else {
                    text.draw("You Lost. Tap to Play Again", globals.width / 2 - 200, globals.height / 2);
				}

                if (Gdx.input.justTouched()) {
                    globals.gameState = Globals.GameState.MENU;
//					String response = globals.tools.pushNetRequest(new String []{"request"}, new String []{ "CHECK"});
//					Setup.connect(globals);
//					globals.gameState = Globals.GameState.WAITING;
                    game.pOne.score = 0;
                    game.pTwo.score = 0;
				}
				globals.batch.end();
				break;
			case pDisconnect:
				Gdx.gl.glClearColor(0, 0, 0, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				globals.batch.begin();
				text.draw("Please fix your wifi connection and try again", globals.width / 2 - 200, globals.height / 2);
				globals.batch.end();
				break;
			case ERROR:
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				globals.batch.begin();
				text.draw("You're wifi disconnected.\n Please reconnect and restart the app", globals.width / 2 - 200, globals.height / 2);
				if (globals.error != null) {
					text.draw(globals.error, 0, 0);
					System.out.println(globals.error);
				}

//				text.draw(globals.error, 200, 175);
				globals.batch.end();
				break;

			default:
				System.err.println("Error: Render game state switch defaulted");
				break;
		}
		//globals.batch.setProjectionMatrix(camera.combined);
	}
}