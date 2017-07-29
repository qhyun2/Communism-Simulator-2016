package states;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import core.Game;
import gui.Button;


public class Tutorial extends BasicGameState{

	private Image bg;
	private Button button;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException{

		int screenWidth = container.getWidth();

		int padding = screenWidth / 75;
		int width = screenWidth / 9;
		int height = width / 3;

		bg = new Image("res/images/tutorial.png");

		Shape rect = new Rectangle(padding, padding, width, height);
		button = new Button("res/images/empty.png", rect, "Back");
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException{

		g.drawImage(bg, 0, 0, container.getWidth(), container.getHeight(), 0, 0, bg.getWidth(), bg.getHeight());
		button.render(g, Game.heading);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException{

		Input input = container.getInput();
		int x = Mouse.getX();
		int y = container.getHeight() - Mouse.getY();

		button.update(x, y, input.isMouseButtonDown(0));

		if(button.state){
			button.state = false;
			game.enterState(Game.menu, new FadeOutTransition(Color.black, 200), new FadeInTransition(Color.black, 200));
		}

	}

	@Override
	public int getID(){

		return Game.tutorial;
	}

}
