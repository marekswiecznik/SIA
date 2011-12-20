package sia.models;

/**
 * Contact
 * 
 * @author jumper
 */
public class Contact {
	private int id;
	private String name;
	private String uid;
	private String otherinfo;

	/**
	 * Default and only constructor
	 * @param name
	 * @param uid
	 * @param otherinfo
	 */
	public Contact(int id, String name, String uid, String otherinfo) {
		this.id = id;
		this.name = name;
		this.uid = uid;
		this.otherinfo = otherinfo;
	}

	/**
	 * Get ID
	 * @return ID
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
	 * Get name
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
	 * Get uid
	 * @return uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * Set uid
	 * @param uid
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * Get other info
	 * @return other info
	 */
	public String getOtherinfo() {
		return otherinfo;
	}

	/**
	 * Set other info
	 * @param otherinfo
	 */
	public void setOtherinfo(String otherinfo) {
		this.otherinfo = otherinfo;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Contact [id=" + this.id + ", name=" + this.name + ", uid=" + this.uid + "]";
	}
}
