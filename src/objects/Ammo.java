package objects;

import java.util.Iterator;
import java.util.LinkedList;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import core.Game;

public class Ammo extends Item{

	private Image magazine;
	public int ammo, reload;
	private int padding, size, sHeight, mWidth, mHeight;
	private LinkedList<Bullet> b;
	private int reloadSpeed = 100, negevReloadSpeed = 25;
	public int ammoPerMagazine = 50;
	private LinkedList<Enemy> enemy;
	public boolean negevMode = false;

	public Ammo(int[] specs, String img, Player player, StateBasedGame game, GameContainer gc, LinkedList<Enemy> enemy) throws SlickException{

		super(specs, img, player, game, gc);

		magazine = new Image("res/images/magazine.png");

		padding = specs[8] / 4;
		size = specs[8] * 2;
		sHeight = gc.getHeight();
		mWidth = magazine.getWidth();
		mHeight = magazine.getHeight();

		b = new LinkedList<Bullet>();

		this.enemy = enemy;

		// debugmode starts with 300 ammo
		ammo = (Game.debugMode) ? 150 : 0;

	}

	@Override
	public void render(Graphics g){

		ammoDisplay(g);

		for (Bullet b : b){
			b.render(g);
		}

		super.render(g);
	}

	@Override
	public void onPickup(){

		Game.sm.reloadsound.play(1, 0.4f);
		ammo += ammoPerMagazine;
	};

	@Override
	public void update(int delta){

		// delay between shots
		if(reload > 0)
			reload -= delta;

		// update or remove bullets
		Iterator<Bullet> i = b.iterator();

		while (i.hasNext()){
			Bullet x = i.next();
			x.update(container, game, delta, enemy);
			if(!x.alive){
				i.remove();
			}
		}

		// able to shoot
		if(ammo > 0 && reload < 1 && (negevMode || container.getInput().isMouseButtonDown(0))){
			shoot();
		}
		super.update(delta);
	}

	private void shoot(){

		ammo--;

		// x and y distances from target
		float xdif = (container.getInput().getMouseX() - player.rect.getCenterX());
		float ydif = (container.getInput().getMouseY() - player.rect.getCenterY());

		// actual distance
		double distance = (Math.sqrt(xdif * xdif + ydif * ydif));

		// speed based of distance
		float speed = (float)(distance / 5000);

		// x and y velocitys based on speed and distance
		float xvel = (xdif / speed) / 500;
		float yvel = (ydif / speed) / 500;

		// add spread with more spread when not standing still
		float bulletSpread = 0.1f;
		if(player.direction != 4){
			if(ran.nextBoolean()){
				xvel = (float)(xvel + ((ran.nextInt(15)) * bulletSpread));
			}
			else{
				xvel = (float)(xvel - ((ran.nextInt(15)) * bulletSpread));
			}
			if(ran.nextBoolean()){
				yvel = (float)(yvel + ((ran.nextInt(15)) * bulletSpread));
			}
			else{
				yvel = (float)(yvel - ((ran.nextInt(15)) * bulletSpread));
			}
		}
		else{
			if(ran.nextBoolean()){
				xvel = (float)(xvel + ((ran.nextInt(5)) * bulletSpread));
			}
			else{
				xvel = (float)(xvel - ((ran.nextInt(5)) * bulletSpread));
			}
			if(ran.nextBoolean()){
				yvel = (float)(yvel + ((ran.nextInt(5)) * bulletSpread));
			}
			else{
				yvel = (float)(yvel - ((ran.nextInt(5)) * bulletSpread));
			}
		}

		Vector2f location = new Vector2f(player.rect.getCenterX(), player.rect.getCenterY());
		Vector2f velocity = new Vector2f(xvel, yvel);

		try{
			b.add(new Bullet(location, velocity, 12000));
		}catch (SlickException e){
			e.printStackTrace();
		}

		Game.sm.shot.play(1, 0.05f);

		// decreased reloading time if shooting is in negev mode
		reload = (negevMode) ? negevReloadSpeed : reloadSpeed;
	}

	// still not too sure how it all works

	public void renderMag(Graphics g, int term){

		int x = padding;
		int y = sHeight - (padding + size) * term;
		int x2 = padding + size;
		int y2 = sHeight - ((padding * term) + (size * (term - 1)));

		g.drawImage(magazine, x, y, x2, y2, 0, 0, mWidth, mHeight);
	}

	public void renderMagAmount(Graphics g, int term){

		float displayAmount = (ammo % ammoPerMagazine) * size / ammoPerMagazine;
		float sourceAmount = (ammo % ammoPerMagazine) * mHeight / ammoPerMagazine;

		float x = padding;
		float y = (sHeight - (padding + size) * (term - 1) - padding) - displayAmount;
		float x2 = padding + size;
		float y2 = sHeight - ((padding * term) + (size * (term - 1)));

		g.drawImage(magazine, x, y, x2, y2, 0, mHeight - sourceAmount, mWidth, mHeight);
	}

	public void ammoDisplay(Graphics g){

		// finds the number of magazines that need to be drawn
		int amount = ammo / ammoPerMagazine;
		renderMagAmount(g, amount + 1);

		// draw them + 1
		for (int i = 1; i < amount + 1; i++){
			renderMag(g, i);
		}
	}
}
