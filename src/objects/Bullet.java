package objects;

import java.util.LinkedList;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;


public class Bullet{

	private Vector2f position, velocity;
	private float speed = 0.08f;
	boolean alive = true, hit = false;
	private Image hole;
	private int life;

	public Bullet(Vector2f position, Vector2f velocity, int life) throws SlickException{

		this.position = position;
		this.velocity = velocity;
		hole = new Image("res/images/hole.png");
		this.life = life;
	}

	public void render(Graphics g){

		// draw on screen
		if(hit){
			g.drawImage(hole, position.getX() - 5, position.getY() - 5, position.getX() + 5, position.getY() + 5, 0, 0, hole.getWidth(), hole.getHeight());
		}
		else{
			int size = 5;
			g.setColor(Color.gray);
			g.fillOval(position.getX() - size / 2, position.getY() - size / 2, size, size);
		}
	}

	public void update(GameContainer container, StateBasedGame game, int delta, LinkedList<Enemy> e){

		if(!hit){
			position.add(velocity.copy().scale(speed * delta));
		}
		
		if(life > 0){
			life -= delta;
		}else{
			alive = false;
		}

		if(!new Rectangle(0, 0, container.getWidth(), container.getHeight()).contains(position.x, position.y)){
			alive = false;
		}

		for (Enemy enemy : e){
			if(enemy.rect.contains(position.getX(), position.getY()) && alive && !hit){
				hit = true;
				enemy.damage(2);
			}
		}
	}
}
