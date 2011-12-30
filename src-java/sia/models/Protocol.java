package sia.models;

/**
 * Protocol
 * 
 * @author jumper
 */
public class Protocol {
	private int id;
	private String name;
	private String description;
	private String icon;
	
	/**
	 * Default constructor
	 */
	public Protocol() { }
	
	/**
	 * Constructor
	 * @param id
	 * @param name
	 * @param description
	 * @param icon
	 */
	public Protocol(int id, String name, String description, String icon) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.icon = icon;
	}
	
	/**
	 * Returns ID
	 * @return id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Set ID
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Returns name
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set name
	 * @param name 
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns description
	 * @return description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Set description
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Returns icon
	 * @return icon
	 */
	public String getIcon() {
		return icon;
	}
	
	/**
	 * Set icon
	 * @param icon 
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Protocol other = (Protocol) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Protocol [id=" + id + ", name=" + name + ", description="
				+ description + ", icon=" + icon + "]";
	}
}
