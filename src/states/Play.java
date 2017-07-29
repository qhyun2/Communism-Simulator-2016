package states;

import java.util.LinkedList;
import java.util.Random;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import core.Game;
import objects.Ammo;
import objects.Enemy;
import objects.Item;
import objects.Negev;
import objects.Objective;
import objects.Player;
import objects.Vodka;

public class Play extends BasicGameState{

	private Player player;
	private LinkedList<Enemy> enemy;
	private Item items[];
	private Image bg;
	private int scale = 20;
	private int itemSize;
	private Random ran;
	private float enemySpeed = 0.30f;

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException{

		game.getState(Game.gameover).init(container, game);

		bg = new Image("res/images/background.png");
		itemSize = container.getHeight() / scale;
		player = new Player();
		ran = new Random();

		enemy = new LinkedList<Enemy>();

		player.init(container, game, 0.40, itemSize);

		// enemy starting locations
		int[] x = {-itemSize, -itemSize, container.getWidth() + itemSize, container.getWidth() + itemSize};
		int[] y = {-itemSize, container.getHeight() + itemSize, itemSize, container.getHeight() + itemSize};

		for (int i = 0; i < 4; i++){
			enemy.add(new Enemy());
			enemy.get(i).init(enemySpeed, player, x[i], y[i], itemSize);
			enemy.get(i).others = enemy;
		}

		// item template int array arguments
		// 0 - respawn variation
		// 1 - respawn max
		// 2 - despawn variation
		// 3 - despawn max
		// 4 - effect duration variation
		// 5 - effect duration max
		// 6 - initial spawn time variation
		// 7 - initial spawn time max
		// 8 - item size

		// objective
		int[] objectiveSpec = {0, 0, 0, 20, -1, -1, 0, 0, itemSize};
		Objective objective = new Objective(objectiveSpec, "res/images/objective.png", player, game, container);

		// vodka
		int[] vodkaSpec = {2, 8, 3, 13, 3, 6, 2, 5, itemSize};
		Vodka vodka = new Vodka(vodkaSpec, "res/images/powerup.png", player, game, container, enemy);

		// ammo
		int[] ammoSpec = {6, 15, 5, 15, -1, -1, 2, 7, itemSize};
		Ammo ammo = new Ammo(ammoSpec, "res/images/ammo.png", player, game, container, enemy);

		// negev
		int[] negevSpec = {3, 22, 2, 17, 0, 2, 4, 15, itemSize};
		Negev negev = new Negev(negevSpec, "res/images/negev.png", player, game, container, ammo);

		// create array and assign value
		Item[] temp2 = {objective, vodka, ammo, negev};
		items = temp2;
	}

	@SuppressWarnings("unused")
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException{

		player.update(container, game, delta);

		for (Enemy e : enemy){
			e.update(container, game, delta);
		}

		for (Item i : items){
			i.update(delta);
		}

		if(player.health <= 0 && !Game.debugMode){
			endGame(container, game, delta);
		}

		if(container.getInput().isKeyDown(Input.KEY_Z) && Game.debugMode){
			endGame(container, game, delta);
		}

		int score[] = {40, 80, 160, 200};
		int enemies[] = {6, 7, 8, 9};

		for (int i = 0; i < score.length; i++){
			if(player.score >= score[i] && enemy.size() < enemies[i]){
				addObama();
			}
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException{

		g.drawImage(bg, 0, 0, container.getWidth(), container.getHeight(), 0, 0, bg.getWidth(), bg.getHeight());

		player.render(container, game, g);

		for (Item i : items){
			i.render(g);
		}

		for (Enemy e : enemy){
			e.render(container, game, g);
		}


		if(player.score > 0){
			String display = "Score: " + player.score;
			int x = container.getWidth() - (Game.title.getWidth(display) / 2) - scale;
			int y = Game.title.getHeight(display) / 2;
			Game.gui.drawText(g, Game.title, Color.yellow, Color.black, display, x, y, scale / 10);
		}
	}

	public void endGame(GameContainer container, StateBasedGame game, int delta){

		if(Integer.parseInt(Game.fm.readConfig("highscore")) < player.score){
			Game.fm.setConfig("highscore", Integer.toString(player.score));
		}
		Game.fm.setConfig("lastscore", Integer.toString(player.score));
		game.enterState(Game.gameover, new FadeOutTransition(Color.black, 200), new FadeInTransition(Color.black, 200));
	}

	public void addObama() throws SlickException{

		// values for random respawn
		int minimum = 1000;
		int variation = 200;
		int x, y;

		// equal chance to be on either side of screen
		if(ran.nextBoolean()){
			x = (minimum + ran.nextInt(variation));
		}
		else{
			x = -(minimum + ran.nextInt(variation));
		}
		if(ran.nextBoolean()){
			y = (minimum + ran.nextInt(variation));
		}
		else{
			y = -(minimum + ran.nextInt(variation));
		}

		enemy.add(new Enemy());
		enemy.getLast().init(enemySpeed, player, x, y, itemSize);
		enemy.getLast().others = enemy;
	}

	public Play getPlay(){

		return this;
	}


	@Override
	public int getID(){

		return Game.play;
	}
}
