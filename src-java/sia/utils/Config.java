package sia.utils;

/**
 * Config
 * 
 * @author jumper
 */
public class Config {
	
	/**
	 * Get value for given key
	 * @param key
	 */
	public static String get(String key) {
		return Dictionaries.getInstance().getConfiguration().get(key).getValue(); 
	}
}
