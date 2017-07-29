package objects;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Negev extends Item{

	private Ammo ammo;

	public Negev(int[] specs, String img, Player player, StateBasedGame game, GameContainer gc, Ammo ammo) throws SlickException{
		super(specs, img, player, game, gc);
		this.ammo = ammo;
	}

	@Override
	public void onPickup(){

		ammo.ammo += ammo.ammoPerMagazine * 1.6;
	}

	@Override
	public void beginEffect(){

		ammo.negevMode = true;
	}

	@Override
	public void endEffect(){

		ammo.negevMode = false;
	}
}
