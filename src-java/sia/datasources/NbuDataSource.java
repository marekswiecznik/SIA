package sia.datasources;

/**
 * Nbu Data Source
 * 
 * Ovi suite (for Nokia)
 */
public class NbuDataSource extends DataSource {
	private static final String UID_REGEX = "^\\+?[0-9]+$";

	/**
	 * Default and only constructor
	 */
	public NbuDataSource() {
		extensions = new String[] { "*.nbu" };
		descriptions = new String[][] { new String[] { "NBU file", "Nokia Ovi Suite backup file, containing vCard and vMsg" } };
		passwordDescriptions = null;
		parserClassName = "NbuParser";
	}

	@Override
	public String validateFiles(String[] files) {
		if (files == null || files.length != 1 || files[0] == null || !files[0].toLowerCase().endsWith(".nbu"))
			return "You have to choose one NBU file.";
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
