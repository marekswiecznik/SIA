package sia.datasources;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sormula.SormulaException;

import sia.fileparsers.Parser;
import sia.models.Contact;
import sia.models.ContactAccount;
import sia.models.Conversation;
import sia.models.Message;
import sia.models.Protocol;
import sia.models.UserAccount;
import sia.ui.SIA;
import sia.utils.Config;
import sia.utils.Dictionaries;
import sia.utils.ORM;
import sia.utils.ParserFactory;

/**
 * Data source
 * 
 * @author Agnieszka Glabala
 */
public abstract class DataSource {
	public enum Progress {
		SAVE_PROGRESS, CONTACTS_PROGRESS, USER_ACCOUNTS_PROGRESS
	}
	protected String[] extensions;
	protected String[][]  descriptions;
	protected List<UserAccount> userAccounts;
	protected List<Contact> contacts;
	protected String[] passwordDescriptions;
	protected Map<String,Protocol> protocols;
	protected Parser parser;
	private int saveProgress = 0;
	protected static String parserClassName;
	
	/**
	 * Returns accepted by parser file extensions 
	 * @return array of extensions
	 */
	public String[] getFileExtensions() {
		return extensions;
	}
	
	/**
	 * Returns description for required files for every file [short description ie. "Database file" , hint how to find this file ie. "usually in home/.kadu/"]
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
	public Parser getParser() {
		return parser;
		
	}
	
	/**
	 * Init parser
	 */
	public void initParser() {
		if (parser == null)
			parser = ParserFactory.getInstance().create(parserClassName);
	}
	
	/**
	 * Load files
	 * @throws Exception
	 * @param files
	 */
	public void loadFiles(String[] files) throws Exception {
		parser.start();
		parser.loadFiles(files);
	}

	/**
	 * Get specified operation progress
	 * @param progress
	 * @return
	 */
	public int getProgress(Progress progress) {
		switch (progress) {
		case SAVE_PROGRESS:
			return saveProgress;
		case CONTACTS_PROGRESS:
			return parser.getContactsLoadProgress();
		case USER_ACCOUNTS_PROGRESS:
			return parser.getUserAccountsLoadProgress();
		}
		return 0;
	}

	/**
	 * Returns saveProgress
	 * @return saveProgress
	 */
	public int getSaveProgress() {
		return saveProgress;
	}

