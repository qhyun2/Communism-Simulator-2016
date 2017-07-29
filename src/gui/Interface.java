package gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Shape;

public class Interface{

	public void drawText(Graphics g, TrueTypeFont ttf, Color fore, Color back, String text, int x, int y, int offset){

		int drawX = x - (ttf.getWidth(text) / 2);
		int drawY = y - (ttf.getHeight(text) / 2);

		g.setFont(ttf);
		g.setColor(back);
		g.drawString(text, drawX - offset, drawY - offset);
		g.setColor(fore);
		g.drawString(text, drawX, drawY);
	}

	public void healthBar(Graphics g, Shape rect, int size, double health, double baseHealth){

		g.setColor(Color.green);
		g.fillRect(rect.getX(), rect.getMaxY() + 5, size, size / 6);
		g.setColor(Color.red);
		g.fillRect(rect.getX(), rect.getMaxY() + 5, (float)(size - health * size / baseHealth), size / 6);
	}
}
