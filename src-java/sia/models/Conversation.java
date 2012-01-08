package sia.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.sormula.annotation.Column;
import org.sormula.annotation.Transient;
import org.sormula.annotation.cascade.OneToOneCascade;
import org.sormula.annotation.cascade.SelectCascade;

/**
 * Conversation
 * 
 * @author jumper
 */
public class Conversation implements Comparable {
	@Column(identity=true, primaryKey=true)
	private int id;
	private Date time;
	private Date endTime;
	private String title;
	private int length;
	private int contactAccountId;
	@OneToOneCascade(selects = { @SelectCascade(sourceParameterFieldNames = {"contactAccountId"}) }, inserts = {}, updates = {}, deletes = {})
	private ContactAccount contactAccount;
	private int userAccountId;
	@OneToOneCascade(selects = { @SelectCascade(sourceParameterFieldNames = {"userAccountId"}) }, inserts = {}, updates = {}, deletes = {})
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
			message.setConversationId(id);
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
	 * Returns end time
	 * @return end time
	 */
	public Date getEndTime() {
		return endTime;
	}
	
	/**
	 * Set end time
	 * @param end time
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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
	 * Returns contact account ID
	 * @return contactAccountID
	 */
	public int getContactAccountId() {
		if (contactAccount != null)
			contactAccountId = contactAccount.getId();
		return contactAccountId;
	}

	/**
	 * Set contact account ID
	 * @param contactAccountID
	 */
	public void setContactAccountId(int contactAccountId) {
		this.contactAccountId = contactAccountId;
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
	 * @param contactAccount
	 */
	public void setContactAccount(ContactAccount contact) {
		this.contactAccount = contact;
		this.contactAccountId = contactAccount != null ? contactAccount.getId() : 0;
	}
	
	/**
	 * Returns user account ID
	 * @return user account ID
	 */
	public int getUserAccountId() {
		if (userAccount != null)
			userAccountId = userAccount.getId();
		return userAccountId;
	}

	/**
	 * Set user account ID
	 * @param contactAccountID
	 */
	public void setUserAccountId(int userAccountID) {
		this.userAccountId = userAccountID;
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
		this.userAccountId = userAccount != null ? userAccount.getId() : 0;
	}
	
	/**
	 * Returns messages
	 * @return messages
	 */
	public List<Message> getMessages() {
		return messages;
	}
	
	/**
	 * Set messages
	 * @param messages 
	 */
	public void setMessages(List<Message> messages) {
		this.messages = messages;
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
		if (userAccount == null) {
			if (other.userAccount != null)
				return false;
		} else if (!userAccount.equals(other.userAccount))
			return false;
		return true;
	}

	@Override
	public int compareTo(Object obj) {
		Conversation conv = (Conversation) obj;
		if(this.time.before(conv.time)) {
			return -1;
		} else if(this.time.after(conv.time)) {
			return 1;
		}
		return 0;
	}
	
}
