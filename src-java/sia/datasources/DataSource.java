package sia.datasources;

import java.sql.SQLException;
import java.sql.Statement;
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
	private int saveProgress = 0;
	protected Parser parser;
	
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
	 * @return list of user accounts
	 */
	public List<UserAccount> getUserAccounts() {
		if(userAccounts==null) {
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
					if (conversation.getId() == 0) {
						long begin = conversation.getTime().getTime();
						long end = conversation.getEndTime().getTime();
						long interval = Config.get("conversation_interval") != null ? Long.parseLong(Config.get("interval")) : 3600000;
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
		String uas = "";
		for (int i=0; i<userAccounts.size(); i++)
			uas += (i==0 ? "" : ",") + userAccounts.get(i).getId();
		String sql = "UPDATE main.conversation " +
				"SET time = (SELECT MIN(time) FROM main.message WHERE conversationId = main.conversation.id), " +
				"endTime = (SELECT MAX(time) FROM main.message WHERE conversationId = main.conversation.id), " +
				"length = (SELECT COUNT(1) FROM main.message WHERE conversationId = main.conversation.id) " +
				"WHERE userAccountId IN ("+uas+")";
		Statement stmt = SIA.getInstance().getConnection().createStatement();
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
}
