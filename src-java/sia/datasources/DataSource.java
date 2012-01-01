package sia.datasources;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sia.fileparsers.IParser;
import sia.ui.SIA;
import sia.utils.Dictionaries;
import sia.models.Contact;
import sia.models.ContactAccount;
import sia.models.Protocol;
import sia.models.UserAccount;

/**
 * Data source
 * 
 * @author Agnieszka Glabala
 */
public abstract class DataSource {
	protected String[] extensions;
	protected String[][]  descriptions;
	protected List<UserAccount> userAccounts;
	protected List<Contact> contacts;
	protected String[] passwordDescriptions;
	private String[] passwords;
	protected Map<String,Protocol> protocols;
	
	protected IParser parser;
	
	/**
	 * Return accepted by parser file extensions 
	 * @return array of extensions
	 */
	public String[] getFileExtensions() {
		return extensions;
	}
	
	/**
	 * Return description for required files for every file [short description ie. "Database file" , hint how to find this file ie. "usually in home/.kadu/"]
	 * @return array of descriptions
	 */
	public String[][] getFileDescriptions() {
		return descriptions;
	}
	
	/**
	 * Returns Parser
	 *  
	 * @return parser
	 */
	public IParser getParser() {
		return parser;
		
	}

	/**
	 * Return user accounts found in archive files or null if user should to set this manually
	 * @return list of user accounts
	 */
	public List<UserAccount> getUserAccounts() {
		if(userAccounts==null) {
			userAccounts = parser.getUserAccounts();
		}
		return userAccounts;
	}
	
	/**
	 * Set from which accounts user want to import data (given by user)
	 * @param selectedAccounts array in the same order as userAccounts 
	 */
	public void setUserAccounts(List<UserAccount> uas) {
		userAccounts = uas;
	}
	
	/**
	 * Return all contacts with conversations (but not necessarily messages)
	 * @return list of contacts
	 */
	public List<Contact> getContacts() {
		if(contacts==null) {
			contacts = parser.getContacts(userAccounts);
		}
		return contacts;
	}
	
	/**
	 * Load and validate files
	 */
	public abstract void loadFiles(String[] files);
	
	/**
	 * Returns descriptions of passwords, ie. ["your pasword to archive 1", "your password to archive 2"]
	 * @return array of descriptions of passwords
	 */
	public String[] getRequiredPassword() {
		return passwordDescriptions;
	} 
	
	/**
	 * Set passwords
	 * @param passwords
	 */
	public void setPasswords(String[] passwords) {
		this.passwords = passwords;
	}

	/**
	 * Save all imported data
	 */
	public void save() {
		Connection conn = SIA.getInstance().getConnection();
		//conn.
	}

	public void mapContacts() {
		List<Contact> dbContacts = Dictionaries.getInstance().getContacts();
		Map<ContactAccount, Contact> mapContacts = new HashMap<ContactAccount, Contact>();
		for (Contact c : dbContacts) {
			for(ContactAccount ca : c.getContactAccounts()) {
				mapContacts.put(ca, c);
			}
		}
		for (int i=0; i<contacts.size();i++) {
			for(ContactAccount ca : contacts.get(i).getContactAccounts()) {
				if(mapContacts.containsKey(ca)) {
					System.out.println(ca);
					for(ContactAccount ca1 :contacts.get(i).getContactAccounts()) {
						if(!ca1.equals(ca)) {
							mapContacts.get(ca).getContactAccounts().add(ca1);
						}
					}
					contacts.remove(i);
					break;
				} 
			}
		}
	}
}
