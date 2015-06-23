package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends ApplicationAdapter {
	Network network;
	public static Text text;
	private Globals globals;
	private Setup setup;
	private Waiting waiting;

	private Game game;

	public void setNetwork(Network network) {
		this.network = network;
	}

	@Override
	public void create () {
		//Init Objects
		globals = new Globals();
		setup = new Setup(globals);
		waiting = new Waiting(globals);
		game = new Game(globals);
		globals.network = network;
		text = new Text(globals);
//		winner = new String(globals);


		//Connect to Server
		String result = setup.connect();
		System.out.println(result);
		if (result == "success") {
			//Join the Lobby
			globals.gameState = Globals.GameState.WAITING;
			return;
		} else {
			//Display the Error
			globals.error = result;
			globals.gameState = Globals.GameState.ERROR;
			return;
		}
	}

	@Override
	public void render () {
		//Run functions for current game state
		switch (globals.gameState) {

			//Waiting for player to join game
			case WAITING:
				//If both players are ready
				if (waiting.ready == true) {
					globals.gameState = Globals.GameState.STARTED;
					break;
				}
				//Display the waiting screen
				else {
					waiting.update();
					waiting.render();
				}
				break;

			//Game has already started
			case STARTED:
				game.update();
				game.draw();
				break;

			case GAMEOVER:
				Gdx.gl.glClearColor(1, 0, 0, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				globals.batch.begin();
				if(globals.playerId.equals(globals.winner)){
					text.draw(" You beat their ass", 200, 175);
				}
				else {
					text.draw("You got your ass beat", 200, 175);
					System.out.println(globals.playerId);
					System.out.println("^globals.playerId^");
					System.out.println(globals.winner);
					System.out.println("^globals.winner^");
				}
				globals.batch.end();
				break;
			//There was an error
			case ERROR:
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				globals.batch.begin();
				text.draw("You have reached an error", 200, 200);
				text.draw(globals.error, 200, 175);
				globals.batch.end();
				break;

			//Throw an error
			default:
				System.err.println("Error: Render game state switch defaulted");
				//TODO: Quit the app here
				break;
		}
	}
}