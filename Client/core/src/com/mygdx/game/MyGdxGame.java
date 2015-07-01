package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class MyGdxGame extends ApplicationAdapter {
	private Network network;
	public static Text text;
	private Globals globals;
	private Setup setup;
	private Game game;
    private Menu menu;
//	private OrthographicCamera camera;
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
        menu = new Menu(globals);
		setup.connect();
		//camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

	}

	@Override
	public void render () {


		switch (globals.gameState) {
			case STARTED:
				game.update();
				game.draw();
				break;
			case MENU:
				menu.update();
                menu.draw();
				break;
			case GAMEOVER:
				Gdx.gl.glClearColor(0, 0, 0, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				globals.batch.begin();
				if(globals.playerId.equals(globals.winner)){
                   text.draw("You Won", globals.width/2-20, globals.height-200);
				}
				else {
                    text.draw("You Lost", globals.width/2-20, globals.height-200);
				}
                if (Gdx.input.justTouched()) {
                    globals.gameState = Globals.GameState.MENU;
                    globals.tools.pushNetRequest(new String[]{"request"}, new String[]{"RESET"});
                    setup.connect();
                    game.pOne.score = 0;
                    game.pTwo.score = 0;
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
		//globals.batch.setProjectionMatrix(camera.combined);
	}
}