package sia.models;

import java.util.Date;

/**
 * Conversation
 * 
 * @author jumper
 */
public class Conversation {
	private int id;
	private Date begin;
	private int length;
	private ContactProtocol contact;
	
	/**
	 * Default and only constructor
	 * @param id
	 * @param begin
	 * @param length
	 * @param contact
	 */
	public Conversation(int id, Date begin, int length, ContactProtocol contact) {
		this.id = id;
		this.begin = begin;
		this.length = length;
		this.contact = contact;
	}
	
	/**
	 * Get id
	 * @return id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Set id
	 * @param id id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Get begin
	 * @return begin
	 */
	public Date getBegin() {
		return begin;
	}
	
	/**
	 * Set begin
	 * @param begin begin to set
	 */
	public void setBegin(Date begin) {
		this.begin = begin;
	}
	
	/**
	 * Get length
	 * @return length
	 */
	public int getLength() {
		return length;
	}
	
	/**
	 * Set length
	 * @param length length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}
	
	/**
	 * Get contact
	 * @return contact
	 */
	public ContactProtocol getContact() {
		return contact;
	}
	
	/**
	 * Set contact
	 * @param contact contact to set
	 */
	public void setContact(ContactProtocol contact) {
		this.contact = contact;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Conversation [id=" + id + ", begin=" + begin + ", length="
				+ length + ", contact=" + contact + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((begin == null) ? 0 : begin.hashCode());
		result = prime * result + ((contact == null) ? 0 : contact.hashCode());
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
		Conversation other = (Conversation) obj;
		if (begin == null) {
			if (other.begin != null)
				return false;
		} else if (!begin.equals(other.begin))
			return false;
		if (contact == null) {
			if (other.contact != null)
				return false;
		} else if (!contact.equals(other.contact))
			return false;
		return true;
	}
	
}
