package states;

import java.awt.Font;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import core.Game;

public class Gameover extends BasicGameState{

	private TrueTypeFont ttf;
	private int offset = 2;
	private Image bg;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException{

		Font font = new Font("Verdana", Font.BOLD, 30);
		ttf = new TrueTypeFont(font, true);
		bg = new Image("res/images/gameover.png");
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException{

		g.drawImage(bg, 0, 0, container.getWidth(), container.getHeight(), 0, 0, bg.getWidth(), bg.getHeight());

		String score = "Score: " + Game.fm.readConfig("lastscore");
		String highscore = "Highscore: " + Game.fm.readConfig("highscore");
		String message = "Press Escape to Continue";

		Game.gui.drawText(g, ttf, Color.red, Color.black, score, container.getWidth() / 2, container.getHeight() * 17 / 24, offset);
		Game.gui.drawText(g, ttf, Color.red, Color.black, highscore, container.getWidth() / 2, container.getHeight() * 18 / 24, offset);
		Game.gui.drawText(g, ttf, Color.red, Color.black, message, container.getWidth() / 2, container.getHeight() * 19 / 24, offset);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException{

		if(container.getInput().isKeyPressed(Input.KEY_ESCAPE)){
			game.getState(Game.play).init(container, game);
			game.enterState(Game.menu, new FadeOutTransition(Color.black, 200), new FadeInTransition(Color.black, 200));
		}
	}

	@Override
	public int getID(){

		return Game.gameover;
	}

}
