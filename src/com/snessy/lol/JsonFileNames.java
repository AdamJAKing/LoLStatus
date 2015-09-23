package com.snessy.lol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class JsonFileNames {
	
	public static final String API_KEY;
	
	/* Static block will execute when the JVM loads in the class file
	 * This allows us to read in the key from a file, rather than declare it
	 * inside the class
	 */
	static{
		API_KEY = readApiKey();
	}
	
	public static String readApiKey(){
		File file = new File("res/ApiKey.txt");
		String key = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String value;
			
			while((value = reader.readLine()) != null ){
				key = value;
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("Could not find file containing API key");
		} catch (IOException e) {
			System.out.println("Could not read file " + file);
		}
		if(key == null){
			throw new  NullPointerException();
		}
		return key;
		System.out.println();
	}

}
