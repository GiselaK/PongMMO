package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class MyGdxGame extends ApplicationAdapter {
	private Network network;
	public static Text text;
	private Globals globals;
	private Setup setup;
	private Game game;
	public void setNetwork(Network network) {
		this.network = network;
	}

	@Override
	public void create () {
		globals = new Globals();
		setup = new Setup(globals);
		game = new Game(globals);
		globals.network = network;
		text = new Text(globals);
		setup.connect();
	}

	@Override
	public void render () {
		switch (globals.gameState) {
			case STARTED:
				game.update();
				game.draw();
				break;
			case GAMEOVER:
				Gdx.gl.glClearColor(1, 0, 0, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				globals.batch.begin();
				if(globals.playerId.equals(globals.winner)){
					text.draw("You Won", 200, 175);
				}
				else {
					text.draw("You Lost", 200, 175);
				}
				globals.batch.end();
				break;
			case ERROR:
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				globals.batch.begin();
				text.draw("You have reached an error", 200, 200);
				text.draw(globals.error, 200, 175);
				globals.batch.end();
				break;

			default:
				System.err.println("Error: Render game state switch defaulted");
				break;
		}
	}
}