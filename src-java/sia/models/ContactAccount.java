package sia.models;

import java.util.ArrayList;
import java.util.List;

import org.sormula.annotation.Transient;
import org.sormula.annotation.cascade.OneToOneCascade;
import org.sormula.annotation.cascade.SelectCascade;

/**
 * Contact
 * 
 * @author jumper
 */
public class ContactAccount implements IModel {
	private int id;
	private String name;
	private String uid;
	private String otherinfo;
	private int protocolID;
	@OneToOneCascade(selects = { @SelectCascade(sourceParameterFieldNames = {"protocolID"}) })
	private Protocol protocol;
	private int contactID;
	@OneToOneCascade(selects = { @SelectCascade(sourceParameterFieldNames = {"contactID"}) })
	private Contact contact;
	private int avatar;
	@Transient
	private List<Conversation> conversations;
	
	/**
	 * Default constructor
	 */
	public ContactAccount() {
		this.avatar = 0;
		this.conversations = new ArrayList<Conversation>();
	}

	/**
	 * Constructor
	 * @param name
	 * @param uid
	 * @param otherinfo
	 */
	public ContactAccount(int id, String name, String uid, String otherinfo, Contact contact, Protocol protocol) {
		this();
		this.id = id;
		this.name = name;
		this.uid = uid;
		this.otherinfo = otherinfo;
		this.protocol = protocol;
		this.contact = contact;
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
		for (Conversation conversation : conversations) {
			conversation.setContactAccountID(id);
		}
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
	 * Returns uid
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
	 * Returns other info
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
	 * @return the protocolID
	 */
	public int getProtocolID() {
		return protocolID;
	}

	/**
	 * @param protocolID the protocolID to set
	 */
	public void setProtocolID(int protocolID) {
		this.protocolID = protocolID;
	}

	/**
	 * Returns protocol
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
	 * Returns contact
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
	 * @return the contactID
	 */
	public int getContactID() {
		return contactID;
	}

	/**
	 * @param contactID the contactID to set
	 */
	public void setContactID(int contactID) {
		this.contactID = contactID;
	}

	/**
	 * Returns conversations
	 * @return conversations
	 */
	public List<Conversation> getConversations() {
		return conversations;
	}

	/**
	 * Set conversations
	 * @param conversations 
	 */
	public void setConversations(List<Conversation> conversations) {
		this.conversations = conversations;
	}

	/**
	 * Returns avatar
	 * @return avatar
	 */
	public int getAvatar() {
		return avatar;
	}

	/**
	 * Is avatar present?
	 * @return is avatar present
	 */
	public boolean isAvatar() {
		return avatar != 0;
	}

	/**
	 * Set avatar
	 * @param avatar 
	 */
	public void setAvatar(int avatar) {
		this.avatar = avatar;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "ContactAccount [id=" + this.id + ", name=" + this.name + ", uid=" 
				+ this.uid + ", protocol=" + (protocol != null ? protocol.getName() : "null") + "]";
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
		ContactAccount other = (ContactAccount) obj;
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
