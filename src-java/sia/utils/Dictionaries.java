package sia.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sormula.SormulaException;

import sia.datasources.DataSource;
import sia.datasources.ExampleDataSource;
import sia.datasources.FMADataSource;
import sia.datasources.GtalkDataSource;
import sia.models.Configuration;
import sia.models.Contact;
import sia.models.ContactAccount;
import sia.models.Protocol;
import sia.models.UserAccount;
import sia.ui.SIA;

/**
 * Dictionaries
 * 
 * @author jumper
 */
public class Dictionaries {
	private static Dictionaries instance;
	private ORM orm;
	
	private Map<String, String> dataSources;
	private Map<String, Protocol> protocols;
	private Map<String, Configuration> configuration;
	private List<Contact> contacts;
	private List<UserAccount> userAccounts;
	
	/**
	 * Default constructor
	 */
	private Dictionaries() {
		orm = SIA.getInstance().getORM();
	}
	
	/**
	 * Load dictionaries
	 * @throws SormulaException 
	 */
	public void init() throws SormulaException {
		List<Configuration> configuration = orm.getTable(Configuration.class).selectAll();
		this.configuration = new HashMap<String, Configuration>();
		for (Configuration c : configuration)
			this.configuration.put(c.getKey(), c);
		
		List<Protocol> protocols = orm.getTable(Protocol.class).selectAll();
		this.protocols = new HashMap<String, Protocol>();
		for (Protocol p : protocols)
			this.protocols.put(p.getName(), p);
		
		loadContacts();
	
		dataSources = new HashMap<String, String>();
		//dataSources.put("kadu", new KaduDataSource());
		dataSources.put("Float's Mobile Agent", FMADataSource.class.getSimpleName());
		dataSources.put("Google Talk (via IMAP)", GtalkDataSource.class.getSimpleName());
		dataSources.put("Example", ExampleDataSource.class.getSimpleName());
	}
	
	/**
	 * Returns data sources dictionary
	 * @return data sources dictionary
	 */
	public Map<String, String> getDataSources() {
		return dataSources;
	}
	
	/**
	 * Returns data source for given key
	 * @param key
	 * @return data source
	 */
	public DataSource getDataSource(String key) {
		try {
			return (DataSource) Class.forName(DataSource.class.getPackage().getName()+"."+dataSources.get(key)).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Returns protocols dictionary
	 * @return protocols dictionary
	 */
	public Map<String, Protocol> getProtocols() {
		return protocols;
	}
	
	/**
	 * Returns protocol for given key
	 * @param key
	 * @return protocol
	 */
	public Protocol getProtocol(String key) {
		return protocols.get(key);
	}
	
	/**
	 * Returns contacts
	 * @return contacts
	 */
	public List<Contact> getContacts() {
		return contacts;
	}
	
	/**
	 * Returns user accounts
	 * @return user acounts
	 */
	public List<UserAccount> getUserAccounts() {
		return userAccounts;
	}
	
	/**
	 * Returns instance
	 * @return dictionaries
	 */
	public static Dictionaries getInstance() {
		if (instance == null)
			instance = new Dictionaries();
		return instance;
	}

	/**
	 * Get configuration
	 * @return
	 */
	public Map<String, Configuration> getConfiguration() {
		return configuration;
	}

	/**
	 * (Re)load contacts
	 * @throws SormulaException 
	 */
	public void loadContacts() throws SormulaException {
		this.contacts = orm.getTable(Contact.class).selectAll();
		for (Contact contact : contacts) {
			for (ContactAccount ca : orm.getTable(ContactAccount.class).selectAllCustom("where contactId = "+ contact.getId()))
				contact.addContactAccount(ca);
		}
		
		this.userAccounts = orm.getTable(UserAccount.class).selectAll();
	}
}
