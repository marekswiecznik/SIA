package sia.models;

/**
 * Configuration
 * 
 * @author jumper
 */
public class Configuration implements IModel {
	private String key;
	private String value;
	
	/**
	 * Default constructor
	 */
	public Configuration() { }
	
	/**
	 * Constructor
	 * @param key option key
	 * @param value option value
	 */
	public Configuration(String key, String value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * Get key
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Set key
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Get value
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Set value
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Configuration other = (Configuration) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Configuration [key=" + key + ", value=" + value + "]";
	}
}
