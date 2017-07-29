package objects;

import java.util.Random;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;

public class Item{

	protected Shape rect;
	protected Player player;
	private Image icon;
	private int cooldown; // respawn countdown
	private int despawn; // despawn countdown
	private int active; // active countdown
	private int respawnVariation; // variation in respawn time
	private int respawnMax; // maxium time for respawning
	private int despawnVariation; // variation in despawn time
	private int despawnMax; // maxium time for despawning
	private int activeVariation; // variation in despawn time
	private int activeMax; // maxium time for despawning
	private boolean taken, hasDuration;
	protected StateBasedGame game;
	protected GameContainer container;
	protected Random ran; // random object

	public Item(int[] specs, String img, Player player, StateBasedGame game, GameContainer gc) throws SlickException{

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

		// convert ms to seconds
		respawnVariation = specs[0] * 1000;
		respawnMax = specs[1] * 1000;
		despawnVariation = specs[2] * 1000;
		despawnMax = specs[3] * 1000;
		activeVariation = specs[4] * 1000;
		activeMax = specs[5] * 1000;

		// make sure duration time doesn't happen item without a duration
		hasDuration = (specs[5] < 1) ? false : true;

		// rectangle outside of screen, made to size
		rect = new Rectangle(-specs[8], -specs[8], specs[8], specs[8]);
		icon = new Image(img);

		this.player = player;
		this.game = game;
		this.container = gc;
		ran = new Random();
		taken = true;

		cooldown = randomizeDuration(specs[7] * 1000, specs[6] * 1000);
	}

	public void update(int delta){

		// tick timers
		if(cooldown > 0 && taken)
			cooldown -= delta;

		if(despawn > 0 && !taken)
			despawn -= delta;

		if(active > 0)
			active -= delta;

		// duration ended
		if(active < 1)
			endEffect();

		// item respawned
		if(cooldown < 1 && taken){
			taken = false;
			reset();
		}

		// item despawned
		if(despawn < 1 && !taken){
			taken = true;
			startCooldown();
		}

		// when player picks up item
		if(rect.intersects(player.rect) && !taken){
			taken = true;
			startCooldown();
			onPickup();

			if(hasDuration){
				active = randomizeDuration(activeMax, activeVariation);
				beginEffect();
			}
		}
	}

	public void render(Graphics g){

		// if the Item is in play
		if(!taken)
			g.drawImage(icon, rect.getX(), rect.getY(), rect.getMaxX(), rect.getMaxY(), 0, 0, icon.getWidth(), icon.getHeight());
	}

	public void reset(){

		// set despawn with random on variation and fixed max
		despawn = randomizeDuration(despawnMax, despawnVariation);

		cooldown = -1;
		int x = ran.nextInt(container.getWidth() - ((int)rect.getHeight()));
		int y = ran.nextInt(container.getHeight() - ((int)rect.getHeight()));
		rect.setLocation(x, y);
	}

	public void startCooldown(){

		cooldown = randomizeDuration(respawnMax, respawnVariation);
	}

	public int randomizeDuration(int max, int variation){

		if(max < 1)
			return 0;

		if(variation < 1)
			variation = 1;

		return (ran.nextInt(variation) + (max - variation));
	}

	public void beginEffect(){

	}

	public void endEffect(){

	}

	public void onPickup(){

	}
}
