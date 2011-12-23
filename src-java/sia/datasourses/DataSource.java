package sia.datasourses;

import java.util.ArrayList;
import java.util.List;

import sia.fileparsers.IParser;
import sia.utils.ParserFactory;
import sia.models.Contact;
import sia.models.Protocol;
import sia.models.UserAccount;

/**
 * Data source
 * 
 * @author Agnieszka Glabala
 */
public class DataSource {
	protected String[] extensions = new String[] {"*.jpg;*.png" };
	protected String[][]  descriptions = new String[][] {new String[] {"PNG file", 
			"To implement the canFlipToNextPage method for the first page of wizard,page when \n" +
			"we first prevent the user from moving to the next page when the destination\n" +
			"the page has any errors. When there are no errors, the destination\n" +
			"and departure fields are filled, the return date is set and a mode\n" +
			"of transport is selected, the user can move to the next page."}};
	protected List<UserAccount> userAccounts = new ArrayList<UserAccount>();
	protected String[] passwordDescriptions;
	protected String[] passwords;
	protected ParserFactory parserFactory;
	
	public DataSource() {
		userAccounts.add(new UserAccount(0, new Protocol(0, "gg", "gg", "gg.png"), "4053074"));
		userAccounts.add(new UserAccount(1, new Protocol(1, "jabber", "jabb", "jabber.png"), "4053074@jabb.pl"));
		userAccounts.add(new UserAccount(2, new Protocol(2, "gtalk", "google talk", "gtalk.png"), "4053074@gmail.com"));
		userAccounts.add(new UserAccount(3, new Protocol(2, "gtalk", "google talk", "gtalk.png"), "4053074@google.com"));
	}
	
	/**
	 * Returns accepted by parser file extensions
	 * 
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
		return parserFactory.create();
		
	}

	/*
	 * Return user accounts found in archive files or null if null if user should to set this manually
	 * @return list of user accounts
	 */
	public List<UserAccount> getUserAccouts() {
		return userAccounts;
	}
	
	/**
	 * Set from which accounts user want to import data
	 * @param selectedAccounts array in the same order as userAccounts 
	 */
	public void setUserAccounts(boolean[] selectedAccounts) {
		
	}
	
	/**
	 * Set from which accounts user want to import data (given by user)
	 * @param selectedAccounts array in the same order as userAccounts 
	 */
	public void setUserAccounts(List<UserAccount> uas) {
		
	}
	
	/**
	 * Return all contacts with conversations (but not necessarily messages)
	 * @return list of contacts
	 */
	public List<Contact> getContacts() {
		return null;
	}
	
	/**
	 * Read files and fill models
	 * @param files array of files to parse
	 */
	public void parse() {
		//TODO parse
	}
	
	/**
	 * Load and validate files
	 */
	public void loadFiles(String[] files) {
		//TODO load
	}
	
	/**
	 * Returns descriptions of passwords, ie. ["your pasword to archive 1", "your password to archive 2"]
	 * @return array of descriptions of passwords
	 */
	public String[] getRequiredPassword() {
		return new String[] {"Hasło 1:", "Hasło 2:"};
	} 
	
	/**
	 * Set passwords
	 * @param passwords
	 */
	public void setPasswords(String[] passwords) {
		this.passwords = passwords;
	}
}
