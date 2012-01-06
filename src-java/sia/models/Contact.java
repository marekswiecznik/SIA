package sia.models;

import java.util.ArrayList;
import java.util.List;

import org.sormula.annotation.Column;
import org.sormula.annotation.Transient;

/**
 * Contact
 * 
 * @author jumper
 */
public class Contact {
	@Column(identity=true, primaryKey=true)
	private int id;
	private String firstname;
	private String lastname;
	private String name;
	@Transient
	private List<ContactAccount> contactAccounts;
	
	/**
	 * Default constructor
	 */
	public Contact() { 
		this.contactAccounts = new ArrayList<ContactAccount>();
	}
	
	/**
	 * Constructor
	 * @param id
	 * @param firstname
	 * @param lastname
	 * @param name
	 */
	public Contact(int id, String firstname, String lastname, String name) {
		this();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.name = name;
	}
	
	/**
	 * Returns id
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * Set id
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
		for (ContactAccount contactAccount : contactAccounts) {
			contactAccount.setContactId(id);
		}
	}
	
	/**
	 * Returns firstname
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}
	
	/**
	 * Set firstname
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	/**
	 * Returns lastname
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}
	
	/**
	 * Set lastname
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
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
	 * Returns contact protocols
	 * @return contact protocols
	 */
	public List<ContactAccount> getContactAccounts() {
		return contactAccounts;
	}
	
	/**
	 * Add message 
	 * @param msg message
	 */
	public void addContactAccount(ContactAccount contactAccount) {
		contactAccount.setContact(this);
		contactAccounts.add(contactAccount);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Contact [id=" + id + ", firstname=" + firstname + ", lastname="
				+ lastname + ", name=" + name + "]";
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
		Contact other = (Contact) obj;
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
	public Contact clone() {
		Contact c = new Contact(this.id, this.firstname, this.lastname, this.name);
		for(ContactAccount ca : getContactAccounts()) {
			c.addContactAccount(ca.clone());
		}
		return c;
	}
	
}
