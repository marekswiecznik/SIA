package sia.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.sormula.annotation.Transient;
import org.sormula.annotation.cascade.OneToOneCascade;
import org.sormula.annotation.cascade.SelectCascade;

/**
 * Conversation
 * 
 * @author jumper
 */
public class Conversation implements IModel {
	private int id;
	private Date time;
	private String title;
	private int length;
	private int contactAccountID;
	@OneToOneCascade(selects = { @SelectCascade(sourceParameterFieldNames = {"contactAccountID"}) })
	private ContactAccount contactAccount;
	private int userAccountID;
	@OneToOneCascade(selects = { @SelectCascade(sourceParameterFieldNames = {"userAccountID"}) })
	private UserAccount userAccount;
	@Transient
	private List<Message> messages;
	
	/**
	 * Default constructor
	 */
	public Conversation() { this.messages = new ArrayList<Message>(); }
	
	/**
	 * Constructor
	 * @param id
	 * @param time
	 * @param title
	 * @param length
	 * @param contactAccount
	 * @param userAccount
	 */
	public Conversation(int id, Date begin, String firstMessage, int length, ContactAccount contactAccount, UserAccount userAccount) {
		this();
		this.id = id;
		this.time = begin;
		this.title = firstMessage;
		this.length = length;
		setContactAccount(contactAccount);
		setUserAccount(userAccount);
	}
	
	/**
	 * Returns id
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
		for (Message message : messages) {
			message.setConversationID(id);
		}
	}
	
	/**
	 * Returns time
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
	 * Returns title
	 * @return title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Set title
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Returns length
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
	 * @return the contactAccountID
	 */
	public int getContactAccountID() {
		return contactAccountID;
	}

	/**
	 * @param contactAccountID the contactAccountID to set
	 */
	public void setContactAccountID(int contactAccountID) {
		this.contactAccountID = contactAccountID;
	}

	/**
	 * Returns contactAccount
	 * @return contactAccount
	 */
	public ContactAccount getContactAccount() {
		return contactAccount;
	}
	
	/**
	 * Set contactAccount
	 * @param contactAccount contactAccount to set
	 */
	public void setContactAccount(ContactAccount contact) {
		this.contactAccount = contact;
		this.contactAccountID = contactAccount != null ? contactAccount.getId() : -1;
	}
	
	/**
	 * Returns user account ID
	 * @return user account ID
	 */
	public int getUserAccountID() {
		return userAccountID;
	}

	/**
	 * Set user account ID
	 * @param contactAccountID the contactAccountID to set
	 */
	public void setUserAccountID(int userAccountID) {
		this.userAccountID = userAccountID;
	}

	/**
	 * Returns contactAccount
	 * @return contactAccount
	 */
	public UserAccount getUserAccount() {
		return userAccount;
	}
	
	/**
	 * Set userAccount
	 * @param userAccount 
	 */
	public void setUserAccount(UserAccount ua) {
		this.userAccount = ua;
		this.userAccountID = userAccount != null ? userAccount.getId() : -1;
	}
	
	/**
	 * Returns messages
	 * @return messages
	 */
	public List<Message> getMessages() {
		return messages;
	}
	
	/**
	 * Add message 
	 * @param msg message
	 */
	public void addMessage(Message msg) {
		msg.setConversation(this);
		messages.add(msg);
		length++;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Conversation [id=" + id + ", time=" + time + ", title=" + title +"..., length=" + length + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((contactAccount == null) ? 0 : contactAccount.hashCode());
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
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (contactAccount == null) {
			if (other.contactAccount != null)
				return false;
		} else if (!contactAccount.equals(other.contactAccount))
			return false;
		return true;
	}
	
}
