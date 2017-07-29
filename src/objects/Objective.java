package objects;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Objective extends Item{

	public Objective(int[] specs, String img, Player player, StateBasedGame game, GameContainer gc) throws SlickException{
		super(specs, img, player, game, gc);
	}

	@Override
	public void onPickup(){

		player.score++;
	}
}
