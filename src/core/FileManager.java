package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class FileManager{

	private File file = new File(System.getProperty("user.home") + "/AppData/Local/Communism Simulator/config.properties");

	public void setConfig(String type, String value){

		try{

			// create default file if there is no file
			makeConfig();

			// create input stream and load properties
			InputStream input = new FileInputStream(file);
			Properties propout = new Properties();
			propout.load(input);
			input.close();

			// set properties
			propout.setProperty(type, value);

			// create output stream and save properties
			OutputStream output = new FileOutputStream(file);
			propout.store(output, null);
			output.close();

		}catch (FileNotFoundException e){
			e.printStackTrace();
			System.out.println("Path:" + file.getName());
		}catch (IOException e){
			e.printStackTrace();
		}
	}


	public String readConfig(String type){


		// properties object
		Properties propin = new Properties();
		try{
			makeConfig();

			// input stream
			InputStream input = new FileInputStream(file);

			// load properties
			propin.load(input);

			// close stream
			input.close();

		}catch (IOException e){
			System.out.println("Could not open file: " + file.getName());
		}

		// return value
		return propin.getProperty(type);
	}

	private void makeConfig() throws IOException{

		if(!file.exists()){
			System.out.println("Config file not found, creating new one");

			// set up properties object
			Properties def = new Properties();
			def.setProperty("highscore", "0");
			def.setProperty("lastscore", "0");
			def.setProperty("displaymode", "100");

			// write to file
			file.getParentFile().mkdirs();
			file.createNewFile();
			OutputStream output = new FileOutputStream(file);
			def.store(output, "Default Config File");
			output.close();
		}
	}
}

