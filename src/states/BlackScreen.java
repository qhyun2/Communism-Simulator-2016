package states;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import core.Game;

public class BlackScreen extends BasicGameState{

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException{

	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException{

		g.setBackground(Color.black);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException{

		game.enterState(Game.splash, new FadeOutTransition(Color.black, 500), new FadeInTransition(Color.black, 500));
		
		if(Display.getWidth() != container.getWidth() || Display.getHeight() != container.getHeight()) {
	        try {
	        	Game.appgc.setDisplayMode(Display.getWidth(), Display.getHeight(), false);
	        	Game.appgc.reinit();
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
	    }
	}

	@Override
	public int getID(){

		return Game.blackScreen;
	}
}
