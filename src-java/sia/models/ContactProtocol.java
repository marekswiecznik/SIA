package sia.models;

/**
 * Contact
 * 
 * @author jumper
 */
public class ContactProtocol {
	private int id;
	private String name;
	private String uid;
	private String otherinfo;
	private Protocol protocol;
	private Contact contact;

	/**
	 * Default and only constructor
	 * @param name
	 * @param uid
	 * @param otherinfo
	 */
	public ContactProtocol(int id, String name, String uid, String otherinfo, Contact contact, Protocol protocol) {
		this.id = id;
		this.name = name;
		this.uid = uid;
		this.otherinfo = otherinfo;
		this.protocol = protocol;
		this.contact = contact;
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
	 * Get contact
	 * @return contact
	 */
	public Contact getContact() {
		return contact;
	}

	/**
	 * Set contact
	 * @param contact 
	 */
	public void setContact(Contact contact) {
		this.contact = contact;
	}

	/**
	 * Get protocol
	 * @return protocol
	 */
	public Protocol getProtocol() {
		return protocol;
	}

	/**
	 * Set protocol
	 * @param protocol 
	 */
	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Contact [id=" + this.id + ", name=" + this.name + ", uid=" + this.uid + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		result = prime * result + ((protocol == null) ? 0 : protocol.hashCode());
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
		ContactProtocol other = (ContactProtocol) obj;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		if (protocol == null) {
			if (other.protocol != null)
				return false;
		} else if (!protocol.equals(other.protocol))
			return false;
		return true;
	}
	
}
