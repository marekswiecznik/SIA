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
		return parser.getUserAccounts();
	}

	@Override
	public List<Contact> getContacts() {
		if (userAccounts != null && userAccounts.size() > 0)
			return parser.getContacts(userAccounts);
		return null;
	}

	@Override
	public void loadFiles(String[] files) {
		if (files.length == 1)
			parser.loadFiles(files);
		else
			throw new IllegalArgumentException("Only one XML file allowed");
	}
}
