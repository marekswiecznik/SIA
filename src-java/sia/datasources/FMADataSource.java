package sia.datasources;

import sia.utils.ParserFactory;

/**
 * FMA Data source
 * 
 * Float's Mobile Agent (for Sony Ericsson)
 */
public class FMADataSource extends DataSource {
	
	/**
	 * Default and only constructor
	 */
	public FMADataSource() {
		extensions = new String[] {"*.xml" };
		descriptions = new String[][] {new String[] {"XML file", "SMS archive in XML"}};
		passwordDescriptions = null;
		parser = new ParserFactory("FmaParser").create();
	}

	@Override
	public void loadFiles(String[] files) {
		if (files.length != 0)
			throw new IllegalArgumentException("No files required");
	}
}
