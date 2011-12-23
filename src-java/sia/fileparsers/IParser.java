package sia.fileparsers;

import java.util.List;

import sia.models.Contact;
import sia.models.UserAccount;

public interface IParser {
	/**
	 * Load required files
	 * @param fileNames
	 */
	public void loadFiles(String[] fileNames);
	
	/**
	 * Returns user accounts
	 * @return user accounts
	 */
	public List<UserAccount> getUserAccounts();
	
	/**
	 * Returns contacts with conversations
	 * @return contacts
	 */
	public List<Contact> getContacts();
}
