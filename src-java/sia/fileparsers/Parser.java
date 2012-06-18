package sia.fileparsers;

import java.util.List;

import sia.models.Contact;
import sia.models.UserAccount;
import sia.utils.Config;

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
	
	/**
	 * Is parsing aborted?
	 * @return true if aborted
	 */
	public boolean isAborted() {
		return abort;
	}

	/**
	 * Set contact names depending on configuration
	 * @param contacts
	 */
	public void setContactNames(List<Contact> contacts) {
		for (Contact c : contacts) {
			if (c.getName().equals("")) {
				if (Config.getBoolean("import.last_then_first_name")) {
					c.setName(c.getLastname()+" "+c.getFirstname());
				} else {
					c.setName(c.getFirstname()+" "+c.getLastname());
				}
			}
			if (c.getFirstname().equals("") && c.getLastname().equals("")) {
				String[] tmp = c.getName().split("(\\s|\\;|\\:|\\_)+", 1);
				if (Config.getInt("import.split_name") == 1) {
					c.setFirstname(tmp[0]);
					c.setLastname(tmp[1]);
				} else if (Config.getInt("import.split_name") == 2) {
					c.setFirstname(tmp[1]);
					c.setLastname(tmp[0]);
				}
			}
		}
	}
}
