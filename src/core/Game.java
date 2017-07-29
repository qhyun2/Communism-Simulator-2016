package core;

import java.awt.Font;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.StateBasedGame;
import gui.Interface;
import states.BlackScreen;
import states.Gameover;
import states.Menu;
import states.Options;
import states.Play;
import states.Splash;
import states.Tutorial;

public class Game extends StateBasedGame{

	// static vars
	public static final String gameName = "Communism Simulator 2016";
	public static final String version = "4.3.5";
	public static final int fps = 119;
	public static final boolean debugMode = false;
	public static final int blackScreen = 0, splash = 1, menu = 2, play = 3, options = 4, gameover = 5, tutorial = 6;

	// game core
	public static FileManager fm;
	public static SoundManager sm;
	public static Interface gui;

	public static TrueTypeFont heading, text, title;

	// game container
	public static AppGameContainer appgc;

	public Game(String gameName) throws SlickException{

		super(gameName);
		this.addState(new BlackScreen());
		this.addState(new Splash());
		this.addState(new Menu());
		this.addState(new Play());
		this.addState(new Options());
		this.addState(new Gameover());
		this.addState(new Tutorial());

		fm = new FileManager();
		sm = new SoundManager();
		gui = new Interface();
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException{

		// initalize states
		this.getState(blackScreen).init(container, this);
		this.getState(splash).init(container, this);
		this.getState(menu).init(container, this);
		this.getState(play).init(container, this);
		this.getState(options).init(container, this);
		this.getState(gameover).init(container, this);
		this.getState(tutorial).init(container, this);

		// fonts
		updateFonts();

		// starting screen
		if(debugMode){
			this.enterState(play);
		}
		else{
			this.enterState(blackScreen);
		}
	}

	public static void changeScreenSize(int size) throws SlickException{

		if(size > 119){
			appgc.setDisplayMode(size * 16, size * 9, true);
		}
		else{
			appgc.setDisplayMode(size * 16, size * 9, false);
		}
	}

	public static void updateFonts(){

		boolean antiali = true;

		// fonts
		Font javaFont = new Font("Verdana", Font.BOLD, (int)(appgc.getWidth() / 80));
		heading = new TrueTypeFont(javaFont, antiali);

		javaFont = new Font("Verdana", Font.PLAIN, (int)(appgc.getWidth() / 90));
		text = new TrueTypeFont(javaFont, antiali);

		javaFont = new Font("Verdana", Font.BOLD, (int)(appgc.getWidth() / 60));
		title = new TrueTypeFont(javaFont, antiali);

	}

	public static void main(String[] args){

		try{
			appgc = new AppGameContainer(new Game(gameName + " " + version));
			changeScreenSize(Integer.valueOf(fm.readConfig("displaymode")));
			appgc.setDefaultFont(text);
			appgc.setShowFPS(debugMode);
			appgc.setTargetFrameRate(fps);
			appgc.setIcon("res/images/icon.png");
			appgc.start();

		}catch (SlickException e){
			e.printStackTrace();
		}
	}
}

/*
 * TO DO LIST
 * TODO missile rain on player death
 * TODO add sound options
 * TODO Animations - Death, Trump.
 * TODO Trumps Wall.
 * TODO make and use cooldown object
 * TODO Boss Battle - Make up idea
 * TODO Recent damage indicator (like in league)
 * TODO Add different difficulties
 * TODO Add fullscreen, windowed and fullscreen windowed mode
 */
