package sia.datasources;

import java.util.List;

import sia.models.Contact;
import sia.models.UserAccount;
import sia.utils.ParserFactory;

public class FMADataSource extends DataSource {
	
	/**
	 * FMA Data source
	 * 
	 * Float's Mobile Agent (for Sony Ericsson)
	 */
	public FMADataSource() {
		extensions = new String[] {"*.xml" };
		descriptions = new String[][] {new String[] {"XML file", "SMS archive in XML"}};
		passwordDescriptions = null;
		parser = new ParserFactory("FmaParser").create();
	}

	@Override
	public List<UserAccount> getUserAccounts() {
		if (userAccounts == null) {
			userAccounts = parser.getUserAccounts();
		}
		return userAccounts;
	}

	@Override
	public List<Contact> getContacts() {
		if (contacts == null && userAccounts != null && userAccounts.size() > 0)
			contacts = parser.getContacts(userAccounts);
		return contacts;
	}

	@Override
	public void loadFiles(String[] files) {
		if (files.length == 1)
			parser.loadFiles(files);
		else
			throw new IllegalArgumentException("Only one XML file allowed");
	}
}
