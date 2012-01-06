package sia.datasources;

import sia.utils.ParserFactory;

/**
 * Google talk IMAP data source
 * @author jumper
 */
public class GtalkDataSource extends DataSource {

	/**
	 * Default and only constructor
	 */
	public GtalkDataSource() {
		extensions = new String[] { };
		descriptions = new String[][] {new String[] { }};
		passwordDescriptions = new String[] {"Email address (JID)", "*Password", "Chats label"};
		parser = new ParserFactory("GtalkParser").create();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void loadFiles(String[] files) {
		if (files != null && files.length != 0)
			throw new IllegalArgumentException("No files required");
	}

}
