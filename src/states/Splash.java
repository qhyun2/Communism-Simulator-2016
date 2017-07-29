package states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import core.Game;

public class Splash extends BasicGameState{

	public Image bg;
	public int time;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException{
		bg = new Image("res/images/gandhi.png");
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException{
		g.drawImage(bg, 0, 0, container.getWidth(), container.getHeight(), 0, 0, bg.getWidth(), bg.getHeight());
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException{
		time += delta;
		if(time > 1000) game.enterState(Game.menu, new FadeOutTransition(Color.black, 500), new FadeInTransition(Color.black, 500));
	}

	@Override
	public int getID(){
		return Game.splash;
	}

}
