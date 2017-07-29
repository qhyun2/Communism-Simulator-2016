package objects;

import java.util.LinkedList;
import java.util.Random;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;
import core.Game;


public class Enemy{

	public Shape rect; // represents enemy on gameboard
	public double enemyBaseSpeed, enemyActualSpeed, enemySpeedModifier = 0; // speed
	private int enemyDirectionChangeCooldown, enemyDefaultDirectionChangeCooldown = 200, enemyDir = 4; // ai cooldown
	private double health = 30, baseHealth, maxhealth = 200, increaseOnDeath = 5, regenHealthPerMS = 0.0003f; // health
	private double lowHealthSpeedReduction = 0.3f;
	private int damageToBeTaken = 0, damageToBeTakenCooldown, defaultDamageToBeTakenCooldown = 50;
	private int damageToPlayer = 5, defaultDamageToPlayerCoolDown = 100, damageToPlayerCoolDown; // player damage cooldown
	private int damageSoundCooldown, defaultDamageSoundCooldown = 500; // enemy damage sound cooldown
	private int size;
	private Image[] icon_array;
	private Player player;
	private Random ran;
	private Explosion explosion;
	public LinkedList<Enemy> others;

	public void init(float speed, Player p, int x, int y, int size) throws SlickException{

		enemyBaseSpeed = speed;
		baseHealth = health;
		damageToPlayerCoolDown = defaultDamageToPlayerCoolDown;
		enemyDirectionChangeCooldown = enemyDefaultDirectionChangeCooldown;
		damageToBeTakenCooldown = defaultDamageToBeTakenCooldown;

		player = p;
		this.size = size;
		rect = new Rectangle(x, y, size, size);
		Image[] temp = {new Image("res/images/enemy3.png"), new Image("res/images/enemy2.png"), new Image("res/images/enemy1.png"), new Image("res/images/enemy4.png")};
		icon_array = temp;
		ran = new Random();
		explosion = new Explosion();
	}

	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException{

		// damage player on contact
		if(rect.intersects(player.rect) && damageToPlayerCoolDown < 1){
			player.health -= damageToPlayer;
			Game.sm.hit.play(1f, 5f);
			damageToPlayerCoolDown = defaultDamageToPlayerCoolDown;
		}

		// speed
		// calculate actual speed
		double speedModifier = 1 - health / baseHealth;
		double speedHealthPenalty = speedModifier * enemyBaseSpeed * lowHealthSpeedReduction;
		double scoreSpeedModifier = 0.00005f * (float)((player.score * player.score));

		enemyActualSpeed = enemyBaseSpeed - speedHealthPenalty + scoreSpeedModifier;

		if(enemyActualSpeed > player.speed * 0.85f)
			enemyActualSpeed = player.speed * 0.85f;

		enemyActualSpeed += enemySpeedModifier;

		// tick cooldowns
		if(damageToPlayerCoolDown > 0)
			damageToPlayerCoolDown -= delta;

		if(enemyDirectionChangeCooldown > 0)
			enemyDirectionChangeCooldown -= delta;

		if(damageSoundCooldown > 0)
			damageSoundCooldown -= delta;

		if(damageToBeTakenCooldown > 0)
			damageToBeTakenCooldown -= delta;


		if(damageToBeTakenCooldown < 1 && damageToBeTaken > 0){
			damageToBeTakenCooldown = defaultDamageToBeTakenCooldown;
			health--;
			damageToBeTaken--;
		}

		// regen health
		if(health < baseHealth){
			health += regenHealthPerMS * delta;
		}


		// explosion animation update
		explosion.update(delta);

		// debug kill enemy
		if(Game.debugMode && container.getInput().isKeyDown(Input.KEY_M) && rect.contains(Mouse.getX(), container.getHeight() - Mouse.getY()))
			killed();
		
		// no health kill
		if(health < 0)
			killed();

		// find direction
		calculateDirection();

		// move enemy
		move(delta);

		// avoid others
		avoid(delta);
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException{

		// head
		g.drawImage(icon_array[enemyDir - 1], rect.getX(), rect.getY(), rect.getMaxX(), rect.getMaxY(), 0, 0, icon_array[enemyDir - 1].getWidth(), icon_array[enemyDir - 1].getHeight());

		// health bar
		Game.gui.healthBar(g, rect, size, health, baseHealth);

		// explosion
		explosion.render();
	}

	public void increaseSpeed(double increase){

		// make sure enemies aren't faster than player
		if(enemyBaseSpeed + increase < player.speed * 0.90)
			enemyBaseSpeed += increase;
	}

	public void move(int delta){

		// move enemy based on direction and delta
		float xChange = 0;
		float yChange = 0;

		// determine movement
		switch (enemyDir) {
			case 1:
				yChange = (float)(enemyActualSpeed * delta);
				break;
			case 2:
				xChange = (float)(enemyActualSpeed * delta);
				break;
			case 3:
				yChange = (float)(-enemyActualSpeed * delta);
				break;
			case 4:
				xChange = (float)(-enemyActualSpeed * delta);
				break;
			default:
				break;
		}

		// add on movement
		rect.setLocation(rect.getX() + xChange, rect.getY() + yChange);
	}

	public void avoid(int delta){

		// check all enemies
		for (Enemy i : others){

			// if rectangles intersect and the rectangle is not our own then undo the last move
			if(i.rect != this.rect && this.rect.intersects(i.rect)){
				// move enemy based on direction and delta
				float xChange = 0;
				float yChange = 0;

				// determine movement
				switch (enemyDir) {
					case 1:
						yChange = (float)(enemyActualSpeed * delta);
						break;
					case 2:
						xChange = (float)(enemyActualSpeed * delta);
						break;
					case 3:
						yChange = (float)(-enemyActualSpeed * delta);
						break;
					case 4:
						xChange = (float)(-enemyActualSpeed * delta);
						break;
					default:
						break;
				}

				// remove movement
				rect.setLocation(rect.getX() - xChange, rect.getY() - yChange);
			}
		}
	}

	public void calculateDirection(){

		// when a direction change is due
		if(enemyDirectionChangeCooldown < 1){

			// distances in relative to x and y axis
			int xdiff = Math.abs((int)(rect.getMaxX() - player.rect.getMaxX()));
			int ydiff = Math.abs((int)(rect.getMaxY() - player.rect.getMaxY()));

			int dirVarModifier = 0;
			// set modifiers based on whether player is farther x or y
			if(xdiff > ydiff){
				dirVarModifier = 3;
			}
			if(xdiff < ydiff){
				dirVarModifier = 7;
			}

			// direction decider
			int dirVar = ran.nextInt(10) + 1;

			// move on x
			if(dirVar > dirVarModifier){
				if(rect.getMaxX() > player.rect.getMaxX()){
					enemyDir = 4;
				}
				if(rect.getMaxX() < player.rect.getMaxX()){
					enemyDir = 2;
				}
			}

			// move on y
			if(dirVar < dirVarModifier){
				if(rect.getMaxY() > player.rect.getMaxY()){
					enemyDir = 3;
				}
				if(rect.getMaxY() < player.rect.getMaxY()){
					enemyDir = 1;
				}
			}
			// reset cooldown
			enemyDirectionChangeCooldown = enemyDefaultDirectionChangeCooldown;
		}
	}

	public void damage(int damage){

		health -= damage;

		if(damageSoundCooldown < 1){

			// random damage sound
			if(ran.nextBoolean()){
				Game.sm.ouch.play(1f, 0.35f);
			}
			else{
				Game.sm.hit.play(1f, 0.35f);
			}

			// reset cooldown
			damageSoundCooldown = defaultDamageSoundCooldown;
		}
	}

	private void killed(){

		// increase health
		if(baseHealth + increaseOnDeath < maxhealth){
			baseHealth += increaseOnDeath;
		}

		// reset health
		health = baseHealth;

		// set up explosion where death was
		explosion.activate((int)(rect.getCenterX()), (int)(rect.getCenterY()));

		// damage surrounding enemies
		for (Enemy i : others){

			// make sure current element is not it self
			if(this != i){
				// use line to find distance
				Line distance = new Line(i.rect.getCenterX(), i.rect.getCenterY(), rect.getCenterX(), rect.getCenterY());
				int dis = (int)distance.length();
				if(dis < 30){
					i.damage(8);
				}
				else if(dis < 80){
					i.damage(4);
				}
				else if(dis < 120){
					i.damage(2);
				}
			}
		}

		// values for random respawn
		int minimum = 2000;
		int variation = 1000;
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

		// respawn
		rect.setLocation(x, y);
		Game.sm.dead[ran.nextInt(4)].play(1f, 0.9f);
		player.score += 5;
	}
}
