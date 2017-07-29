package objects;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;
import core.Game;

public class Player{

	public int direction, score = 0;
	private int diagonalCooldown = 0, defaultDiagonalCooldown = 150, internalDirection = 8, size;
	public int health = 40, maxHealth;
	public double speed;
	public Shape rect;
	private Image[] icon;

	public void init(GameContainer container, StateBasedGame game, double speed, int size) throws SlickException{

		this.speed = speed;
		this.size = size;

		// rect at middle
		rect = new Rectangle(container.getWidth() / 2 - size / 2, container.getHeight() / 2 - size / 2, size, size);

		// import images
		icon = new Image[5];
		for (int i = 0; i < 4; i++){
			icon[i] = new Image("res/images/lead" + (i + 1) + ".png");
		}
		icon[4] = new Image("res/images/lead3.png");

		maxHealth = health;
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException{

		// character image
		g.drawImage(icon[direction], rect.getX(), rect.getY(), rect.getMaxX(), rect.getMaxY(), 0, 0, 100, 100);

		// health bar
		Game.gui.healthBar(g, rect, size, health, maxHealth);
	}

	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException{

		// tick cooldowns
		if(diagonalCooldown > 0)
			diagonalCooldown -= delta;

		// mapping input to directions
		Input input = container.getInput();

		// get immediate values
		boolean up = input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP);
		boolean right = input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT);
		boolean down = input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_DOWN);
		boolean left = input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT);

		// map to 8 directions
		internalDirection = (up) ? 0 : internalDirection;
		internalDirection = (down) ? 4 : internalDirection;
		internalDirection = (right) ? 2 : internalDirection;
		internalDirection = (left) ? 6 : internalDirection;

		internalDirection = (up && right) ? 1 : internalDirection;
		internalDirection = (down && left) ? 5 : internalDirection;
		internalDirection = (right && down) ? 3 : internalDirection;
		internalDirection = (left && up) ? 7 : internalDirection;

		// standing still
		internalDirection = (input.isKeyDown(Input.KEY_SPACE)) ? 8 : internalDirection;

		// regular directions
		switch (internalDirection) {
			case 0:
				direction = 0;
				break;
			case 2:
				direction = 1;
				break;
			case 4:
				direction = 2;
				break;
			case 6:
				direction = 3;
				break;
			case 8:
				direction = 4;
				break;
			default:
				break;
		}

		// diagonals
		if(diagonalCooldown < 1){
			switch (internalDirection) {
				case 1:
					direction = (direction == 1) ? 0 : 1;
					break;
				case 3:
					direction = (direction == 2) ? 1 : 2;
					break;
				case 5:
					direction = (direction == 3) ? 2 : 3;
					break;
				case 7:
					direction = (direction == 0) ? 3 : 0;
					break;
				default:
					break;
			}
			diagonalCooldown = defaultDiagonalCooldown;
		}

		// movment
		switch (direction) {
			case 0:
				rect.setCenterY((float)(rect.getCenterY() - speed * delta));
				break;
			case 1:
				rect.setCenterX((float)(rect.getCenterX() + speed * delta));
				break;
			case 2:
				rect.setCenterY((float)(rect.getCenterY() + speed * delta));
				break;
			case 3:
				rect.setCenterX((float)(rect.getCenterX() - speed * delta));
				break;
			default:
				break;
		}

		// area player has to be in
		Shape screen = new Rectangle(size, size, container.getWidth() - size * 2, container.getHeight() - size * 2);

		if(!screen.intersects(rect))
			undo(delta);
	}

	private void undo(int delta){

		switch (direction) {
			case 0:
				rect.setCenterY((float)(rect.getCenterY() + speed * delta));
				break;
			case 1:
				rect.setCenterX((float)(rect.getCenterX() - speed * delta));
				break;
			case 2:
				rect.setCenterY((float)(rect.getCenterY() - speed * delta));
				break;
			case 3:
				rect.setCenterX((float)(rect.getCenterX() + speed * delta));
				break;
			default:
				break;
		}
	}
}
