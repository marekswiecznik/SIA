package sia.models;

import java.util.Date;

import org.sormula.annotation.cascade.OneToOneCascade;
import org.sormula.annotation.cascade.SelectCascade;

/**
 * Message 
 * 
 * @author jumper
 */
public class Message {
	private int id;
	private int conversationId;
	@OneToOneCascade(selects = { @SelectCascade(sourceParameterFieldNames = {"conversationId"}) })
	private Conversation conversation;
	private String message;
	private Date time;
	private int received;
	
	/**
	 * Default message
	 */
	public Message() { }
	
	/**
	 * Constructor
	 * @param id
	 * @param contact
	 * @param message
	 * @param time
	 * @param received
	 */
	public Message(int id, Conversation conversation, String message, Date time, int received) {
		this.id = id;
		setConversation(conversation);
		this.message = message;
		this.time = time;
		this.received = received;
	}

	/**
	 * Returns ID
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
	 * Returns conversation ID
	 * @return conversationId
	 */
	public int getConversationId() {
		return conversationId;
	}

	/**
	 * Set conversation ID
	 * @param conversationId 
	 */
	public void setConversationId(int conversationID) {
		this.conversationId = conversationID;
	}

	/**
	 * Returns conversation
	 * @return conversation
	 */
	public Conversation getConversation() {
		return conversation;
	}

	/**
	 * Set conversation
	 * @param conversation
	 */
	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
		this.conversationId = conversation != null ? conversation.getId() : -1;
	}

	/**
	 * Returns message
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
	 * Returns received
	 * @return received
	 */
	public int getReceived() {
		return received;
	}

	/**
	 * Is received?
	 * @return is received
	 */
	public boolean isReceived() {
		return received != 0;
	}

	/**
	 * Set received
	 * @param received is received
	 */
	public void setReceived(int received) {
		this.received = received;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Message [id=" + id + ", message=" + message + ", time=" + time + ", rcv=" + received + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((conversation == null) ? 0 : conversation.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + (received == 0 ? 1231 : 1237);
		result = prime * result + ((time == null) ? 0 : time.hashCode());
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
		Message other = (Message) obj;
		if (conversation == null) {
			if (other.conversation != null)
				return false;
		} else if (!conversation.equals(other.conversation))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (received != other.received)
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		return true;
	}
	
}
