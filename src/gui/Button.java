package gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Shape;

public class Button{

	private Shape rect;
	public boolean state, hover;
	private String name;
	private Image icon;
	int offset = -3;

	public Button(String image, Shape rect, String name) throws SlickException{
		icon = new Image(image);
		this.rect = rect;
		this.name = name;
	}

	public void update(int x, int y, boolean click){

		if(rect.contains(x, y)){
			hover = true;
			state = click;
		}
		else{
			state = false;
			hover = false;
		}
	}

	public void render(Graphics g, TrueTypeFont ttf){

		g.setFont(ttf);
		g.setColor(Color.black);

		float x = rect.getCenterX() - ttf.getWidth(name) / 2;
		float y = rect.getCenterY() - ttf.getHeight(name) / 2;

		if(hover){
			g.drawImage(icon, rect.getX() - offset, rect.getY() - offset, rect.getMaxX() - offset, rect.getMaxY() - offset, 0, 0, icon.getWidth(), icon.getHeight());
			g.drawString(name, x - offset, y - offset);
		}
		else{
			g.drawImage(icon, rect.getX(), rect.getY(), rect.getMaxX(), rect.getMaxY(), 0, 0, icon.getWidth(), icon.getHeight());
			g.drawString(name, x, y);
		}
	}

	public void setName(String name){

		this.name = name;
	}

}
