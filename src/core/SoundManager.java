package core;

import java.util.Random;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;


public class SoundManager{

	private Music theme;
	public Sound reloadsound, shot, slurp, ouch, hit;
	public Sound[] dead;

	public SoundManager(){
		try{
			
			//choose random song
			Random ran = new Random();
			int m = ran.nextInt(4) + 1;

			if(!Game.debugMode){
				theme = new Music("res/audio/song" + m + ".ogg");
				theme.loop(1f, 0.11f);
			}

			reloadsound = new Sound("res/audio/ammo.ogg");
			shot = new Sound("res/audio/shoot.ogg");
			Sound[] temp2 = {new Sound("res/audio/dead1.wav"), new Sound("res/audio/dead2.wav"), new Sound("res/audio/dead3.wav"), new Sound("res/audio/dead4.wav") };
			dead = temp2;
			ouch = new Sound("res/audio/ouch.ogg");
			hit = new Sound("res/audio/hit.wav");
			slurp = new Sound("res/audio/powerup.ogg");

		}catch (SlickException e){
			e.printStackTrace();
		}
	}

}
