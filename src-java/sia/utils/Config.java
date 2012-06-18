package sia.utils;

import sia.models.Configuration;

/**
 * Config
 * 
 * @author jumper
 */
public class Config {

	/**
	 * Returns true when exists value for given key
	 * 
	 * @param key
	 * @return true if key exists in dictionary
	 */
	public static boolean hasValue(String key) {
		return !(config(key) == null || config(key).getValue() == null || config(key).getValue().equals(""));
	}
	
	/**
	 * Returns string value for given key
	 * 
	 * @param key
	 * @return string value for given key, default empty string
	 */
	public static String get(String key) {
		return config(key) == null ? "" : config(key).getValue();
	}
	
	/**
	 * Returns boolean value for given key
	 * 
	 * @param key
	 * @return boolean value for given key, default false
	 */
	public static boolean getBoolean(String key) {
		return config(key) == null ? false : Boolean.parseBoolean(config(key).getValue());
	}
	
	/**
	 * Returns int value for given key
	 * 
	 * @param key
	 * @return int value for given key, default 0
	 */
	public static int getInt(String key) {
		return config(key) == null ? 0 : Integer.parseInt(config(key).getValue());
	}
	
	/**
	 * Returns double value for given key
	 * 
	 * @param key
	 * @return double value for given key, default 0.0d
	 */
	public static double getDouble(String key) {
		return config(key) == null ? 0.0d : Double.parseDouble(config(key).getValue());
	}
	
	/**
	 * Returns long value for given key
	 * 
	 * @param key
	 * @return long value for given key, default 0L
	 */
	public static long getLong(String key) {
		return config(key) == null ? 0L : Long.parseLong(config(key).getValue());
	}
	
	/**
	 * Returns configuration dictionary
	 * 
	 * @return configuration dictionary
	 */
	private static Configuration config (String key) {
		return Dictionaries.getInstance().getConfiguration().get(key);
	}
}