	/**
	 * Returns user accounts found in archive files or null if user should to set this manually
	 * @throws Exception
	 * @return list of user accounts
	 */
	public List<UserAccount> getUserAccounts() throws Exception {
		if(userAccounts==null) {
			parser.start();
			userAccounts = parser.getUserAccounts();
			List <UserAccount> dictUserAccounts = Dictionaries.getInstance().getUserAccounts();
			for (UserAccount ua : userAccounts) {
				if (dictUserAccounts.contains(ua)) {
					ua.setId(dictUserAccounts.get(dictUserAccounts.indexOf(ua)).getId());
				}
			}
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
	 * Returns all contacts with conversations (but not necessarily messages)
	 * @throws Exception
	 * @return list of contacts
	 */
	public List<Contact> getContacts() throws Exception {
		if(contacts==null) {
			parser.start();
			contacts = parser.getContacts(userAccounts);
			parser.setContactNames(contacts);
		}
		return contacts;
	}
	
	/**
	 * Validate files
	 * @param files
	 * @return error message or null
	 */
	public abstract String validateFiles(String[] files);
	
	/**
	 * Validate passwords and other parameters
	 * @param passwords
	 * @return error message or null
	 */
	public abstract String validatePasswords(String[] passwords);
	
	/**
	 * Validate user account UID
	 * @param UID
	 * @return error message or null
	 */
	public abstract String validateUid(String uid);
	
	/**
	 * Returns descriptions of passwords, eg. ["your pasword to archive 1", "your password to archive 2"]
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
		parser.setPasswords(passwords);
	}

	/**
	 * Save all imported data
	 * @throws SQLException 
	 * @throws SormulaException 
	 */
	public final void save(List<Contact> contacts) throws SQLException, SormulaException {
		saveProgress = 0;
		int messagesCount = parser.getMessagesCount();
		int msgs = 0;
		ORM orm = SIA.getInstance().getORM();
		SIA.getInstance().tmpInit();
		for (UserAccount userAccount : userAccounts) 
			if (userAccount.getId() == 0)
				orm.getTempTable(UserAccount.class).insert(userAccount);
		for (Contact contact : contacts) {
			if (contact.getId() == 0)
				orm.getTempTable(Contact.class).insert(contact);
			for (ContactAccount contactAccount : contact.getContactAccounts()) {
				if (contactAccount.getId() == 0) 
					orm.getTempTable(ContactAccount.class).insert(contactAccount);
				for (Conversation conversation : contactAccount.getConversations()) {
					conversation.setContactAccount(contactAccount);
					if (conversation.getId() == 0) {
						long begin = conversation.getTime().getTime();
						long end = conversation.getEndTime().getTime();
						long interval = Config.hasValue("import.conversation_interval") ? Config.getLong("import.conversation_interval") : 3600000L;
						Conversation conv = orm.getTable(Conversation.class).selectCustom(
							"WHERE (time BETWEEN "+begin+" AND "+end+
							" OR endTime BETWEEN "+begin+" AND "+end+
							" OR "+begin+" BETWEEN time AND (endTime + "+interval+")"+
							" OR "+end+" BETWEEN (time - "+interval+") AND endTime)" +
							" AND contactAccountId = "+conversation.getContactAccountId() +
							" AND userAccountId = "+conversation.getUserAccountId());
						if (conv != null)
							conversation.setId(conv.getId());
						else
							orm.getTempTable(Conversation.class).insert(conversation);
						for (Message message : conversation.getMessages()) {
							if (message.getId() == 0) {
								saveProgress = msgs++ / messagesCount;
								try {
									orm.getTempTable(Message.class).insert(message);
								} catch (SormulaException e) {
									if (e.getMessage().indexOf("unique") < 0)
										throw e;
								}
							}
						}
					}
				}
			}
		}
		SIA.getInstance().tmpSave();
		SIA.getInstance().updateConversations(userAccounts);
		saveProgress = 100;
	}

	public void mapContacts(List<Contact> dbContacts) {
		Map<ContactAccount, Contact> mapContacts = new HashMap<ContactAccount, Contact>();
		for (Contact c : dbContacts) {
			for(ContactAccount ca : c.getContactAccounts()) {
				mapContacts.put(ca, c);
			}
		}
		for (int i=0; i<contacts.size();i++) {
			for (int j = 0; j < contacts.get(i).getContactAccounts().size(); j++) {
				if(mapContacts.containsKey(contacts.get(i).getContactAccounts().get(j))) {
					//znaleziono wspólnego uida:
					for(ContactAccount ca1 : contacts.get(i).getContactAccounts()) {
						//po wszystkich CA z tych przeparsowanych
						if(!mapContacts.containsKey(ca1)) {
							//jeśli taki nie istnieje to dodajemy go do danego contactu
							mapContacts.get(contacts.get(i).getContactAccounts().get(j)).addContactAccount(ca1);
						} else {
							//jeśli istnieje to trzeba przepisać konwersacje
							//index z DB danego CA (get() znajduje Contact a potem znajduje index identycznego CA ale z DB )
							int index = mapContacts.get(ca1).getContactAccounts().indexOf(ca1);
							ContactAccount ca = mapContacts.get(ca1).getContactAccounts().get(index);
							//do ca (z DB) przypisujemy przeparsowane conversaction
							ca.setConversations(ca1.getConversations());
						}
					}
					contacts.remove(i);
					i--;
					break;
				} 
			}
		}
	}

	/**
	 * Abort parser's current action
	 */
	public void stopParserCurrentAction() {
		parser.stop();
	}
}
