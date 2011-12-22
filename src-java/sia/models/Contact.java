package sia.models;

import java.util.ArrayList;
import java.util.List;

import org.sormula.annotation.Transient;

/**
 * Contact
 * 
 * @author jumper
 */
public class Contact implements IModel {
	private int id;
	private String firstname;
	private String lastname;
	private String name;
	@Transient
	private List<ContactAccount> contactAccounts;
	
	/**
	 * Default constructor
	 */
	public Contact() { }
	
	/**
	 * Constructor
	 * @param id
	 * @param firstname
	 * @param lastname
	 * @param name
	 */
	public Contact(int id, String firstname, String lastname, String name) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.name = name;
		this.contactAccounts = new ArrayList<ContactAccount>();
	}
	
	/**
	 * Get id
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
	}
	
	/**
	 * Get firstname
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
	 * Get lastname
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
	 * Get contact protocols
	 * @return contact protocols
	 */
	public List<ContactAccount> getContactAccounts() {
		return contactAccounts;
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
	
}
