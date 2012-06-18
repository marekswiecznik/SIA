package sia.datasources;

/**
 * MPE Data source
 * 
 * MyPhoneExplorer (for Sony Ericsson)
 */
public class MpeDataSource extends DataSource {
	private static final String UID_REGEX = "^\\+?[0-9]+$";

	/**
	 * Default and only constructor
	 */
	public MpeDataSource() {
		extensions = new String[] { "*.xml" };
		descriptions = new String[][] { new String[] { "XML file", "SMS archive in XML" } };
		passwordDescriptions = null;
		parserClassName = "MpeParser";
	}

	@Override
	public String validateFiles(String[] files) {
		if (files == null || files.length != 1 || files[0] == null || !files[0].toLowerCase().endsWith(".xml"))
			return "You have to choose one XML file.";
		return null;
	}

	@Override
	public String validatePasswords(String[] passwords) {
		return null;
	}

	@Override
	public String validateUid(String uid) {
		if (!uid.matches(UID_REGEX))
			return "Phone number (UID) incorrect. Only digits allowed and optional plus sign (+) as the first character.";
		return null;
	}
}
