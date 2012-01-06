package sia.fileparsers;

import java.util.List;

import sia.models.Contact;
import sia.models.UserAccount;

/**
 * Abstract parser
 * 
 * @author jumper
 */
public abstract class Parser {
	protected int contactsLoadProgress = 0;
	protected int userAccountsLoadProgress = 0;
	protected int messagesCount = 0;
	protected String[] passwords = new String[0];
	
	/**
	 * Load required files (if neccessary)
	 * @param fileNames
	 */
	public abstract void loadFiles(String[] fileNames);
	
	/**
	 * Returns user accounts.
	 * 
	 * This method should return all user accounts found in
	 * parsed files.
	 * 
	 * @return user accounts
	 */
	public abstract List<UserAccount> getUserAccounts();
	
	/**
	 * Returns contacts with conversations
	 * @return contacts
	 */
	public abstract List<Contact> getContacts(List<UserAccount> userAccounts);
	
	/**
	 * Set passwords and other parameters
	 * @param passwords
	 */
	public void setPassowrds(String[] passwords) {
		this.passwords = passwords;
	}

	/**
	 * Returns user accounts load progress
	 * @return user accounts load progress
	 */
	public int getUserAccountsLoadProgress() {
		return userAccountsLoadProgress;
	}

	/**
	 * Set userAccountsLoadProgress
	 * @param userAccountsLoadProgress
	 */
	public void setUserAccountsLoadProgress(int userAccountsLoadProgress) {
		this.userAccountsLoadProgress = userAccountsLoadProgress;
	}

	/**
	 * Returns contacts load progress
	 * @return contacts load progress
	 */
	public int getContactsLoadProgress() {
		return contactsLoadProgress;
	}

	/**
	 * Set contactsLoadProgress
	 * @param contactsLoadProgress
	 */
	public void setContactsLoadProgress(int contactsLoadProgress) {
		this.contactsLoadProgress = contactsLoadProgress;
	}

	/**
	 * Returns messages count
	 * @return messages count
	 */
	public int getMessagesCount() {
		return messagesCount;
	}

	/**
	 * Set messagesCount
	 * @param messagesCount
	 */
	public void setMessagesCount(int messagesCount) {
		this.messagesCount = messagesCount;
	}
}
