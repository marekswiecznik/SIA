package sia.datasources;

import sia.utils.ParserFactory;

/**
 * Google talk IMAP data source
 * @author jumper
 */
public class GtalkDataSource extends DataSource {
	public static final String NAME = "Google Talk (via IMAP)";
	private static final String UID_REGEX ="^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[a-z]{2,4}$";

	/**
	 * Default and only constructor
	 */
	public GtalkDataSource() {
		extensions = new String[] { };
		descriptions = new String[][] {new String[] { }};
		passwordDescriptions = new String[] {"Email address (JID)", "*Password", "Chats label"};
		parser = new ParserFactory("GtalkParser").create();
	}

	@Override
	public String validateFiles(String[] files) {
		return null;
	}

	@Override
	public String validatePasswords(String[] passwords) {
		if (passwords[1].length() == 0)
			return "Password can't be empty.";
		if (passwords[2].length() == 0)
			return "You have to specify chats label, e.g. 'Chats' for US, 'Czat' for PL.";
		return validateUid(passwords[0]);
	}

	@Override
	public String validateUid(String uid) {
		if (!uid.matches(UID_REGEX))
			return "JID has to be a valid bare JID (without resource), e.g. your.name@some.company.com";
		return null;
	}

}
