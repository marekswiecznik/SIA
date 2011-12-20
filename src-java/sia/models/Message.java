package sia.models;

import java.util.Date;

public class Message {
	private int id;
	private Contact contact;
	private String message;
	private Date time;
	private boolean received;
	
	/**
	 * Default and only constructor
	 * @param id
	 * @param contact
	 * @param message
	 * @param time
	 * @param received
	 */
	public Message(Contact contact, String message, Date time, boolean received) {
		this.contact = contact;
		this.message = message;
		this.time = time;
		this.received = received;
	}

	/**
	 * Get ID
	 * @return ID
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set contact
	 * @param contact
	 */
	public void setId(int id) {
		this.id = id;
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
	 * Get message
	 * @return message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Set message
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Get time
	 * @return time
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * Set time
	 * @param time 
	 */
	public void setTime(Date time) {
		this.time = time;
	}

	/**
	 * Is received?
	 * @return is received
	 */
	public boolean isReceived() {
		return received;
	}

	/**
	 * Set received
	 * @param received is received
	 */
	public void setReceived(boolean received) {
		this.received = received;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Message [contact=" + contact + ", message=" + message
				+ ", time=" + time + ", received=" + received + "]";
	}
	
}
