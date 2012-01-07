package sia.models;

import java.util.ArrayList;
import java.util.List;

import org.sormula.annotation.Column;
import org.sormula.annotation.Transient;
import org.sormula.annotation.cascade.OneToOneCascade;
import org.sormula.annotation.cascade.SelectCascade;

import sia.ui.SIA;

/**
 * Contact
 * 
 * @author jumper
 */
public class ContactAccount {
	@Column(identity=true, primaryKey=true)
	private int id;
	private String name;
	private String uid;
	private String otherinfo;
	private int protocolId;
	@OneToOneCascade(selects = { @SelectCascade(sourceParameterFieldNames = {"protocolId"}) }, inserts = {}, updates = {}, deletes = {})
	private Protocol protocol;
	private int contactId;
	@OneToOneCascade(selects = { @SelectCascade(sourceParameterFieldNames = {"contactId"}) }, inserts = {}, updates = {}, deletes = {})
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
		setProtocol(protocol);
		setContact(contact);
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
			conversation.setContactAccountId(id);
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
	 * Returns protocol ID
	 * @return protocolId
	 */
	public int getProtocolId() {
		if (protocol != null)
			protocolId = protocol.getId();
		return protocolId;
	}

	/**
	 * Set protocol ID
	 * @param protocolId 
	 */
	public void setProtocolId(int protocolID) {
		this.protocolId = protocolID;
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
		if (protocol != null)
			this.protocolId = protocol.getId();
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
		if (contact != null)
			this.contactId = contact.getId();
	}

	/**
	 * Returns contact ID
	 * @return contactId
	 */
	public int getContactId() {
		if (contact != null)
			contactId = contact.getId();
		return contactId;
	}

	/**
	 * Set contact ID
	 * @param contactId 
	 */
	public void setContactId(int contactID) {
		this.contactId = contactID;
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
		for (Conversation c : conversations) {
			c.setContactAccount(this);
		}
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContactAccount clone() {
<<<<<<< HEAD
		ContactAccount ca = new ContactAccount(id, name, uid, otherinfo, contact, protocol);
=======
		ContactAccount ca = new ContactAccount(id, uid, name, otherinfo, contact, protocol);
>>>>>>> 56a16cbc6b4c776b9e14334c040ebdfc521d2017
		ca.setConversations(conversations);
		return ca;
	}
}
