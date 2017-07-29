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

public class Menu extends BasicGameState{

	private Image bg;
	private Button[] buttons;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException{

		float scale = 1.3f;

		buttons = new Button[4];

		int buttonWidth = (int)(container.getWidth() / 9 * scale);
		int buttonHeight = (int)(container.getHeight() / 15 * scale);

		int x = container.getWidth() / 10;
		int y = container.getHeight() / 4;

		int padding = (int)(container.getWidth() / 16 * scale);

		Shape b1 = new Rectangle(x, y, buttonWidth, buttonHeight);
		Shape b2 = new Rectangle(x, y + padding, buttonWidth, buttonHeight);
		Shape b3 = new Rectangle(x, y + padding * 2, buttonWidth, buttonHeight);
		Shape b4 = new Rectangle(x, y + padding * 3, buttonWidth, buttonHeight);

		String buttonImage = "res/images/empty.png";

		buttons[0] = new Button(buttonImage, b1, "Play");
		buttons[1] = new Button(buttonImage, b2, "Options");
		buttons[2] = new Button(buttonImage, b3, "How to Play");
		buttons[3] = new Button(buttonImage, b4, "Quit");

		bg = new Image("res/images/menu.png");
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException{

		g.drawImage(bg, 0, 0, container.getWidth(), container.getHeight(), 0, 0, bg.getWidth(), bg.getHeight());
		for (int i = 0; i < buttons.length; i++){
			buttons[i].render(g, Game.heading);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException{

		Input input = container.getInput();
		int x = Mouse.getX();
		int y = container.getHeight() - Mouse.getY();

		for (int i = 0; i < buttons.length; i++){
			buttons[i].update(x, y, input.isMouseButtonDown(0));
			if(buttons[i].state){
				buttons[i].state = false;
				handleEvent(i, container, game);
			}
		}
	}

	private void handleEvent(int i, GameContainer container, StateBasedGame game) throws SlickException{

		switch (i) {
			case 0:
				game.enterState(Game.play, new FadeOutTransition(Color.black, 200), new FadeInTransition(Color.black, 200));
				break;
			case 1:
				game.enterState(Game.options, new FadeOutTransition(Color.black, 200), new FadeInTransition(Color.black, 200));
				break;
			case 2:
				game.enterState(Game.tutorial, new FadeOutTransition(Color.black, 200), new FadeInTransition(Color.black, 200));
				break;
			case 3:
				container.exit();
				break;
			default:
				break;
		}
	}

	@Override
	public int getID(){

		return Game.menu;
	}
}
