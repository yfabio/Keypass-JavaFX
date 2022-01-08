package com.tech.developer.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ListResourceBundle;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * 
 * The Dictonary is used to manager the config file * 
 * @author yfabio
 * 
 */
public class Dictionary {

	private static ResourceBundle bundle = ListResourceBundle.getBundle("com.tech.developer.util.H2");	
	
	private static final String FILE_NAME = "/config.properties";
			
	private static Properties prop = new Properties();
	
	private static Path dir;

	static {
		try {			
			dir = Paths.get(System.getProperty("user.home"),"keypass",FILE_NAME);
			setDefault(dir);
			try (InputStream is = Files.newInputStream(dir)) {
				prop.load(is);
			} 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String getDriver() {
		return bundle.getString("driver");
	}

	public static String getURL() {
		return bundle.getString("url");
	}

	public static String getUsername() {
		return bundle.getString("username");
	}

	public static String getPassword() {
		return bundle.getString("password");
	}

	public static String getCurrent() {
		return prop.getProperty("current");
	}

	public static String getLogged() {
		return prop.getProperty("logged");
	}

	public static String getLang() {
		return prop.getProperty("lang");
	}

	public static void setProperty(String key, String value) {
		try {				
			try (OutputStream out = Files.newOutputStream(dir)) {
				prop.setProperty(key, value);
				prop.store(out, "");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public static void setDefault(Path dir) throws IOException {	
		createDirectoryIfNotExists(dir);
		if(!Files.exists(dir)) {		
			Files.copy(Dictionary.class.getResourceAsStream("/properties/config.properties"),dir);
		}
	}
	
	public static void createDirectoryIfNotExists(Path dir) throws IOException {
		  Path root = dir.getParent();		
		  if(!Files.exists(root)) {
			Files.createDirectory(root);			  
		  }			  
	}
	

}
