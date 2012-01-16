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
	protected boolean abort;

	/**
	 * Load required files (if neccessary)
	 * 
	 * @throws Exception
	 * @param fileNames
	 */
	public abstract void loadFiles(String[] fileNames) throws Exception;

	/**
	 * Returns user accounts.
	 * 
	 * This method should return all user accounts found in parsed files.
	 * 
	 * @throws Exception
	 * @return user accounts
	 */
	public abstract List<UserAccount> getUserAccounts() throws Exception;

	/**
	 * Returns contacts with conversations
	 * 
	 * @throws Exception
	 * @return contacts
	 */
	public abstract List<Contact> getContacts(List<UserAccount> userAccounts) throws Exception;

	/**
	 * Set passwords and other parameters
	 * 
	 * @param passwords
	 */
	public void setPasswords(String[] passwords) {
		this.passwords = passwords;
	}

	/**
	 * Returns passwords
	 * 
	 * @return passwords
	 */
	public String[] getPasswords() {
		return passwords;
	}

	/**
	 * Returns user accounts load progress
	 * 
	 * @return user accounts load progress
	 */
	public int getUserAccountsLoadProgress() {
		return userAccountsLoadProgress;
	}

	/**
	 * Set userAccountsLoadProgress
	 * 
	 * @param userAccountsLoadProgress
	 */
	public void setUserAccountsLoadProgress(int userAccountsLoadProgress) {
		this.userAccountsLoadProgress = userAccountsLoadProgress;
	}

	/**
	 * Returns contacts load progress
	 * 
	 * @return contacts load progress
	 */
	public int getContactsLoadProgress() {
		return contactsLoadProgress;
	}

	/**
	 * Set contactsLoadProgress
	 * 
	 * @param contactsLoadProgress
	 */
	public void setContactsLoadProgress(int contactsLoadProgress) {
		this.contactsLoadProgress = contactsLoadProgress;
	}

	/**
	 * Returns messages count
	 * 
	 * @return messages count
	 */
	public int getMessagesCount() {
		return messagesCount;
	}

	/**
	 * Set messagesCount
	 * 
	 * @param messagesCount
	 */
	public void setMessagesCount(int messagesCount) {
		this.messagesCount = messagesCount;
	}

	/**
	 * Stop current action
	 */
	public void stop() {
		abort = true;
	}
	
	/**
	 * Reset abort flag
	 */
	public void start() {
		abort = false;
	}
	
	public boolean isAborted() {
		return abort;
	}
}
