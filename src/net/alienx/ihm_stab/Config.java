package net.alienx.ihm_stab;

import java.awt.Color;
import java.io.File;
import java.util.prefs.Preferences;

import org.ini4j.Ini;
import org.ini4j.IniPreferences;

public class Config {
	static final String configFile = "config.ini";
	public static final String NODE_UI = "interface";
	
	private Preferences prefs;
	private static Config instance;
	
	/**
	 * 
	 */
	private Config()
	{
		loadPreferences();
	}
	
	/**
	 * 
	 * @return
	 */
	public static Config getInstance()
	{
		if (instance == null ) {
			instance = new Config();
		}
		
		return instance;
	}
	
	/**
	 * 
	 */
	public static void init()
	{
		getInstance();
	}
	
	/**
	 * 
	 */
	private void loadPreferences() 
	{
		try {
			prefs = new IniPreferences(new Ini(new File(configFile)));
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * 
	 * @param node
	 * @param key
	 * 
	 * @return
	 */
	public String getString(String node, String key)
	{
		return getString(node,key,null);
	}
	
	/**
	 * 
	 * @param node
	 * @param key
	 * @param def
	 * 
	 * @return
	 */
	public String getString(String node, String key, String def)
	{
		Preferences nodePrefs = this.getNode(node);
		if (nodePrefs == null) {
			return null;
		}
		
		return nodePrefs.get(key,def);
	}
	
	/**
	 * 
	 * @param node
	 * @param key
	 * 
	 * @return
	 */
	public Color getColor(String node, String key)
	{
		return getColor(node,key,null);
	}
	
	/**
	 * 
	 * @param node
	 * @param key
	 * @param def
	 * 
	 * @return
	 */
	public Color getColor(String node, String key, Color def)
	{
		Preferences nodePrefs = this.getNode(node);
		
		if (nodePrefs == null) {
			return def;
		}
		
		String color = nodePrefs.get(key,null);
		if (color == null) {
			return def;
		}
		
		return Color.decode(color);
		
	}	
	
	/**
	 * 
	 * @param node
	 * 
	 * @return
	 */
	public Preferences getNode(String node)
	{
		try {
			return prefs.node(node);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
