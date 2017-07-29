package objects;

import java.util.LinkedList;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import core.Game;


public class Vodka extends Item{

	private LinkedList<Enemy> enemys;
	private Player player;
	private int healthOnPickup = 5;

	public Vodka(int[] specs, String img, Player player, StateBasedGame game, GameContainer gc, LinkedList<Enemy> enemys) throws SlickException{
		super(specs, img, player, game, gc);
		this.enemys = enemys;
		this.player = player;

	}

	@Override
	public void onPickup(){

		Game.sm.slurp.play(1, 0.4f);
		player.health += healthOnPickup;
		if(player.health > player.maxHealth){
			player.health = player.maxHealth;
		}
	}

	@Override

	public void beginEffect(){

		for (Enemy i : enemys){
			i.enemySpeedModifier = (float)-(i.enemyBaseSpeed * 0.45);
		}
	}

	@Override
	public void endEffect(){

		for (Enemy i : enemys){
			i.enemySpeedModifier = 0f;
		}
	}
}
