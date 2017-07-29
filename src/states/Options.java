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

public class Options extends BasicGameState{

	private Button[] buttons;
	private Image bg;
	private int padding, width, height;
	private int max = 121, min = 60;
	private int increment = 10;
	private int displaymode;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException{

		buttons = new Button[4];

		padding = container.getHeight() / 45;
		width = container.getHeight() / 5;
		height = container.getHeight() / 18;


		bg = new Image("res/images/options.png");

		Shape b1 = new Rectangle(padding, padding, width, height);
		Shape b2 = new Rectangle(padding * 2 + width, padding, width / 4, height);
		Shape b3 = new Rectangle(padding * 3 + width / 4 + width, padding, width, height);
		Shape b4 = new Rectangle(padding * 4 + width / 4 + width * 2, padding, width / 4, height);

		buttons[0] = new Button("res/images/empty.png", b1, "Back");
		buttons[1] = new Button("res/images/up.png", b2, "");
		buttons[2] = new Button("res/images/empty.png", b3, "");
		buttons[3] = new Button("res/images/down.png", b4, "");

		displaymode = Integer.valueOf(Game.fm.readConfig("displaymode"));
	}


	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException{

		Input input = container.getInput();
		int x = Mouse.getX();
		int y = container.getHeight() - Mouse.getY();

		for (int i = 0; i < buttons.length; i++){

			if(i != 2){
				buttons[i].update(x, y, input.isMouseButtonDown(0));
			}

			if(buttons[i].state){
				buttons[i].state = false;
				handleEvent(i, container, game);
			}
		}

		String size = displaymode * 16 + " x " + displaymode * 9;
		buttons[2].setName(size);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException{

		g.drawImage(bg, 0, 0, container.getWidth(), container.getHeight(), 0, 0, bg.getWidth(), bg.getHeight());

		for (Button button : buttons){
			button.render(g, Game.heading);
		}
	}

	private void handleEvent(int event, GameContainer container, StateBasedGame game) throws SlickException{

		switch (event) {
			case 0:
				game.initStatesList(container);
				game.enterState(Game.menu, new FadeOutTransition(Color.black, 200), new FadeInTransition(Color.black, 200));
				break;
			case 1:
				if(displaymode + increment < max)
					displaymode += increment;
				Game.changeScreenSize(displaymode);
				Game.fm.setConfig("displaymode", Integer.toString(displaymode));
				game.getState(Game.options).init(container, game);
				Game.updateFonts();

				break;
			case 3:
				if(displaymode - increment > min)
					displaymode -= increment;
				Game.changeScreenSize(displaymode);
				Game.fm.setConfig("displaymode", Integer.toString(displaymode));
				game.getState(Game.options).init(container, game);
				Game.updateFonts();
				break;
			default:
				break;
		}
	}

	@Override
	public int getID(){

		return Game.options;
	}

}
