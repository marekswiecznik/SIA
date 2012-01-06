package sia.datasources;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sormula.SormulaException;

import sia.fileparsers.IParser;
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
	 * @throws SQLException 
	 * @throws SormulaException 
	 */
	public void save(List<Contact> contacts) throws SQLException, SormulaException {
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
							"where (time between "+begin+" AND "+end+
							" or endTime between "+begin+" AND "+end+
							" or "+begin+" between time AND endTime"+
							" or "+end+" between time AND endTime" +
							" or "+begin+" <= (endTime + "+interval+")" +
							" or "+end+" >= (time - "+interval+"))" +
							" and contactAccountId = "+conversation.getContactAccountId() +
							" and userAccountId = "+conversation.getUserAccountId());
						if (conv != null)
							conversation.setId(conv.getId());
						orm.getTempTable(Conversation.class).insert(conversation);
						for (Message message : conversation.getMessages()) {
							if (message.getId() == 0) {
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
		String sql = "UPDATE aux1.conversation " +
				"SET time = (SELECT MIN(time) FROM aux1.message WHERE conversationId = aux1.conversation.id), " +
				"endTime = (SELECT MAX(time) FROM aux1.message WHERE conversationId = aux1.conversation.id), " +
				"length = (SELECT COUNT(1) FROM aux1.message WHERE conversationId = aux1.conversation.id)";
		Statement stmt = SIA.getInstance().getConnection().createStatement();
		System.out.println("Conversation update "+stmt.executeUpdate(sql));
		SIA.getInstance().tmpSave();
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
