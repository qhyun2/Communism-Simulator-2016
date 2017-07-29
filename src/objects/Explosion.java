package objects;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Explosion{

	private SpriteSheet explosionSprite;
	private Animation explosion;
	private int x, y;
	private int size = 100;
	public int life;
	private int speed = 20; // time between each frame in ms

	public Explosion() throws SlickException{
		explosionSprite = new SpriteSheet("res/images/explosion.png", size, size);
		explosion = new Animation(explosionSprite, speed);
	}

	public void update(int delta){

		explosion.update(delta);
		if(life > 0){
			life -= delta;
		}
		else{
			explosion.stop();
		}
	}

	public void render(){

		if(life > 0){
			explosion.draw(x, y, size, size);
		}
	}

	public void activate(int x, int y){

		life = (74 * speed) / 2;
		this.x = x - (size / 2);
		this.y = y - (size / 2);
		explosion.restart();
		explosion.start();
	}
}
